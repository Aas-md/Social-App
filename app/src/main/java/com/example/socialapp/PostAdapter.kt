package com.example.socialapp

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.Models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class PostAdapter(options: FirestoreRecyclerOptions<Post>,private val listener:LikeClicked) : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(
    options)
 {

    class PostViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val userImage:ImageView = itemView.findViewById(R.id.userImage)
        val userName :TextView = itemView.findViewById(R.id.userName)
        val createdAt:TextView = itemView.findViewById(R.id.createdAt)
        val postTitle:TextView = itemView.findViewById(R.id.postTitle)
        val likeButton:ImageView = itemView.findViewById(R.id.likeButton)
        val likeCount:TextView = itemView.findViewById(R.id.likeCount)
    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

         val postHolder = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false))

         postHolder.likeButton.setOnClickListener{

             listener.onLikeClicked(snapshots.getSnapshot(postHolder.adapterPosition).id)
         }

         return postHolder

     }

     override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {

         Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage)
         holder.userName.text = model.createdBy.displayName
         holder.postTitle.text = model.text
         holder.likeCount.text = model.likedBy.size.toString()

         holder.createdAt.text = Utils.getTimeAgo(model.createdAt)

         val auth = Firebase.auth
         val currentUser = auth.currentUser!!.uid

         val isLike = model.likedBy.contains(currentUser)

         if(isLike){

             holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context,R.drawable.liked))
         }else{
             holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context,R.drawable.unlike))
         }
     }
 }

interface LikeClicked {

    fun onLikeClicked(postId: String)


}