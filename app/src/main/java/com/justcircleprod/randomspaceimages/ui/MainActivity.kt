package com.justcircleprod.randomspaceimages.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.dataStore.DataStoreConstants
import com.justcircleprod.randomspaceimages.databinding.ActivityMainBinding
import com.justcircleprod.randomspaceimages.ui.bottomNavigation.BottomNavigation
import com.justcircleprod.randomspaceimages.ui.themeState.ThemeState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

    private lateinit var navHostFragment: NavHostFragment
    private val navController: NavController get() = navHostFragment.navController

    private lateinit var initJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // if the activity is recreated due to a change of theme, then set the themeState to APPLIED
        if (viewModel.themeState == ThemeState.APPLYING) {
            viewModel.themeState = ThemeState.APPLIED
        }

        setThemeObserver()

        installSplashScreen().setKeepOnScreenCondition {
            if (::initJob.isInitialized) {
                !(initJob.isCompleted && viewModel.themeState == ThemeState.APPLIED)
            } else {
                true
            }
        }

        initJob = lifecycleScope.launch {
            binding = ActivityMainBinding.inflate(layoutInflater)

            setContentView(binding.root)

            setupNavController()

            binding.bottomNavigationComposeView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

                setContent {
                    BottomNavigation(navController)
                }
            }
        }
    }

    private fun setThemeObserver() {
        viewModel.themeValue.observe(this) { themeValue ->
            val isSystemInDarkTheme = resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

            // if the application theme is equal to the system theme, then set the themeState to APPLIED.
            // Otherwise, the activity will be recreated, so the themeState is set to APPLIED
            // (when creating an activity, the themeState will be set to APPLIED in the onCreate).
            if (viewModel.themeState == ThemeState.NOT_APPLIED) {
                when {
                    !isSystemInDarkTheme && themeValue == DataStoreConstants.LIGHT_THEME ||
                            isSystemInDarkTheme && themeValue == DataStoreConstants.DARK_THEME ||
                            themeValue == DataStoreConstants.SYSTEM_THEME ||
                            themeValue == null -> {
                        viewModel.themeState = ThemeState.APPLIED
                    }
                    else -> {
                        viewModel.themeState = ThemeState.APPLYING
                    }
                }
            }

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

    private fun setupNavController() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        val graph = navController.navInflater.inflate(R.navigation.nav_graph)

        navHostFragment.navController.setGraph(graph, null)
    }
}