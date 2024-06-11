package com.jaylangkung.e_jartas.view

import android.animation.Animator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.showProgress
import com.jaylangkung.e_jartas.BuildConfig
import com.jaylangkung.e_jartas.MainActivity
import com.jaylangkung.e_jartas.databinding.ActivityLoginBinding
import com.jaylangkung.e_jartas.utils.Constants
import com.jaylangkung.e_jartas.utils.MySharedPreferences

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@LoginActivity)

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.view.alpha = 1f
            splashScreenView.view.animate()
                .alpha(0f)
                .setDuration(500L)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        splashScreenView.remove()
                        if (myPreferences.getValue(Constants.USER).equals(Constants.LOGIN)) {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
        }

        val apiKey = BuildConfig.API_KEY
        myPreferences.setValue(Constants.TokenAuth, apiKey)

        binding.apply {
            bindProgressButton(btnLogin)
            btnLogin.setOnClickListener {
                val email = tvValueEmailLogin.text.toString()
                val pass = tvValuePasswordLogin.text.toString()
                btnLogin.apply {
                    attachTextChangeAnimator()
                    showProgress {
                        progressColor = Color.WHITE
                        buttonText = "Proses Login"
                    }

                }
//                if (validate()) {
//                    loginProcess(email, pass)
//                    btnLogin.showProgress {
//                        buttonTextRes = com.jaylangkung.e_jartas.R.string.loading
//                        progressColor = android.R.color.white
//                    }
//                }
            }
        }
    }
}