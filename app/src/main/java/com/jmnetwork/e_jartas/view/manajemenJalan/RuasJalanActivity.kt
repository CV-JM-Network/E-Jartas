package com.jmnetwork.e_jartas.view.manajemenJalan

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jmnetwork.e_jartas.databinding.ActivityRuasJalanBinding
import com.jmnetwork.e_jartas.utils.MySharedPreferences
import com.jmnetwork.e_jartas.view.MainActivity
import com.jmnetwork.e_jartas.viewModel.ManajemenJalanViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory

class RuasJalanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRuasJalanBinding
    private lateinit var viewModel: ManajemenJalanViewModel
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var adapter: RuasJalanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRuasJalanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this@RuasJalanActivity, factory)[ManajemenJalanViewModel::class.java]
        myPreferences = MySharedPreferences(this@RuasJalanActivity)
        adapter = RuasJalanAdapter()

        var page = 1
        val limit = 10
        var totalPage = 0

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(
                    Intent(this@RuasJalanActivity, MainActivity::class.java).putExtra(MainActivity.EXTRA_FRAGMENT, "home")
                )
                finish()
            }
        })

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            viewModel.ruasJalanData.observe(this@RuasJalanActivity) {
                if (it != null) {
                    adapter.setItem(it.data)
                    progressBar.visibility = View.GONE
                    totalPage = it.totalData.totalData.div(limit)
                    totalPage += if (it.totalData.totalData.rem(limit) > 0) 1 else 0
                } else {
                    adapter.setItem(emptyList())
                }
            }

            rvRuasJalan.apply {
                layoutManager = LinearLayoutManager(this@RuasJalanActivity)
                setHasFixedSize(false)
                adapter = this@RuasJalanActivity.adapter
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
                        }
                    }
                })
            }
        }
    }
}