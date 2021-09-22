package com.ceiba.padawan

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ceiba.padawan.adapters.PostsAdapter
import com.ceiba.padawan.store.vo.Post
import com.ceiba.padawan.store.vo.User
import com.ceiba.padawan.store.vo.User_
import com.ceiba.padawan.services.ClientServices
import com.ceiba.padawan.services.UserServices
import com.ceiba.padawan.store.ObjectBoxStores.userStore
import com.ceiba.padawan.utils.Constants.USER_ID
import com.google.android.material.button.MaterialButton
import io.objectbox.reactive.DataSubscription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersDetailActivity : AppCompatActivity() {

    private var postAdapter: PostsAdapter = PostsAdapter(listOf())
    private var observer: DataSubscription? = null
    private var userName: TextView? = null
    private var userEmail: TextView? = null
    private var userPhone: TextView? = null
    private var loader: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_posts)
        val actionBar: ActionBar? = supportActionBar
        var currentUserId: Long? = null
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentUserId = bundle.getLong(USER_ID)
        }
        val userPosts: MaterialButton = findViewById(R.id.action_user)
        val progressLabel: TextView = findViewById(R.id.progress_label)
        progressLabel.text = resources.getText(R.string.user_posts_loading_label)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        userName = findViewById(R.id.user_name)
        userEmail = findViewById(R.id.user_email)
        userPhone = findViewById(R.id.user_phone)
        userPosts.visibility = View.INVISIBLE
        currentUserId?.let {
            val query = userStore.query().equal(User_.id, it).build()
            observer = query.subscribe().observer { data -> updateDataUser(data[0]) }
        }
        val postListRecyclerView: RecyclerView = findViewById(R.id.post_list)
        postListRecyclerView.adapter = postAdapter
        loader = findViewById(R.id.post_loader)
    }

    override fun onStop() {
        observer?.cancel()
        super.onStop()
    }

    private fun updateDataUser(user: User) {
        userEmail?.text = user.email
        userName?.text = user.name
        userPhone?.text = user.phone
        if (user.posts.isEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val client = ClientServices.retrofit.create(UserServices::class.java).getPosts(user.id)
                    if (client.isSuccessful) {
                        val posts = client.body() as List<Post>
                        user.posts.addAll(posts)
                        userStore.put(user)
                    }
                } catch (error: Throwable) {
                    print(error.message)
                }
                // TODO Handle errors on request
            }
        }
        runOnUiThread {
            title = user.name + " Posts"
            postAdapter.setPosts(user.posts)
            if (user.posts.isNotEmpty()) {
                loader?.visibility = View.INVISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
