package com.ceiba.padawan

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ceiba.padawan.adapters.UsersAdapter
import com.ceiba.padawan.adapters.UsersSearchAdapter
import com.ceiba.padawan.store.vo.User
import com.ceiba.padawan.store.vo.User_
import com.ceiba.padawan.databinding.ActivityMainBinding
import com.ceiba.padawan.services.ClientServices.retrofit
import com.ceiba.padawan.services.UserServices
import com.ceiba.padawan.store.ObjectBoxStores.userStore
import com.ceiba.padawan.utils.Constants.USER_ID
import io.objectbox.query.Query
import io.objectbox.query.QueryBuilder
import io.objectbox.reactive.DataSubscription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var users: List<User> = arrayListOf()
    private var loader: View? = null
    private var usersAdapter: UsersAdapter = UsersAdapter(users) { user ->
        gotToUserDetail(user)
    }
    private var headerAdapter: UsersSearchAdapter = UsersSearchAdapter(listOf())
    private var observer: DataSubscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        users = userStore.all
        if (users.isEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val client = retrofit.create(UserServices::class.java).getUsers()
                    if (client.isSuccessful) {
                        userStore.put(client.body())
                    }
                } catch (error: Throwable) {
                    print(error.message)
                }
                // TODO Handle errors on request
            }
        }
        val usersListRecyclerView: RecyclerView = findViewById(R.id.users_list)

        usersListRecyclerView.adapter = ConcatAdapter(headerAdapter, usersAdapter)
        val query: Query<User> = userStore.query().build()
        observer = query.subscribe().observer { data -> updateDataUsers(data) }
        loader = findViewById(R.id.users_loader)
    }

    override fun onStop() {
        observer?.cancel()
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.action_search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(searchString: String?): Boolean {
                if (searchString != null) {
                    findUsers(searchString)
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun gotToUserDetail(user: User) {
        startActivity(
            Intent(this, UsersDetailActivity::class.java).apply {
                putExtra(USER_ID, user.identifier)
            }
        )
    }

    private fun updateDataUsers(userList: List<User>) {
        runOnUiThread {
            var message: List<String> = listOf()
            if (userList.isNotEmpty()) {
                loader?.visibility = View.INVISIBLE
            } else {
                message = listOf("")
            }
            headerAdapter.setMessage(message)
            usersAdapter.setUsers(userList)
        }
    }

    private fun findUsers(search: String) {
        val query: Query<User> = userStore
            .query()
            .contains(
                User_.name,
                search,
                QueryBuilder.StringOrder.CASE_INSENSITIVE
            )
            .build()
        updateDataUsers(query.find())
    }
}
