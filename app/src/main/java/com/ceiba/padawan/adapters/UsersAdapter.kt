package com.ceiba.padawan.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ceiba.padawan.R
import com.ceiba.padawan.data.User
import com.google.android.material.button.MaterialButton


class UsersAdapter(
    private var usersData: List<User>,
    private val onClick: ( User ) -> Unit
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    class UserViewHolder( view: View, val onClick: ( User ) -> Unit ) : RecyclerView.ViewHolder(view) {
        private val userName: TextView = view.findViewById(R.id.user_name)
        private val userEmail: TextView = view.findViewById(R.id.user_email)
        private val userPhone: TextView = view.findViewById(R.id.user_phone)
        private val userPosts: MaterialButton = view.findViewById( R.id.action_user )
        private var user: User? = null
        private fun goUser ( ) {
            user?.let {
                onClick(it)
            }
        }

        init {
            itemView.setOnClickListener { goUser() }
            userPosts.setOnClickListener { goUser() }
        }

        fun bind ( currentUser: User ) {
            user = currentUser
            userName.text = currentUser.name
            userEmail.text = currentUser.email
            userPhone.text = currentUser.phone
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater
            .from(viewGroup.context)
            .inflate( R.layout.user_card, viewGroup, false )
        return UserViewHolder( view, onClick )
    }

    override fun onBindViewHolder(viewHolder: UserViewHolder, position: Int) {
        val currentUser = usersData[position]
        viewHolder.bind( currentUser )
    }

    override fun getItemCount() = usersData.size

    // TODO Consider change this lint warning
    @SuppressLint("NotifyDataSetChanged")
    fun setUsers ( usersData: List<User> ) {
        this.usersData = usersData
        this.notifyDataSetChanged()
    }
}

