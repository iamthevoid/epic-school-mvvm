package iam.thevoid.epic.myapplication.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

open class BaseFragment<T: ViewDataBinding>(@LayoutRes val layoutId: Int): Fragment() {

    lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Lifecycle", "${this::class.simpleName} onCreateView")
        binding = DataBindingUtil.inflate<T>(inflater, layoutId, container, false)
        return binding.root
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