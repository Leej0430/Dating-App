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
import com.google.firebase.database.FirebaseDatabase


class Repository {

    private lateinit var application: Application
    private lateinit var mAuth :FirebaseAuth
    private lateinit var mDatabaseReference :FirebaseDatabase
    private lateinit var users: MutableLiveData<FirebaseUser>
    private lateinit var usersLoggedOut: MutableLiveData<Boolean>



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
                        val userId = mAuth.currentUser?.uid
                        val currentUserDbMale = mDatabaseReference.reference.child("users")


                            currentUserDbMale.child(sex.toString()).child(userId).child("sex").setValue(sex.toString())
                            currentUserDbMale.child(sex.toString()).child(userId).child("name").setValue(name)
                            currentUserDbMale.child(sex.toString()).child(userId).child("email").setValue(email)
                            currentUserDbMale.child(sex.toString()).child(userId).child("password").setValue(password)

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
    fun UpdateUserInfos(name:String, email:String, password: String?, sex: String)
    {

        //todo: The update function need to be done in the Repository and ViewModel
        //todo : Make the update function is able to take : the name, img, sex, bio, and age


        val userId = mAuth.currentUser.toString()
        val currentUserDb = mDatabaseReference.reference.child(sex)

        currentUserDb.child(userId).child("name").setValue(name)
        currentUserDb.child(userId).child("email").setValue(email)
        currentUserDb.child(userId).child("password").setValue(password)
    }


}