package com.example.scouthub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.scouthub.database.AppDatabase
import com.example.scouthub.databinding.ActivitySearchBinding
import com.example.scouthub.networking.GitHubRepository
import com.example.scouthub.networking.RetrofitClient
import com.example.scouthub.viewmodel.GitHubViewModel
import com.example.scouthub.viewmodel.GitHubViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: GitHubViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val apiService = RetrofitClient.apiService
        val appDatabase =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user-database")
                .build()
        val userDao = appDatabase.userDao()
        val repository = GitHubRepository(apiService, userDao)
        viewModel =
            ViewModelProvider(this, GitHubViewModelFactory(repository))[GitHubViewModel::class.java]

        binding.etSearchUsername.requestFocus()

        binding.etSearchUsername.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val inputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
                    val username = binding.etSearchUsername.text.toString().trim()
                    binding.userSearchResultContainer.isVisible = false
                    binding.progressLoader.isVisible = true
                    viewModel.getUser(username)
                    true
                }
                else -> false
            }
        }

        viewModel.user.observe(this) { user ->
            viewModel.viewModelScope.launch(Dispatchers.Main) {
                binding.progressLoader.isVisible = false
                binding.userSearchResultContainer.isVisible = true
                binding.tvSearchUsername.text = user?.name ?: "N/A"
                user?.avatarUrl?.let {
                    Glide.with(this@SearchActivity)
                        .load(it)
                        .into(binding.searchProfileImage)
                }
            }
        }

        binding.saveProfile.setOnClickListener {
            viewModel.insertUserIntoDatabase()
        }
    }
}