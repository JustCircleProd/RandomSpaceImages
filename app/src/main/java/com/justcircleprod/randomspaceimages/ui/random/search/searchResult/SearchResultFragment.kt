package com.justcircleprod.randomspaceimages.ui.random.search.searchResult

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
import com.justcircleprod.randomspaceimages.databinding.FragmentSearchResultBinding
import com.justcircleprod.randomspaceimages.ui.extensions.navigateSafety
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultBinding
    private val viewModel: SearchResultViewModel by viewModels()

    private val navController: NavController get() = findNavController()

    @IdRes
    private val destinationId = R.id.navigation_search_result

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(layoutInflater)

        binding.contentComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                SearchResultScreen(
                    viewModel = viewModel,
                    onImageEntryClick = {
                        val direction = SearchResultFragmentDirections.toDetail(it)
                        navController.navigateSafety(destinationId, direction)
                    },
                    onBackButtonClick = {
                        navController.popBackStack()
                    }
                )
            }
        }

        return binding.root
    }

    companion object {
        const val Q_ARGUMENT_NAME = "q"
        const val YEAR_START_ARGUMENT_NAME = "yearStart"
        const val YEAR_END_ARGUMENT_NAME = "year_end"
    }
}