package com.mobileappdemotest.yumnaali_task.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobileappdemotest.yumnaali_task.models.MovieWatchList

@Database(entities = [MovieWatchList::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun MovieDao(): MovieDao

    companion object {
        private var instance: AppDatabase? = null


        fun getDatabase(context: Context) : AppDatabase?{

            if(instance ==null){
                instance = Room.databaseBuilder(
                  context ,
                   AppDatabase::class.java, "movies"
               ).build()

           }
            return instance
        }
    }




}