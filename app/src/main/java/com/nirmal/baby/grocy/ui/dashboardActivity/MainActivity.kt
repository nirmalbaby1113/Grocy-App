package com.nirmal.baby.grocy.ui.dashboardActivity

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nirmal.baby.grocy.R
import com.nirmal.baby.grocy.data.model.GroceryItem
import com.nirmal.baby.grocy.databinding.ActivityMainBinding
import com.nirmal.baby.grocy.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityMainBinding
    private val groceryViewModel: MainActivityViewModel by viewModels()
    private var isFabExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val groceryAdapter = GroceryListAdapter()
        binding.groceryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.groceryRecyclerView.adapter = groceryAdapter

        groceryViewModel.getGroceryList().observe(this) { groceries ->
            if (groceries.isNullOrEmpty()) {
                binding.emptyStateLayout.visibility = View.VISIBLE
                binding.listLayout.visibility = View.GONE
            } else {
                binding.emptyStateLayout.visibility = View.GONE
                binding.listLayout.visibility = View.VISIBLE
                groceryAdapter.submitList(groceries)
            }
            Log.d("MainActivity", "Groceries received: $groceries")
        }


        // Set up FAB click listener
        binding.fab.setOnClickListener {
            if (isFabExpanded) {
                hideButtons()
            } else {
                showButtons()
            }
            //isFabExpanded = !isFabExpanded
        }


        // Add button click listeners
        binding.addGroceryButton.setOnClickListener {
            // Handle Add Grocery click
            hideButtons()
            val dialog = AddGroceryDialogFragment()
            dialog.show(supportFragmentManager, "AddGroceryDialogFragment")

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

        isFabExpanded = !isFabExpanded

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

        isFabExpanded = !isFabExpanded
    }


}