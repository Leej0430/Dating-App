
package com.example.datingapp.view


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.R
import com.example.datingapp.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ChatListAdapter(var chatList: ArrayList<User>, val btnlistener: BtnClickListener) :
    RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

    companion object{
        var mClickListener: BtnClickListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder
    {

        val layoutView: View =
            LayoutInflater.from(parent.context).inflate(
                com.example.datingapp.R.layout.list_of_chats,null,
                false
            )


        val lp = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutView.setLayoutParams(lp)

        return ChatListViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        mClickListener = btnlistener


        holder.mName.setText(chatList[position].name)
        Picasso.get().load(chatList.get(position).imageUrl).into(holder.mImage)

        holder.mLayout.setOnClickListener{
            View.OnClickListener { mClickListener?.onBtClick(position) }

        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    inner class ChatListViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var mName: TextView
        var mImage: ImageView
        var mLayout: LinearLayout

        init {
            mImage = view.findViewById(R.id.image_chats_user)
            mName = view.findViewById(R.id.name_chats_user)
            mLayout = view.findViewById(R.id.linear_list_of_chats)

        }

    }
}

interface BtnClickListener {

    fun onBtClick(position: Int)

}
