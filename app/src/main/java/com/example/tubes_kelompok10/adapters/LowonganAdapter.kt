package com.example.tubes_kelompok10.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes_kelompok10.AddEditLowonganActivity
import com.example.tubes_kelompok10.LowonganActivity
import com.example.tubes_kelompok10.R
import com.example.tubes_kelompok10.models.Lowongan
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class LowonganAdapter(private var lowonganList: List<Lowongan>, context: Context) : RecyclerView.Adapter<LowonganAdapter.ViewHolder>(), Filterable {

    private var filteredLowonganList: MutableList<Lowongan>
    private val context: Context

    init {
        filteredLowonganList = ArrayList(lowonganList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_lowongan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredLowonganList.size
    }
    fun setLowonganList(lowonganList: Array<Lowongan>){
        this.lowonganList = lowonganList.toList()
        filteredLowonganList = lowonganList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val lowongan = filteredLowonganList[position]
        holder.tvNamaPerusahaan.text = lowongan.namaperusahaan
        holder.tvPosisi.text = lowongan.posisi
        holder.tvTanggalPenutupan.text = lowongan.tanggalpenutupan


        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data lowongan ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") {_, _ ->
                    if (context is LowonganActivity) lowongan.id?.let { it1 ->
                        context.deleteLowongan(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvLowongan.setOnClickListener {
            val i = Intent(context, AddEditLowonganActivity::class.java)
            i.putExtra("id", lowongan.id)
            if (context is LowonganActivity)
                context.startActivityForResult(i, LowonganActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                var charSequenceString = charSequence.toString()
                val filtered: MutableList<Lowongan> = java.util.ArrayList()
                if (charSequenceString.isEmpty()){
                    filtered.addAll(lowonganList)
                }else{
                    for (lowongan in lowonganList){
                        if (lowongan.namaperusahaan.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        )filtered.add(lowongan)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredLowonganList.clear()
                filteredLowonganList.addAll((filterResults.values as List<Lowongan>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNamaPerusahaan: TextView
        var tvPosisi: TextView
        var tvTanggalPenutupan: TextView
        var btnDelete: ImageButton
        var cvLowongan: CardView

        init {
            tvNamaPerusahaan = itemView.findViewById(R.id.tv_namaPerusahaan)
            tvPosisi = itemView.findViewById(R.id.tv_posisi)
            tvTanggalPenutupan = itemView.findViewById(R.id.tv_tanggalPenutupan)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvLowongan = itemView.findViewById(R.id.cv_lowongan)

        }
    }
}