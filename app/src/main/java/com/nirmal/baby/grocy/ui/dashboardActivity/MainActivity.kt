package com.nirmal.baby.grocy.ui.dashboardActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nirmal.baby.grocy.R
import com.nirmal.baby.grocy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isFabExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up FAB click listener
        binding.fab.setOnClickListener {
            if (isFabExpanded) {
                hideButtons()
            } else {
                showButtons()
            }
            isFabExpanded = !isFabExpanded
        }


        // Add button click listeners
        binding.addGroceryButton.setOnClickListener {
            // Handle Add Grocery click
        }

        binding.createShoppingListButton.setOnClickListener {
            // Handle Create Shopping List click
        }
    }

    private fun showButtons() {
        // Show buttons with animation
        binding.addGroceryButton.visibility = View.VISIBLE
        binding.createShoppingListButton.visibility = View.VISIBLE

        val addGroceryAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val createShoppingListAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        binding.addGroceryButton.startAnimation(addGroceryAnim)
        binding.createShoppingListButton.startAnimation(createShoppingListAnim)

        binding.fab.setImageResource(R.drawable.ic_close)
    }

    private fun hideButtons() {
        // Hide buttons with animation
        val addGroceryAnim = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        val createShoppingListAnim = AnimationUtils.loadAnimation(this, R.anim.slide_down)

        binding.addGroceryButton.startAnimation(addGroceryAnim)
        binding.createShoppingListButton.startAnimation(createShoppingListAnim)

        // Delay hiding until animation ends
        addGroceryAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                binding.addGroceryButton.visibility = View.GONE
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })

        createShoppingListAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                binding.createShoppingListButton.visibility = View.GONE
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })

        binding.fab.setImageResource(R.drawable.ic_add)
    }
}