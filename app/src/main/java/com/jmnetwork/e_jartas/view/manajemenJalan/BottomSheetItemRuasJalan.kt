package com.jmnetwork.e_jartas.view.manajemenJalan

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jmnetwork.e_jartas.databinding.BottomSheetRuasDetailBinding

class BottomSheetItemRuasJalan : BottomSheetDialogFragment(), OnMapReadyCallback {

    private lateinit var binding: BottomSheetRuasDetailBinding
    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView
    private var latLng: LatLng? = null
    private var namaRuasJalan: String = ""

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latLng = it.getParcelable("latLng", LatLng::class.java)
            namaRuasJalan = it.getString("namaRuasJalan").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetRuasDetailBinding.inflate(inflater, container, false)
        if (latLng == null) {
            binding.ruasDetilMap.visibility = View.GONE
        } else {
            binding.ruasDetilMap.visibility = View.VISIBLE
        }
        mapView = binding.ruasDetilMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding.btnHapusItem.setOnClickListener { }

        binding.btnEditItem.setOnClickListener { }

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (latLng != null) {
            with(mMap) {
                uiSettings.apply {
                    isScrollGesturesEnabled = false
                    isZoomGesturesEnabled = false
                }
                mapType = GoogleMap.MAP_TYPE_SATELLITE
                moveCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 17f))
                val markerOptions = MarkerOptions()
                    .position(latLng!!)
                    .title(namaRuasJalan)
                    .draggable(false)
                addMarker(markerOptions)?.showInfoWindow()
            }
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