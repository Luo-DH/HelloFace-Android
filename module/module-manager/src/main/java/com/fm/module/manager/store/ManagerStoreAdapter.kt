package com.fm.module.manager.store

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fm.module.manager.databinding.ManagerItemStoreBinding
import com.fm.library.common.constants.module.FaceMsg
import com.fm.library.common.constants.module.FaceMsg2

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
        holder.avatar.load(data.pic) {
            crossfade(500)
        }
    }

    override fun getItemCount(): Int = users.size

    private val diffCallback = object : DiffUtil.ItemCallback<FaceMsg2>() {
        override fun areItemsTheSame(oldItem: FaceMsg2, newItem: FaceMsg2): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: FaceMsg2, newItem: FaceMsg2): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }


    private val differ = AsyncListDiffer(this, diffCallback)

    var users: List<FaceMsg2>
        get() = differ.currentList
        set(value) {
            notifyDataSetChanged()
            return differ.submitList(value)
        }


}