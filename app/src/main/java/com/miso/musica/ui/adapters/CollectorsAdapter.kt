package com.miso.musica.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.miso.musica.models.Collector
import com.miso.musica.ui.CollectorsFragmentDirections
import com.miso.musica.R
import com.miso.musica.databinding.CollectorItemBinding

class CollectorsAdapter : RecyclerView.Adapter<CollectorsAdapter.CollectorViewHolder>(){

    var collectors :List<Collector> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val withDataBinding: CollectorItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CollectorViewHolder.LAYOUT,
            parent,
            false)
        return CollectorViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.collector = collectors[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            val action = CollectorsFragmentDirections.actionCollectorsFragmentToAlbumsOfCollectorFragment(collectors[position].collectorId)
            // Navigate using that action
            holder.viewDataBinding.root.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return collectors.size
    }


    class CollectorViewHolder(val viewDataBinding: CollectorItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.collector_item
        }
    }


}