package com.makeus.milliewillie.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.net.toUri
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentPhotoSelectBinding
import com.makeus.milliewillie.ui.profile.adapter.PhotoSelectAdapter
import com.makeus.milliewillie.util.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PhotoSelectFragment:BaseDataBindingFragment<FragmentPhotoSelectBinding>(R.layout.fragment_photo_select), LoaderManager.LoaderCallbacks<Cursor> {
    companion object {
        const val PROFILE_URL_KEY = "PROFILE_URL_KEY"
    }

    private var  fbStorage: FirebaseStorage? = null
    private var  viewProfile: View? = null

    private var image = ""

    lateinit var uploadRecyclerAdapter: PhotoSelectAdapter
    lateinit var gridLayoutManager: GridLayoutManager

    private val IMAGE_LOADER_ID = 1
    private val listOfAllImages = ArrayList<String>()

    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1

    @SuppressLint("WrongConstant")
    override fun FragmentPhotoSelectBinding.onBind() {
        vi = this@PhotoSelectFragment
        if (checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return
        }


        val loaderManager: LoaderManager = LoaderManager.getInstance(this@PhotoSelectFragment)
        loaderManager.initLoader(IMAGE_LOADER_ID, null, this@PhotoSelectFragment)

        viewProfile = view

        android.util.Log.d("TAG", "fbStorage: $fbStorage")
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        listOfAllImages.clear()
        cursor?.let {
            val columnIndexData = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            while (it.moveToNext()) {
                listOfAllImages.add(it.getString(columnIndexData));
            }
            Log.e("onLoadFinished: $listOfAllImages")
            Log.d("onLoadFinished: ${listOfAllImages.size}")
            setRecyclerAdapter()
        }
    }

    fun setRecyclerAdapter() {
        uploadRecyclerAdapter = PhotoSelectAdapter(context, listOfAllImages)
        binding.photoRecycler.apply {
            gridLayoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            layoutManager = gridLayoutManager

            // 메인 리사이클러 아이템클릭 리스터
            uploadRecyclerAdapter.let {
                it.setMyUploadItemClickListener(object :
                    PhotoSelectAdapter.MyUploadItemClickListener {
                    override fun onItemClick(position: Int) {
                        image = listOfAllImages[position]
                        Log.e("onItemClick: $image")

                    }
                })

            }

            setHasFixedSize(true)
            adapter = uploadRecyclerAdapter
        }

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val selection: String? = null     //Selection criteria
        val selectionArgs = arrayOf<String>()  //Selection criteria
        val sortOrder: String? = null

        return CursorLoader(
            activity!!.applicationContext,
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {}


    fun onClickComplete() {
        (activity as ProfileActivity).transitionFragment(
            EditProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(PROFILE_URL_KEY, image)
                }
        }, "replace")
        (activity as ProfileActivity).onBackPressed()
    }

}