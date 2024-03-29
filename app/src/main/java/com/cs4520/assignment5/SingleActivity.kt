package com.cs4520.assignment5

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.cs4520.assignment5.databinding.ActivityMainBinding
import com.cs4520.assignment5.ui.navhost.AmazingProductListApp


class SingleActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainBinding

    /*
    The onCreate method is a lifecycle callback that runs when the
    activity is being created.
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        setContent{
            AmazingProductListApp()
        }
    }
}


