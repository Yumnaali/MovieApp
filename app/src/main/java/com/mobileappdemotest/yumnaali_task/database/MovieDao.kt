package com.mobileappdemotest.yumnaali_task.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mobileappdemotest.yumnaali_task.models.MovieWatchList

@Dao
interface MovieDao {
    @Query("SELECT * FROM MovieWatchList")
    fun getAll(): List<MovieWatchList>

    @Insert
    fun insert( watchList: MovieWatchList)

    @Query("DELETE FROM MOVIEWATCHLIST WHERE movieID =:movieId")
    fun deleteMovieById(movieId:Int)
}