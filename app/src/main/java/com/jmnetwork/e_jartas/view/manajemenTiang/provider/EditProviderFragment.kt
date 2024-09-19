package com.jmnetwork.e_jartas.view.manajemenTiang.provider

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
import com.jmnetwork.e_jartas.model.Additional
import com.jmnetwork.e_jartas.view.MainActivity
import com.jmnetwork.e_jartas.viewModel.ManajemenTiangViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory
import es.dmoral.toasty.Toasty


class EditProviderFragment : Fragment() {

    private lateinit var _binding: FormProviderBinding
    private val binding get() = _binding
    private lateinit var viewModel: ManajemenTiangViewModel

    private var idProvider: Int = 0

    companion object {
        const val ID_PROVIDER = "id_provider"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FormProviderBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory)[ManajemenTiangViewModel::class.java]

        idProvider = requireArguments().getInt(ID_PROVIDER)

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

        val providers = viewModel.providerData.value
        val provider = providers?.values?.find { it.idProvider == idProvider }

        binding.apply {
            bindProgressButton(btnProvider)
            tvTitle.text = getString(R.string.edit_provider)
            btnProvider.text = getString(R.string.edit_provider)

            var additional = Additional()
            if (provider != null) {
                inputNamaProvider.setText(provider.provider)
                inputAlamatProvider.setText(provider.alamat)
                additional = provider.additional
            }

            btnProvider.setOnClickListener {
                val namaProvider = inputNamaProvider.text.toString()
                val alamatProvider = inputAlamatProvider.text.toString()

                btnProvider.attachTextChangeAnimator()
                btnProvider.showProgress()

                val validate = viewModel.setRequestDataProvider(
                    additional,
                    alamatProvider,
                    namaProvider
                )

                if (validate.isNotEmpty()) {
                    Toasty.error(requireContext(), validate, Toasty.LENGTH_SHORT).show()
                    btnProvider.hideProgress(R.string.edit_provider)
                    return@setOnClickListener
                }

                viewModel.editProvider(idProvider) { status, message ->
                    when (status) {
                        "success" -> {
                            Toasty.success(requireContext(), message, Toasty.LENGTH_SHORT).show()
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }

                        else -> {
                            Toasty.error(requireContext(), message, Toasty.LENGTH_SHORT).show()
                            btnProvider.hideProgress(R.string.edit_provider)
                        }
                    }
                }
            }
        }
    }
}