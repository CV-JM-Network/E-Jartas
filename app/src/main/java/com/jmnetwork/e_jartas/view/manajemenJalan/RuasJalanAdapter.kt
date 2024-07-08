package com.jmnetwork.e_jartas.view.manajemenJalan

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.databinding.ItemRuasJalanBinding
import com.jmnetwork.e_jartas.model.RuasJalanData

class RuasJalanAdapter : RecyclerView.Adapter<RuasJalanAdapter.ItemHolder>(), Filterable {

    private var list = ArrayList<RuasJalanData>()
    private var listFiltered = ArrayList<RuasJalanData>()

    fun setItem(item: List<RuasJalanData>?) {
        if (item == null) return
        val prevSize = list.size
        this.list.addAll(item)
        this.listFiltered = ArrayList(this.list) // Make a copy of the original list
        notifyItemRangeChanged(prevSize, item.size)
//        Log.d("Original", "count: ${list.size}; item: $list")
//        Log.d("Filtered", "count: ${listFiltered.size}; item: $listFiltered")
    }

    class ItemHolder(private val binding: ItemRuasJalanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RuasJalanData) {
            binding.apply {
                tvNomorRuasJalan.text = itemView.context.getString(R.string.nomor_ruas_jalan, item.noRuas)
                tvNamaRuasJalan.text = item.namaRuasJalan
                tvKecamatan.text = itemView.context.getString(R.string.kecamatan, item.kecamatan)
                tvDesa.text = itemView.context.getString(R.string.desa, item.desa)
                tvPanjang.text = itemView.context.getString(R.string.panjang_jalan, item.panjang)
                tvStatus.text = itemView.context.getString(R.string.status, item.status)
                tvTipe.text = itemView.context.getString(R.string.tipe, item.tipe)
                tvFungsi.text = itemView.context.getString(R.string.fungsi, item.fungsi)

                itemRuasJalan.setOnClickListener {
                    val bottomSheet = BottomSheetItemRuasJalan().apply {
                        arguments = Bundle().apply {
                            putInt("idRuasJalan", item.idRuasJalan)
                        }
                    }
                    bottomSheet.show((itemView.context as AppCompatActivity).supportFragmentManager, bottomSheet.tag)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemRuasJalanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = listFiltered[position] // Use the filtered list
        holder.bind(item)
    }

    override fun getItemCount(): Int = listFiltered.size // Return the size of the filtered list

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint == null || constraint.length < 0) {
                    filterResults.count = list.size
                    filterResults.values = ArrayList(list) // Make a copy of the original list
                } else {
                    val charSearch = constraint.toString()
                    val resultList = ArrayList<RuasJalanData>()
                    for (row in list) { // Filter the original list
                        if (row.namaRuasJalan.lowercase().contains(charSearch.lowercase())
                            || row.noRuas.lowercase().contains(charSearch.lowercase())
                        ) {
                            resultList.add(row)
                        }
                    }
                    filterResults.count = resultList.size
                    filterResults.values = resultList
//                    Log.d("Filter1", "count: ${filterResults.count}; item: ${filterResults.values}")
                }
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listFiltered = results?.values as? ArrayList<RuasJalanData> ?: arrayListOf()
//                Log.d("Filter2", "count: ${listFiltered.size}; item: $listFiltered")
                notifyDataSetChanged()
            }
        }
    }
}
