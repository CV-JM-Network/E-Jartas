package com.jmnetwork.e_jartas.view.manajemenJalan

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.databinding.FormRuasJalanBinding
import com.jmnetwork.e_jartas.model.Location
import com.jmnetwork.e_jartas.utils.CustomHandler
import com.jmnetwork.e_jartas.utils.MySharedPreferences
import com.jmnetwork.e_jartas.viewModel.ManajemenJalanViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory
import es.dmoral.toasty.Toasty

class EditRuasJalanActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: FormRuasJalanBinding
    private lateinit var viewModel: ManajemenJalanViewModel
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var mMap: GoogleMap
    private lateinit var client: FusedLocationProviderClient

    private var idRuasJalan: Int = 0
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
        viewModel = ViewModelProvider(this@EditRuasJalanActivity, factory)[ManajemenJalanViewModel::class.java]
        myPreferences = MySharedPreferences(this@EditRuasJalanActivity)
        client = LocationServices.getFusedLocationProviderClient(this@EditRuasJalanActivity)

        idRuasJalan = intent.getIntExtra("idRuasJalan", -1)

//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                when (intent.getStringExtra(AddRuasJalanActivity.EXTRA_SOURCE_ACTIVITY)) {
//                    "ruas" -> {
//                        startActivity(Intent(this@EditRuasJalanActivity, RuasJalanActivity::class.java))
//                        finish()
//                    }
//
//                    else -> {
//                        startActivity(Intent(this@EditRuasJalanActivity, MainActivity::class.java).putExtra(MainActivity.EXTRA_FRAGMENT, "home"))
//                        finish()
//                    }
//                }
//            }
//        })

        binding.apply {
            tvTitle.text = getString(R.string.edit_ruas_jalan)

            viewModel.apply {
                if (idRuasJalan != -1) {
                    ruasJalanData.observe(this@EditRuasJalanActivity) { ruasJalanData ->
                        val currentData = ruasJalanData.data.find { it.idRuasJalan == idRuasJalan }
                        if (currentData != null) {
                            inputNomorRuasJalan.setText(currentData.noRuas)
                            inputNamaRuasJalan.setText(currentData.namaRuasJalan)
                            inputPanjangRuasJalan.setText(currentData.panjang)

                            kecamatanSpinner.observe(this@EditRuasJalanActivity) { response ->
                                val listData = ArrayList<String>()
                                for (i in 0 until response.data.size) {
                                    listData.add(response.data[i].kecamatan)
                                }
                                listData.sort()
                                spinnerKecamatan.item = listData as List<String>?
                                val selectedData = response.data.indexOfFirst { it.kecamatan == currentData.kecamatan }
                            }

                            val latlng = Location(
                                latLng?.latitude.toString(),
                                latLng?.longitude.toString()
                            )
                        }
                    }
                } else {
                    Toasty.error(this@EditRuasJalanActivity, "Data tidak ditemukan").show()
                    CustomHandler().responseHandler(this@EditRuasJalanActivity, "EditRuasJalanActivity", "Data tidak ditemukan")
                }
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }
}