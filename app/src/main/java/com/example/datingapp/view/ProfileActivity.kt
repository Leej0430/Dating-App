package com.example.datingapp.view
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.datingapp.R
import com.example.datingapp.viewmodel.LoginRegisterViewModel
import com.google.firebase.auth.FirebaseUser
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private val REQUEST_FROM_CAMERA =1
    private val REQUEST_FROM_ALBUM=2
    lateinit var currentPhotoPath:String

    lateinit var authMainViewModel : LoginRegisterViewModel
    lateinit var imgPic: ImageButton
    lateinit var etName: EditText
    lateinit var etAge: EditText
    lateinit var etSex: EditText
    lateinit var etBio: EditText
    lateinit var btnSave:Button
    lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        imgPic= findViewById(R.id.img_picture)
        etName = findViewById(R.id.et_profile_name)
        etAge = findViewById(R.id.et_profile_age)
        etSex = findViewById(R.id.et_profile_sex)
        etBio = findViewById(R.id.et_profile_bio)
        btnSave = findViewById(R.id.btn_profile_save)
        authMainViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel::class.java)



        imgPic.setOnClickListener{
            builder = AlertDialog.Builder(this)
            builder.setTitle("Select picture from")
            val lst = arrayOf("Camera","Gallery")
            builder.setItems(lst){dialog,which->
                when(which){
                    0->startCapture()
                    1->goToGallery()
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
        etSex.setOnClickListener{
            builder = AlertDialog.Builder(this)
            builder.setTitle("Sex")
            val lst = arrayOf("Male","Female")
            builder.setItems(lst){dialog,which->
                when(which){
                    0->etSex.setText(lst[0])
                    1->etSex.setText(lst[1])
                }
            }
            val dialog = builder.create()
            dialog.show()
        }


        authMainViewModel.userLiveData.observe(this,
            Observer<FirebaseUser> { t ->
                if (t != null) {
                    //val intent = Intent(baseContext, MainActivity::class.java)
                    //startActivity(intent)
                }
            })
        btnSave.setOnClickListener {

            authMainViewModel.updateUserInfos(imgPic.toString(), etName.text.toString(),
            etSex.text.toString(), etAge.text.toString(), etBio.text.toString())
        }


    }


    //function for the permission
    private fun settingPermission() {
        var permission = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(this@ProfileActivity,
                    "Access Allowed",
                    Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@ProfileActivity,
                    "Access Denied",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permission)
            .setRationaleMessage("Need Authorization for Camera")
            .setDeniedMessage("Access to Camera Denied")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ).check()
    }

    //function for the save image as file to prevent duplication set the time stamp
    @Throws(IOException::class)
    private fun createFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss")
            .format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    //function for the call the camera (takePictureIntent for the call camera)
    private fun startCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createFile()
                } catch (e: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.myapplication.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_FROM_CAMERA)
                }
            }
        }
    }

    //this function for the showing image on the image view
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_FROM_CAMERA ) {
                val file = File(currentPhotoPath)
                if (Build.VERSION.SDK_INT < 28) {
                    val bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))

                    imgPic.setImageBitmap(bitmap)
                }
                else {
                    val decode = ImageDecoder.createSource(
                        this.contentResolver,
                        Uri.fromFile(file)
                    )
                    val bitmap = ImageDecoder.decodeBitmap(decode)
                    imgPic.setImageBitmap(bitmap)


                }
            }
            else if (requestCode == REQUEST_FROM_ALBUM){
                var currentImageUrl: Uri? = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,
                    currentImageUrl)
                imgPic.setImageBitmap(bitmap)
            }
        }
    }
    //lead user to the Gallery
    private fun goToGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
        startActivityForResult(intent,REQUEST_FROM_ALBUM)
    }



}