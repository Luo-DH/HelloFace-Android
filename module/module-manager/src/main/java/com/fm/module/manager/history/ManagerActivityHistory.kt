package com.fm.module.manager.history

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.fm.library.common.constants.RouterPath
import com.fm.module.manager.User
import com.fm.module.manager.databinding.ManagerActivityHistoryBinding
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

        thread {
            Thread.sleep(5_000)
            mock()
            Thread.sleep(5_000)
            mock2()
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
            historyAdapter.users = users
        }
    }

    private fun mock2() {
        val users = ArrayList<User>()
        repeat(50) {
            users.add(
                User(
                    id = (it+100).toString(),
                    name = "hello${it+100}",
                    date = System.currentTimeMillis().toString(),
                    avatar = ""
                )
            )
        }
        runOnUiThread {
            historyAdapter.users = users
        }
    }
}