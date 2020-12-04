package com.example.datingapp.model

data class User(

    val name: String,
    val email: String,
    val password: String,
    val sex : String,
    val uid: String,
    val imageUrl:String

)

data class ChatObject(
    val chatId: String,
    val chatName: String,
    val image:String,
   val  usersList: ArrayList<User>

){
    fun addUserToList(user: User){
        usersList.add(user)
    }
    fun getUserList(): ArrayList<User>{
        return usersList
    }
}

