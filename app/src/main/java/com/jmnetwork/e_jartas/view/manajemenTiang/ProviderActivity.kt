package com.jmnetwork.e_jartas.view.manajemenTiang

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
    private lateinit var viewModel: ManajemenTiangViewModel

    companion object {
        const val DESTINATION = "destination"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProviderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(this@ProviderActivity.application)
        viewModel = ViewModelProvider(this@ProviderActivity, factory)[ManajemenTiangViewModel::class.java]
        viewModel.getProvider(10, 1) // Get Provider data from API

        if (intent.hasExtra(DESTINATION)) {
            when (intent.getStringExtra(DESTINATION)) {
                "list" -> {
                    loadFragment(ListProviderFragment())
                }
            }
        } else {
            loadFragment(ListProviderFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.provider_fragment_container, fragment)
            .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}