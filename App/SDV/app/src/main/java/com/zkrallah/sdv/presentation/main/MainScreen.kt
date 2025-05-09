package com.zkrallah.sdv.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.zkrallah.sdv.ROUTES
import com.zkrallah.sdv.SCREENS
import com.zkrallah.sdv.presentation.home.HomeScreen
import com.zkrallah.sdv.presentation.intro.OnBoarding
import com.zkrallah.sdv.presentation.intro.OnBoardingViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun AppNavigation(
    onBoardingViewModel: OnBoardingViewModel = hiltViewModel()
) {
    runBlocking {
        onBoardingViewModel.getStartingDestination()
    }

    val startingDestination = onBoardingViewModel.startingDestination.collectAsState()
    SetupNavigation(
        startingScreen = startingDestination.value
    )
}

@Composable
fun SetupNavigation(startingScreen: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        Scaffold(
            bottomBar = {
                if (ROUTES.contains(navBackStackEntry?.destination?.route)) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        tonalElevation = 4.dp
                    ) {
                        SCREENS.forEach { item ->
                            val selected = item.route == navBackStackEntry?.destination?.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
//                                    navController.navigate(item.route) {
//                                        popUpTo(navController.graph.startDestinationId) {
//                                            saveState = true
//                                        }
//                                        launchSingleTop = true
//                                        restoreState = true
//                                    }
                                },
                                icon = {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            painter = painterResource(id = if (selected) item.selectedIcon else item.unSelectedIcon),
                                            contentDescription = item.name,
                                            tint = if (selected)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                label = {
                                    Text(
                                        text = item.name,
                                        fontSize = 12.sp,
                                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (selected)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                alwaysShowLabel = true
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Navigation(startingScreen, navController)
            }
        }
    }
}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun Navigation(
    startingScreen: String, navController: NavHostController
) {
    NavHost(navController = navController, startDestination = startingScreen) {
        composable("onboarding") { OnBoarding(navController = navController) }
        composable("home") {
            HomeScreen()
        }
    }
}