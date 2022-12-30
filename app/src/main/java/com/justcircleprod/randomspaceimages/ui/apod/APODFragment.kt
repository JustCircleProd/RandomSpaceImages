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
import com.google.android.material.datepicker.*
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.remote.apod.APODConstants
import com.justcircleprod.randomspaceimages.databinding.FragmentApodBinding
import com.justcircleprod.randomspaceimages.ui.apod.apodFavourites.APODFavouritesPageViewModel
import com.justcircleprod.randomspaceimages.ui.apod.apodPage.APODPageViewModel
import com.justcircleprod.randomspaceimages.ui.common.navigateSafety
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class APODFragment : Fragment() {
    private lateinit var binding: FragmentApodBinding

    private val apodPageViewModel: APODPageViewModel by viewModels()
    private val apodFavouritesPageViewModel: APODFavouritesPageViewModel by viewModels()

    @IdRes
    private val destinationId: Int = R.id.navigation_apod
    private val navController get() = findNavController()

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
                    onAPODEntryImageClick = {
                        navController.navigateSafety(
                            destinationId,
                            APODFragmentDirections.toDetailImage(it)
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

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraints)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val pickedDate = SimpleDateFormat(APODConstants.DATE_FORMAT, Locale.US).format(Date(it))
            apodPageViewModel.loadAPODByDay(pickedDate)
        }

        datePicker.show(
            requireActivity().supportFragmentManager,
            null
        )
    }
}