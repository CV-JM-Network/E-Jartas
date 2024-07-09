package com.jmnetwork.e_jartas.view.manajemenJalan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.databinding.ActivityRuasJalanBinding
import com.jmnetwork.e_jartas.viewModel.ManajemenJalanViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory

class RuasJalanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRuasJalanBinding
    private lateinit var viewModel: ManajemenJalanViewModel

    companion object {
        const val DESTINATION = "destination"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRuasJalanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this@RuasJalanActivity, factory)[ManajemenJalanViewModel::class.java]
        viewModel.getRuasJalan(100, 1) // Get Ruas Jalan data from API

        if (intent.hasExtra(DESTINATION)) {
            when (intent.getStringExtra(DESTINATION)) {
                "list" -> {
                    loadFragment(ListRuasJalanFragment())
                }

                "add" -> {
                    loadFragment(AddRuasJalanFragment())
                }
            }
        } else {
            loadFragment(ListRuasJalanFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.ruas_jalan_fragment_container, fragment)
            .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}