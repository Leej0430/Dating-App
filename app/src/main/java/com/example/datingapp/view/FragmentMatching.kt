package com.example.datingapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.datingapp.R
import kotlinx.android.synthetic.main.fragment_profile.view.*


class FragmentMatching: Fragment(){
    var name:String=""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile,container,false)
        view.tv_name_fragment.text = name
        return view
    }
}