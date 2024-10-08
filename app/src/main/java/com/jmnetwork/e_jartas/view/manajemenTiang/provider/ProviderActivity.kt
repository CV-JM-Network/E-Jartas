package com.jmnetwork.e_jartas.view.manajemenTiang.provider

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.databinding.ActivityProviderBinding
import com.jmnetwork.e_jartas.viewModel.ManajemenTiangViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory

class ProviderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProviderBinding
    lateinit var viewModel: ManajemenTiangViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProviderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(this@ProviderActivity.application)
        viewModel = ViewModelProvider(this@ProviderActivity, factory)[ManajemenTiangViewModel::class.java]
        viewModel.getProvider(5, 1) // Get Provider data from API

        loadFragment(ListProviderFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.provider_fragment_container, fragment)
            .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}