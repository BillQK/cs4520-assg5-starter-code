package com.cs4520.assignment5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cs4520.assignment5.ui.navhost.AmazingProductListApp


class SingleActivity : ComponentActivity() {
    /*
    The onCreate method is a lifecycle callback that runs when the
    activity is being created.
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            AmazingProductListApp()
        }
    }
}


