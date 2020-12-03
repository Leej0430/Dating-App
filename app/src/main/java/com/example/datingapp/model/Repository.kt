package com.example.datingapp.model

import android.app.Application
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class Repository {

    private  var application: Application
    private  var mAuth :FirebaseAuth
    private  var mDatabaseReference :FirebaseDatabase
    private  var users: MutableLiveData<FirebaseUser>
    private  var usersLoggedOut: MutableLiveData<Boolean>
    lateinit var currentUserDbMale: DatabaseReference
    lateinit var userId: String


    constructor (application: Application)
    {
        this.application = application
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance()
        users = MutableLiveData()
        usersLoggedOut = MutableLiveData()

        if (mAuth.getCurrentUser() != null) {
            users.postValue(mAuth.currentUser)
            usersLoggedOut.postValue(false)
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun register(name: String?, email: String?, password: String?, sex: String?) {
        mAuth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(
                application.mainExecutor,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                      // userId=  mAuth.currentUser?.uid!!
                        currentUserDbMale= mDatabaseReference.reference.child("users")

                        userId = mAuth.currentUser!!.uid

                        currentUserDbMale.child(sex.toString()).child(userId).child("sex").setValue(
                            sex.toString()
                        )
                        currentUserDbMale.child(sex.toString()).child(userId).child("name")
                            .setValue(
                                name
                            )
                        currentUserDbMale.child(sex.toString()).child(userId).child("email")
                            .setValue(
                                email
                            )
                        currentUserDbMale.child(sex.toString()).child(userId).child("password")
                            .setValue(
                                password
                            )

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
    fun getLodInUser(): FirebaseUser{
        return mAuth.currentUser!!
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean> {
        return usersLoggedOut
    }
    fun UpdateUserInfos(imgUr: String?, name: String?, sex: String?, age: String?, bio: String?)
    {

        //todo: The update function need to be done in the Repository and ViewModel
        //todo : Make the update function is able to take : the name, img, sex, bio, and age
        userId = mAuth.currentUser!!.uid

        currentUserDbMale= mDatabaseReference.reference.child("users")
        currentUserDbMale.child(sex.toString()).child(userId).child("umageUrl").setValue(Uri.parse(imgUr).toString())
        currentUserDbMale.child(sex.toString()).child(userId).child("name").setValue(name)
        currentUserDbMale.child(sex.toString()).child(userId).child("sex").setValue(sex)
        currentUserDbMale.child(sex.toString()).child(userId).child("age").setValue(age)
        currentUserDbMale.child(sex.toString()).child(userId).child("bio").setValue(bio)

        users.postValue(mAuth.currentUser)




    }
/**
 * First, I took the user's zipcode as input, than found the latitude and longitude
 *  values by using one of the freely available APIs,
    Once I got the latitude, longitude pair,I was able
    to easily use the function suggested by @Bohemian...
    However after sometimes I found that google and Yahoo APIs are the best
    possible way for this IMHO, so I integrated that in my project. If any one
    interested I can paste the code for that here.
 *
 *
    fun haversine(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val r = 6371 // average radius of the earth in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lng2 - lng1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                (Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }
    **/


}