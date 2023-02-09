package com.mobileappdemotest.yumnaali_task.models

import java.io.Serializable
import java.util.Date


data class MovieDataModel(
    val id: Int,
    val title: String,
    val description: String,
    val rating: Double,
    val duration: String,
    val genre: String,
    val releasedDate: Date,
    val trailerLink: String,
    val thumbnail:String
) : Serializable
