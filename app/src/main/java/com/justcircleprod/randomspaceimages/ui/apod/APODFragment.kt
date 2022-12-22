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
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.databinding.FragmentApodBinding
import com.justcircleprod.randomspaceimages.ui.apod.apodFavourites.APODFavouritesPageViewModel
import com.justcircleprod.randomspaceimages.ui.apod.apodPage.APODPageViewModel
import com.justcircleprod.randomspaceimages.ui.common.navigateSafety
import dagger.hilt.android.AndroidEntryPoint

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
}