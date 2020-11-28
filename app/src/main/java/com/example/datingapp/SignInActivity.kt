package com.example.datingapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    //Global variables
    private lateinit var email:String
    private lateinit var password:String
    //UI elements
    private lateinit var etEmail :EditText
    private lateinit var etPassword:EditText
    private lateinit var btnSignIn:Button


    //Fire Base reference
    private lateinit var mAuth :FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initialize()


    }
    private fun initialize(){
        etEmail = findViewById(R.id.et_login_email)
        etPassword = findViewById(R.id.et_login_password)
        btnSignIn = findViewById(R.id.btn_log_in)
        mAuth = FirebaseAuth.getInstance()
        btnSignIn.setOnClickListener {
            loginUser()
        }
    }
    private fun loginUser(){
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) {
            if(it.isSuccessful){
                moveActivity()
            }
            else{
                Toast.makeText(this,"Authentication failed",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun moveActivity(){
        val intent = Intent(this,ProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}