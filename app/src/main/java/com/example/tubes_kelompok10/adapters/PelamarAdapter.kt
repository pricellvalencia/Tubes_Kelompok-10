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
import com.example.tubes_kelompok10.AddEditPelamarActivity
import com.example.tubes_kelompok10.PelamarActivity
import com.example.tubes_kelompok10.R
import com.example.tubes_kelompok10.models.Pelamar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class PelamarAdapter(private var pelamarList: List<Pelamar>, context: Context) : RecyclerView.Adapter<PelamarAdapter.ViewHolder>(), Filterable {

    private var filteredPelamarList: MutableList<Pelamar>
    private val context: Context

    init {
        filteredPelamarList = ArrayList(pelamarList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_pelamar, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredPelamarList.size
    }
    fun setPelamarList(pelamarList: Array<Pelamar>){
        this.pelamarList = pelamarList.toList()
        filteredPelamarList = pelamarList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val pelamar = filteredPelamarList[position]
        holder.tvNamaPelamar.text = pelamar.nama
        holder.tvEmailPelamar.text = pelamar.email
        holder.tvPendidikanPelamar.text = pelamar.pendidikan


        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data Pelamar ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") {_, _ ->
                    if (context is PelamarActivity) pelamar.id?.let { it1 ->
                        context.deletePelamar(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvPelamar.setOnClickListener {
            val i = Intent(context, AddEditPelamarActivity::class.java)
            i.putExtra("id", pelamar.id)
            if (context is PelamarActivity)
                context.startActivityForResult(i, PelamarActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                var charSequenceString = charSequence.toString()
                val filtered: MutableList<Pelamar> = java.util.ArrayList()
                if (charSequenceString.isEmpty()){
                    filtered.addAll(pelamarList)
                }else{
                    for (pelamar in pelamarList){
                        if (pelamar.nama.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        )filtered.add(pelamar)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredPelamarList.clear()
                filteredPelamarList.addAll((filterResults.values as List<Pelamar>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNamaPelamar: TextView
        var tvEmailPelamar: TextView
        var tvPendidikanPelamar: TextView
        var btnDelete: ImageButton
        var cvPelamar: CardView

        init {
            tvNamaPelamar = itemView.findViewById(R.id.tv_namaPelamar)
            tvEmailPelamar = itemView.findViewById(R.id.tv_emailPelamar)
            tvPendidikanPelamar = itemView.findViewById(R.id.tv_pendidiakanPelamar)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvPelamar = itemView.findViewById(R.id.item_pelamar)

        }
    }
}