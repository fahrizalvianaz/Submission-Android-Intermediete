package com.example.storyapp.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.storyapp.data.StoryRemoteMediator
import com.example.storyapp.data.local.Preference
import com.example.storyapp.data.local.database.StoryDatabase
import com.example.storyapp.data.remote.Result
import com.example.storyapp.data.remote.network.ApiService
import com.example.storyapp.data.remote.response.login.LoginResponse
import com.example.storyapp.data.remote.response.register.RegisterResponse
import com.example.storyapp.data.remote.response.story.DetailStoryResponse
import com.example.storyapp.data.remote.response.story.ListStoryItem
import com.example.storyapp.data.remote.response.story.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException


class StoryAppRepository(private val database : StoryDatabase, private val apiService: ApiService) {

    fun postSignUp(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postRegister(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("SignUpViewModel", "postSignUp: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postLogin(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("LoginViewModel", "postLogin: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }
    fun setPreference(token: String, context: Context) {
        val settingPreference = Preference(context)
        settingPreference.setUser(token)
    }
    fun getPreference(context: Context): String? {
        val settingPreference = Preference(context)
        return settingPreference.getUser()
    }
//    fun getStory(token: String): LiveData<Result<StoryResponse>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.getStory(token)
//            emit(Result.Success(response))
//        } catch (e: Exception) {
//            emit(Result.Error(e.message.toString()))
//        }
//    }
    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator =  StoryRemoteMediator(token, database, apiService),
            pagingSourceFactory = {
//                StoryPagingSource(token,apiService)
                database.storyDao().getAllStory()
            }
        ).liveData
    }
    fun getStoryDetail(token: String, id: String): LiveData<Result<DetailStoryResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getStoryDetail(token, id)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    suspend fun postStory(
        token: String,
        image: MultipartBody.Part,
        desc: RequestBody
    ) : Result<RegisterResponse> {
        return try {
            val response = apiService.postStory(token, image, desc)
            Result.Success(response)

        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")
            Result.Error(errorMessage)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }
    fun getStoriesLocation(token: String) : LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesLocation(token,1)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("ListStoryViewModel", "getStoriesWithLocation: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }


}