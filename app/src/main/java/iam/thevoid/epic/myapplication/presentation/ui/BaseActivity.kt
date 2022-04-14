package iam.thevoid.epic.myapplication.presentation.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle", "${this::class.simpleName} onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "${this::class.simpleName} onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "${this::class.simpleName} onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "${this::class.simpleName} onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "${this::class.simpleName} onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "${this::class.simpleName} onDestroy")
    }

}