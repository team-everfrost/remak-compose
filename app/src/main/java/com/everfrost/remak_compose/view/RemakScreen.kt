package com.everfrost.remak_compose.view

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.everfrost.remak_compose.view.account.onboarding.OnboardingScreen
import com.everfrost.remak_compose.view.account.signin.SignInScreen


enum class RemakScreen(val route: String, val title: String, val icon: Int? = null) {
    OnBoarding("onBoarding", "온보딩"),
    SignIn("signIn", "로그인"),


}

fun NavGraphBuilder.composableWithAnimation(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )

        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            )
        },
        content = content

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemakApp(
    startDestination: String
) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composableWithAnimation(RemakScreen.OnBoarding.route) {
            OnboardingScreen(navController = navController)
        }

        composableWithAnimation(
            route = RemakScreen.SignIn.route
        ) { navBackStackEntry ->
            SignInScreen(navController = navController)
        }


    }

}