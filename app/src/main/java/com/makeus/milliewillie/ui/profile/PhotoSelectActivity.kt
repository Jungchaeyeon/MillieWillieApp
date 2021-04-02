package com.makeus.milliewillie.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.MyApplication
import com.makeus.milliewillie.MyApplication.Companion.userProfileImgUrl
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityPhotoSelectBinding
import com.makeus.milliewillie.model.PhotoSelectedItems
import com.makeus.milliewillie.ui.SampleToast
import com.makeus.milliewillie.ui.profile.adapter.PhotoSelectAdapter
import com.makeus.milliewillie.util.Log
import java.util.*
import kotlin.collections.ArrayList

class PhotoSelectActivity:BaseDataBindingActivity<ActivityPhotoSelectBinding>(R.layout.activity_photo_select), LoaderManager.LoaderCallbacks<Cursor> {
    companion object {
        const val PROFILE_URL_KEY = "PROFILE_URL_KEY"
    }

    private var  fbStorage: FirebaseStorage? = null
    private var  viewProfile: View? = null

    lateinit var uploadRecyclerAdapter: PhotoSelectAdapter
    lateinit var gridLayoutManager: GridLayoutManager

    private val IMAGE_LOADER_ID = 1
    private val listOfAllImages = ArrayList<PhotoSelectedItems>()

    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1

    @SuppressLint("WrongConstant")
    override fun ActivityPhotoSelectBinding.onBind() {
        vi = this@PhotoSelectActivity

        // 최초 1회 퍼미션 허용 팝업
        if (checkSelfPermission(this@PhotoSelectActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)

            ActivityNavigator.with(this@PhotoSelectActivity).photoSelect().start()
            finish()

            return
//            (activity as ProfileActivity).transitionFragment(PhotoSelectFragment(), "replace")
        }

        val loaderManager: LoaderManager = LoaderManager.getInstance(this@PhotoSelectActivity)
        loaderManager.initLoader(IMAGE_LOADER_ID, null, this@PhotoSelectActivity)

        viewProfile = binding.root

        android.util.Log.d("TAG", "fbStorage: $fbStorage")
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        listOfAllImages.clear()
        cursor?.let {
            val columnIndexData = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            while (it.moveToNext()) {
                listOfAllImages.add(PhotoSelectedItems(it.getString(columnIndexData)));
            }
            Log.e("onLoadFinished: $listOfAllImages")
            Log.d("onLoadFinished: ${listOfAllImages.size}")
            setRecyclerAdapter()
        }
    }

    var postPosition: Int = -1
    fun setRecyclerAdapter() {
        uploadRecyclerAdapter = PhotoSelectAdapter(this, listOfAllImages)
        binding.photoRecycler.apply {
            gridLayoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            layoutManager = gridLayoutManager

            // 메인 리사이클러 아이템클릭 리스터
            uploadRecyclerAdapter.let {
                it.setMyUploadItemClickListener(object :
                    PhotoSelectAdapter.MyUploadItemClickListener {
                    override fun onItemClick(position: Int) {
                        val items = listOfAllImages[position]
                        when (items.isCheck) {
                            false -> {
                                if (postPosition == -1) {
                                    items.isCheck = true
                                    postPosition = position
                                    uploadRecyclerAdapter.notifyDataSetChanged()
                                } else {
                                    listOfAllImages[postPosition].isCheck = false
                                    items.isCheck = true
                                    postPosition = position
                                    uploadRecyclerAdapter.notifyDataSetChanged()
                                }
                            }
                            true -> {
                                items.isCheck = false
                                postPosition = -1
                                uploadRecyclerAdapter.notifyDataSetChanged()
                            }
                        }
                        userProfileImgUrl = listOfAllImages[position].uri
                        Log.e(userProfileImgUrl)
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
            applicationContext,
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {}


    fun onClickComplete() {
        Log.e("onComplete")
        onBackPressed()
    }

}