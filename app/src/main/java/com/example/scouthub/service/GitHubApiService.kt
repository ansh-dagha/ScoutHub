package com.example.scouthub.service

import com.example.scouthub.data.Repo
import com.example.scouthub.data.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApiService {
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<User>

    @GET("users/{username}/repos")
    suspend fun getRepos(@Path("username") username: String): Response<List<Repo>>
}