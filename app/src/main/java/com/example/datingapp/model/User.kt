package com.example.datingapp.model

data class User(

    var name: String="1",
    var bio:String ="1",
    var sex : String="1",
    var age:String ="1"

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

