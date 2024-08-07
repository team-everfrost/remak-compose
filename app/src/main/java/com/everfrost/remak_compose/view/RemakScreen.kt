package com.everfrost.remak_compose.view

import SearchScreen
import TagScreen
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.everfrost.remak_compose.R
import com.everfrost.remak_compose.view.account.onboarding.OnboardingScreen
import com.everfrost.remak_compose.view.account.signin.SignInScreen
import com.everfrost.remak_compose.view.collection.CollectionScreen
import com.everfrost.remak_compose.view.home.File.FileDetailScreen
import com.everfrost.remak_compose.view.home.add.AddLoadingScreen
import com.everfrost.remak_compose.view.home.add.AddScreen
import com.everfrost.remak_compose.view.home.add.LinkAddScreen
import com.everfrost.remak_compose.view.home.main.HomeMainScreen
import com.everfrost.remak_compose.view.profile.ProfileScreen


enum class RemakScreen(val route: String, val title: String, val icon: Int? = null) {
    OnBoarding("OnBoarding", "온보딩"),
    SignIn("SignIn", "로그인"),
    Main("Main", "메인", icon = R.drawable.icon_home),
    Search("Search", "검색", icon = R.drawable.icon_search),
    Tag("Tag", "태그", icon = R.drawable.icon_tag),
    Collection("Collection", "컬렉션", icon = R.drawable.icon_collection),
    Profile("Profile", "프로필", icon = R.drawable.icon_profile),
    FileDetail("FileDetail/{docId}", "파일 상세"),
    Add("Add", "추가"),
    AddLoading("AddLoading", "추가 중"),
    LinkAdd("LinkAdd", "링크 추가"),
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

        composable(
            route = RemakScreen.Main.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.KeepUntilTransitionsFinished },
        ) { navBackStackEntry ->
            HomeMainScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }

        composable(
            route = RemakScreen.Search.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            // MainScreen(navController = navController)
            SearchScreen(
                navController = navController
            )
        }

        composable(
            route = RemakScreen.Tag.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            // MainScreen(navController = navController)
            TagScreen(
                navController = navController
            )
        }

        composable(
            route = RemakScreen.Collection.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            // MainScreen(navController = navController)
            CollectionScreen(
                navController = navController
            )
        }

        composable(
            route = RemakScreen.Profile.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            // MainScreen(navController = navController)
            ProfileScreen(
                navController = navController
            )
        }

        composable(
            route = RemakScreen.FileDetail.route,
            arguments = listOf(
                navArgument("docId") { type = NavType.StringType }
            ),
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            },
        ) { backStackEntry ->
            val docIdx = backStackEntry.arguments?.getString("docId")
            FileDetailScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                docIdx = docIdx
            )
        }

        composable(
            route = RemakScreen.Add.route,
            enterTransition = {
                fadeIn(
                    animationSpec =
                    tween(400)
                )
            },
            exitTransition = { fadeOut(animationSpec = tween(400)) },
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(RemakScreen.Add.route)
            }
            AddScreen(
                navController = navController,
                viewModel = hiltViewModel(parentEntry)
            )
        }

        composable(
            route = RemakScreen.AddLoading.route,
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = { ExitTransition.None },
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(RemakScreen.Add.route)
            }
            AddLoadingScreen(
                navController = navController,
                viewModel = hiltViewModel(parentEntry)
            )
        }

        composableWithAnimation(RemakScreen.LinkAdd.route) {
            LinkAddScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }

    }

}