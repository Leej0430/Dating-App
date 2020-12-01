package com.example.datingapp.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.datingapp.R
import com.example.datingapp.viewmodel.LoginRegisterViewModel


class MainActivity : AppCompatActivity() {


    private lateinit var btnLogOut: Button
    private lateinit var logOutViewModel: LoginRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnLogOut = findViewById(R.id.btn_logOut)

        logOutViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel::class.java)

        logOutViewModel.logOutLiveData.observe(this, Observer<Boolean> { t ->
            if (t ) {

                val intent = Intent(baseContext, SignInActivity::class.java)
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            }
        })

        btnLogOut.setOnClickListener {
            logOutViewModel.logout()


        }
    }


}