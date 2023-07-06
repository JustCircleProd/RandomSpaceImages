package com.justcircleprod.randomspaceimages.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.local.settings.DataStoreConstants
import com.justcircleprod.randomspaceimages.databinding.ActivityMainBinding
import com.justcircleprod.randomspaceimages.ui.bottomNavigation.BottomNavItem
import com.justcircleprod.randomspaceimages.ui.bottomNavigation.BottomNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

    private lateinit var navHostFragment: NavHostFragment
    private val navController: NavController get() = navHostFragment.navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setThemeObserver()

        lifecycleScope.launch {
            binding = ActivityMainBinding.inflate(layoutInflater)

            setContentView(binding.root)

            setupNavController(viewModel.startScreen.first())

            binding.bottomNavigationComposeView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

                viewModel.startScreen.asLiveData().observe(this@MainActivity) { startScreen ->
                    viewModel.bottomNavItems.value = BottomNavItem.getItems(startScreen)

                    setContent {
                        val bottomNavItems by viewModel.bottomNavItems.collectAsState()

                        BottomNavigation(
                            navController = navController,
                            items = bottomNavItems
                        )
                    }
                }
            }
        }
    }

    private fun setThemeObserver() {
        viewModel.themeValue.observe(this) { themeValue ->
            when (themeValue) {
                DataStoreConstants.LIGHT_THEME -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                DataStoreConstants.DARK_THEME -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }

    private fun setupNavController(startScreen: String?) {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        val graph = navController.navInflater.inflate(R.navigation.nav_graph)

        val startDestinationId = when (startScreen) {
            DataStoreConstants.RANDOM_SCREEN -> R.id.navigation_random
            DataStoreConstants.APOD_SCREEN -> R.id.navigation_apod
            else -> R.id.navigation_apod
        }
        graph.setStartDestination(startDestinationId)

        navHostFragment.navController.setGraph(graph, null)
    }
}