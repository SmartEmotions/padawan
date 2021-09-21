package com.ceiba.padawan.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ceiba.padawan.R
import com.ceiba.padawan.data.Post
import java.util.*

class PostsAdapter(
    private var postData: List<Post>,
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    class PostViewHolder( view: View ) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById( R.id.title )
        private val body: TextView = view.findViewById( R.id.body )

        fun bind ( currentPost: Post ) {
            title.text = currentPost.title?.replaceFirstChar {capitalize( it )}
            body.text = currentPost.body?.replaceFirstChar { capitalize( it ) }
        }

        private fun capitalize (char: Char): String {
            return if (char.isLowerCase()) {
                char.titlecase(
                    Locale.getDefault()
                )
            } else {
                char.toString()
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater
            .from(viewGroup.context)
            .inflate( R.layout.post_card, viewGroup, false )
        return PostViewHolder( view )
    }

    override fun onBindViewHolder(viewHolder: PostViewHolder, position: Int) {
        val currentPost = postData[position]
        viewHolder.bind( currentPost )
    }

    override fun getItemCount() = postData.size

    // TODO Consider change this lint warning
    @SuppressLint("NotifyDataSetChanged")
    fun setPosts ( postData: List<Post> ) {
        this.postData = postData
        this.notifyDataSetChanged()
    }
}

