package com.gl.habitalarm.ui.createoredit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gl.habitalarm.R
import com.gl.habitalarm.databinding.ActivityHabitCreateOrEditBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "HabitCreateOrEditActivity"

@AndroidEntryPoint
class HabitCreateOrEditActivity : AppCompatActivity() {
    private val mViewModel: HabitCreateOrEditViewModel by viewModels()

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

        val binding: ActivityHabitCreateOrEditBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_habit_create_or_edit)
        binding.viewModel = mViewModel
        binding.lifecycleOwner = this
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finishAfterTransition()
        return true
    }

    companion object {
        fun createIntent(packageContext: Context?, habitId: Long): Intent? {
            Log.d(TAG, "createIntent(): called with habitId $habitId")

            return Intent(packageContext, HabitCreateOrEditActivity::class.java)
                    .putExtra(HabitCreateOrEditViewModel.INTENT_HABIT_ID, habitId)
        }
    }
}