package com.justcircleprod.randomspaceimages.ui.random.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.databinding.FragmentDetailBinding
import com.justcircleprod.randomspaceimages.ui.MainActivity
import com.justcircleprod.randomspaceimages.ui.common.localCompositions.ImageActionStates
import com.justcircleprod.randomspaceimages.ui.common.localCompositions.LocalImageActionStates
import com.justcircleprod.randomspaceimages.ui.extensions.navigateSafety
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    @IdRes
    private val destinationId: Int = R.id.navigation_detail
    private val navController: NavController get() = findNavController()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.contentComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                val imageActionStates = ImageActionStates(
                    savingToGallery = (requireActivity() as MainActivity).viewModel.savingToGallery,
                    sharingImage = (requireActivity() as MainActivity).viewModel.sharingImage,
                )

                CompositionLocalProvider(LocalImageActionStates provides imageActionStates) {
                    DetailFragmentContent(
                        viewModel = viewModel,
                        nasaLibraryImageEntry = args.nasaLibraryImageEntry,
                        onBackButtonClick = {
                            navController.popBackStack()
                        },
                        onImageClick = {
                            navController.navigateSafety(
                                destinationId,
                                DetailFragmentDirections.toDetailImage(it)
                            )
                        }
                    )
                }
            }
        }
    }
}