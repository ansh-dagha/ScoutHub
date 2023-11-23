package com.example.scouthub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.scouthub.database.AppDatabase
import com.example.scouthub.databinding.ActivityMainBinding
import com.example.scouthub.networking.GitHubRepository
import com.example.scouthub.networking.RetrofitClient
import com.example.scouthub.viewmodel.GitHubViewModel
import com.example.scouthub.viewmodel.GitHubViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: GitHubViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val search = Intent(this, SearchActivity::class.java)

        binding.addProfileFab.setOnClickListener {
            startActivity(search)
        }

        val apiService = RetrofitClient.apiService
        val appDatabase =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user-database")
                .build()
        val userDao = appDatabase.userDao()
        val repository = GitHubRepository(apiService, userDao)
        viewModel =
            ViewModelProvider(this, GitHubViewModelFactory(repository))[GitHubViewModel::class.java]

        userAdapter = UserAdapter()

        viewModel.allUsers.observe(this) { userList ->
            // Update the RecyclerView when the user list changes
            userAdapter.submitList(userList)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }
    }
}