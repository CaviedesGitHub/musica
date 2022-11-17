package com.miso.musica.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.miso.musica.R
import com.miso.musica.databinding.CollectoralbumItemBinding
import com.miso.musica.models.Album
import com.miso.musica.models.CollectorAlbum


class CollectorAlbumAdapter : RecyclerView.Adapter<CollectorAlbumAdapter.CollectorAlbumViewHolder>(){

    var albumscollector :List<CollectorAlbum> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorAlbumAdapter.CollectorAlbumViewHolder {
        val withDataBinding: CollectoralbumItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CollectorAlbumAdapter.CollectorAlbumViewHolder.LAYOUT,
            parent,
            false)
        return CollectorAlbumAdapter.CollectorAlbumViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CollectorAlbumAdapter.CollectorAlbumViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.collectorAlbum= albumscollector[position]
        }
        holder.bind(albumscollector[position])
    }

    override fun getItemCount(): Int {
        return albumscollector.size
    }


    class CollectorAlbumViewHolder(val viewDataBinding: CollectoralbumItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.collectoralbum_item
        }
        fun bind(collectoralbum: CollectorAlbum) {
            Glide.with(itemView)
                .load(collectoralbum.album.cover.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_broken_image))
                .into(viewDataBinding.albumCover)
        }
    }

}