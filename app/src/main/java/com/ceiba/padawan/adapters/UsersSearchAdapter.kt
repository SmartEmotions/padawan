package com.ceiba.padawan.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ceiba.padawan.R

class UsersSearchAdapter(
    private var messages: List<String>
) : RecyclerView.Adapter<UsersSearchAdapter.UserNotFoundViewHolder>() {

    class UserNotFoundViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): UserNotFoundViewHolder {
        val view = LayoutInflater
            .from(viewGroup.context)
            .inflate(R.layout.user_not_foun_card, viewGroup, false)
        return UserNotFoundViewHolder(view)
    }

    override fun getItemCount() = messages.size

    // TODO Consider change this lint warning
    @SuppressLint("NotifyDataSetChanged")
    fun setMessage(messages: List<String>) {
        this.messages = messages
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: UserNotFoundViewHolder, position: Int) {}
}
