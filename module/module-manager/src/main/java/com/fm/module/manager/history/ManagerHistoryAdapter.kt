package com.fm.module.manager.history

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
import com.fm.library.common.constants.module.FaceHistorySubRsp
import com.fm.module.manager.R
import com.fm.module.manager.User
import com.fm.module.manager.databinding.ManagerItemHistoryBinding

class ManagerHistoryAdapter :
    RecyclerView.Adapter<ManagerHistoryAdapter.ManagerHistoryViewHolder>() {

    inner class ManagerHistoryViewHolder(itemView: ManagerItemHistoryBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val avatar: ImageView = itemView.managerHistoryItemAvatar
        val name: TextView = itemView.managerHistoryItemName
        val time: TextView = itemView.managerHistoryItemTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagerHistoryViewHolder {
        val view = ManagerItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        val holder = ManagerHistoryViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ManagerHistoryViewHolder, position: Int) {

        val data = users[position]
        holder.name.text = data.name
        holder.time.text = data.time
//        holder.avatar.load("https://pic4.zhimg.com/v2-abed1a8c04700ba7d72b45195223e0ff_im.jpg")
        holder.avatar.load(R.drawable.ic_launcher_background)
    }

    override fun getItemCount(): Int = users.size

    private val diffCallback = object : DiffUtil.ItemCallback<FaceHistorySubRsp>() {
        override fun areItemsTheSame(oldItem: FaceHistorySubRsp, newItem: FaceHistorySubRsp): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: FaceHistorySubRsp, newItem: FaceHistorySubRsp): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }


    private val differ = AsyncListDiffer(this, diffCallback)

    var users: List<FaceHistorySubRsp>
        get() = differ.currentList
        set(value) {
            notifyDataSetChanged()
            return differ.submitList(value)
        }


}