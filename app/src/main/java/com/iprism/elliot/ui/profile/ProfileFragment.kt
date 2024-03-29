package com.iprism.elliot.ui.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.iprism.elliot.R
import com.iprism.elliot.adapter.SuggestionsAdapter
import com.iprism.elliot.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPreferences

    private fun startTimeSetter(mealStringStart: String, mealStringEnd: String, meal: String) {
            val timePicker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(10)
                    .setTitleText(mealStringStart)
                    .build()

            timePicker.show(parentFragmentManager, "tag")

            timePicker.addOnPositiveButtonClickListener {
                val hour = timePicker.hour
                // val min = timePicker.minute
                // val timeStart = "$hour:$min"
                endTimeSetter(mealStringEnd, meal, hour)
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
            Toast.makeText(context, getString(R.string.meal_time_toast), Toast.LENGTH_SHORT).show()
            timePicker.dismiss()
        }
    }

    private fun initiateListeners() {
        binding.breakfast.setOnClickListener {
            startTimeSetter(
                getString(R.string.breakfast_string_start),
                getString(R.string.breakfast_string_end),
                "breakfast"
            )
        }

        binding.lunch.setOnClickListener {
            startTimeSetter(
                getString(R.string.lunch_string_start),
                getString(R.string.breakfast_string_end),
                "lunch"
            )
        }

        binding.dinner.setOnClickListener {
            startTimeSetter(
                getString(R.string.dinner_string_start),
                getString(R.string.breakfast_string_end),
                "dinner"
            )
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
        // val username = activity?.intent?.extras?.getString("USERNAME")
        // val token = activity?.intent?.extras?.getString("TOKEN")
        // Log.e("test", activity?.intent?.toUri(0).toString())

        val recyclerView: RecyclerView = view.findViewById(R.id.suggestion_recycler)

        val cameraButton = activity?.findViewById<FloatingActionButton>(R.id.floating_action_button)
        cameraButton?.visibility = View.VISIBLE

        val homeButton = activity?.findViewById<MaterialButton>(R.id.home_button)
        homeButton?.setOnClickListener {
            activity?.finish()
        }

        val profileName = activity?.findViewById<TextView>(R.id.profileName)
        profileName?.text = sharedPref.getString("username", "")

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