package com.jxd.mapdemo.scene.map

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.*
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.Marker
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.jxd.mapdemo.pojo.HouseResource
import com.jxd.mapdemo.ui.theme.MyShapes
import com.jxd.mapdemo.widget.LitNetImage
import com.jxd.mapdemo.widget.rememberMapViewWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.jxd.mapdemo.R
import kotlinx.coroutines.flow.collect
import kotlin.math.absoluteValue


@Composable
fun MapScene() {
    val mapViewModel: MapStateViewModel = hiltViewModel()
    val loading = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        mapViewModel.loadResource()
        launch(Dispatchers.IO) {
            delay(1000)
            launch(Dispatchers.Main) {
                loading.value = false
            }
        }
    }
    if (loading.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        MapAsync(mapViewModel)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            HouseList(mapViewModel)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HouseList(mapViewModel: MapStateViewModel) {
    val resourceList by mapViewModel.resourceList.collectAsState(initial = emptyList())
    val pageIndex by mapViewModel.pageIndex.collectAsState(initial = 0)
    val pageState = rememberPagerState()
    LaunchedEffect(pageIndex) {
        pageState.animateScrollToPage(pageIndex)
    }
    HorizontalPager(
        count = resourceList.count(),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.4f),
        contentPadding = PaddingValues(horizontal = 48.dp),
        state = pageState
    ) { pos ->
        resourceList.getOrNull(pos)?.also { h -> HouseItem(pos, h) }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerScope.HouseItem(page: Int, resource: HouseResource) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                lerp(
                    start = 0.9f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                ).also { scale ->
                    scaleX = scale
                    scaleY = scale
                }
            }, shape = MyShapes.cardShape
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ) {
                LitNetImage(path = resource.photo, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            }
            Box(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 12.dp, end = 12.dp), verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = String.format(stringResource(id = R.string.card_price), resource.price), fontWeight = FontWeight.W700, fontSize = 24.sp)
                    Text(text = String.format(stringResource(id = R.string.card_address), resource.address), fontWeight = FontWeight.W700, fontSize = 18.sp)
                    Text(
                        text = String.format(stringResource(id = R.string.card_status), resource.status),
                        fontWeight = FontWeight.W700,
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.error
                    )
                }
            }
        }
    }
}

@Composable
fun MapAsync(mapViewModel: MapStateViewModel) {
    val mapView = rememberMapViewWithLifecycle()
    MapContainer(mapView, mapViewModel)
}

@Composable
fun MapContainer(mapView: MapView, mapViewModel: MapStateViewModel) {
    val markOptions by mapViewModel.markOptions.collectAsState(initial = emptyList())
    val markList = mutableListOf<Marker>()
    LaunchedEffect(mapView) {
        val googleMap = mapView.awaitMap()
        markOptions.forEach {
            markList += googleMap.addMarker(it)
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(markOptions.first().position))
        googleMap.setOnMarkerClickListener {
            mapViewModel.pageIndex(it)
            false
        }
    }

    val coroutineScope = rememberCoroutineScope()
    AndroidView({ mapView }) { map ->
        coroutineScope.launch {
            val googleMap = map.awaitMap()
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(markOptions.first().position))
            googleMap.resetMinMaxZoomPreference()
            googleMap.setMinZoomPreference(10f)
            googleMap.setMaxZoomPreference(20f)
        }
    }
}
