package com.example.scouthub.networking

import androidx.lifecycle.LiveData
import com.example.scouthub.service.GitHubApiService
import com.example.scouthub.data.User
import com.example.scouthub.database.UserDao
import com.example.scouthub.database.UserEntity

class GitHubRepository(private val apiService: GitHubApiService, private val userDao: UserDao) {

    val allUsers: LiveData<List<UserEntity>> = userDao.getAllUsers()

    suspend fun getUser(username: String): User? {
        return try {
            val response = apiService.getUser(username)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun insertUser(user: User) {
        val userEntity = UserEntity(
            login = user.login,
            id = user.id,
            nodeId = user.nodeId,
            avatarUrl = user.avatarUrl,
            gravatarId = user.gravatarId,
            url = user.url,
            htmlUrl = user.htmlUrl,
            followersUrl = user.followersUrl,
            followingUrl = user.followingUrl,
            gistsUrl = user.gistsUrl,
            starredUrl = user.starredUrl,
            subscriptionsUrl = user.subscriptionsUrl,
            organizationsUrl = user.organizationsUrl,
            reposUrl = user.reposUrl,
            eventsUrl = user.eventsUrl,
            receivedEventsUrl = user.receivedEventsUrl,
            type = user.type,
            siteAdmin = user.siteAdmin,
            name = user.name,
            company = user.company,
            blog = user.blog,
            location = user.location,
            email = user.email,
            hireable = user.hireable,
            bio = user.bio,
            twitterUsername = user.twitterUsername,
            publicRepos = user.publicRepos,
            publicGists = user.publicGists,
            followers = user.followers,
            following = user.following,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
        userDao.insertUser(userEntity)
    }
}
