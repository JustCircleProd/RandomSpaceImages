package com.justcircleprod.randomspaceimages.ui.detailImage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.justcircleprod.randomspaceimages.databinding.FragmentDetailImageBinding


class DetailImageFragment : Fragment() {
    private lateinit var binding: FragmentDetailImageBinding

    private val navController: NavController get() = findNavController()
    private val args: DetailImageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailImageBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.contentComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                DetailImageFragmentContent(
                    imageUrl = args.imageUrl,
                    onBackButtonClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}