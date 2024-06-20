package com.jmnetwork.e_jartas.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jmnetwork.e_jartas.databinding.FragmentHomeBinding
import com.jmnetwork.e_jartas.viewModel.HomeViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory
import java.util.Locale

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        binding.apply {
            viewModel.getDashboard()
            viewModel.dashboardData.observe(viewLifecycleOwner) { data ->
                tvTiang.text = data.tiangTerdata.toString()
                tvTitikLokasi.text = formatNumber(data.titikTiang)
                tvJalan.text = formatNumber(data.jumlahRuasJalan)
                tvPanjang.text = formatNumber(data.panjangRuasJalan)
                tvProvider.text = formatNumber(data.jumlahProvider)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun formatNumber(number: Int): String {
        return String.format(Locale("id", "ID"), "%,d", number).replace(',', '.')
    }

}