package com.example.elliot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class FoodDetailsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar?.visibility = View.GONE

        return inflater.inflate(R.layout.fragment_food_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val foodName : TextView? = getView()?.findViewById(R.id.food_name_details)
        foodName?.text = arguments?.getString("foodName")

        val foodDate : TextView? = getView()?.findViewById(R.id.food_date_details)
        foodDate?.text = arguments?.getString("dateEaten")

        val foodTime : TextView? = getView()?.findViewById(R.id.food_time_details)
        foodTime?.text = arguments?.getString("timeEaten")

        val foodMeal : TextView? = getView()?.findViewById(R.id.food_meal_details)
        foodMeal?.text = arguments?.getString("meal")

        val foodIngredients : TextView? = getView()?.findViewById(R.id.food_ingredients)
        foodIngredients?.text = arguments?.getString("ingredients")
    }
}