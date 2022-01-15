package com.jxd.mapdemo.scene

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jxd.mapdemo.R
import com.jxd.mapdemo.ui.theme.MyShapes
import com.jxd.mapdemo.ui.theme.ThemeModel
import com.jxd.mapdemo.ui.theme.ThemeViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScene(themeViewModel: ThemeViewModel, webClick: () -> Unit, mapClick: () -> Unit) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden, confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded })
    ModalBottomSheetLayout(
        sheetContent = { ThemeSheet(themeViewModel) },
        content = {
            HomeContent(
                themeClick = {
                    scope.launch {
                        sheetState.show()
                    }
                }, webClick = webClick,
                mapClick = mapClick
            )
        },
        sheetState = sheetState,
        sheetShape = MyShapes.sheetShape
    )
}

@Composable
fun HomeContent(themeClick: () -> Unit, webClick: () -> Unit, mapClick: () -> Unit) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

            Button(onClick = { mapClick() }) {
                Text(text = stringResource(id = R.string.main_map))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { webClick() }) {
                Text(text = stringResource(id = R.string.main_web))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                themeClick()
            }) {
                Text(text = stringResource(id = R.string.main_theme))
            }


        }
    }
}

@Composable
fun ThemeSheet(themeViewModel: ThemeViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.5f)
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CheckItem(ThemeModel.AUTO, themeViewModel, stringResource(id = R.string.theme_auto))
            Spacer(modifier = Modifier.height(8.dp))
            CheckItem(ThemeModel.LIGHT, themeViewModel, stringResource(id = R.string.theme_light))
            Spacer(modifier = Modifier.height(8.dp))
            CheckItem(ThemeModel.DARK, themeViewModel, stringResource(id = R.string.theme_dark))
        }
    }
}


@Composable
fun CheckItem(themeModel: ThemeModel, themeViewModel: ThemeViewModel, title: String) {
    val currentTheme by themeViewModel.currentTheme.collectAsState(initial = ThemeModel.AUTO)
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
            Text(text = title)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.weight(1f)) {
            Checkbox(checked = currentTheme == themeModel, onCheckedChange = { _checked ->
                if (_checked) {
                    themeViewModel.currentTheme(themeModel = themeModel)
                }
            })
        }
    }
}