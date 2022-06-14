package com.fm.module.manager.store

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.fm.library.common.constants.RouterPath
import com.fm.library.common.constants.net.GlobalServiceCreator
import com.fm.module.manager.databinding.ManagerActivityStoreBinding
import com.fm.module.manager.net.ManagerStoreApi
import com.wildma.pictureselector.PictureBean
import com.wildma.pictureselector.PictureSelector
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@Route(path = RouterPath.Manager.PAGE_STORE)
class ManagerActivityStore : AppCompatActivity() {

    private val TAG = "ManagerActivityStore"

    private lateinit var binding: ManagerActivityStoreBinding

    private val storeAdapter = ManagerStoreAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ManagerActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // 网络请求
        requestFaceMsg()

        // 点击监听
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.managerStoreAddUser.setOnClickListener {
            PictureSelector
                .create(this, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(true, 200, 200, 1, 1);
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*结果回调*/if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                val pictureBean: PictureBean =
                    data.getParcelableExtra(PictureSelector.PICTURE_RESULT)!!
                Log.d(TAG, "onActivityResult: ${pictureBean.path}")
                val file = File(pictureBean.path)

                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file);

                // MultipartBody.Part  和后端约定好Key，这里的partName是用file
                val body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                runBlocking {
                    val api = GlobalServiceCreator.create<ManagerStoreApi>()
                    val res = api.upload(body)
                    Log.d(TAG, "onActivityResult: ${res.body()!!.msg}")
                }

            }
        }
    }


    private fun requestFaceMsg() {
        val api = GlobalServiceCreator.create<ManagerStoreApi>()
        runBlocking {
            val res = api.getAllFaces2()
            if (res.isSuccessful
//                && res.body()!!.code == 0
            ) {
                storeAdapter.users = res.body()!!
            }
        }
    }


    private fun setupRecyclerView() {
        binding.managerStoreRv.apply {
            this.layoutManager = GridLayoutManager(applicationContext, 2)
            this.adapter = storeAdapter
            this.addItemDecoration(CardItemDecoration())
        }
    }

}