package com.jmnetwork.e_jartas.view.manajemenJalan

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
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
import com.jmnetwork.e_jartas.model.LocationItem
import com.jmnetwork.e_jartas.utils.CustomHandler
import com.jmnetwork.e_jartas.utils.Utils
import com.jmnetwork.e_jartas.view.MainActivity
import com.jmnetwork.e_jartas.viewModel.ManajemenJalanViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory
import es.dmoral.toasty.Toasty


class EditRuasJalanFragment : Fragment(), OnMapReadyCallback {

    private lateinit var _binding: FormRuasJalanBinding
    private val binding get() = _binding
    private lateinit var viewModel: ManajemenJalanViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var client: FusedLocationProviderClient

    private var latLng: LatLng? = null
    private var currentMarker: Marker? = null
    private var idRuasJalan: Int = 0

    companion object {
        const val ID_RUAS_JALAN = "id_ruas_jalan"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FormRuasJalanBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory)[ManajemenJalanViewModel::class.java]
        client = LocationServices.getFusedLocationProviderClient(requireActivity())

        idRuasJalan = requireArguments().getInt(ID_RUAS_JALAN)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // TODO: Implement the back button action properly
                if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }//                requireActivity().finish()
            }
        })

        val ruasJalanDatas = viewModel.ruasJalanData.value
        val ruasJalanData = ruasJalanDatas?.values?.find { it.idRuasJalan == idRuasJalan }

        binding.apply {
            bindProgressButton(btnRuasJalan)
            tvTitle.text = getString(R.string.edit_ruas_jalan)
            btnRuasJalan.text = getString(R.string.edit_ruas_jalan)

            viewModel.apply {
                if (ruasJalanData != null) {
                    if (ruasJalanData.latLong != null) {
                        latLng = Utils().latLongConverter(ruasJalanData.latLong)
                    }

                    inputNomorRuasJalan.setText(ruasJalanData.noRuas)
                    inputNamaRuasJalan.setText(ruasJalanData.namaRuasJalan)
                    inputPanjangRuasJalan.setText(ruasJalanData.panjang)

                    kecamatanSpinner.observe(viewLifecycleOwner) { response ->
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

                    desaSpinner.observe(viewLifecycleOwner) { response ->
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

                    tipeSpinner.observe(viewLifecycleOwner) { response ->
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

                    fungsiSpinner.observe(viewLifecycleOwner) { response ->
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

                    statusSpinner.observe(viewLifecycleOwner) { response ->
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
                        (requireActivity()).supportFragmentManager
                            .beginTransaction()
                            .replace(id, mapFragment)
                            .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit()
                    }
                    mapFragment.getMapAsync(this@EditRuasJalanFragment)
                } else {
                    Toasty.error(requireContext(), "Data tidak ditemukan").show()
                    CustomHandler().responseHandler(requireContext(), "EditRuasJalanFragment", "Data tidak ditemukan")
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

                val location = Location().apply {
                    add(LocationItem(latLng?.latitude.toString(), latLng?.longitude.toString()))
                }

                val validate = viewModel.setRequestData(
                    idRuasJalan,
                    inputNoRuas,
                    inputNamaRuas,
                    inputDesa,
                    inputKecamatan,
                    inputPanjangRuas,
                    inputStatus,
                    inputTipe,
                    inputFungsi,
                    location,
                    emptyList()
                )
                btnRuasJalan.attachTextChangeAnimator()
                btnRuasJalan.showProgress()

                if (validate != "") {
                    Toasty.error(requireContext(), validate, Toasty.LENGTH_SHORT).show()
                    btnRuasJalan.hideProgress(R.string.edit_ruas_jalan)
                } else {
                    viewModel.editRuasJalan(idRuasJalan) { status, message ->
                        when (status) {
                            "success" -> {
                                Toasty.success(requireContext(), message, Toasty.LENGTH_SHORT).show()
                                requireActivity().onBackPressedDispatcher.onBackPressed()
                            }

                            else -> {
                                Toasty.error(requireContext(), message, Toasty.LENGTH_SHORT).show()
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
        val fragmentView = childFragmentManager.findFragmentById(R.id.ruas_jalan_map_frame)?.view

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
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
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