package com.example.chattingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btn_sign_up: Button
    private lateinit var name: EditText
    private lateinit var mDBRef: DatabaseReference

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        btn_sign_up = findViewById(R.id.btn_sign_up)

        btn_sign_up.setOnClickListener {

            val email = email.text.toString().trim()
            val password = password.text.toString()
            val name = name.text.toString().trim()

            signup(name, email, password)
        }

    }

    private fun signup(name: String, email: String, password: String) {

        //logic for creating user
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {

                    mAuth.currentUser?.let { addUserToDatabase(name, email, it.uid, password) }

                    val intent = Intent(this@SignUp, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this@SignUp, "Some error occurred while creating the account.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //logic for adding users to db after signup
    private fun addUserToDatabase(name: String, email: String, uid: String, password: String) {
        mDBRef = FirebaseDatabase.getInstance().getReference()
        mDBRef.child("users").child(uid).setValue(User(name, email, uid, password))
    }
}