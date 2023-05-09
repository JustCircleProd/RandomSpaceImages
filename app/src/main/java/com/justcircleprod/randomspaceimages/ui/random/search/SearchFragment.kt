package com.justcircleprod.randomspaceimages.ui.random.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.databinding.FragmentSearchBinding
import com.justcircleprod.randomspaceimages.ui.extensions.navigateSafety

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private val navController: NavController get() = findNavController()

    @IdRes
    private val destinationId = R.id.navigation_search

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)

        binding.contentComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                SearchFragmentContent(
                    onBackButtonClick = { navController.popBackStack() },
                    onSearchCallback = { q, yearStart, yearEnd ->
                        val direction =
                            SearchFragmentDirections.toSearchResult(q, yearStart, yearEnd)
                        navController.navigateSafety(destinationId, direction)
                    }
                )
            }
        }

        return binding.root
    }
}