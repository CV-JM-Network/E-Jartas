package com.jmnetwork.e_jartas.view.manajemenJalan

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
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
import com.jmnetwork.e_jartas.model.RuasJalanRequest
import com.jmnetwork.e_jartas.utils.CustomHandler
import com.jmnetwork.e_jartas.utils.MySharedPreferences
import com.jmnetwork.e_jartas.viewModel.ManajemenJalanViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory
import es.dmoral.toasty.Toasty

@Suppress("DEPRECATION")
class EditRuasJalanActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: FormRuasJalanBinding
    private lateinit var viewModel: ManajemenJalanViewModel
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var mMap: GoogleMap
    private lateinit var client: FusedLocationProviderClient

    private var latLng: LatLng? = null
    private var currentMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FormRuasJalanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this@EditRuasJalanActivity, factory)[ManajemenJalanViewModel::class.java]
        myPreferences = MySharedPreferences(this@EditRuasJalanActivity)
        client = LocationServices.getFusedLocationProviderClient(this@EditRuasJalanActivity)

        val ruasJalanData = intent.getParcelableExtra<RuasJalanRequest>("ruasJalanData")
        val idRuasJalan = ruasJalanData?.idRuasJalan

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@EditRuasJalanActivity, RuasJalanActivity::class.java))
                finish()
            }
        })

        binding.apply {
            bindProgressButton(btnRuasJalan)
            tvTitle.text = getString(R.string.edit_ruas_jalan)
            btnRuasJalan.text = getString(R.string.edit_ruas_jalan)

            viewModel.apply {
                if (idRuasJalan != -1) {
                    if (ruasJalanData != null) {
                        if (ruasJalanData.latLong[0].lat != "" && ruasJalanData.latLong[0].lng != "") {
                            latLng = LatLng(ruasJalanData.latLong[0].lat.toDouble(), ruasJalanData.latLong[0].lng.toDouble())
                        }
                        inputNomorRuasJalan.setText(ruasJalanData.noRuas)
                        inputNamaRuasJalan.setText(ruasJalanData.namaRuasJalan)
                        inputPanjangRuasJalan.setText(ruasJalanData.panjang)

                        kecamatanSpinner.observe(this@EditRuasJalanActivity) { response ->
                            val listData = ArrayList<String>()
                            for (i in 0 until response.data.size) {
                                if (response.data[i].kecamatan != ruasJalanData.kecamatan) {
                                    listData.add(response.data[i].kecamatan)
                                }
                            }
                            listData.sort()
                            listData.add(0, ruasJalanData.kecamatan) // Add the selected kecamatan to the first index
                            spinnerKecamatan.item = listData as List<String>?
                            spinnerKecamatan.setSelection(0)
                        }

                        desaSpinner.observe(this@EditRuasJalanActivity) { response ->
                            val listData = ArrayList<String>()
                            for (i in 0 until response.data.size) {
                                if (response.data[i].desa != ruasJalanData.desa) {
                                    listData.add(response.data[i].desa)
                                }
                            }
                            listData.sort()
                            listData.add(0, ruasJalanData.desa) // Add the selected desa to the first index
                            spinnerDesa.item = listData as List<String>?
                            spinnerDesa.setSelection(0)
                        }

                        tipeSpinner.observe(this@EditRuasJalanActivity) { response ->
                            val listData = ArrayList<String>()
                            for (i in 0 until response.data.size) {
                                if (response.data[i].tipe != ruasJalanData.tipe) {
                                    listData.add(response.data[i].tipe)
                                }
                            }
                            listData.sort()
                            listData.add(0, ruasJalanData.tipe) // Add the selected tipe to the first index
                            spinnerTipe.item = listData as List<String>?
                            spinnerTipe.setSelection(0)
                        }

                        fungsiSpinner.observe(this@EditRuasJalanActivity) { response ->
                            val listData = ArrayList<String>()
                            for (i in 0 until response.data.size) {
                                if (response.data[i].fungsi != ruasJalanData.fungsi) {
                                    listData.add(response.data[i].fungsi)
                                }
                            }
                            listData.sort()
                            listData.add(0, ruasJalanData.fungsi) // Add the selected fungsi to the first index
                            spinnerFungsi.item = listData as List<String>?
                            spinnerFungsi.setSelection(0)
                        }

                        statusSpinner.observe(this@EditRuasJalanActivity) { response ->
                            val listData = ArrayList<String>()
                            for (i in 0 until response.data.size) {
                                if (response.data[i].status != ruasJalanData.status) {
                                    listData.add(response.data[i].status)
                                }
                            }
                            listData.sort()
                            listData.add(0, ruasJalanData.status) // Add the selected status to the first index
                            spinnerStatus.item = listData as List<String>?
                            spinnerStatus.setSelection(0)
                        }

                        val mapFragment = SupportMapFragment.newInstance()
                        ruasJalanMapFrame.apply {
                            (this@EditRuasJalanActivity as AppCompatActivity).supportFragmentManager
                                .beginTransaction()
                                .replace(id, mapFragment)
                                .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commitNow()
                        }
                        mapFragment.getMapAsync(this@EditRuasJalanActivity)
                    }
                } else {
                    Toasty.error(this@EditRuasJalanActivity, "Data tidak ditemukan").show()
                    CustomHandler().responseHandler(this@EditRuasJalanActivity, "EditRuasJalanActivity", "Data tidak ditemukan")
                }
            }

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
                    idRuasJalan!!,
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
                    Toasty.error(this@EditRuasJalanActivity, validate, Toasty.LENGTH_SHORT).show()
                    btnRuasJalan.hideProgress(R.string.edit_ruas_jalan)
                } else {
                    viewModel.editRuasJalan(idRuasJalan) { status, message ->
                        when (status) {
                            "success" -> {
                                Toasty.success(this@EditRuasJalanActivity, message, Toasty.LENGTH_SHORT).show()
                                onBackPressedDispatcher.onBackPressed()
                            }

                            else -> {
                                Toasty.error(this@EditRuasJalanActivity, message, Toasty.LENGTH_SHORT).show()
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
            uiSettings.apply {
                isScrollGesturesEnabled = false
                isZoomGesturesEnabled = true
                isScrollGesturesEnabledDuringRotateOrZoom = false
                isMyLocationButtonEnabled = true
            }
            mapType = GoogleMap.MAP_TYPE_SATELLITE
        }

        if (latLng != null) {
            drawMarker()
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 15f))
        } else {
            client.lastLocation.addOnSuccessListener { location ->
                Priority.PRIORITY_HIGH_ACCURACY
                if (location != null) {
                    latLng = LatLng(location.latitude, location.longitude)
                    drawMarker()
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 15f))
                }
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