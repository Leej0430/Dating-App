package com.example.datingapp.view


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.datingapp.R
import com.example.datingapp.viewmodel.LoginRegisterViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tab_button.view.*


class MainActivity : AppCompatActivity() {
    private lateinit var logOutViewModel: LoginRegisterViewModel
    private lateinit var mContext :Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logOutViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel::class.java)

        //set the action bar
        setSupportActionBar(tl_tl)

        //set the viewPage
        mContext = applicationContext
        initViewPager()

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
    private fun initViewPager(){
        val profileFragment= FragmentProfile()
        profileFragment.pageName = "Profile"
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