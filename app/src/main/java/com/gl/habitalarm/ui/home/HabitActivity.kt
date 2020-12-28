package com.gl.habitalarm.ui.home

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gl.habitalarm.R
import com.gl.habitalarm.databinding.ActivityHabitBinding
import com.gl.habitalarm.ui.create.CreateActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_habit.*

private const val TAG = "HabitActivity"

@AndroidEntryPoint
class HabitActivity : AppCompatActivity() {
    private val mViewModel: HabitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate(): called")
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val binding: ActivityHabitBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_habit)
        binding.bottomNav.setOnNavigationItemSelectedListener { item: MenuItem ->
            when(item.itemId) {
                R.id.navigation_create -> startNewActivity(bottom_nav, CreateActivity::class.java)
                R.id.navigation_settings -> {
                }
            }
            true
        }
    }

    private fun startNewActivity(view: View, activityClass: Class<*>) {
        val options = ActivityOptions.makeScaleUpAnimation(
                view,
                0,
                0,
                view.width,
                view.height)
        startActivity(Intent(this, activityClass), options.toBundle())
    }
}