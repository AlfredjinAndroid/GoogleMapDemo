package com.jxd.mapdemo.scene

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.jxd.mapdemo.router.LitRouter
import com.jxd.mapdemo.router.defaultAnimationComposable
import com.jxd.mapdemo.scene.map.MapScene
import com.jxd.mapdemo.ui.theme.ThemeViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App(themeModel: ThemeViewModel) {
    val animatedNavController = rememberAnimatedNavController()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        AnimatedNavHost(navController = animatedNavController, startDestination = LitRouter.HomeScene.getDestination()) {

            // Home Scene
            defaultAnimationComposable(LitRouter.HomeScene.getDestination()) {
                /**
                 * 主页面
                 */
                HomeScene(
                    themeViewModel = themeModel,
                    webClick = {
                        animatedNavController.navigate(LitRouter.WebScene.buildNavigation())
                    }, mapClick = {
                        animatedNavController.navigate(LitRouter.MapScene.buildNavigation())
                    })
            }

            // Web Scene
            defaultAnimationComposable(LitRouter.WebScene.getDestination()) {
                WebScene()
            }

            defaultAnimationComposable(LitRouter.MapScene.getDestination()) {
                MapScene()
            }
        }
    }
}