package com.example.plotline.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plotline.ImageData
import com.example.plotline.MainActivity
import com.example.plotline.MainViewModel
import com.example.plotline.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        homeAdapter = HomeAdapter()
        binding.recycle.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = homeAdapter
        }
        lifecycleScope.launchWhenStarted {

            viewModel.getImages().collect{
                if(it.isEmpty()){
                    binding.noImages.visibility = View.VISIBLE
                }
                homeAdapter.submitList(it)
            }
        }


        Timber.e("${homeAdapter.currentList}")
        setContentView(binding.root)
        binding.add.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }



}