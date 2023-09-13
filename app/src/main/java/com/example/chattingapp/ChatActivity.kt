package com.example.chattingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRescyclerView: RecyclerView
    private lateinit var edMessage: EditText
    private lateinit var sendButton: Button
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    var receiverRoom: String? = null
    var senderRoom: String? = null
    private lateinit var mDBRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var name = intent.getStringExtra("name")
        var receiver_uid = intent.getStringExtra("uid")
        val sender_uid = FirebaseAuth.getInstance().uid

        mDBRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiver_uid + sender_uid
        receiverRoom = sender_uid + receiver_uid

        supportActionBar?.title = name

        chatRescyclerView = findViewById(R.id.chatRecyclerView)
        edMessage = findViewById(R.id.edMessageBox)
        sendButton = findViewById(R.id.btnSend)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        chatRescyclerView.layoutManager = LinearLayoutManager(this)
        chatRescyclerView.adapter = messageAdapter

        //logic for showing data in recycler from db
        mDBRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(i in snapshot.children) {
                        val message = i.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        //adding message to db
        sendButton.setOnClickListener {
            val message = edMessage.text.toString().trim()
            val messageObject = Message(message, sender_uid)

            mDBRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDBRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }

            edMessage.setText("")
        }
    }
}