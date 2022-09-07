package com.iprism.elliot.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.iprism.elliot.R
import com.iprism.elliot.adapter.SuggestionsAdapter
import com.iprism.elliot.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private fun startTimeSetter(mealString: String, meal: String) {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText(mealString)
                .build()

        timePicker.show(parentFragmentManager, "tag")

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            // val min = timePicker.minute
            // val timeStart = "$hour:$min"
            endTimeSetter(mealString, meal, hour)
            timePicker.dismiss()
        }
    }

    private fun endTimeSetter(mealString: String, meal: String, timeStart: Int) {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText(mealString)
                .build()

        timePicker.show(parentFragmentManager, "tag")

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            // val min = timePicker.minute
            // val timeEnd = "$hour:$min"

            profileViewModel.onEvent(ProfileEvent.SetMealTime(meal, timeStart, hour))
            timePicker.dismiss()
        }
    }

    private fun initiateListeners() {
        binding.breakfast.setOnClickListener {
            startTimeSetter(getString(R.string.breakfast_string), "breakfast")
        }

        binding.lunch.setOnClickListener {
            startTimeSetter(getString(R.string.lunch_string), "lunch")
        }

        binding.dinner.setOnClickListener {
            startTimeSetter(getString(R.string.dinner_string), "dinner")
        }
    }

    private fun callRecycleView(
        recyclerView: RecyclerView,
        dataset: List<String>
    ) {
        recyclerView.apply {
            adapter = SuggestionsAdapter(dataset)
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    LinearLayoutManager.VERTICAL
                )
            )
            setHasFixedSize(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val username = activity?.intent?.extras?.getString("USERNAME")
        val id = activity?.intent?.extras?.getString("TOKEN")
        // Log.e("TEST", activity?.intent?.toUri(0) ?: "")

        val recyclerView: RecyclerView = view.findViewById(R.id.suggestion_recycler)

        val cameraButton = activity?.findViewById<FloatingActionButton>(R.id.floating_action_button)
        cameraButton?.visibility = View.VISIBLE

        val profileName = activity?.findViewById<TextView>(R.id.profileName)
        profileName?.text = username

        initiateListeners()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.suggestionUiState.collect { suggestions ->
                    callRecycleView(recyclerView, suggestions)
                }
            }
        }

        profileViewModel.onEvent(ProfileEvent.LoadSuggestions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}