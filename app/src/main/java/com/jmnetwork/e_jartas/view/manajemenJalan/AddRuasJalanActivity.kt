package com.jmnetwork.e_jartas.view.manajemenJalan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jmnetwork.e_jartas.databinding.ActivityAddRuasJalanBinding

class AddRuasJalanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRuasJalanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRuasJalanBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}