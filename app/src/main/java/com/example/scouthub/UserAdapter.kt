package com.example.scouthub

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scouthub.database.UserEntity
import com.example.scouthub.databinding.ItemUserBinding

class UserAdapter(private val onDeleteClickListener: (UserEntity) -> Unit, private val onItemClick: (UserEntity) -> Unit) : ListAdapter<UserEntity, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userEntity = getItem(position)
        holder.bind(userEntity)
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userEntity: UserEntity) {
            binding.tvName.text = userEntity.name
            binding.tvUsername.text = "#" + userEntity.login

            Glide.with(binding.root.context)
                .load(userEntity.avatarUrl)
                .into(binding.ivAvatar)

            binding.btnDelete.setOnClickListener {
                onDeleteClickListener.invoke(userEntity)
            }

            binding.root.setOnClickListener {
                onItemClick.invoke(userEntity)
            }
        }
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem.login == newItem.login
        }

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem == newItem
        }
    }
}
