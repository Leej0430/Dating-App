package com.example.datingapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.datingapp.R
import kotlinx.android.synthetic.main.fragment_profile.view.*


class FragmentProfile: Fragment(){
    lateinit var userImage:ImageView
     var name:String=""
     var age:String=""
     var sex:String=""
     var bio:String=""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile,container,false)
        view.tv_user_name?.text = name
        view.tv_user_age?.text = age
        view.tv_user_sex?.text = sex
        view.tv_user_bio?.text =bio
        return view
    }
}