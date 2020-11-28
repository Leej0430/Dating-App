package com.example.datingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LogInOrSignUp : AppCompatActivity() {
  //UI elements
   private lateinit var logIn : Button
   private lateinit var signUp: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_or_sign_up)

        logIn= findViewById(R.id.btn_sign_in)
        signUp = findViewById(R.id.btn_sign_up)

        logIn.setOnClickListener {
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)

        }
        signUp.setOnClickListener {
            val intent = Intent(this,RegisterationActivity::class.java)
            startActivity(intent)

        }

    }
}