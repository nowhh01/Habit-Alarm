package com.gl.habitalarm.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gl.habitalarm.R
import com.gl.habitalarm.databinding.ActivityHabitBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HabitActivity : AppCompatActivity() {
    private val viewModel: HabitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val binding: ActivityHabitBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_habit)
    }
}