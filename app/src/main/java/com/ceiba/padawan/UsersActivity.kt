package com.ceiba.padawan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ceiba.padawan.adapters.UsersAdapter
import com.ceiba.padawan.adapters.UsersSearchAdapter
import com.ceiba.padawan.data.User
import com.ceiba.padawan.data.User_
import com.ceiba.padawan.databinding.ActivityMainBinding
import com.ceiba.padawan.services.ClientServices.retrofit
import com.ceiba.padawan.services.UserServices
import com.ceiba.padawan.store.ObjectBox
import com.ceiba.padawan.utils.Constants.USER_ID
import io.objectbox.query.Query
import io.objectbox.query.QueryBuilder
import io.objectbox.reactive.DataSubscription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val boxStore = ObjectBox.store.boxFor( User::class.java )
    private var users: List<User> = arrayListOf()
    private var loader: View? = null
    private var usersAdapter: UsersAdapter = UsersAdapter( users ) { user ->
        gotToUserPosts( user )
    }
    private var headerAdapter: UsersSearchAdapter = UsersSearchAdapter( listOf() )
    private var observer: DataSubscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        users = boxStore.all
        if ( users.isEmpty() ) {
            CoroutineScope( Dispatchers.IO ).launch {
                val client = retrofit.create( UserServices::class.java ).getUsers()
                if( client.isSuccessful ) {
                    boxStore.put( client.body() )
                }
                // TODO Handle errors on request
            }
        }
        val usersListRecyclerView: RecyclerView = findViewById( R.id.users_list )

        usersListRecyclerView.adapter = ConcatAdapter( headerAdapter, usersAdapter )
        val query: Query<User> = boxStore.query().build()
        observer = query.subscribe().observer { data -> updateDataUsers( data ) }
        loader = findViewById( R.id.users_loader )
    }

    override fun onStop() {
        observer?.cancel()
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate( R.menu.menu_main, menu )
        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView
        searchView.queryHint = resources.getString( R.string.action_search_hint )
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange( searchString: String? ): Boolean {
                if ( searchString != null ) {
                    findUsers( searchString )
                }
                return true
            }
        })

        return super.onCreateOptionsMenu( menu )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when ( item.itemId ) {
            R.id.action_search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun gotToUserPosts ( user: User ) {
        val intent = Intent(this, UserPostsActivity()::class.java)
        intent.putExtra( USER_ID, user.identifier )
        startActivity( intent )
    }

    private fun updateDataUsers ( userList: List<User> ) {
        runOnUiThread {
            var message: List<String> = listOf()
            if ( userList.isNotEmpty() ) {
                loader?.visibility = View.INVISIBLE
            } else {
                message = listOf("")
            }
            headerAdapter.setMessage( message )
            usersAdapter.setUsers( userList )
        }
    }

    fun findUsers ( search: String ) {
        val query: Query<User> = boxStore
            .query()
            .contains(
                User_.name,
                search,
                QueryBuilder.StringOrder.CASE_INSENSITIVE )
            .build()
        updateDataUsers( query.find() )
    }
}