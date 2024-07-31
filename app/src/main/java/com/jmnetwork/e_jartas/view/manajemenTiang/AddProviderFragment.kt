package com.jmnetwork.e_jartas.view.manajemenTiang

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.databinding.FormProviderBinding
import com.jmnetwork.e_jartas.view.MainActivity
import com.jmnetwork.e_jartas.viewModel.ManajemenTiangViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory
import es.dmoral.toasty.Toasty

class AddProviderFragment : Fragment() {

    private lateinit var _binding: FormProviderBinding
    private val binding get() = _binding
    private lateinit var viewModel: ManajemenTiangViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FormProviderBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory)[ManajemenTiangViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        })

        binding.apply {
            tvTitle.text = getString(R.string.add_provider)
            bindProgressButton(btnTambahProvider)

            btnBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            btnTambahProvider.setOnClickListener {
                btnTambahProvider.attachTextChangeAnimator()
                btnTambahProvider.showProgress()

                val namaProvider = inputNamaProvider.text.toString()
                val alamatProvider = inputAlamatProvider.text.toString()

                val validate = viewModel.setRequestData(
                    emptyList(),
                    alamatProvider,
                    namaProvider
                )

                if (validate.isEmpty()) {
                    viewModel.addProvider { status, message ->
                        when (status) {
                            "success" -> {
                                Toasty.success(requireContext(), message, Toasty.LENGTH_SHORT).show()
                                requireActivity().onBackPressedDispatcher.onBackPressed()
                            }

                            else -> {
                                Toasty.error(requireContext(), message, Toasty.LENGTH_SHORT).show()
                                btnTambahProvider.hideProgress(R.string.add_provider)
                            }
                        }
                    }
                } else {
                    Toasty.error(requireContext(), validate, Toasty.LENGTH_SHORT).show()
                    btnTambahProvider.hideProgress(R.string.add_provider)
                }
            }
        }
    }
}