package com.example.scouthub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scouthub.data.Repo
import com.example.scouthub.networking.GitHubRepository
import kotlinx.coroutines.launch

class UserDetailsViewModel(private val repository: GitHubRepository) : ViewModel() {
    private val _repos = MutableLiveData<List<Repo>>()
    val repos: LiveData<List<Repo>>
        get() = _repos
    fun getRepos(username: String) {
        viewModelScope.launch {
            val repos = repository.getRepos(username)
            _repos.value = repos
        }
    }

}
