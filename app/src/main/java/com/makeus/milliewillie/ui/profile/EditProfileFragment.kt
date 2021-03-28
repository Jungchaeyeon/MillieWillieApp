package com.makeus.milliewillie.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentInfoEditProfileBinding
import com.makeus.milliewillie.ui.profile.PhotoSelectFragment.Companion.PROFILE_URL_KEY
import com.makeus.milliewillie.util.Log
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*

class EditProfileFragment:BaseDataBindingFragment<FragmentInfoEditProfileBinding>(R.layout.fragment_info_edit_profile) {
    var pickImageFromAlbum = 0
    var fbStorage: FirebaseStorage? = null
    var uriPhoto: Uri? = null
    var viewProfile: View? = null


    override fun FragmentInfoEditProfileBinding.onBind() {
        vi = this@EditProfileFragment

        arguments?.let {
            if (it.getString(PROFILE_URL_KEY)!!.isNotBlank()) {
                uriPhoto = it.getString(PROFILE_URL_KEY)!!.toUri()
            }
            Log.e("uriPhoto1: $uriPhoto")
        }
        if (uriPhoto != null) {
            Log.e("uriPhoto2: $uriPhoto")
            Glide.with(binding.userImgUserImage).load(uriPhoto.toString())
                .placeholder(R.drawable.graphic_profile_big_with_camera).circleCrop().into(binding.userImgUserImage)
        }

        viewProfile = view

        fbStorage = FirebaseStorage.getInstance()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.e("requestCode: $requestCode, pickImageFromAlbum: $pickImageFromAlbum")
        Log.e("resultCode: $resultCode, resultCode: ${Activity.RESULT_OK}")
        if (requestCode == pickImageFromAlbum) {
            if (resultCode == Activity.RESULT_OK) {
                uriPhoto = data?.data
                Log.e("uriPhoto: $uriPhoto")
                if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    funImageUpload(viewProfile!!)
                }
            }
        }

    }

    private fun funImageUpload(view: View) {
        val timeStamp = SimpleDateFormat("yyyyMMDD_HHmmss").format(Date())
        val imgFileName = "IMAGE_${timeStamp}_.png"
        val storageRef = fbStorage?.reference?.child("profiles")?.child(imgFileName)

        Log.e("storageRef = $storageRef")
        Log.e("uriPhoto: $uriPhoto")
        val stream: FileInputStream
        val uploadTask: UploadTask?

        if (uriPhoto != null) {
            stream = FileInputStream(File("$uriPhoto"))
            Log.e("stream: $stream")

            uploadTask = storageRef?.putStream(stream)
            Log.e("uploadTask: $uploadTask")

            uploadTask?.addOnFailureListener {
                Toast.makeText(view.context, "Image Uploaded Failure", Toast.LENGTH_SHORT).show()
            }?.addOnSuccessListener { taskSnapshot ->
                Toast.makeText(view.context, "Image Uploaded", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun onClickPhoto() {
        (activity as ProfileActivity).transitionFragment(PhotoSelectFragment(), "add")
    }

    fun onClickComplete() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            funImageUpload(viewProfile!!)
        }
        // api 호출 예정
        activity?.supportFragmentManager?.popBackStack()
        (activity as ProfileActivity).onBackPressed()
    }

    fun onClickCancel() {
        (activity as ProfileActivity).onBackPressed()
    }

}