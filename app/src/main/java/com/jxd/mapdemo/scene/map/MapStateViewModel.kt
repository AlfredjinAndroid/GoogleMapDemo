package com.jxd.mapdemo.scene.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.Marker
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jxd.mapdemo.fake.MAP_DATA
import com.jxd.mapdemo.pojo.HouseResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapStateViewModel @Inject constructor() : ViewModel() {
    private val _resourceList = MutableStateFlow<List<HouseResource>>(value = emptyList())
    val resourceList: StateFlow<List<HouseResource>> = _resourceList

    private val _markOptions = MutableStateFlow<List<MarkerOptions>>(value = emptyList())
    val markOptions: StateFlow<List<MarkerOptions>> = _markOptions

    private val _pageIndex = MutableStateFlow(value = 0)
    val pageIndex: StateFlow<Int> = _pageIndex

    fun loadResource() {
        viewModelScope.launch {
            val result = try {
                Gson().fromJson<List<HouseResource>>(MAP_DATA, object : TypeToken<List<HouseResource>>() {}.type)
            } catch (e: Exception) {
                emptyList()
            }
            _resourceList.emit(result)
            result.map { r ->
                MarkerOptions().position(LatLng(r.location.lat, r.location.lon))
                    .title(r.address)
            }.also {
                _markOptions.emit(it)
            }
        }
    }

    fun pageIndex(options: Marker) {
        val index = _markOptions.value.indexOfFirst { f -> f.position == options.position }
        if (index >= 0) {
            _pageIndex.value = index
        }
    }
}