package com.example.datingapp.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.datingapp.R
import com.example.datingapp.viewmodel.LoginRegisterViewModel
import com.google.firebase.auth.FirebaseUser


class RegisterationActivity : AppCompatActivity() {
    //UI elements
    private lateinit var btnRegister:Button
    private lateinit var etEmail:EditText
    private lateinit var etPassword:EditText
    private lateinit var etUsername:EditText
    private lateinit var rbSex :RadioGroup
    private lateinit var mProgressBar:ProgressDialog
    lateinit var authMainViewModel : LoginRegisterViewModel
    lateinit var sex : String

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeration)
        btnRegister = findViewById(R.id.btn_register_register)
        etEmail = findViewById(R.id.et_register_email)
        etPassword = findViewById(R.id.et_register_password)
        mProgressBar = ProgressDialog(this)
        etUsername = findViewById(R.id.et_register_username)
        rbSex = findViewById(R.id.rb_register_sex)
        authMainViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel::class.java)


        rbSex.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { rg, checkedId ->
            for (i in 0 until rg.childCount) {
                val btn = rg.getChildAt(i) as RadioButton
                if (btn.id == checkedId) {
                    sex = btn.text.toString()
                    // do something with text
                    return@OnCheckedChangeListener
                }
            }
        })



        authMainViewModel.userLiveData.observe(this, object : Observer<FirebaseUser> {
            override fun onChanged(t: FirebaseUser?) {
                if (t != null) {
                    val intent = Intent(baseContext, SignInActivity::class.java)
                    startActivity(intent)
                }
            }
        })

        btnRegister!!.setOnClickListener {
            if (etUsername.text.length > 0 &&
                etEmail.text.length > 0 &&
                etPassword.text.length > 0 )
            {


                authMainViewModel.register(
                    etUsername.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString(), sex
                )

            } else {
                Toast.makeText(
                    this,
                    "Email Address and Password Must Be Entered",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }





}