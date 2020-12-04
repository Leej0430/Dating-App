package com.example.datingapp.view


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.datingapp.R
import com.example.datingapp.R.layout.activity_main
import com.example.datingapp.model.User
import com.example.datingapp.viewmodel.LoginRegisterViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tab_button.view.*

const val TAG ="MAIN_ACTIVITY"
class MainActivity : AppCompatActivity() {
    private lateinit var logOutViewModel: LoginRegisterViewModel
    private lateinit var mContext :Context
    private lateinit var user:User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        logOutViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel::class.java)
        user = User()
        //set the viewPage
        mContext = applicationContext


        val auth = logOutViewModel.getAuth()
        val database = logOutViewModel.getDatabase().getReference("users").child(auth.currentUser?.uid.toString())
        database.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                    user.name=p0?.child("name")?.value.toString()
                    user.sex=p0?.child("sex")?.value.toString()

                initViewPager(user)
            }

        })
        //set the action bar
        setSupportActionBar(tl_tl)

        //logout observer
        logoutListener(logOutViewModel)
    }
    //for the button on action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.setting,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            R.id.user_profile-> move()
            R.id.log_out->logOutViewModel.logout()
        }
        return super.onOptionsItemSelected(item)
    }

    //Show TabView for each tab
    private fun createTabView(tabName:String):View{
        var tabView = LayoutInflater.from(mContext).inflate(R.layout.tab_button,null)
        tabView.tab_text.text = tabName
        when(tabName){
            "Profile"-> return tabView
            "Matching"-> return tabView
            "Chatting"->return tabView
            else-> return tabView
        }

    }

    //initialize each viewPage
    private fun initViewPager(u:User){
        val profileFragment= FragmentProfile()
        profileFragment.name = u.name
        profileFragment.sex = u.sex
        profileFragment.bio = "Profile"
        profileFragment.age = "Profile"
        val matchingFragment = FragmentMatching()
        matchingFragment.name = "Matching"
        val chattingFragment = FragmentChatting()
        chattingFragment.name = "Chatting"

        val adapter = PageAdapter(supportFragmentManager)
        adapter.addPages(profileFragment)
        adapter.addPages(matchingFragment)
        adapter.addPages(chattingFragment)

        main_view_page.adapter = adapter
        tl_tabs.setupWithViewPager(main_view_page)

        tl_tabs.getTabAt(0)?.setCustomView(createTabView("Profile"))
        tl_tabs.getTabAt(1)?.setCustomView(createTabView("Matching"))
        tl_tabs.getTabAt(2)?.setCustomView(createTabView("Chatting"))
    }
    //Move to Profile Page
    private fun move(){
        val intent = Intent(this,ProfileActivity::class.java)
        startActivity(intent)
    }
    //logout listener
    private fun logoutListener (logout:LoginRegisterViewModel){
        logout.logOutLiveData.observe(this, Observer<Boolean> { t ->
            if (t ) {
                val intent = Intent(baseContext, SignInActivity::class.java)
                startActivity(intent)

            }
        })
    }




}