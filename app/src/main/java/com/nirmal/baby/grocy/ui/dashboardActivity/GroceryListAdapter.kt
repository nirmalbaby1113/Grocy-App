package com.nirmal.baby.grocy.ui.dashboardActivity

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nirmal.baby.grocy.R
import com.nirmal.baby.grocy.data.model.GroceryItem
import com.nirmal.baby.grocy.databinding.ItemGroceryBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class GroceryListAdapter : ListAdapter<GroceryItem, GroceryListAdapter.GroceryViewHolder>(DIFF_CALLBACK) {

    private var groceryList = listOf<GroceryItem>() // Holds the full list
    private var filteredList = listOf<GroceryItem>() // Holds the filtered list


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GroceryItem>() {
            override fun areItemsTheSame(oldItem: GroceryItem, newItem: GroceryItem): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: GroceryItem, newItem: GroceryItem): Boolean = oldItem == newItem
        }
    }

    fun submitFullList(list: List<GroceryItem>) {
        groceryList = list
        filteredList = list // Initially, the filtered list is the full list
        submitList(filteredList)
    }

    // Function to sort the list by name
    fun sortListByName() {
        filteredList = filteredList.sortedBy { it.name.lowercase() } // Sort by grocery name
        submitList(filteredList)
    }

    fun sortListByNameDesc() {
        filteredList = filteredList.sortedByDescending { it.name.lowercase() } // Sort by grocery name
        submitList(filteredList)
    }

    fun sortListByDate() {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        filteredList = filteredList.sortedBy { dateFormat.parse(it.expiryDate.toString()) } // Sort by date
        submitList(filteredList)
    }

    fun sortListByDateDesc() {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        filteredList = filteredList.sortedByDescending { dateFormat.parse(it.expiryDate.toString()) } // Sort by date added
        submitList(filteredList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val binding = ItemGroceryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroceryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        val item = filteredList[position]
        Log.d("GroceryListAdapter", "Binding item: $item at position: $position")
        holder.bind(item)
    }


    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            groceryList
        } else {
            groceryList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        submitList(filteredList)
    }

    class GroceryViewHolder(private val binding: ItemGroceryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GroceryItem) {
            //Log.d("GroceryListAdapter", "Binding item: ${item.expiryDate}")
            binding.itemName.text = item.name
            binding.itemQuantity.text = "Quantity: ${item.quantity}"

            // Calculate remaining days
            Log.d("GroceryListAdapter", "Parsed expiry date: ${item.expiryDate}")
            val remainingDays = calculateRemainingDays(item.expiryDate)
            Log.d("GroceryListAdapter", "Remaining days: ${remainingDays}")
            binding.itemExpiry.text = when {
                remainingDays > 0 -> "Expires in $remainingDays days"

                remainingDays == 0 -> "Expires today"
                else -> "Expired"
            }

            // Change color based on remaining days
            val expiryColor = when {
                remainingDays in 0..9 -> ContextCompat.getColor(binding.root.context, R.color.classic_red) // Yellow for less than 10 days
                remainingDays > 10 -> ContextCompat.getColor(binding.root.context, R.color.green) // Green for more than 10 days
                else -> ContextCompat.getColor(binding.root.context, R.color.black) // Red for expired
            }
            binding.itemExpiry.setTextColor(expiryColor)

            binding.itemQuantityUnit.text = item.unit
        }

        private fun calculateRemainingDays(expiryDate: String?): Int {
            if (expiryDate.isNullOrEmpty()) return -1 // Handle missing or null dates

            return try {
                // Use the correct date format for "27 Dec 2024"
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                val cleanedExpiryDate = expiryDate?.trim()
                val expiry = sdf.parse(cleanedExpiryDate)
                val currentCalendar = Calendar.getInstance()
                val expiryCalendar = Calendar.getInstance()


                if (expiry != null) {
                    expiryCalendar.time = expiry

                    // Set both dates to midnight to ignore time component
                    currentCalendar.set(Calendar.HOUR_OF_DAY, 0)
                    currentCalendar.set(Calendar.MINUTE, 0)
                    currentCalendar.set(Calendar.SECOND, 0)
                    currentCalendar.set(Calendar.MILLISECOND, 0)

                    expiryCalendar.set(Calendar.HOUR_OF_DAY, 0)
                    expiryCalendar.set(Calendar.MINUTE, 0)
                    expiryCalendar.set(Calendar.SECOND, 0)
                    expiryCalendar.set(Calendar.MILLISECOND, 0)

                    // Calculate difference in milliseconds
                    val diff = expiryCalendar.timeInMillis - currentCalendar.timeInMillis
                    // Convert milliseconds to days
                    TimeUnit.MILLISECONDS.toDays(diff).toInt()
                } else {
                    -1 // If parsing fails
                }
            } catch (e: Exception) {
                -1 // Handle invalid date format
            }
        }

    }
}