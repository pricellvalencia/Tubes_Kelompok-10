package com.example.tubes_kelompok10

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes_kelompok10.entity.Home

class RVHome(private val data: Array<Home>) : RecyclerView.Adapter<RVHome.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_home, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int){
        val currentItem = data[position]
        holder.tvNamaPerusahan.text = currentItem.name
        holder.tvDetailPerusahaan.text = currentItem.position
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView : View) :  RecyclerView.ViewHolder(itemView){
        val tvNamaPerusahan : TextView = itemView.findViewById(R.id.tv_nama_perusahaan)
        val tvDetailPerusahaan : TextView = itemView.findViewById(R.id.tv_details_perusahaan)
    }

}