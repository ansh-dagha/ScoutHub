package com.example.scouthub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scouthub.data.User
import com.example.scouthub.database.UserEntity
import com.example.scouthub.networking.GitHubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GitHubViewModel(private val repository: GitHubRepository) : ViewModel() {

    private var currentUser: User? = null
    val allUsers: LiveData<List<UserEntity>> = repository.allUsers

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private fun setCurrentUser(user: User) {
        currentUser = user
        _user.value = user
    }

    fun getUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getUser(username)
            withContext(Dispatchers.Main) {
                if (user != null) {
                    setCurrentUser(user)
                }
            }
        }
    }

    fun insertUserIntoDatabase() {
        currentUser?.let { user ->
            viewModelScope.launch(Dispatchers.IO) {
                repository.insertUser(user)
            }
        }
    }

    fun deleteUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(userEntity)
        }
    }
}