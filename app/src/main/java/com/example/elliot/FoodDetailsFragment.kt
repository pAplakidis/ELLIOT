package com.example.elliot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.elliot.databinding.FragmentFoodDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FoodDetailsFragment : Fragment() {

    private var _binding: FragmentFoodDetailsBinding? = null

    private val binding get() = _binding!!

    private fun hideNavbar() {
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar?.visibility = View.GONE

        val cameraButton = activity?.findViewById<FloatingActionButton>(R.id.floating_action_button)
        cameraButton?.visibility = View.GONE
    }

    private fun fillTextViews() {
        binding.foodNameDetails.text = arguments?.getString("foodName")
        binding.foodDateDetails.text = arguments?.getString("dateEaten")
        binding.foodTimeDetails.text = arguments?.getString("timeEaten")
        binding.foodMealDetails.text = arguments?.getString("meal")
        binding.foodIngredients.text = arguments?.getString("ingredients")

        binding.backButtonHistory.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_foodDetailsFragment_to_calendarFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        hideNavbar()

        _binding = FragmentFoodDetailsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fillTextViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}