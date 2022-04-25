package iam.thevoid.epic.sandbox

import android.os.Bundle
import androidx.activity.viewModels
import iam.thevoid.epic.sandbox.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val vm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
    }
}