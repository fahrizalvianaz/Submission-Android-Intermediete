package com.example.storyapp.ui.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailsStoryBinding
import com.example.storyapp.utils.ViewModelFactory
import com.example.storyapp.viewmodel.MainViewModel
import com.example.storyapp.data.remote.Result
import com.example.storyapp.data.remote.response.story.Story
import com.example.storyapp.utils.dateConverter

class DetailsStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsStoryBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= 33) {
            val id = intent.getStringExtra("id")
            if (id != null) setData(mainViewModel, id)
        } else {
            @Suppress("DEPRECATION")
            val id = intent.getStringExtra("id") as String
            setData(mainViewModel, id)
        }

    }

    private fun setData(mainViewModel: MainViewModel, id: String) {
        val token = getToken()
        if (token != null) {
            mainViewModel.getStoryDetail("Bearer $token", id)
                .observe(this@DetailsStoryActivity) { detail ->
                    if (detail != null) {
                        when (detail) {
                            is Result.Loading -> {}
                            is Result.Success -> {
                                val data = detail.data.story
                                if (data != null) {
                                    bindingData(data)
                                }
                            }
                            is Result.Error -> {
                                Toast.makeText(this, detail.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
        }
    }


    private fun bindingData(data: Story) {
        Glide.with(this)
            .load(data.photoUrl)
            .into(binding.ivDetails)
        binding.tvNamaDetails.text = data.name
        binding.tvDeskripsiDetails.text = data.description

        if (data.createdAt != null) {
            binding.tvDateDetails.text = dateConverter(data.createdAt)
        }
    }
    private fun getToken(): String? {
        return mainViewModel.getPreference(this).value
    }
}