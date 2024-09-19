package com.jmnetwork.e_jartas.view.manajemenTiang.titikTiang

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.databinding.ActivityTitikTiangBinding
import com.jmnetwork.e_jartas.viewModel.ManajemenTiangViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory

class TitikTiangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTitikTiangBinding
    lateinit var viewModel: ManajemenTiangViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTitikTiangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(this@TitikTiangActivity.application)
        viewModel = ViewModelProvider(this@TitikTiangActivity, factory)[ManajemenTiangViewModel::class.java]
        viewModel.getTitikTiang(5, 1) // Get Titik Tiang data from API

        loadFragment(ListTitikTiangFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.titik_tiang_fragment_container, fragment)
            .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}