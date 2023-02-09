package com.mobileappdemotest.yumnaali_task.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mobileappdemotest.yumnaali_task.R
import com.mobileappdemotest.yumnaali_task.models.MovieDataModel
import com.mobileappdemotest.yumnaali_task.databinding.LayoutMovieListItemBinding
import com.mobileappdemotest.yumnaali_task.models.MovieWatchList
import java.io.IOException
import java.io.InputStream
import java.util.*


class MovieListAdapter(
    val context: Context,
    private var mList: List<MovieDataModel>,
    val watchList: List<MovieWatchList>
) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutMovieListItemBinding.inflate(layoutInflater)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = mList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    inner class ViewHolder(private val binding: LayoutMovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieDataModel) {

            binding.tvMovieTitle.text = "${movie.title} (${getYear(movie.releasedDate)})"
            binding.tvMovieGenre.text = "${movie.duration} - ${movie.genre}"

            //if it is in watchlist
            val watchListItem = MovieWatchList(movie.id, false)
            if (watchList.contains(watchListItem)) {
                binding.tvWatchlist.visibility = View.VISIBLE

            } else {
                binding.tvWatchlist.visibility = View.GONE

            }

            binding.mainLayout.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("MovieObj", movie)
                Navigation.findNavController(it).navigate(R.id.detailViewFragment, bundle)
            }


            try {
                val ims: InputStream? = context.assets?.open("${movie.thumbnail}.png")
                val d = Drawable.createFromStream(ims, null)
                binding.ivMovie.setImageDrawable(d)
            } catch (ex: IOException) {
                // could not load image for item
            }
        }
    }


    fun getYear(date: Date?): Int {
        val cal: Calendar = Calendar.getInstance()
        cal.time = date!!
        return cal.get(Calendar.YEAR)
    }

    fun setData(sortedList:kotlin.collections.List<MovieDataModel>){
        mList.toMutableList().clear()
        mList = (sortedList)
        notifyDataSetChanged()
    }
}
