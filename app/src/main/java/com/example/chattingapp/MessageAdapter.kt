package com.example.chattingapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import java.util.zip.Inflater

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType == 1) {
            //inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view)
        }
        else {
            //inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val curMsg = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid == curMsg.senderId) {
            return ITEM_SENT
        }
        return ITEM_RECEIVE
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currMessage = messageList[position]

        if(holder.javaClass == SentViewHolder::class.java) {
            //do stuff for sent view holder

            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currMessage.message
        }
        else {
            //do stuff for receive view holder
            val viewHolder = holder as ReceiveViewHolder
            holder.sentMessage.text = currMessage.message
        }
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.txtSentMessage)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.txtReceiveMessage)
    }

}