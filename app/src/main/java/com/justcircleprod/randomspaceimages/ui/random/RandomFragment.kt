package com.justcircleprod.randomspaceimages.ui.random

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.databinding.FragmentRandomBinding
import com.justcircleprod.randomspaceimages.ui.common.navigateSafety
import com.justcircleprod.randomspaceimages.ui.random.randomFavouritesPage.RandomFavouritesPageViewModel
import com.justcircleprod.randomspaceimages.ui.random.randomPage.RandomPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomFragment : Fragment() {
    private lateinit var binding: FragmentRandomBinding

    private val randomPageViewModel: RandomPageViewModel by viewModels()
    private val randomFavouritesPageViewModel: RandomFavouritesPageViewModel by viewModels()

    private val navController: NavController get() = findNavController()

    @IdRes
    private val destinationId = R.id.navigation_random

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomBinding.inflate(layoutInflater)

        binding.contentComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                RandomFragmentContent(
                    randomViewModel = randomPageViewModel,
                    favouriteViewModel = randomFavouritesPageViewModel,
                    onSearchClick = {
                        val direction = RandomFragmentDirections.toSearch()
                        navController.navigateSafety(destinationId, direction)
                    },
                    onImageEntryClick = {
                        val direction = RandomFragmentDirections.toDetail(it)
                        navController.navigateSafety(destinationId, direction)
                    }
                )
            }
        }

        return binding.root
    }
}