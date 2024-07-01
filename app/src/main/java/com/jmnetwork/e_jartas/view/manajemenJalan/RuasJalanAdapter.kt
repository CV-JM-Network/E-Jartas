package com.jmnetwork.e_jartas.view.manajemenJalan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.databinding.ItemRuasJalanBinding
import com.jmnetwork.e_jartas.model.Location
import com.jmnetwork.e_jartas.model.RuasJalanData
import com.jmnetwork.e_jartas.model.RuasJalanRequest
import org.json.JSONArray

class RuasJalanAdapter : RecyclerView.Adapter<RuasJalanAdapter.ItemHolder>() {

    private var list = ArrayList<RuasJalanData>()

    fun setItem(item: List<RuasJalanData>?) {
        if (item == null) return
        val prevSize = list.size
        this.list.addAll(item)
        notifyItemRangeChanged(prevSize, item.size)
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

                var position = Location("", "")
                if (item.latLong != null) {
                    val rawLatLng = JSONArray(item.latLong).getJSONObject(0)
                    val lat = rawLatLng.getString("lat")
                    val lng = rawLatLng.getString("lng")
                    position = Location(lat, lng)
                }

                itemRuasJalan.setOnClickListener {
                    val bottomSheet = BottomSheetItemRuasJalan().apply {
                        val requestData = RuasJalanRequest(
                            item.idRuasJalan,
                            item.noRuas,
                            item.namaRuasJalan,
                            item.desa,
                            item.kecamatan,
                            item.panjang,
                            item.status,
                            item.tipe,
                            item.fungsi,
                            listOf(position),
                            listOf()
                        )
                        arguments = Bundle().apply {
                            putParcelable("ruasjalanData", requestData)
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
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}
