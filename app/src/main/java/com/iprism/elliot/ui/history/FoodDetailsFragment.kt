package com.iprism.elliot.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.iprism.elliot.R
import com.iprism.elliot.adapter.IngredientsAdapter
import com.iprism.elliot.databinding.FragmentFoodDetailsBinding

class FoodDetailsFragment : Fragment() {

    private var _binding: FragmentFoodDetailsBinding? = null

    private val binding get() = _binding!!

    private fun hideNavbar() {
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar?.visibility = View.GONE

        val cameraButton = activity?.findViewById<FloatingActionButton>(R.id.floating_action_button)
        cameraButton?.visibility = View.GONE
    }

    private fun fillTextViews(recyclerView: RecyclerView) {
        binding.foodNameDetails.text = arguments?.getString("foodName")
        binding.foodDateDetails.text = arguments?.getString("dateEaten")
        binding.foodTimeDetails.text = arguments?.getString("timeEaten")
        binding.foodMealDetails.text = arguments?.getString("meal")
//        binding.foodIngredients.text = arguments?.getString("ingredients")

        binding.backButtonHistory.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_foodDetailsFragment_to_calendarFragment)
        }

        val ingredients =
            arguments?.getString("ingredients")?.split(",")?.toTypedArray()

//        if (ingredients != null) {
//            for (ingredient in ingredients) {
//                Log.d("TAG", ingredient)
//            }
//        }

        recyclerView.apply {
            adapter = IngredientsAdapter(ingredients)
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

        hideNavbar()

        _binding = FragmentFoodDetailsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = view.findViewById(R.id.ingredient_recycler)

        fillTextViews(recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}