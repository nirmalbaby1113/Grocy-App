package com.nirmal.baby.grocy.ui.dashboardActivity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
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
    private var groceryList: List<GroceryItem> = listOf()

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
                binding.cookForMeButton.visibility = View.GONE
                binding.listLayout.visibility = View.GONE
            } else {
                binding.emptyStateLayout.visibility = View.GONE
                binding.cookForMeButton.visibility = View.VISIBLE
                binding.listLayout.visibility = View.VISIBLE
                groceryAdapter.submitFullList(groceries)
            }
            Log.d("MainActivity", "Groceries received: $groceries")
        }

        // Handle the text input for search
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                groceryAdapter.filter(query) // Call filter method of the adapter
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: You can perform actions before the text changes if needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Optional: Handle text change in real-time if needed
            }
        })


        // Set up FAB click listener
        binding.fab.setOnClickListener {
            if (isFabExpanded) {
                hideButtons()
            } else {
                showButtons()
            }
            //isFabExpanded = !isFabExpanded
        }

        //Sorting Button actions
        binding.sortButton.setOnClickListener {
            showSortDialog()
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

    private fun showSortDialog() {
        // Create the dialog
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_sort)
        dialog.setCancelable(true)

        // Set the dialog window properties
        val window = dialog.window
        if (window != null) {
            val metrics = resources.displayMetrics
            val screenWidth = metrics.widthPixels
            val dialogWidth = (screenWidth * 0.9).toInt() // Calculate 90% of screen width

            window.setLayout(
                dialogWidth,
                LinearLayout.LayoutParams.WRAP_CONTENT // Height remains dynamic
            )
            window.setBackgroundDrawableResource(android.R.color.transparent)
            window.attributes.windowAnimations = R.style.DialogAnimation
        }


        // Get dialog buttons
        val buttonNameAsc: Button = dialog.findViewById(R.id.btnNameAsc)
        val buttonNameDesc: Button = dialog.findViewById(R.id.btnNameDesc)
        val buttonDateAsc: Button = dialog.findViewById(R.id.btnDateAsc)
        val buttonDateDesc: Button = dialog.findViewById(R.id.btnDateDesc)
        val imageClose: ImageView = dialog.findViewById(R.id.closeButton)

        // Handle button clicks
        buttonNameAsc.setOnClickListener {
            // Handle "Sort by Name"
            val groceryAdapter = binding.groceryRecyclerView.adapter as GroceryListAdapter
            groceryAdapter.sortListByName()
            dialog.dismiss()
        }

        buttonNameDesc.setOnClickListener {
            // Handle "Sort by Date"
            val groceryAdapter = binding.groceryRecyclerView.adapter as GroceryListAdapter
            groceryAdapter.sortListByNameDesc()
            dialog.dismiss()
        }

        buttonDateAsc.setOnClickListener {
            // Handle "Sort by Price"
            val groceryAdapter = binding.groceryRecyclerView.adapter as GroceryListAdapter
            groceryAdapter.sortListByDate()
            dialog.dismiss()
        }

        buttonDateDesc.setOnClickListener {
            // Handle "Sort by Price"
            val groceryAdapter = binding.groceryRecyclerView.adapter as GroceryListAdapter
            groceryAdapter.sortListByDateDesc()
            dialog.dismiss()
        }

        imageClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
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