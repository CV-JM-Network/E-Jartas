package com.jmnetwork.e_jartas.view.manajemenTiang

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jmnetwork.e_jartas.databinding.ActivityProviderBinding
import com.jmnetwork.e_jartas.view.MainActivity
import com.jmnetwork.e_jartas.viewModel.ManajemenTiangViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory

class ProviderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProviderBinding
    private lateinit var viewModel: ManajemenTiangViewModel
    private lateinit var adapter: ProviderAdapter

    private var currentSearchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProviderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this@ProviderActivity, factory)[ManajemenTiangViewModel::class.java]
        adapter = ProviderAdapter()

        var page = 1
        val limit = 10
        var totalPage = 0

        onBackPressedDispatcher.addCallback(this@ProviderActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(
                    Intent(this@ProviderActivity, MainActivity::class.java).putExtra(MainActivity.EXTRA_FRAGMENT, "home")
                )
                finish()
            }
        })

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            viewModel.apply {
                getProvider(limit, 1)
                providerData.observe(this@ProviderActivity) {
                    if (it != null) {
                        adapter.setItem(it.data)
                        progressBar.visibility = View.GONE
                        totalPage = it.totalData.totalData.div(limit)
                        totalPage += if (it.totalData.totalData.rem(limit) > 0) 1 else 0
                    } else {
                        adapter.setItem(emptyList())
                    }
                }
            }

            rvProvider.apply {
                layoutManager = LinearLayoutManager(this@ProviderActivity)
                setHasFixedSize(false)
                adapter = this@ProviderActivity.adapter
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
                            currentSearchQuery?.let { this@ProviderActivity.adapter.filter.filter(it) }
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