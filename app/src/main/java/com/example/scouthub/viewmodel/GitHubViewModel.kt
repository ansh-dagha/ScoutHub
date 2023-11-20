package com.example.scouthub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scouthub.data.User
import com.example.scouthub.networking.GitHubRepository
import kotlinx.coroutines.launch

class GitHubViewModel(private val repository: GitHubRepository) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun getUser(username: String) {
        viewModelScope.launch {
            _user.value = repository.getUser(username)
        }
    }
}