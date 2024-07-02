package com.jmnetwork.e_jartas.view.manajemenJalan

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.databinding.BottomSheetRuasDetailBinding
import com.jmnetwork.e_jartas.model.RuasJalanRequest
import com.jmnetwork.e_jartas.viewModel.ManajemenJalanViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import es.dmoral.toasty.Toasty

class BottomSheetItemRuasJalan : BottomSheetDialogFragment(), OnMapReadyCallback {

    private lateinit var binding: BottomSheetRuasDetailBinding
    private lateinit var viewModel: ManajemenJalanViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView

    private var ruasjalanData: RuasJalanRequest? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[ManajemenJalanViewModel::class.java]
        viewModel.ruasJalanData.observe(this) {
            Log.d("ruasJalanData|BottomSheet1", it.toString())
        }

        arguments?.let {
            ruasjalanData = it.getParcelable("ruasjalanData", RuasJalanRequest::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetRuasDetailBinding.inflate(inflater, container, false)

        mapView = binding.ruasDetilMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding.btnHapusItem.setOnClickListener {
            val dialog = MaterialDialog.Builder(requireActivity())
                .setTitle("Hapus Data")
                .setMessage(getString(R.string.confirm_delete))
                .setCancelable(true)

                .setPositiveButton(getString(R.string.no), R.drawable.ic_close) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .setNegativeButton(getString(R.string.yes), R.drawable.ic_trash) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    viewModel.deleteRuasJalan(ruasjalanData!!.idRuasJalan) { status, message ->
                        when (status) {
                            "success" -> {
                                Toasty.success(requireContext(), message, Toasty.LENGTH_SHORT).show()
//                                removeRuasJalanById(idRuasJalan)
                                dismiss()
                            }

                            else -> {
                                Toasty.error(requireContext(), message, Toasty.LENGTH_SHORT).show()
                                dismiss()
                            }
                        }
                    }
                }
                .build()
            dialog.show()
        }

        binding.btnEditItem.setOnClickListener {
            val intent = Intent(requireContext(), EditRuasJalanActivity::class.java)
            intent.putExtra("ruasJalanData", ruasjalanData)
            startActivity(intent)
            dismiss()
        }

        return binding.root
    }

//    private fun removeRuasJalanById(idruasJalan: Int) {
//        val currentList = viewModel.ruasJalanData.value?.data?.toMutableList()
//        val itemToRemove = currentList?.find { it.idruasJalan == idruasJalan }
//        if (itemToRemove != null) {
//            currentList.remove(itemToRemove)
//            viewModel.ruasJalanData.value = viewModel.ruasJalanData.value?.copy(data = currentList)
//        }
//    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latLng: LatLng? = ruasjalanData?.latLong?.getOrNull(0)?.let {
            val lat = it.lat.toDoubleOrNull()
            val lng = it.lng.toDoubleOrNull()
            if (lat != null && lng != null) {
                LatLng(lat, lng)
            } else {
                null
            }
        }

        latLng?.let { location ->
            binding.ruasDetilMap.visibility = View.VISIBLE
            with(mMap) {
                uiSettings.apply {
                    isScrollGesturesEnabled = false
                    isZoomGesturesEnabled = false
                }
                mapType = GoogleMap.MAP_TYPE_SATELLITE
                moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17f))
                val markerOptions = MarkerOptions()
                    .position(location)
                    .title(ruasjalanData?.namaRuasJalan ?: "")
                    .draggable(false)
                addMarker(markerOptions)?.showInfoWindow()
            }
        } ?: run {
            binding.ruasDetilMap.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

}