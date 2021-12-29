package com.fm.module.manager.store

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.alibaba.android.arouter.facade.annotation.Route
import com.fm.library.common.constants.RouterPath
import com.fm.module.manager.R
import com.fm.module.manager.User
import com.fm.module.manager.databinding.ManagerItemHistoryBinding
import com.fm.module.manager.databinding.ManagerItemStoreBinding
import com.fm.module.manager.net.FaceMsg

class ManagerStoreAdapter :
    RecyclerView.Adapter<ManagerStoreAdapter.ManagerStoreViewHolder>() {

    inner class ManagerStoreViewHolder(itemView: ManagerItemStoreBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val avatar: ImageView = itemView.managerStoreItemAvatar
        val name: TextView = itemView.managerStoreItemName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagerStoreViewHolder {
        val view = ManagerItemStoreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        val holder = ManagerStoreViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ManagerStoreViewHolder, position: Int) {

        val data = users[position]
        holder.name.text = data.name
        holder.avatar.load(data.imgUrl) {
            crossfade(500)
        }
    }

    override fun getItemCount(): Int = users.size

    private val diffCallback = object : DiffUtil.ItemCallback<FaceMsg>() {
        override fun areItemsTheSame(oldItem: FaceMsg, newItem: FaceMsg): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: FaceMsg, newItem: FaceMsg): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }


    private val differ = AsyncListDiffer(this, diffCallback)

    var users: List<FaceMsg>
        get() = differ.currentList
        set(value) {
            notifyDataSetChanged()
            return differ.submitList(value)
        }


}