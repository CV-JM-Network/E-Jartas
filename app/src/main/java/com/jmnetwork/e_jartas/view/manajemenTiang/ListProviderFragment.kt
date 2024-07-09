package com.jmnetwork.e_jartas.view.manajemenTiang

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.databinding.FragmentListProviderBinding
import com.jmnetwork.e_jartas.view.MainActivity
import com.jmnetwork.e_jartas.viewModel.ManajemenTiangViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory

class ListProviderFragment : Fragment() {

    private var _binding: FragmentListProviderBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ManajemenTiangViewModel
    private lateinit var adapter: ProviderAdapter

    private var currentSearchQuery: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListProviderBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory)[ManajemenTiangViewModel::class.java]
        adapter = ProviderAdapter()

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

        var page = 1
        val limit = 5
        var totalPage = 0

        binding.apply {
            btnBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            btnTambahProvider.setOnClickListener {
                val fragment = AddProviderFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.provider_fragment_container, fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack("AddProviderFragment")
                    .commit()
            }

            viewModel.apply {
                providerData.observe(viewLifecycleOwner) {
                    if (it != null) {
                        adapter.setItem(it.values.toList())
                        progressBar.visibility = View.GONE
                        totalPage = totalData.value?.div(limit) ?: 0
                        totalPage += if (totalData.value?.rem(limit) != 0) 1 else 0
                    } else {
                        adapter.setItem(emptyList())
                    }
                }
            }

            rvProvider.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(false)
                adapter = this@ListProviderFragment.adapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                        val totalItemCount = layoutManager.itemCount

                        if (lastVisibleItem == totalItemCount - 1 && page < totalPage) {
                            progressBar.visibility = View.VISIBLE
                            page += 1
                            viewModel.getProvider(limit, page)
                            currentSearchQuery?.let { this@ListProviderFragment.adapter.filter.filter(it) }
                        }
                    }
                })
            }

            searchView.setOnClickListener {
                searchView.isIconified = false
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    currentSearchQuery = newText
                    adapter.filter.filter(newText)
                    return false
                }
            })
        }

    }
}