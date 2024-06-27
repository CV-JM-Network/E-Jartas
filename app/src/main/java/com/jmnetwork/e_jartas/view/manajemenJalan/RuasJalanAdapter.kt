package com.jmnetwork.e_jartas.view.manajemenJalan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.databinding.ItemRuasJalanBinding
import com.jmnetwork.e_jartas.model.RuasJalanData

class RuasJalanAdapter : RecyclerView.Adapter<RuasJalanAdapter.ItemHolder>() {

    private var list = ArrayList<RuasJalanData>()

    fun setItem(item: List<RuasJalanData>?) {
        if (item == null) return
        val prevSize = list.size
        this.list.addAll(item)
        notifyItemRangeChanged(prevSize, item.size)
    }

    inner class ItemHolder(private val binding: ItemRuasJalanBinding) : RecyclerView.ViewHolder(binding.root) {

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

//                var position = LatLng(0.0, 0.0)
//                if (item.latlong != null) {
//                    val rawLatLng = JSONArray(item.latlong).getJSONObject(0)
//                    val lat = rawLatLng.getString("lat").toDouble()
//                    val lng = rawLatLng.getString("lng").toDouble()
//                    position = LatLng(lat, lng)
//                }
//
//                btnDetail.setOnClickListener {
//                    selectedPosition = if (position.latitude == 0.0 && position.longitude == 0.0) null else position
//                    showBottomSheet()
//                }
            }
        }

//        private fun showBottomSheet() {
//            val bottomSheetBinding = BottomSheetRuasDetailBinding.inflate(LayoutInflater.from(itemView.context))
//            val dialog = BottomSheetDialog(itemView.context).apply {
//                setCancelable(true)
//                setContentView(bottomSheetBinding.root)
//            }
//
//            val mapFragment = SupportMapFragment.newInstance()
//            bottomSheetBinding.ruasDetilMapFrame.apply {
//                (dialog.context as AppCompatActivity).supportFragmentManager
//                    .beginTransaction()
//                    .replace(id, mapFragment)
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                    .commitNow()
//            }
//
//            mapFragment.getMapAsync(this@RuasJalanAdapter)
//
//            dialog.show()
//        }
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
