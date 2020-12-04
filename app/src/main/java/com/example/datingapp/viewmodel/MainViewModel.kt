package com.example.datingapp.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.datingapp.model.Repository
import com.example.datingapp.model.User
import com.google.firebase.auth.FirebaseUser


class LoginRegisterViewModel(application: Application) :
    AndroidViewModel(application) {

    private val authAppRepository: Repository by lazy { Repository(application) }

    val userLiveData: MutableLiveData<FirebaseUser> = authAppRepository.getUserLiveData()
    var logOutLiveData: MutableLiveData<Boolean> = authAppRepository.getLoggedOutLiveData()

    @RequiresApi(Build.VERSION_CODES.P)
    fun login(email: String?, password: String?) {
        authAppRepository.login(email, password)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun register(name: String?, email: String?, password: String?, sex:String?) {
        authAppRepository.register(name, email, password, sex)
    }
    fun logout(){
        authAppRepository.logOut()
    }
    fun updateUserInfo(mgUr: String?, name: String?, sex: String?, age: String?, bio: String? ){
        authAppRepository.updateUserInfo(mgUr!!, name!!, sex!!, age!!, bio!!)
    }
    fun getUsersList():MutableLiveData<ArrayList<User>>{
        return authAppRepository.getListOfUsers()
    }


}