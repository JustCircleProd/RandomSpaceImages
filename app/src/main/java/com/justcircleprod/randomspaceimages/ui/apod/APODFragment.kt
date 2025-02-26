package com.justcircleprod.randomspaceimages.ui.apod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.remote.apod.APODConstants
import com.justcircleprod.randomspaceimages.databinding.FragmentApodBinding
import com.justcircleprod.randomspaceimages.ui.MainActivity
import com.justcircleprod.randomspaceimages.ui.apod.apodFavourites.APODFavouritesPageViewModel
import com.justcircleprod.randomspaceimages.ui.apod.apodPage.APODPageEvent
import com.justcircleprod.randomspaceimages.ui.apod.apodPage.APODPageViewModel
import com.justcircleprod.randomspaceimages.ui.extensions.navigateSafety
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@AndroidEntryPoint
class APODFragment : Fragment() {
    private lateinit var binding: FragmentApodBinding

    private val apodPageViewModel: APODPageViewModel by viewModels()
    private val apodFavouritesPageViewModel: APODFavouritesPageViewModel by viewModels()

    @IdRes
    private val destinationId: Int = R.id.navigation_apod
    private val navController get() = findNavController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apodPageViewModel.savingToGallery =
            (requireActivity() as MainActivity).viewModel.savingToGallery
        apodPageViewModel.sharingImage = (requireActivity() as MainActivity).viewModel.sharingImage

        apodFavouritesPageViewModel.savingToGallery =
            (requireActivity() as MainActivity).viewModel.savingToGallery
        apodFavouritesPageViewModel.sharingImage =
            (requireActivity() as MainActivity).viewModel.sharingImage
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApodBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.contentComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                APODFragmentContent(
                    apodPageViewModel = apodPageViewModel,
                    apodFavouritesPageViewModel = apodFavouritesPageViewModel,
                    onPickDateButtonClick = {
                        showDatePicker()
                    },
                    onAPODEntryImageClick = { imageUrl, imageUrlHd ->
                        navController.navigateSafety(
                            destinationId,
                            APODFragmentDirections.toDetailImage(imageUrl, imageUrlHd)
                        )
                    }
                )
            }
        }
    }

    private fun showDatePicker() {
        val start = SimpleDateFormat(APODConstants.DATE_FORMAT, Locale.US).parse(
            APODConstants.MIN_DATE
        )!!.time
        val end = Date().time

        val dateValidatorMin: DateValidator =
            DateValidatorPointForward.from(start)
        val dateValidatorMax: DateValidator =
            DateValidatorPointBackward.before(end)

        val listValidators = listOf(dateValidatorMin, dateValidatorMax)
        val validators = CompositeDateValidator.allOf(listValidators)

        val constraints = CalendarConstraints.Builder()
            .setStart(start)
            .setEnd(end)
            .setValidator(validators)
            .build()

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            apodPageViewModel.pickedDateInMills.value?.let {
                timeInMillis = it
            }
        }

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraints)
            .setSelection(calendar.timeInMillis)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            apodPageViewModel.onEvent(APODPageEvent.OnDatePicked(it))
        }

        datePicker.show(
            requireActivity().supportFragmentManager,
            null
        )
    }
}