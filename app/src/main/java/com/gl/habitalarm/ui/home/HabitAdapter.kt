package com.gl.habitalarm.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gl.habitalarm.R
import com.gl.habitalarm.data.HabitWithRepetition
import com.gl.habitalarm.data.Repetition
import com.gl.habitalarm.databinding.ListItemHabitBinding
import com.gl.habitalarm.enums.ERepetitionState

private const val TAG_ADAPTER = "HabitAdapter"
private const val TAG_HOLDER = "HabitHolder"
private const val TAG_DIFF_CALLBACK = "ItemCallback"

class HabitAdapter
    : ListAdapter<HabitWithRepetition, HabitAdapter.HabitViewHolder>(DIFF_CALLBACK) {
    private lateinit var mViewModel: HabitViewModel

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        Log.d(TAG_ADAPTER, "onAttachedToRecyclerView(): called")
        super.onAttachedToRecyclerView(recyclerView)

        val context = recyclerView.context
        mViewModel = ViewModelProvider(context as ViewModelStoreOwner)[HabitViewModel::class.java]
    }

    override fun getItemViewType(position: Int): Int {
        Log.d(TAG_ADAPTER, "getItemViewType(): called with position $position")

        return R.layout.list_item_habit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        Log.d(TAG_ADAPTER, "onCreateViewHolder(): called")

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemHabitBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        binding.checkbox.setOnClickListener { v ->
            mViewModel.saveOrUpdateRepetition(
                binding.repetition,
                binding.habit!!.id,
                (v as CheckBox).isChecked
            )
        }
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        Log.d(TAG_ADAPTER, "onBindViewHolder(): called with position $position")

        val habitWithRepetition: HabitWithRepetition = getItem(position)
        holder.bind(habitWithRepetition)
    }

    inner class HabitViewHolder(private val mBinding: ListItemHabitBinding)
        : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(habitWithRepetition: HabitWithRepetition) {
            Log.d(TAG_HOLDER, "bind(): called with habitId ${habitWithRepetition.habit.id}")

            val repetition: Repetition? = habitWithRepetition.repetition
            mBinding.habit = habitWithRepetition.habit
            mBinding.repetition = repetition

            var state = ERepetitionState.None
            if (repetition != null) {
                state = repetition.state
            }
            mBinding.repetitionState = state
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<HabitWithRepetition> =
            object : DiffUtil.ItemCallback<HabitWithRepetition>() {
                override fun areItemsTheSame(
                    oldItem: HabitWithRepetition,
                    newItem: HabitWithRepetition
                ): Boolean {
                    Log.d(
                        TAG_DIFF_CALLBACK,
                        "areItemsTheSame(): called with habits ${oldItem.habit.id}, ${newItem.habit.id}")

                    val bSame = oldItem.areTheSameItem(newItem)

                    Log.d(
                        TAG_DIFF_CALLBACK,
                        "areItemsTheSame(): returns $bSame")

                    return bSame
                }

                override fun areContentsTheSame(
                    oldItem: HabitWithRepetition,
                    newItem: HabitWithRepetition
                ): Boolean {
                    Log.d(
                        TAG_DIFF_CALLBACK,
                        "areContentsTheSame(): called with habits ${oldItem.habit.id}, ${newItem.habit.id}")

                    val bSame = oldItem.areTheSameContent(newItem)

                    Log.d(
                        TAG_DIFF_CALLBACK,
                        "areContentsTheSame(): returns $bSame")

                    return bSame
                }
            }
    }
}