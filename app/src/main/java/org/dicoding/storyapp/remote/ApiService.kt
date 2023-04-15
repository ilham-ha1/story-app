package org.dicoding.storyapp.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.dicoding.storyapp.model.body.LoginBody
import org.dicoding.storyapp.model.body.RegisterBody
import org.dicoding.storyapp.model.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    fun registerUser(
        @Body registerBody: RegisterBody
    ): Call<RegisterResponse>

    @POST("login")
    fun loginUser(
        @Body loginBody: LoginBody
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token:String
    ): Call<GetAllStoryResponse>

    @GET("stories/{id}")
    fun getDetailStories(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddNewStoryResponse>
}