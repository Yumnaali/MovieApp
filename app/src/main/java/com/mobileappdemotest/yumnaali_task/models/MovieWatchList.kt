package com.mobileappdemotest.yumnaali_task.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieWatchList(
    @PrimaryKey val movieID: Int,
    @ColumnInfo(name = "isAddedToWatchList") val isAddedToWatchList: Boolean
){
    override fun equals(other: Any?): Boolean {
        if (other is MovieWatchList){
            val obj = other as MovieWatchList
            if (obj.movieID == this.movieID){
                return  true
            }
        }
        return false

    }
}