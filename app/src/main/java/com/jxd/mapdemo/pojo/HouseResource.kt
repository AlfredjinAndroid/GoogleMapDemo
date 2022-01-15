package com.jxd.mapdemo.pojo

data class HouseResource(
    val id: String,
    val price: String,
    val address: String,
    val location: ResourceLocation,
    val photo: String,
    val status: String
)

data class ResourceLocation(
    val lat: Double,
    val lon: Double
)