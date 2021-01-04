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
import com.gl.habitalarm.ui.createoredit.HabitCreateOrEditActivity
import com.gl.habitalarm.ui.detail.HabitDetailActivity
import com.gl.habitalarm.ui.settings.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_habit.*

private const val TAG = "HabitActivity"

@AndroidEntryPoint
class HabitActivity : AppCompatActivity(), HabitAdapter.ICallable {
    private val mViewModel: HabitViewModel by viewModels()

    override fun onHabitSelected(habitId: Long) {
        Log.d(TAG, "onHabitSelected(): called with habitId $habitId")

        startActivity(HabitDetailActivity.createIntent(this, habitId))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate(): called")

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val binding: ActivityHabitBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_habit)

        binding.bottomNav.setOnNavigationItemSelectedListener { item: MenuItem ->
            when(item.itemId) {
                R.id.navigation_create -> {
                    startNewActivity(bottom_nav, HabitCreateOrEditActivity::class.java)
                }
                R.id.navigation_settings -> {
                    startNewActivity(bottom_nav, SettingsActivity::class.java)
                }
            }
            true
        }
    }

    private fun startNewActivity(view: View, activityClass: Class<*>) {
        Log.d(TAG, "startNewActivity(): called with $activityClass")

        val options = ActivityOptions.makeScaleUpAnimation(
                view,
                0,
                0,
                view.width,
                view.height)
        startActivity(Intent(this, activityClass), options.toBundle())
    }
}