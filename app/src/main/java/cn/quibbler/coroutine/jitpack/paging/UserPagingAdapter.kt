package cn.quibbler.coroutine.jitpack.paging

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cn.quibbler.coroutine.databinding.UserItemLayoutBinding
import cn.quibbler.coroutine.jitpack.parcelize.User

class UserPagingAdapter : PagingDataAdapter<User, UserPagingAdapter.UserViewHolder>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.firstName == newItem.firstName
            }

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(UserItemLayoutBinding.bind(parent))
    }

    class UserViewHolder(val binding: UserItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(user: User?) {
            user?.let {
                //TODO
            }
        }

    }

}