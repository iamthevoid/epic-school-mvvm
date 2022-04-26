package ru.rt.epic.sandbox

import android.os.Bundle
import androidx.activity.viewModels
import ru.rt.epic.sandbox.base.BaseActivity
import ru.rt.epic.sandbox.databinding.ActivityDemoBinding

class DemoActivity: BaseActivity<ActivityDemoBinding>(R.layout.activity_demo) {

    val vm by viewModels<DemoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
    }
}