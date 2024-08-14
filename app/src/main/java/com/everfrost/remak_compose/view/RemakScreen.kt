package com.everfrost.remak_compose.view

import SearchScreen
import TagScreen
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
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
import com.everfrost.remak_compose.view.collection.AddCollectionScreen
import com.everfrost.remak_compose.view.collection.CollectionDetailScreen
import com.everfrost.remak_compose.view.collection.CollectionScreen
import com.everfrost.remak_compose.view.collection.EditCollectionScreen
import com.everfrost.remak_compose.view.home.add.AddLoadingScreen
import com.everfrost.remak_compose.view.home.add.AddScreen
import com.everfrost.remak_compose.view.home.add.LinkAddScreen
import com.everfrost.remak_compose.view.home.add.MemoAddScreen
import com.everfrost.remak_compose.view.home.detail.files.FileDetailScreen
import com.everfrost.remak_compose.view.home.detail.image.ImageDetailScreen
import com.everfrost.remak_compose.view.home.detail.image.ImageViewerScreen
import com.everfrost.remak_compose.view.home.detail.link.LinkDetailScreen
import com.everfrost.remak_compose.view.home.detail.memo.MemoDetailScreen
import com.everfrost.remak_compose.view.home.main.HomeMainScreen
import com.everfrost.remak_compose.view.profile.ProfileScreen
import com.everfrost.remak_compose.view.tag.TagDetailScreen
import com.everfrost.remak_compose.viewModel.home.main.HomeMainViewModel


enum class RemakScreen(val route: String, val title: String, val icon: Int? = null) {
    OnBoarding("OnBoarding", "온보딩"),
    SignIn("SignIn", "로그인"),
    Main("Main", "메인", icon = R.drawable.icon_home),
    Search("Search", "검색", icon = R.drawable.icon_search),
    Tag("Tag", "태그", icon = R.drawable.icon_tag),
    TagDetail("TagDetail/{tagName}", "태그 상세"),
    Collection("Collection", "컬렉션", icon = R.drawable.icon_collection),
    AddCollection("AddCollection", "컬렉션 추가"),
    CollectionDetail("CollectionDetail/{collectionName}/{collectionDescription}", "컬렉션 상세"),
    EditCollection("EditCollection/{collectionName}/{collectionDescription}", "컬렉션 수정"),
    Profile("Profile", "프로필", icon = R.drawable.icon_profile),
    LinkDetail("LinkDetail/{docId}", "링크 상세"),
    ImageDetail("ImageDetail/{docId}", "이미지 상세"),
    ImageViewer("ImageViewer", "이미지 뷰어"),
    FileDetail("FileDetail/{docId}", "파일 상세"),
    MemoDetail("MemoDetail/{docId}", "메모 상세"),
    Add("Add", "추가"),
    AddLoading("AddLoading", "추가 중"),
    LinkAdd("LinkAdd", "링크 추가"),
    MemoAdd("MemoAdd", "메모 추가"),
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
    startDestination: String,
    homeMainViewModel: HomeMainViewModel
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
            exitTransition = { ExitTransition.KeepUntilTransitionsFinished }, // 화면이 변경완료될 때까지 기존 화면 유지
        ) { navBackStackEntry ->
            HomeMainScreen(
                navController = navController,
                viewModel = homeMainViewModel,
                collectionViewModel = hiltViewModel()
            )
        }

        composable(
            route = RemakScreen.Search.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            // MainScreen(navController = navController)
            SearchScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }

        composable(
            route = RemakScreen.Tag.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.KeepUntilTransitionsFinished },
        ) {
            // MainScreen(navController = navController)
            TagScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }

        composable(
            route = RemakScreen.TagDetail.route,
            arguments = listOf(
                navArgument("tagName") { type = NavType.StringType },
            ),
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            },
        ) { backStackEntry ->
            val tagName = backStackEntry.arguments?.getString("tagName")
            TagDetailScreen(
                tagName = tagName!!,
                navController = navController,
                viewModel = hiltViewModel()
            )
        }

        composable(
            route = RemakScreen.Collection.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            // MainScreen(navController = navController)
            CollectionScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }

        composable(
            route = RemakScreen.AddCollection.route,
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            },
        ) {
            // MainScreen(navController = navController)
            AddCollectionScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }

        composable(
            route = RemakScreen.CollectionDetail.route,
            arguments = listOf(
                navArgument("collectionName") { type = NavType.StringType }
            ),
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            },
        ) { backStackEntry ->
            val collectionName = backStackEntry.arguments?.getString("collectionName")
            val collectionDescription = backStackEntry.arguments?.getString("collectionDescription")
            // MainScreen(navController = navController)
            CollectionDetailScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                collectionName = collectionName!!,
                collectionDescription = collectionDescription
            )
        }

        composable(
            route = RemakScreen.EditCollection.route,
            arguments = listOf(
                navArgument("collectionName") { type = NavType.StringType },
                navArgument("collectionDescription") { type = NavType.StringType }
            ),
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            },
        ) { backStackEntry ->
            val collectionName = backStackEntry.arguments?.getString("collectionName")
            val collectionDescription = backStackEntry.arguments?.getString("collectionDescription")
            // MainScreen(navController = navController)
            EditCollectionScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                collectionName = collectionName!!,
                collectionDescription = collectionDescription!!
            )
        }

        composable(
            route = RemakScreen.Profile.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            // MainScreen(navController = navController)
            ProfileScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }

        composable(
            route = RemakScreen.LinkDetail.route,
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
            LinkDetailScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                docIdx = docIdx,
                collectionViewModel = hiltViewModel()
            )

        }

        composable(
            // 이미지 상세
            route = RemakScreen.ImageDetail.route,
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
            ImageDetailScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                docIdx = docIdx,
                collectionViewModel = hiltViewModel()
            )
        }

        composable(
            route = RemakScreen.ImageViewer.route,
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            },
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(RemakScreen.ImageDetail.route)
            }
            ImageViewerScreen(
                navController = navController, viewModel = hiltViewModel(
                    parentEntry
                )
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
                docIdx = docIdx,
                collectionViewModel = hiltViewModel()
            )
        }

        composable(
            route = RemakScreen.MemoDetail.route,
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
            MemoDetailScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                docIdx = docIdx,
                collectionViewModel = hiltViewModel()
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
//            exitTransition = { fadeOut(animationSpec = tween(400)) },
            exitTransition = { ExitTransition.KeepUntilTransitionsFinished }, // 화면이 변경완료될 때까지 기존 화면 유지


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

        composableWithAnimation(RemakScreen.MemoAdd.route) {
            MemoAddScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }

    }

}