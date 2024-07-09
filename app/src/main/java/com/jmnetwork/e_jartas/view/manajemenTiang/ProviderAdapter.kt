package com.jmnetwork.e_jartas.view.manajemenTiang

import android.annotation.SuppressLint
import android.graphics.Color
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
import com.jmnetwork.e_jartas.databinding.ItemProviderBinding
import com.jmnetwork.e_jartas.model.ProviderData
import com.jmnetwork.e_jartas.utils.Utils
import org.json.JSONArray

class ProviderAdapter : RecyclerView.Adapter<ProviderAdapter.ItemHolder>(), Filterable {

    private var list = ArrayList<ProviderData>()
    private var listFiltered = ArrayList<ProviderData>()

    fun setItem(item: List<ProviderData>?) {
        if (item == null) return
        this.list.clear()
        this.listFiltered.clear()
        val prevSize = list.size
        this.list.addAll(item)
        this.listFiltered = ArrayList(this.list) // Make a copy of the original list
        notifyItemRangeChanged(prevSize, item.size)
    }

    class ItemHolder(private val binding: ItemProviderBinding) : RecyclerView.ViewHolder(binding.root) {
        private var linearLayout: LinearLayout? = null

        fun bind(item: ProviderData) {
            binding.apply {
                tvNamaProvider.text = Utils().capitalizeFirstCharacter(item.provider)
                tvAlamatProvider.text = itemView.context.getString(R.string.alamat_provider, Utils().capitalizeFirstCharacter(item.alamat))
                tvJmlTiang.text = itemView.context.getString(R.string.jml_tiang, item.jumlahKepemilikanTiang)
                tvBlacklist.text = itemView.context.getString(R.string.blacklist, item.blackList)
                if (item.blackList == "ya") {
                    tvBlacklist.setTextColor(Color.RED)
                } else {
                    val colorRes = itemView.context.resources.getIdentifier("colorOnTertiaryContainer", "attr", itemView.context.packageName)
                    tvBlacklist.setTextColor(Color.parseColor("#${Integer.toHexString(itemView.context.theme.obtainStyledAttributes(intArrayOf(colorRes)).getColor(0, Color.BLACK))}"))
                }

                if (item.additional != "[]") {
                    tvInformasiTambahan.visibility = View.VISIBLE

                    val jsonArray = JSONArray(item.additional)
                    val additionalInfoList = mutableListOf<Pair<String, String>>()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        additionalInfoList.add(Pair(jsonObject.getString("parameter"), jsonObject.getString("value")))
                    }

                    linearLayout = LinearLayout(itemView.context).apply {
                        id = View.generateViewId()
                        orientation = LinearLayout.VERTICAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }

                    for ((parameter, value) in additionalInfoList) {
                        val textView = TextView(itemView.context).apply {
                            text = itemView.context.getString(R.string.informasi_tambahan_item, parameter, value)
                            textSize = 16f
                            // get color from ?attr/colorOnSecondaryContainer
                            val colorRes = itemView.context.resources.getIdentifier("colorOnSecondaryContainer", "attr", itemView.context.packageName)
                            setTextColor(Color.parseColor("#${Integer.toHexString(itemView.context.theme.obtainStyledAttributes(intArrayOf(colorRes)).getColor(0, Color.BLACK))}"))
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(0, 8, 0, 0) // Add some top margin between the TextViews
                            }
                        }
                        linearLayout!!.addView(textView)
                    }

                    (tvInformasiTambahan.parent as ConstraintLayout).addView(linearLayout)
                    ConstraintSet().apply {
                        clone(tvInformasiTambahan.parent as ConstraintLayout)
                        connect(linearLayout!!.id, ConstraintSet.TOP, tvInformasiTambahan.id, ConstraintSet.BOTTOM, 8)
                        connect(linearLayout!!.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 8)
                        applyTo(tvInformasiTambahan.parent as ConstraintLayout)
                    }
                } else {
                    binding.tvInformasiTambahan.visibility = View.GONE
                }
            }
        }

        fun clearLinearLayout() {
            // Remove all views from the LinearLayout when the view is recycled
            linearLayout?.removeAllViews()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemProviderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                    val resultList = ArrayList<ProviderData>()
                    for (row in list) { // Filter the original list
                        if (row.provider.lowercase().contains(charSearch.lowercase())) {
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
                listFiltered = results?.values as? ArrayList<ProviderData> ?: arrayListOf()
//                Log.d("Filter2", "count: ${listFiltered.size}; item: $listFiltered")
                notifyDataSetChanged()
            }
        }
    }
}
