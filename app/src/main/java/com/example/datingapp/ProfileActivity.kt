package com.example.datingapp




import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }
    private fun selectCamera(){
        var permission = ContextCompat.checkSelfPermission(this,
            "Manifest.permission.CAMERA")

    }
}