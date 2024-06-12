package com.jmnetwork.e_jartas.view

import android.animation.Animator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.databinding.ActivityLoginBinding
import com.jmnetwork.e_jartas.utils.Constants
import com.jmnetwork.e_jartas.utils.MySharedPreferences
import com.jmnetwork.e_jartas.viewModel.LoginViewModel
import com.jmnetwork.e_jartas.viewModel.ViewModelFactory
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import es.dmoral.toasty.Toasty

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var splashScreen: SplashScreen
    private var isChangingConfigurations = false

    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        myPreferences = MySharedPreferences(this@LoginActivity)

        if (savedInstanceState != null) {
            isChangingConfigurations = savedInstanceState.getBoolean("isChangingConfigurations")
        }

        if (!isChangingConfigurations) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                splashScreenView.view.alpha = 1f
                splashScreenView.view.animate()
                    .alpha(1f)
                    .setDuration(500L)
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {}
                        override fun onAnimationEnd(animation: Animator) {
                            splashScreenView.remove()
                            if (myPreferences.getValue(Constants.USER).equals(Constants.LOGIN)) {
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            } else {
                                binding.cardLogin.visibility = View.VISIBLE
                            }
                        }

                        override fun onAnimationCancel(animation: Animator) {}
                        override fun onAnimationRepeat(animation: Animator) {}
                    })
            }
        } else {
            if (myPreferences.getValue(Constants.USER).equals(Constants.LOGIN)) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                binding.cardLogin.visibility = View.VISIBLE
            }
        }

        viewModel.startActivityEvent.observe(this) {
            when (it) {
                Constants.LOGIN -> {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }

                else -> {
                    Toasty.error(this, it, Toasty.LENGTH_SHORT).show()
                    binding.btnLogin.hideProgress(R.string.login_button)
                }
            }
        }

        binding.apply {
            bindProgressButton(btnLogin)
            btnLogin.setOnClickListener {
                val email = tvValueEmailLogin.text.toString()
                val pass = tvValuePasswordLogin.text.toString()
                viewModel.setLoginRequest(email, pass)
                val validate = viewModel.validate()
                if (validate.isEmpty()) {
                    viewModel.login()
                    btnLogin.apply {
                        attachTextChangeAnimator()
                        showProgress {
                            progressColor = Color.WHITE
                            buttonText = "Proses Login"
                        }
                    }
                } else {
                    // show modal dialog error
                    btnLogin.hideProgress(R.string.login_button)
                    val dialog = MaterialDialog.Builder(this@LoginActivity)
                        .setTitle("Login Gagal")
                        .setMessage(validate)
                        .setCancelable(true)
                        .setPositiveButton("OK") { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .build()
                    dialog.show()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isChangingConfigurations", true)
    }
}