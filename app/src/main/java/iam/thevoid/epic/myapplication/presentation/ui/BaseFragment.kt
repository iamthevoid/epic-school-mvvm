package iam.thevoid.epic.myapplication.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class BaseFragment(@LayoutRes id: Int = 0): Fragment(id) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Lifecycle", "${this::class.simpleName} onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        Log.d("Lifecycle", "${this::class.simpleName} onCreateView")
        super.onDestroyView()
    }

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