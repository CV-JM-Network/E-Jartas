package com.jmnetwork.e_jartas.view.manajemenTiang

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
import com.jmnetwork.e_jartas.databinding.FragmentListProviderBinding
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

        var page = 1
        val limit = 10
        var totalPage = 0

        binding.apply {
            btnBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            viewModel.apply {
                providerData.observe(viewLifecycleOwner) {
                    if (it != null) {
                        adapter.setItem(it.values.toList())
                        progressBar.visibility = View.GONE
                        totalPage = viewModel.totalData.value?.div(limit) ?: 0
                        totalPage += if (viewModel.totalData.value?.rem(limit) != 0) 1 else 0
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack()
                requireActivity().finish()
            }
        })
    }
}