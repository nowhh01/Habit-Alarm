package com.gl.habitalarm.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.gl.habitalarm.R
import com.gl.habitalarm.databinding.FragmentDateSelectorBinding

private const val TAG = "DateSelectorFragment"

class DateSelectorFragment : Fragment() {
    private val mViewModel: HabitViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView(): called")
        val binding: FragmentDateSelectorBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_date_selector,
            container,
            false
        )
        binding.viewModel = mViewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}