package com.mobileappdemotest.yumnaali_task.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mobileappdemotest.yumnaali_task.models.MovieDataModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MoviesViewModel : ViewModel() {

    var movieList = arrayListOf<MovieDataModel>()

    fun prepareData(context: Context): List<MovieDataModel> {
        movieList.addAll(getMovieData(context))
        return movieList
    }

    private fun getMovieData(context: Context): List<MovieDataModel> {
        val reader = InputStreamReader(context.assets?.open("movie_list.json"))
        val jsonContent = reader.buffered().use { it.readText() }
        val moviesList = arrayListOf<MovieDataModel>()
        jsonContent.let {
            val jsonObject = JSONObject(it)
            val movieArray = jsonObject.getJSONArray("list") as JSONArray
            for (i in 0 until movieArray.length()) {
                val movie = movieArray.getJSONObject(i)
                val item = MovieDataModel(
                    movie.getInt("Id"),
                    movie.getString("Title"),
                    movie.getString("Description"),
                    movie.getDouble("Rating"),
                    movie.getString("Duration"),
                    movie.getString("Genre"),
                    convertStringToDate(movie.getString("Released_date")),
                    movie.getString("Trailer_link"),
                    movie.getString("thumbnail")
                )
                movieList.clear()
                moviesList.add(item)
            }
        }
        return moviesList
    }


    private fun convertStringToDate(dateStr: String): Date {
        val format = SimpleDateFormat("dd MMM yyyy")
        return try {
            format.parse(dateStr) as Date
        } catch (e: ParseException) {
            e.printStackTrace()
            Date()
        }
    }


}