package com.gl.habitalarm.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.gl.habitalarm.R

private const val TAG = "HabitListFragment"

class HabitListFragment : Fragment() {
    private val mViewModel: HabitViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView(): called")

        val view: View =
            inflater.inflate(R.layout.fragment_habit_list, container, false)

        if(view is RecyclerView) {
            val habitAdapter = HabitAdapter()
            view.adapter = habitAdapter

            mViewModel.habitsWithRepetitions.observe(
                viewLifecycleOwner,
                { habitsWithRepetitions ->
                    Log.d(TAG, "habitsWithRepetitions: observe(): called")
                    habitAdapter.submitList(habitsWithRepetitions)
                }
            )
        }

        return view
    }
}