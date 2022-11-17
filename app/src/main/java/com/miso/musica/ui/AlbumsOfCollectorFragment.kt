package com.miso.musica.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miso.musica.R
import com.miso.musica.databinding.FragmentCommentsBinding
import com.miso.musica.databinding.FragmentAlbumsOfCollectorBinding
import com.miso.musica.models.CollectorAlbum
import com.miso.musica.models.Comment
import com.miso.musica.ui.adapters.CollectorAlbumAdapter
import com.miso.musica.ui.adapters.CommentsAdapter
import com.miso.musica.viewmodels.CollectorAlbumViewModel
import com.miso.musica.viewmodels.CommentViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumsOfCollectorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumsOfCollectorFragment : Fragment() {
    private var _binding: FragmentAlbumsOfCollectorBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CollectorAlbumViewModel
    private var viewModelAdapter: CollectorAlbumAdapter? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumsOfCollectorBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = CollectorAlbumAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.colalbumrv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_comments)
        val args: AlbumsOfCollectorFragmentArgs by navArgs()
        Log.d("Args", args.idCollector.toString())
        viewModel = ViewModelProvider(this, CollectorAlbumViewModel.Factory(activity.application, args.idCollector)).get(CollectorAlbumViewModel::class.java)
        viewModel.collectorAlbums.observe(viewLifecycleOwner, Observer<List<CollectorAlbum>> {
            it.apply {
                viewModelAdapter!!.albumscollector  = this
            }
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlbumsOfCollectorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlbumsOfCollectorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}