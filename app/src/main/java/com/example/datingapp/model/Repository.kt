package com.example.datingapp.model

import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class Repository {

    private  var application: Application
    private  var mAuth :FirebaseAuth
    private  var mDatabaseReference :FirebaseDatabase
    private  var users: MutableLiveData<FirebaseUser>
    private  var usersLoggedOut: MutableLiveData<Boolean>
    lateinit var currentUserDbMale: DatabaseReference
    lateinit var userId:String



    constructor (application: Application)
    {
        this.application = application
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance()
        users = MutableLiveData()
        usersLoggedOut = MutableLiveData()

        if (mAuth.getCurrentUser() != null) {
            users.postValue(mAuth.getCurrentUser());
            usersLoggedOut.postValue(false);
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun register(name: String?, email: String?, password: String?, sex:String?) {
        mAuth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(
                application.mainExecutor,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful)
                    {
                        userId = mAuth.currentUser?.uid!!
                        currentUserDbMale= mDatabaseReference.reference.child("users")


                            currentUserDbMale.child(userId).child("sex").setValue(sex.toString())
                            currentUserDbMale.child(userId).child("name").setValue(name)
                            currentUserDbMale.child(userId).child("email").setValue(email)
                            currentUserDbMale.child(userId).child("password").setValue(password)

                        users.postValue(mAuth.currentUser)



                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            "Registration Failure: " + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }
    @RequiresApi(Build.VERSION_CODES.P)
    fun login(email: String?, password: String?) {
        mAuth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(
                application.mainExecutor,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        users.postValue(mAuth.getCurrentUser())
                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            "Login Failure: " + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }
    fun logOut() {
        mAuth.signOut()
        usersLoggedOut.postValue(true)
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        return users
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean> {
        return usersLoggedOut
    }
    fun updateUserInfos(mgUr: String, name: String, sex: String, age: String, bio: String)
    {

        userId = mAuth.currentUser.toString()

        currentUserDbMale.child(userId).child("image").setValue(mgUr)
        currentUserDbMale.child(userId).child("name").setValue(name)
        currentUserDbMale.child(userId).child("sex").setValue(sex)
        currentUserDbMale.child(userId).child("age").setValue(age)
        currentUserDbMale.child(userId).child("bio").setValue(bio)

        users.postValue(mAuth.currentUser)

    }


}