package com.luo.module.init

import android.content.Context
import android.graphics.Bitmap
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.fm.library.common.constants.RouterPath
import com.fm.library.common.constants.ext.getImageBitmapByUrl
import com.fm.library.common.constants.module.FaceMsg2
import com.fm.library.common.constants.net.GlobalServiceCreator
import com.fm.library.face.Face
import com.fm.library.face.toCropBitmap
import com.luo.module.init.net.InitApi
import com.luo.module.init.net.domin.toFaceMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Route(path = RouterPath.Init.PAGE_INIT)
class InitActivityMain : AppCompatActivity() {

    companion object {
        const val TAG = "InitActivityMain"
    }

    private var mAdapter = InitStoreAdapter()

    lateinit var mRv: RecyclerView
    lateinit var mTvTotal: TextView
    lateinit var mTvCurrent: TextView
    lateinit var mName: TextView
    lateinit var mProgress: ProgressBar

    private lateinit var mViewModel: InitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.init_activity_main)

        mRv = findViewById<RecyclerView>(R.id.init_rv).also {
            it.adapter = mAdapter
            it.layoutManager = GridLayoutManager(applicationContext, 4)
        }

        mTvTotal = findViewById(R.id.init_tv_total)
        mTvCurrent = findViewById(R.id.init_tv_current)
        mProgress = findViewById(R.id.progress)
        mProgress.apply {
            this.max = 101
        }

        mName = findViewById(R.id.init_tv_name)
        val wifiManager =
            getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        //判断wifi是否开启
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress
        val ip = intToIp(ipAddress)

        mName.text = ip

        mViewModel = ViewModelProvider(this).get(InitViewModel::class.java)

        requestFaceMsg()

        setupObserve()
//        val api = GlobalServiceCreator.create<InitApi>()
//        lifecycleScope.launch {
//            val res = api.getAllFaces2()
//            val list = res.body()!!
//            list.forEachIndexed { index, faceMsg2 ->
//                val bitmap = this@InitActivityMain.getImageBitmapByUrl(faceMsg2.pic!!)!!
////                val box = Face.findFace(bitmap)[0]
////                Face.getFeature(box.toCropBitmap(bitmap), box)
//                mViewModel.initFace(faceMsg2, bitmap)
//                val list = mAdapter.users
//                val tmpList = ArrayList<FaceMsg2>(list.size + 1)
//                tmpList.add(faceMsg2)
//                tmpList.addAll(list)
//
//                mAdapter.users = tmpList
//                mAdapter.notifyDataSetChanged()
//
//                mTvCurrent.text = (index + 1).toString()
//                mProgress.progress = (index + 1)
//
//                mRv.scrollToPosition(0)
//            }
//        }
    }

    private fun setupObserve() {
        mViewModel.haveFeaLiveData.observe(this) {
            runOnUiThread {
                mAdapter.users = it
                mAdapter.notifyDataSetChanged()
                mRv.scrollToPosition(0)
                mTvCurrent.text = it.size.toString()
                mProgress.progress = it.size
            }
        }
        mViewModel.noFeaLiveData.observe(this) {
            runOnUiThread {
                mTvTotal.text = it.size.toString()
            }
//            mViewModel.requestFaces()
            if (it.isNotEmpty()) {
                lifecycleScope.launch {
                    val size = 5.coerceAtMost(it.size)
                    val faceList = ArrayList<FaceMsg2>(size)
                    val bitList = ArrayList<Bitmap>(size)
                    for (i in 0 until size) {
                        faceList.add(it[i])
                        bitList.add(this@InitActivityMain.getImageBitmapByUrl(it[i].pic!!)!!)
                    }
                    mViewModel.initFace(faceList, bitList)
                }
            }
        }
        mViewModel.needToUpdate.observe(this) {
            if (it) mViewModel.requestFaces()
        }
    }

    private fun requestFaceMsg() {

        mViewModel.requestFaces()

    }

    private fun intToIp(i: Int): String {
        return (i and 0xFF).toString() + "." +
                (i shr 8 and 0xFF) + "." +
                (i shr 16 and 0xFF) + "." +
                (i shr 24 and 0xFF)
    }
}