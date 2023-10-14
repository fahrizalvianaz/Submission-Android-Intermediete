package com.example.storyapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.remote.response.register.RegisterResponse
import com.example.storyapp.data.remote.Result
import com.example.storyapp.data.remote.response.story.ListStoryItem
import com.example.storyapp.data.repository.StoryAppRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody


class MainViewModel(private val storyAppRepository: StoryAppRepository) : ViewModel() {

    private val resultLiveData = MutableLiveData<Result<RegisterResponse>>()
    private val token = MutableLiveData<String?>()

    fun register(name: String, email : String, password: String) = storyAppRepository.postSignUp(name,email,password)
    fun login(email: String, password: String) = storyAppRepository.postLogin(email, password)

    fun setPreference(token: String, context: Context) = storyAppRepository.setPreference(token,context)
    fun getPreference(context: Context): LiveData<String?> {
        val tokenData = storyAppRepository.getPreference(context)
        token.value = tokenData
        return token
    }

//    fun getStory(token: String) = storyAppRepository.getStory(token)
    fun story(token: String): LiveData<PagingData<ListStoryItem>> =
        storyAppRepository.getStory(token).cachedIn(viewModelScope)
    fun getStoryDetail(token: String, id: String) = storyAppRepository.getStoryDetail(token, id)
    fun postStory(
        token: String,
        image: MultipartBody.Part,
        desc: RequestBody
    ): LiveData<Result<RegisterResponse>> {
        viewModelScope.launch {
            val result = storyAppRepository.postStory(token, image, desc)
            resultLiveData.value = result
        }
        return resultLiveData
    }
    fun getStoriesLocation(token: String) = storyAppRepository.getStoriesLocation(token)
}