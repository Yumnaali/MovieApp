package com.mobileappdemotest.yumnaali_task.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobileappdemotest.yumnaali_task.adapter.MovieListAdapter
import com.mobileappdemotest.yumnaali_task.R
import com.mobileappdemotest.yumnaali_task.database.AppDatabase
import com.mobileappdemotest.yumnaali_task.databinding.FragmentMainViewBinding
import com.mobileappdemotest.yumnaali_task.models.MovieWatchList
import com.mobileappdemotest.yumnaali_task.viewModels.MoviesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewFragment : Fragment() {

    private lateinit var viewBinding: FragmentMainViewBinding
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var adapter: MovieListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setListener()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentMainViewBinding.inflate(layoutInflater)

        return viewBinding.root
    }

    private fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            val userWatchlist = AppDatabase.getDatabase(requireContext())?.MovieDao()?.getAll()
            setAdapter(userWatchlist as ArrayList<MovieWatchList>)
        }
        insertData()
    }

    private fun insertData() {
        viewModel.prepareData(requireContext())
    }

    private fun setAdapter(movieWatchlist: ArrayList<MovieWatchList>) {
        viewBinding.rvMovies.layoutManager = LinearLayoutManager(context)
        adapter = MovieListAdapter(requireContext(), viewModel.movieList, movieWatchlist)
        viewBinding.rvMovies.adapter = adapter
    }


    private fun setListener() {
        viewBinding.tvSort.setOnClickListener {
            showSortDialog()
        }
    }

    private fun showSortDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.sort_dialog_layout)
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels

        dialog.window?.setLayout((6 * width) / 7, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        val sortByTitle = dialog.findViewById(R.id.tvTitle) as TextView
        val sortByReleasedDate = dialog.findViewById(R.id.tvReleaseDate) as TextView
        val cancelBtn = dialog.findViewById(R.id.btn_cancel) as Button
        sortByTitle.setOnClickListener {
            val sortedList = viewModel.movieList.sortedWith(compareBy { it.title })
            adapter.setData(sortedList)

            dialog.dismiss()
        }
        sortByReleasedDate.setOnClickListener {
            val sortedList = viewModel.movieList.sortedWith(compareBy { it.releasedDate })
            adapter.setData(sortedList)
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}