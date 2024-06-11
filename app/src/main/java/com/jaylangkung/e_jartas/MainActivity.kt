package com.jaylangkung.e_jartas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jaylangkung.e_jartas.databinding.ActivityMainBinding
import com.jaylangkung.e_jartas.utils.MySharedPreferences

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@MainActivity)


    }
}