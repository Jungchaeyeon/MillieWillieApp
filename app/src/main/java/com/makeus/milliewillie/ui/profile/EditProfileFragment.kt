package com.makeus.milliewillie.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.makeus.base.disposeOnDestroy
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.MyApplication.Companion.userName
import com.makeus.milliewillie.MyApplication.Companion.userProfileImgUrl
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentInfoEditProfileBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.PatchUsersRequest
import com.makeus.milliewillie.ui.fragment.DatePickerDdayBottomSheetDialogFragment
import com.makeus.milliewillie.ui.profile.PhotoSelectFragment.Companion.PROFILE_URL_KEY
import com.makeus.milliewillie.ui.profile.ProfileActivity.Companion.USER_BIRTHDAY_KEY
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*

class EditProfileFragment:BaseDataBindingFragment<FragmentInfoEditProfileBinding>(R.layout.fragment_info_edit_profile) {
    var pickImageFromAlbum = 0
    var fbStorage: FirebaseStorage? = null
    var uriPhoto: Uri? = null
    var downloadUri: Uri? = null
    var viewProfile: View? = null
    private var userBirthday: String? = null


    private val viewModel by viewModel<ProfileViewModel>()

    override fun FragmentInfoEditProfileBinding.onBind() {
        vi = this@EditProfileFragment
        vm = viewModel

        arguments?.let {
            Log.e("userBirthday = $it")
            userBirthday = it.getString(USER_BIRTHDAY_KEY).toString()
            viewModel.liveDataUserBirth.value = userBirthday
            Log.e("liveDataUserBirth = ${viewModel.liveDataUserBirth.value}")
        }

//        if (viewModel.liveDataUserBirth.value.isNullOrBlank()) viewModel.liveDataUserBirth.postValue("0000. 00. 00")

        arguments?.let {
            if (it.getString(PROFILE_URL_KEY)?.isNotBlank() == true) {
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
                funImgDownLoad(view)
            }
        } else {
            executePatchUsers()
        }

    }

    private fun funImgDownLoad(view: View) {
        val timeStamp = SimpleDateFormat("yyyyMMDD_HHmmss").format(Date())
        val imgFileName = "IMAGE_${timeStamp}_.png"
        val storageRef = fbStorage?.reference?.child("profiles")?.child(imgFileName)

        val stream: FileInputStream
        val uploadTask: UploadTask?

        if (uriPhoto != null) {
            stream = FileInputStream(File("$uriPhoto"))
            uploadTask = storageRef?.putStream(stream)

            val urlTask = uploadTask?.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageRef?.downloadUrl
            }?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    this.downloadUri = downloadUri
                    Log.e("downloadUri = ${this.downloadUri}")
                    executePatchUsers()
                } else {
                    // Handle failures
                    // ...
                    Log.e("Handler Failure")
                }
            }
        }
    }

    private fun executePatchUsers() {
        if (userBirthday.isNullOrBlank()
            || userBirthday == "0000. 00. 00") userBirthday = null

        val profileImg = if (downloadUri == null) null
        else downloadUri.toString()

        Log.e("profileImg = $profileImg")
        viewModel.apiRepository.patchUsers(
            body = PatchUsersRequest(
                name = binding.userEditUserName.text.toString(),
                profileImg = profileImg, birthday = userBirthday
            )
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess){
                    Log.e("patchUsers 호출 성공")

                    userName = it.result.name
                    userProfileImgUrl = it.result.profileImg
                    userBirthday = it.result.birthday
                    viewModel.liveDataUserBirth.value = userBirthday

                    Log.e("userName = $userName")
                    Log.e("userProfileImgUrl = $userProfileImgUrl")
                    Log.e("userBirthday = ${viewModel.liveDataUserBirth.value.toString()}")
                } else {
                    Log.e("patchUsers 호출 실패")
                    Log.e(it.message)
                }
                activity?.supportFragmentManager?.popBackStack()
                (activity as ProfileActivity).onBackPressed()
            }.disposeOnDestroy(this)
    }

    fun onClickBirthday() {
        DatePickerDdayBottomSheetDialogFragment.getInstance()
            .setOnClickOk{date, dotDate, gapDays, dateYear, dateMonth ->
                userBirthday = dotDate
                viewModel.liveDataUserBirth.value = dotDate
            }.show(fragmentManager!!)
    }

    fun onClickPhoto() {
        (activity as ProfileActivity).transitionFragment(PhotoSelectFragment(), "add")
    }

    fun onClickComplete() {
        if (TextUtils.isEmpty(binding.userEditUserName.text.toString())) {
            getString(R.string.toast_must_input_name).showShortToastSafe()
        } else {
            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                funImageUpload(viewProfile!!)
            }
        }
    }

    fun onClickCancel() {
        (activity as ProfileActivity).onBackPressed()
    }

}