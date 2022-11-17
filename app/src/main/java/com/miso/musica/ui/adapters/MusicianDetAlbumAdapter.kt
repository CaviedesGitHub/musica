package com.miso.musica.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.miso.musica.R
import com.miso.musica.databinding.AlbumItemBinding
import com.miso.musica.databinding.MusiciandetItemBinding
import com.miso.musica.models.Album
import com.miso.musica.ui.AlbumsFragmentDirections

class MusicianDetAlbumAdapter : RecyclerView.Adapter<MusicianDetAlbumAdapter.MusicianDetAlbumViewHolder>(){

    var albums :List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicianDetAlbumViewHolder {
        val withDataBinding: MusiciandetItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            MusicianDetAlbumViewHolder.LAYOUT,
            parent,
            false)
        return MusicianDetAlbumViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: MusicianDetAlbumViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.album = albums[position]
        }
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    class MusicianDetAlbumViewHolder(val viewDataBinding: MusiciandetItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.musiciandet_item
        }
        fun bind(album: Album) {
            Glide.with(itemView)
                .load(album.cover.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_broken_image))
                .into(viewDataBinding.albumCover)
        }
    }
}