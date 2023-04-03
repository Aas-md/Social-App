package com.example.socialapp

import android.app.DownloadManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialapp.Daos.PostDao
import com.example.socialapp.Models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity( ), LikeClicked {

    private lateinit var adapter: PostAdapter
    private lateinit var postDao : PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       fab.setOnClickListener{

           val intent = Intent(this,CreatePostActivity::class.java)
           startActivity(intent)

       }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

        postDao = PostDao()
        val collection = postDao.postCollection
        val query = collection.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOpetion = FirestoreRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()

        adapter = PostAdapter(recyclerViewOpetion,this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart(){
        super.onStart()
    adapter.startListening()

    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {

        postDao.updateLikes(postId)

    }
}