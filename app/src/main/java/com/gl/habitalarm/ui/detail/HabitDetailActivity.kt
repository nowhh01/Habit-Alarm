package com.gl.habitalarm.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gl.habitalarm.R
import com.gl.habitalarm.databinding.ActivityHabitDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val TAG = "HabitDetailActivity"
private const val POS_WEEK = 0
private const val POS_MONTH = 1

@AndroidEntryPoint
class HabitDetailActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val mViewModel: HabitDetailViewModel by viewModels()

    private lateinit var mBinding: ActivityHabitDetailBinding
    private var mHabitId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate(): called")

        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mHabitId = intent.getLongExtra(HabitDetailViewModel.INTENT_HABIT_ID, 0)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_habit_detail)
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        val categories: MutableList<String> = ArrayList()
        categories.add("Week")
        categories.add("Month")

        // Create adapter for spinner
        val adapter = ArrayAdapter(this, R.layout.spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        mBinding.spinner.adapter = adapter
        mBinding.spinner.onItemSelectedListener = this

        mViewModel.habitWithRepetitions.observe(
                this,
                { habitWithRepetitions ->
                    Log.d(TAG, "habitWithRepetitions: observe(): called with habit ${habitWithRepetitions.habit.name}, ${habitWithRepetitions.repetitions.size} repetitions")

                    habitWithRepetitions?.let {
                        mBinding.habitChart.setHabitAndRepetitions(it)
                        supportActionBar?.title = it.habit.name.toUpperCase()
                    }
                }
        )
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(TAG, "onItemSelected(): called")

        when (position) {
            POS_WEEK -> mBinding.habitChart.changeToMonth(false)
            POS_MONTH -> mBinding.habitChart.changeToMonth(true)
            else -> {
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(TAG, "onCreateOptionsMenu(): called")

        menuInflater.inflate(R.menu.menu_habit_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected(): called with item ${item.title}")

        when (item.itemId) {
            R.id.edit -> {

            }
            R.id.delete -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.dialog_delete_title)
                    .setMessage(R.string.dialog_delete_message)
                    .setPositiveButton(R.string.delete) { _, _ ->
                        mViewModel.removeHabit()
                        onBackPressed()
                    }
                    .setNegativeButton(R.string.cancel) { _, _ -> }

                val dialog = builder.create()
                dialog.show()
            }
            else -> finishAfterTransition()
        }
        return true
    }

    companion object {
        fun createIntent(packageContext: Context?, habitId: Long): Intent? {
            Log.d(TAG, "createIntent(): called with habitId $habitId")

            return Intent(packageContext, HabitDetailActivity::class.java)
                .putExtra(HabitDetailViewModel.INTENT_HABIT_ID, habitId)
        }
    }
}