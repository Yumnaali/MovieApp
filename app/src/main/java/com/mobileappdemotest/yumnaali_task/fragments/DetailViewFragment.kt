package com.mobileappdemotest.yumnaali_task.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mobileappdemotest.yumnaali_task.database.AppDatabase
import com.mobileappdemotest.yumnaali_task.databinding.FragmentDetailViewBinding
import com.mobileappdemotest.yumnaali_task.models.MovieDataModel

import com.mobileappdemotest.yumnaali_task.models.MovieWatchList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat


class DetailViewFragment : Fragment() {

    lateinit var binding: FragmentDetailViewBinding
    private lateinit var movieData: MovieDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailViewBinding.inflate(layoutInflater)

        setData()
        setListener()
        return binding.root
    }

    private fun setData() {

        val mTitle = binding.toolbarTitle
        mTitle.text = "Movies"
        arguments?.let {
           movieData = arguments?.getSerializable("MovieObj") as MovieDataModel

            try {
                val ims: InputStream? = requireActivity().assets?.open("${movieData.thumbnail}.png")
                val d = Drawable.createFromStream(ims, null)
                binding.ivMovie.setImageDrawable(d)
            } catch (ex: IOException) {
                // could not load image for item
                Toast.makeText(
                    requireContext(),
                    "Could not load image for item",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.tvMovieTitle.text = movieData.title
            binding.tvDesc.text = movieData.description

            val sourceString =
                "<font color='#000000'><b><big>${movieData.rating}</big></b></font><small>/10</small>"

            binding.tvRating.text = Html.fromHtml(sourceString)

            val dateFormat = SimpleDateFormat("yyyy, dd MMMM")
            val date = dateFormat.format(movieData.releasedDate)
            binding.textDate.text = date
            binding.textGenre.text = movieData.genre



            CoroutineScope(Dispatchers.IO).launch {
                val userWatchlist = AppDatabase.getDatabase(requireContext())?.MovieDao()?.getAll()
                val movieWatchListItem = MovieWatchList(movieData.id,false)
                userWatchlist?.let {
                    if (it.contains(movieWatchListItem)){
                        binding.btnAddToWatchList.text = "REMOVE FROM WATCHLIST"
                        binding.btnAddToWatchList.tag = "REMOVE"
                    }else{
                        binding.btnAddToWatchList.text ="+ADD TO WATCHLIST"
                        binding.btnAddToWatchList.tag = "ADD"
                    }
                }

            }
        }
    }

    private fun setListener(){
        /**
         * Back button listener
         */
        binding.ivBack.setOnClickListener {

             findNavController().navigateUp()
        }

        /**
         * Watch trailer listener
         */
        binding.btnWatchTrailer.setOnClickListener {
            openYoutubeLink(movieData.trailerLink)
        }

        /**
         * add to watchList
         */
        binding.btnAddToWatchList.setOnClickListener{
         if (binding.btnAddToWatchList.tag.equals("REMOVE")){
             binding.btnAddToWatchList.text = "+ADD TO WATCHLIST"
             binding.btnAddToWatchList.tag= "ADD"
             CoroutineScope(Dispatchers.IO).launch {
                 AppDatabase.getDatabase(requireContext())?.MovieDao()?.deleteMovieById(movieData.id)
             }
         }else{

             binding.btnAddToWatchList.text = "REMOVE FROM WATCHLIST"
             binding.btnAddToWatchList.tag= "REMOVE"
             CoroutineScope(Dispatchers.IO).launch {
                 val movieWatchList = MovieWatchList(movieData.id,true)
                 AppDatabase.getDatabase(requireContext())?.MovieDao()?.insert(movieWatchList)
             }
         }
        }
    }


    private fun openYoutubeLink(link: String) {
     val youtubeID=   link.split("=")[1]
        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$youtubeID"))
        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        try {
            this.startActivity(intentApp)
        } catch (ex: ActivityNotFoundException) {
            this.startActivity(intentBrowser)
        }

    }

}