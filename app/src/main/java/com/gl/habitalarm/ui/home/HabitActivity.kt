package com.gl.habitalarm.ui.home

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gl.habitalarm.R
import com.gl.habitalarm.databinding.ActivityHabitBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "HabitActivity"

@AndroidEntryPoint
class HabitActivity : AppCompatActivity() {
    private val viewModel: HabitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate(): called")
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val binding: ActivityHabitBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_habit)
    }
}