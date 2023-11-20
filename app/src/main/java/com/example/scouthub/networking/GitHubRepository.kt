package com.example.scouthub.networking

import com.example.scouthub.service.GitHubApiService
import com.example.scouthub.data.User

class GitHubRepository(private val apiService: GitHubApiService) {

    suspend fun getUser(username: String): User? {
        val response = apiService.getUser(username)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }
}
