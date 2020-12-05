package com.example.datingapp.model

import android.app.Application
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class Repository(private var application: Application) {



    private  var mAuth :FirebaseAuth = FirebaseAuth.getInstance()
    private  var mDatabaseReference :FirebaseDatabase = FirebaseDatabase.getInstance()
    private  var users: MutableLiveData<FirebaseUser> = MutableLiveData()
    private var userList: MutableLiveData<ArrayList<User>> = MutableLiveData()
    private  var usersLoggedOut: MutableLiveData<Boolean> = MutableLiveData()
    private val currentUserDbMale: DatabaseReference = mDatabaseReference.reference.child("users")

    // private lateinit var currentUserDbMale: DatabaseReference
    private  var userId:String? = mAuth.currentUser?.uid.toString()


    init {
        if (mAuth.currentUser != null) {
            users.postValue(mAuth.currentUser);
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

                        //userId = mAuth.currentUser?.uid!!

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
                        users.postValue(mAuth.currentUser)
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
    private  val TAG = "Repository"
    fun updateUserInfo(imgUrl: String, name: String, sex: String, age: String, bio: String)
    {

        Log.d(TAG, "updateUserInfo: #### ${userId} #####")
        //userId = mAuth.currentUser.toString()

        currentUserDbMale.child(userId).child("image").setValue(Uri.parse(imgUrl).toString())
        currentUserDbMale.child(userId).child("name").setValue(name)
        currentUserDbMale.child(userId).child("sex").setValue(sex)
        currentUserDbMale.child(userId).child("age").setValue(age)
        currentUserDbMale.child(userId).child("bio").setValue(bio)

        users.postValue(mAuth.currentUser)

    }
    fun getDatabase():FirebaseDatabase{
        return mDatabaseReference
    }
    fun getAuth():FirebaseAuth{
        return mAuth
    }

    fun getUser():User{


        val user: User()
        val database = mDatabaseReference.getReference("users").child(mAuth.currentUser?.uid.toString())

        database.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                user.name=p0?.child("name")?.value.toString()
                user.sex=p0?.child("sex")?.value.toString()

                initViewPager(user)
            }
            return user
    }

}