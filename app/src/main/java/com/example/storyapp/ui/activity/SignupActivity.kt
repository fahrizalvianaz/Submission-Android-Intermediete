package com.example.storyapp.ui.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.storyapp.databinding.ActivitySignupBinding
import com.example.storyapp.utils.ViewModelFactory
import com.example.storyapp.data.remote.Result
import com.example.storyapp.viewmodel.MainViewModel


class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btSignup.setOnClickListener {
            val name = binding.etNamaSignup.text.toString()
            val email = binding.etEmailSignup.text.toString()
            val password = binding.etPasswordSignup.text.toString()

            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            when {
                name.isBlank() -> {
                    binding.etNamaSignup.requestFocus()
                    binding.etNamaSignup.error = "Masukkan nama kamu !"
                }
                email.isBlank() -> {
                    binding.etEmailSignup.requestFocus()
                    binding.etEmailSignup.error = "Masukkan email kamu !"
                }
                password.isBlank() -> {
                    binding.etPasswordSignup.requestFocus()
                    binding.etPasswordSignup.error = "Masukkan password kamu !"
                }
                else -> {
                    if (binding.etEmailSignup.error == null && binding.etPasswordSignup.error == null) {
                        processRegister(name, email,password)
                    }
                }
            }
        }
    }
    private fun processRegister(nama : String, email : String, password : String) {
        mainViewModel.register(nama,email,password).observe(this) {
            if (it != null) {
                when(it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val data = it.data
                        if (data.error) {
                            Toast.makeText(this, "Gagal Register", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Register berhasil, silahkan login!", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    private fun showLoading(state : Boolean) {
        binding.pbSignup.isVisible = state
        binding.etNamaSignup.isInvisible = state
        binding.etEmailSignup.isInvisible = state
        binding.etPasswordSignup.isInvisible = state
        binding.tvLogin.isInvisible = state
        binding.btSignup.isInvisible = state
        binding.tvSudahPunyaAkun.isInvisible = state
    }
}