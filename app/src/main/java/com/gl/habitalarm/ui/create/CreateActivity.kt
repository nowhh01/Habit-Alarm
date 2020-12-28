package com.gl.habitalarm.ui.create

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gl.habitalarm.R
import com.gl.habitalarm.databinding.ActivityCreateBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "CreateActivity"

@AndroidEntryPoint
class CreateActivity : AppCompatActivity() {
    private val mViewModel: CreateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate(): called")

        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mViewModel.isSaved.observe(
                this,
                { isSaved ->
                    if (isSaved) {
                        finishAfterTransition()
                    }
                }
        )

        val binding: ActivityCreateBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_create)
        binding.viewModel = mViewModel
        binding.lifecycleOwner = this
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finishAfterTransition()
        return true
    }
}