package com.justcircleprod.randomspaceimages.ui.home

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
import com.justcircleprod.randomspaceimages.databinding.FragmentHomeBinding
import com.justcircleprod.randomspaceimages.ui.common.navigateSafety
import com.justcircleprod.randomspaceimages.ui.home.favourites.FavouriteImageListViewModel
import com.justcircleprod.randomspaceimages.ui.home.random.RandomImageListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val randomImageListViewModel: RandomImageListViewModel by viewModels()
    private val favouriteImageListViewModel: FavouriteImageListViewModel by viewModels()

    private val navController: NavController get() = findNavController()

    @IdRes
    private val destinationId = R.id.navigation_home

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.contentComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                HomeFragmentContent(
                    randomImageListViewModel = randomImageListViewModel,
                    favouriteImageListViewModel = favouriteImageListViewModel,
                    onImageEntryClick = {
                        val direction = HomeFragmentDirections.toDetail(it)
                        navController.navigateSafety(destinationId, direction)
                    }
                )
            }
        }

        return binding.root
    }
}