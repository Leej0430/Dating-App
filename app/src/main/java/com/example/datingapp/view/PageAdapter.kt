package com.example.datingapp.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PageAdapter(fragmentManager: FragmentManager):FragmentStatePagerAdapter(fragmentManager){
    private var fragments :ArrayList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int =fragments.size
    fun addPages(fragment:Fragment){
        fragments.add(fragment)
    }

}