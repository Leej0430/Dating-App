package com.example.datingapp.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.datingapp.R
import com.example.datingapp.viewmodel.LoginRegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.security.AccessController.getContext

class SignInActivity : AppCompatActivity() {
    //Global variables
    private lateinit var email:String
    private lateinit var password:String
    //UI elements
    private lateinit var etEmail :EditText
    private lateinit var etPassword:EditText
    private lateinit var btnSignIn:Button
    private lateinit var signUp: Button
    lateinit var loginViewModel: LoginRegisterViewModel
    //Fire Base reference
    private lateinit var mAuth :FirebaseAuth


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        etEmail = findViewById(R.id.et_login_email)
        etPassword = findViewById(R.id.et_login_password)
        btnSignIn = findViewById(R.id.btn_log_in)
        signUp = findViewById(R.id.btn_sign_up)
        mAuth = FirebaseAuth.getInstance()
        email = etEmail.text.toString()
        password = etPassword.text.toString()
        loginViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel::class.java)


        loginViewModel.userLiveData.observe(this,
            Observer<FirebaseUser> { t ->
                if (t != null) {

                    val intent = Intent(baseContext, MainActivity::class.java)
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)

                }
            })

        btnSignIn.setOnClickListener(View.OnClickListener {

            if (etEmail.text.length > 0 && etPassword.text.length >0) {
                loginViewModel.login(etEmail.text.toString(), etPassword.text.toString())
            } else {
                Toast.makeText(
                    this,
                    "Email Address and Password Must Be Entered"+ email,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        signUp.setOnClickListener {
            val intent = Intent(this, RegisterationActivity::class.java)
            startActivity(intent)

        }

    }

}