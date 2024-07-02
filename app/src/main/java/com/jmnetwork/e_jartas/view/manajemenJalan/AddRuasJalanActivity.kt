package com.jmnetwork.e_jartas.view.manajemenJalan

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.databinding.FormRuasJalanBinding
import com.jmnetwork.e_jartas.model.Location
import com.jmnetwork.e_jartas.utils.MySharedPreferences
import com.jmnetwork.e_jartas.view.MainActivity
import com.jmnetwork.e_jartas.viewModel.ManajemenJalanViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory
import es.dmoral.toasty.Toasty

class AddRuasJalanActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: FormRuasJalanBinding
    private lateinit var viewModel: ManajemenJalanViewModel
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var mMap: GoogleMap
    private lateinit var client: FusedLocationProviderClient

    private var latLng: LatLng? = null
    private var currentMarker: Marker? = null

    companion object {
        const val EXTRA_SOURCE_ACTIVITY = "extra_source_activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FormRuasJalanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this@AddRuasJalanActivity, factory)[ManajemenJalanViewModel::class.java]
        myPreferences = MySharedPreferences(this@AddRuasJalanActivity)
        client = LocationServices.getFusedLocationProviderClient(this@AddRuasJalanActivity)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (intent.getStringExtra(EXTRA_SOURCE_ACTIVITY)) {
                    "ruas" -> {
                        startActivity(Intent(this@AddRuasJalanActivity, RuasJalanActivity::class.java))
                        finish()
                    }

                    else -> {
                        startActivity(Intent(this@AddRuasJalanActivity, MainActivity::class.java).putExtra(MainActivity.EXTRA_FRAGMENT, "home"))
                        finish()
                    }
                }
            }
        })

        binding.apply {
            bindProgressButton(btnRuasJalan)
            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            viewModel.kecamatanSpinner.observe(this@AddRuasJalanActivity) { response ->
                val listData = ArrayList<String>()
                for (i in 0 until response.data.size) {
                    listData.add(response.data[i].kecamatan)
                }
                listData.sort()
                spinnerKecamatan.item = listData as List<String>?
            }

            viewModel.desaSpinner.observe(this@AddRuasJalanActivity) { response ->
                val listData = ArrayList<String>()
                for (i in 0 until response.data.size) {
                    listData.add(response.data[i].desa)
                }
                listData.sort()
                spinnerDesa.item = listData as List<String>?
            }

            viewModel.statusSpinner.observe(this@AddRuasJalanActivity) { response ->
                val listData = ArrayList<String>()
                for (i in 0 until response.data.size) {
                    listData.add(response.data[i].status)
                }
                listData.sort()
                spinnerStatus.item = listData as List<String>?
            }

            viewModel.tipeSpinner.observe(this@AddRuasJalanActivity) { response ->
                val listData = ArrayList<String>()
                for (i in 0 until response.data.size) {
                    listData.add(response.data[i].tipe)
                }
                listData.sort()
                spinnerTipe.item = listData as List<String>?
            }

            viewModel.fungsiSpinner.observe(this@AddRuasJalanActivity) { response ->
                val listData = ArrayList<String>()
                for (i in 0 until response.data.size) {
                    listData.add(response.data[i].fungsi)
                }
                listData.sort()
                spinnerFungsi.item = listData as List<String>?
            }

            val mapFragment = SupportMapFragment.newInstance()
            ruasJalanMapFrame.apply {
                (this@AddRuasJalanActivity as AppCompatActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(id, mapFragment)
                    .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commitNow()
            }
            mapFragment.getMapAsync(this@AddRuasJalanActivity)

            btnRuasJalan.setOnClickListener {
                val inputNoRuas = inputNomorRuasJalan.text.toString()
                val inputNamaRuas = inputNamaRuasJalan.text.toString()
                val inputPanjangRuas = inputPanjangRuasJalan.text.toString()
                val inputKecamatan = spinnerKecamatan.selectedItem.takeIf { it != null }?.toString().orEmpty()
                val inputDesa = spinnerDesa.selectedItem.takeIf { it != null }?.toString().orEmpty()
                val inputStatus = spinnerStatus.selectedItem.takeIf { it != null }?.toString().orEmpty()
                val inputTipe = spinnerTipe.selectedItem.takeIf { it != null }?.toString().orEmpty()
                val inputFungsi = spinnerFungsi.selectedItem.takeIf { it != null }?.toString().orEmpty()

                val latlng = Location(
                    latLng?.latitude.toString(),
                    latLng?.longitude.toString()
                )

                val validate = viewModel.setAddRuasJalanRequest(
                    0,
                    inputNoRuas,
                    inputNamaRuas,
                    inputDesa,
                    inputKecamatan,
                    inputPanjangRuas,
                    inputStatus,
                    inputTipe,
                    inputFungsi,
                    listOf(latlng),
                    emptyList()
                )
                btnRuasJalan.attachTextChangeAnimator()
                btnRuasJalan.showProgress()

                if (validate != "") {
                    Toasty.error(this@AddRuasJalanActivity, validate, Toasty.LENGTH_SHORT).show()
                    btnRuasJalan.hideProgress(R.string.tambah_ruas_jalan)
                } else {
                    viewModel.addRuasJalan { status, message ->
                        when (status) {
                            "success" -> {
                                Toasty.success(this@AddRuasJalanActivity, message, Toasty.LENGTH_SHORT).show()
                                onBackPressedDispatcher.onBackPressed()
                            }

                            else -> {
                                Toasty.error(this@AddRuasJalanActivity, message, Toasty.LENGTH_SHORT).show()
                                btnRuasJalan.hideProgress(R.string.tambah_ruas_jalan)
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Get the fragment view of the Google Map
        val fragmentView = (supportFragmentManager.findFragmentById(R.id.ruas_jalan_map_frame) as SupportMapFragment).view

        // Set an OnTouchListener on the fragment view
        fragmentView?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Disable the ScrollView when the Google Map is touched
                    binding.scrollView.requestDisallowInterceptTouchEvent(true)
                }

                MotionEvent.ACTION_UP -> {
                    // Enable the ScrollView when the touch is released
                    binding.scrollView.requestDisallowInterceptTouchEvent(false)
                    v.performClick() // Call performClick when a click is detected
                }
            }
            false
        }

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
            return
        }

        with(mMap) {
            isMyLocationEnabled = true
            uiSettings.apply {
                isScrollGesturesEnabled = false
                isZoomGesturesEnabled = true
                isScrollGesturesEnabledDuringRotateOrZoom = false
                isMyLocationButtonEnabled = true
            }
            mapType = GoogleMap.MAP_TYPE_SATELLITE
        }

        client.lastLocation.addOnSuccessListener { location ->
            Priority.PRIORITY_HIGH_ACCURACY
            if (location != null) {
                latLng = LatLng(location.latitude, location.longitude)
                drawMarker()
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 15f))
            }
        }

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}

            override fun onMarkerDrag(marker: Marker) {}

            override fun onMarkerDragEnd(marker: Marker) {
                currentMarker?.remove()
                latLng = marker.position
                drawMarker()
            }
        })

        mMap.setOnMapLongClickListener { latLng ->
            currentMarker?.remove()
            this.latLng = latLng
            drawMarker()
        }

        mMap.setOnMyLocationClickListener {
            client.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    // Remove the old marker
                    currentMarker?.remove()

                    // Update the LatLng object with the current location
                    latLng = LatLng(location.latitude, location.longitude)

                    // Draw a new marker on the map
                    drawMarker()
                }
            }
        }
    }

    private fun drawMarker() {
        val markerOpt = MarkerOptions()
            .position(latLng!!)
            .title("Lat: ${latLng?.latitude}, Long: ${latLng?.longitude}")
            .draggable(true)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng!!))
        currentMarker = mMap.addMarker(markerOpt)
        currentMarker?.showInfoWindow()
    }
}