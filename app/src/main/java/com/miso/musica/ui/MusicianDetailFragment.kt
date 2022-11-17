package com.miso.musica.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.miso.musica.R
import com.miso.musica.databinding.FragmentCommentsBinding
import com.miso.musica.databinding.FragmentMusicianDetailBinding
import com.miso.musica.models.Album
import com.miso.musica.models.Comment
import com.miso.musica.models.Musician
import com.miso.musica.ui.adapters.AlbumsAdapter
import com.miso.musica.ui.adapters.CommentsAdapter
import com.miso.musica.ui.adapters.MusicianDetAlbumAdapter
import com.miso.musica.viewmodels.CommentViewModel
import com.miso.musica.viewmodels.MusicianDetViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MusicianDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MusicianDetailFragment : Fragment() {
    private var _binding: FragmentMusicianDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MusicianDetViewModel
    private var viewModelAdapter: MusicianDetAlbumAdapter? = null

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
        _binding = FragmentMusicianDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = MusicianDetAlbumAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.musicianDetRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.TituloDetAlbum)
        val args: MusicianDetailFragmentArgs by navArgs()
        Log.d("Args", args.idMusician.toString())
        viewModel = ViewModelProvider(this, MusicianDetViewModel.Factory(activity.application, args.idMusician)).get(
            MusicianDetViewModel::class.java)
        viewModel.musician.observe(viewLifecycleOwner, Observer<Musician> {
            it.apply {
                viewModelAdapter!!.albums = this.albums
                binding.musician1=this
                loadImage()
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

    private fun loadImage() {
        binding.imageView?.let {
            Glide.with(this)
                .load(binding.musician1?.image)
                .into(it)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MusicianDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MusicianDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}