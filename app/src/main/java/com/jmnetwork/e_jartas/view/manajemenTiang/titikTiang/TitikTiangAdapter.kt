package com.jmnetwork.e_jartas.view.manajemenTiang.titikTiang

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.databinding.ItemTiangBinding
import com.jmnetwork.e_jartas.model.TiangData
import com.jmnetwork.e_jartas.model.TiangProvider
import com.jmnetwork.e_jartas.utils.Utils

class TitikTiangAdapter : RecyclerView.Adapter<TitikTiangAdapter.ItemHolder>(), Filterable {

    private var list = ArrayList<TiangData>()
    private var listFiltered = ArrayList<TiangData>()

    fun setItem(item: List<TiangData>?) {
        if (item == null) return
        this.list.clear()
        this.listFiltered.clear()
        val prevSize = list.size
        this.list.addAll(item)
        this.listFiltered = ArrayList(this.list) // Make a copy of the original list
        notifyItemRangeChanged(prevSize, item.size)
    }

    inner class ItemHolder(private val binding: ItemTiangBinding) : RecyclerView.ViewHolder(binding.root) {
        private var linearLayout: LinearLayout? = null

        fun bind(item: TiangData) {
            binding.apply {
                tvIdTiang.text = itemView.context.getString(R.string.id_tiang, item.idtiang)
                tvlTglBuat.text = itemView.context.getString(R.string.tanggal_buat, Utils().formatDate(item.createddate))
                tvTglData.text = itemView.context.getString(R.string.tanggal_data, Utils().formatDate(item.lastupdate))

                if (item.tiangProvider != TiangProvider()) {
                    linearLayout = LinearLayout(itemView.context).apply {
                        id = View.generateViewId()
                        orientation = LinearLayout.VERTICAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }

                    for (tiangProvider in item.tiangProvider) {
                        val inflater = LayoutInflater.from(itemView.context)
                        val tiangProviderView = inflater.inflate(R.layout.item_tiang_provider, linearLayout, false)

                        val number = tiangProviderView.findViewById<TextView>(R.id.tv_no_provider)
                        val provider = tiangProviderView.findViewById<TextView>(R.id.tv_provider)
                        val utilitas = tiangProviderView.findViewById<TextView>(R.id.tv_utilitas)
                        val catatan = tiangProviderView.findViewById<TextView>(R.id.tv_catatan)

                        number.text = itemView.context.getString(R.string.tiang_provider_number, item.tiangProvider.indexOf(tiangProvider) + 1)
                        provider.text = itemView.context.getString(R.string.tiang_provider, tiangProvider.provider)
                        utilitas.text = itemView.context.getString(R.string.tiang_utilitas, tiangProvider.utilitas)
                        catatan.text = itemView.context.getString(R.string.tiang_catatan, tiangProvider.catatan)

                        linearLayout!!.addView(tiangProviderView)
                    }

                    (tvListProvider.parent as ConstraintLayout).addView(linearLayout)
                    ConstraintSet().apply {
                        clone(tvListProvider.parent as ConstraintLayout)
                        connect(linearLayout!!.id, ConstraintSet.TOP, tvListProvider.id, ConstraintSet.BOTTOM)
                        connect(linearLayout!!.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                        applyTo(tvListProvider.parent as ConstraintLayout)
                    }
                }

            }
        }

        fun clearLinearLayout() {
            // Remove all views from the LinearLayout when the view is recycled
            linearLayout?.removeAllViews()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemTiangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = listFiltered[position] // Use the filtered list
        holder.bind(item)
    }

    override fun onViewRecycled(holder: ItemHolder) {
        super.onViewRecycled(holder)
        // Call the clearLinearLayout method when the view is recycled
        holder.clearLinearLayout()
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
                    val resultList = ArrayList<TiangData>()
                    for (row in list) { // Filter the original list
                        for (tiangProvider in row.tiangProvider) {
                            if (tiangProvider.provider.lowercase().contains(charSearch.lowercase())) {
                                resultList.add(row)
                            }
                        }
                    }
                    filterResults.count = resultList.size
                    filterResults.values = resultList
                }
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listFiltered = results?.values as? ArrayList<TiangData> ?: arrayListOf()
                notifyDataSetChanged()
            }
        }
    }
}
