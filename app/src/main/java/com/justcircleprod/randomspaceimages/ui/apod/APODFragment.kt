package com.justcircleprod.randomspaceimages.ui.apod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.justcircleprod.randomspaceimages.databinding.FragmentApodBinding
import com.justcircleprod.randomspaceimages.ui.apod.apodPage.APODPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class APODFragment : Fragment() {
    private lateinit var binding: FragmentApodBinding

    private val apodPageViewModel: APODPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApodBinding.inflate(layoutInflater)

        binding.contentComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                APODFragmentContent(
                    viewModel = apodPageViewModel
                )
            }
        }

        return binding.root
    }
}