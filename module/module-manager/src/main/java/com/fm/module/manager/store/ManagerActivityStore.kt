package com.fm.module.manager.store

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.fm.library.common.constants.RouterPath
import com.fm.module.manager.User
import com.fm.module.manager.databinding.ManagerActivityStoreBinding
import kotlin.concurrent.thread

@Route(path = RouterPath.Manager.PAGE_STORE)
class ManagerActivityStore : AppCompatActivity() {

    private lateinit var binding: ManagerActivityStoreBinding

    private val storeAdapter = ManagerStoreAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ManagerActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        thread {
            Thread.sleep(5_000)
            mock()
            Thread.sleep(5_000)
            mock2()
        }
    }

    private fun setupRecyclerView() {
        binding.managerStoreRv.apply {
            this.layoutManager = GridLayoutManager(applicationContext, 2)
            this.adapter = storeAdapter
            this.addItemDecoration(CardItemDecoration())
        }
    }

    private fun mock() {
        val users = ArrayList<User>()
        repeat(50) {
            users.add(
                User(
                    id = it.toString(),
                    name = "hello$it",
                    date = System.currentTimeMillis().toString(),
                    avatar = ""
                )
            )
        }
        runOnUiThread {
            storeAdapter.users = users
        }
    }

    private fun mock2() {
        val users = ArrayList<User>()
        repeat(50) {
            users.add(
                User(
                    id = (it + 100).toString(),
                    name = "hello${it + 100}",
                    date = System.currentTimeMillis().toString(),
                    avatar = ""
                )
            )
        }
        runOnUiThread {
            storeAdapter.users = users
        }
    }
}