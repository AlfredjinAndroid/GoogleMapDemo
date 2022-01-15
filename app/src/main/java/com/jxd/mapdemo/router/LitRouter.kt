package com.jxd.mapdemo.router


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable

/**
 * @author : Alfred
 * 路由基础封装
 * 进行统一的路由管理
 */
sealed interface LitRouter {
    fun getDestination(): String
    fun buildNavigation(vararg params: Pair<String, String>): String

    companion object {
        const val ROOT = "/app"
    }

    object HomeScene : LitRouter {

        private const val PATH = "$ROOT/home"

        override fun getDestination(): String = PATH

        override fun buildNavigation(vararg params: Pair<String, String>): String = PATH
    }

    object WebScene : LitRouter {

        private const val PATH = "$ROOT/web"

        override fun getDestination(): String = PATH

        override fun buildNavigation(vararg params: Pair<String, String>): String = PATH
    }

    object MapScene : LitRouter {
        private const val PATH = "$ROOT/map"

        override fun getDestination(): String = PATH

        override fun buildNavigation(vararg params: Pair<String, String>): String = PATH
    }
}

/**
 * @author : Alfred
 * 统一的composable page 封装，增加动画处理
 */
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.defaultAnimationComposable(
    dest: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(
        dest,
        arguments,
        deepLinks,
        enterTransition = { _, _ ->
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(700))
        },
        exitTransition = { _, _ ->
            slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(700))
        },
        popEnterTransition = { _, _ ->
            slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(700))
        },
        popExitTransition = { _, _ ->
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(700))
        }
    ) { navBackStackEntry ->
        content(navBackStackEntry)
    }
}