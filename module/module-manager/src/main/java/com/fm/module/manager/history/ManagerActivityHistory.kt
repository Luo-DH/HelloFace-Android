package com.fm.module.manager.history

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.fm.library.common.constants.RouterPath
import com.fm.library.common.constants.net.GlobalServiceCreator
import com.fm.module.manager.User
import com.fm.module.manager.databinding.ManagerActivityHistoryBinding
import com.fm.module.manager.net.ManagerStoreApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

@Route(path = RouterPath.Manager.PAGE_HISTORY)
class ManagerActivityHistory : AppCompatActivity() {

    private lateinit var binding: ManagerActivityHistoryBinding

    private val historyAdapter = ManagerHistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ManagerActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        setupRecyclerView()

        getHistoryData()

    }

    private fun getHistoryData() {
        runBlocking {
            val api = GlobalServiceCreator.create<ManagerStoreApi>()
            val res = api.getAllHistoryData()
            if (res.isSuccessful) {
                historyAdapter.users = res.body()!!.res
            }
        }
    }

    private fun setupRecyclerView() {
        val divider = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
        binding.managerHistoryRv.apply {
            this.layoutManager = LinearLayoutManager(applicationContext)
            this.adapter = historyAdapter
            this.addItemDecoration(divider)
        }
    }

}