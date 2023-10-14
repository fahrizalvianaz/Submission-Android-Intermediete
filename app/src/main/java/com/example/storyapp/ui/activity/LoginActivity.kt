package com.example.storyapp.ui.activity


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.utils.ViewModelFactory
import com.example.storyapp.data.remote.Result
import com.example.storyapp.viewmodel.MainViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isLogin(this)
        binding.tvDaftar.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.btLogin.setOnClickListener {
            val email = binding.etEmailLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()
            when {
                email.isBlank() -> {
                    binding.etEmailLogin.requestFocus()
                    binding.etEmailLogin.error = "Masukkan email kamu !"
                }
                password.isBlank() -> {
                    binding.etPasswordLogin.requestFocus()
                    binding.etPasswordLogin.error = "Masukkan password kamu !"
                }
                else -> {
                    if (binding.etEmailLogin.error == null && binding.etPasswordLogin.error == null) {
                        processLogin(email,password)
                    }
                }
            }
        }
    }
    private fun processLogin(email: String, password: String) {
        mainViewModel.login(email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val data = result.data
                        Toast.makeText(this@LoginActivity, data.message, Toast.LENGTH_SHORT).show()
                        if (data.loginResult?.token != null) {
                            mainViewModel.setPreference(data.loginResult.token, this)
                        }
                        val mainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(mainActivity)
                        finish()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun isLogin(context: Context) {
        mainViewModel.getPreference(context).observe(this) { token ->
            if (token?.isEmpty() == false) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    private fun showLoading(state : Boolean) {
        binding.pbLogin.isVisible = state
        binding.etEmailLogin.isInvisible = state
        binding.etPasswordLogin.isInvisible = state
        binding.tvDaftar.isInvisible = state
        binding.btLogin.isInvisible = state
        binding.tvBelumPunyaAkun.isInvisible = state
    }

}