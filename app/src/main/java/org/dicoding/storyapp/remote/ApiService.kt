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
    suspend fun getAllStories(
        @Header("Authorization") token:String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): GetAllStoryResponse

    @GET("stories")
    fun getStoriesMap(
        @Header("Authorization") token: String,
        @Query("location") location : Int
    ): Call<GetAllStoryResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<AddNewStoryResponse>

    @GET("stories/{id}")
    fun getDetailStories(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailStoryResponse>

}