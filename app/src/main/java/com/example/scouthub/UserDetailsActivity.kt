package com.example.scouthub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.scouthub.database.AppDatabase
import com.example.scouthub.databinding.ActivityUserDetailsBinding
import com.example.scouthub.networking.GitHubRepository
import com.example.scouthub.networking.RetrofitClient
import com.example.scouthub.viewmodel.UserDetailsViewModel
import com.example.scouthub.viewmodel.UserDetailsViewModelFactory

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var viewModel: UserDetailsViewModel
    private lateinit var repoAdapter: RepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val apiService = RetrofitClient.apiService
        val appDatabase =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user-database")
                .build()
        val userDao = appDatabase.userDao()
        val repository = GitHubRepository(apiService, userDao)
        val username = intent.getStringExtra("username")

        viewModel = ViewModelProvider(this, UserDetailsViewModelFactory(repository))
            .get(UserDetailsViewModel::class.java)

        repoAdapter = RepoAdapter()

        binding.recyclerViewRepos.apply {
            layoutManager = LinearLayoutManager(this@UserDetailsActivity)
            adapter = repoAdapter
        }

        viewModel.repos.observe(this) { repoList ->
            repoAdapter.submitList(repoList)
        }

        if (username != null) {
            viewModel.getRepos(username)
        }

    }
}
