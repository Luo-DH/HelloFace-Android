package com.fm.module.manager.store

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.fm.library.common.constants.RouterPath
import com.fm.library.common.constants.net.GlobalServiceCreator
import com.fm.module.manager.databinding.ManagerActivityStoreBinding
import com.fm.module.manager.net.ManagerStoreApi
import kotlinx.coroutines.runBlocking

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
    }

    private fun requestFaceMsg() {
        val api = GlobalServiceCreator.create<ManagerStoreApi>()
        runBlocking {
            val res = api.getAllFaces()
            if (res.isSuccessful && res.body()!!.code == 0) {
                storeAdapter.users = res.body()!!.res
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