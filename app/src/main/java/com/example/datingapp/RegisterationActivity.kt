package com.example.datingapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterationActivity : AppCompatActivity() {
    //UI elements
    private lateinit var btnRegister:Button
    private lateinit var etEmail:EditText
    private lateinit var etPassword:EditText
    private lateinit var etUsername:EditText
    private lateinit var rbSex :RadioGroup
    private lateinit var mProgressBar:ProgressDialog
    // Firebase reference
    private var mDatabaseReference:DatabaseReference? =null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth:FirebaseAuth ? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeration)
        initialize()

    }
    private fun initialize(){
        btnRegister = findViewById(R.id.btn_register_register)
        etEmail = findViewById(R.id.et_register_email)
        etPassword = findViewById(R.id.et_register_password)
        mProgressBar = ProgressDialog(this)
        etUsername = findViewById(R.id.et_register_username)
        rbSex = findViewById(R.id.rb_register_sex)

        //Initialize the database
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()

        btnRegister!!.setOnClickListener {
            createNewAccount()
        }

    }

    private fun createNewAccount(){
        val username = etUsername.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val sex =findViewById<RadioButton>(rbSex.checkedRadioButtonId)

        mProgressBar!!.setMessage("Registering")
        mProgressBar.show()
        //Set the email and password
        mAuth!!.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this)
        {task ->
            mProgressBar.hide()
            if(task.isSuccessful){
                Toast.makeText(this@RegisterationActivity,
                    "Regesteration Success",
                    Toast.LENGTH_SHORT).show()
                //update user profile information
                val userId = mAuth!!.currentUser!!.uid
                val currentUserDb = mDatabaseReference!!.child(sex.text.toString())

                currentUserDb.child(userId).child("name").setValue(username)
                currentUserDb.child(userId).child("email").setValue(email)
                currentUserDb.child(userId).child("password").setValue(password)
                finish()
                move()
            }
            else{
                //Make a Toast when fail to the registeration
                Toast.makeText(this@RegisterationActivity,
                    "Regesteration failed",
                    Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun move(){
        //Move to SignIn page
        val intent = Intent(this@RegisterationActivity,SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}