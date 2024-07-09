package com.jmnetwork.e_jartas.view.manajemenJalan

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
import com.jmnetwork.e_jartas.databinding.FragmentListRuasJalanBinding
import com.jmnetwork.e_jartas.view.MainActivity
import com.jmnetwork.e_jartas.viewModel.ManajemenJalanViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory

class ListRuasJalanFragment : Fragment() {

    private lateinit var _binding: FragmentListRuasJalanBinding
    private val binding get() = _binding
    private lateinit var viewModel: ManajemenJalanViewModel
    private lateinit var adapter: RuasJalanAdapter

    private var currentSearchQuery: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRuasJalanBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory)[ManajemenJalanViewModel::class.java]
        adapter = RuasJalanAdapter()

        var page = 1
        val limit = 100
        var totalPage = 0

        binding.apply {
            btnBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            btnTambahRuas.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.ruas_jalan_fragment_container, AddRuasJalanFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack("AddRuasJalanFragment")
                    .commit()
            }

            viewModel.ruasJalanData.observe(viewLifecycleOwner) {
                if (it != null) {
                    adapter.setItem(it.values.toList())
                    progressBar.visibility = View.GONE
                    totalPage = viewModel.totalData.value?.div(limit) ?: 0
                    totalPage += if (viewModel.totalData.value?.rem(limit) != 0) 1 else 0
                } else {
                    adapter.setItem(emptyList())
                }
            }

            rvRuasJalan.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(false)
                adapter = this@ListRuasJalanFragment.adapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                        val totalItemCount = layoutManager.itemCount

                        if (lastVisibleItem == totalItemCount - 1 && page < totalPage) {
                            progressBar.visibility = View.VISIBLE
                            page += 1
                            viewModel.getRuasJalan(limit, page)
                            currentSearchQuery?.let { this@ListRuasJalanFragment.adapter.filter.filter(it) }
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
                if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                //                requireActivity().finish()
            }
        })
    }
}