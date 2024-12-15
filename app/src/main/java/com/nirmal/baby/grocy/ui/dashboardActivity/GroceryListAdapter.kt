package com.nirmal.baby.grocy.ui.dashboardActivity

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nirmal.baby.grocy.data.model.GroceryItem
import com.nirmal.baby.grocy.databinding.ItemGroceryBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class GroceryListAdapter : ListAdapter<GroceryItem, GroceryListAdapter.GroceryViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GroceryItem>() {
            override fun areItemsTheSame(oldItem: GroceryItem, newItem: GroceryItem): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: GroceryItem, newItem: GroceryItem): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val binding = ItemGroceryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroceryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        val item = getItem(position)
        Log.d("GroceryListAdapter", "Binding item: $item at position: $position")
        holder.bind(item)
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

            binding.itemQuantityUnit.text = item.unit
        }

        private fun calculateRemainingDays(expiryDate: String?): Int {
            Log.d("GroceryListAdapter", "In func before if: ${expiryDate}")
            if (expiryDate.isNullOrEmpty()) return -1 // Handle missing or null dates
            Log.d("GroceryListAdapter", "In func after if: ${expiryDate}")

            return try {
                // Use the correct date format for "27 Dec 2024"
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                val cleanedExpiryDate = expiryDate?.trim()
                Log.d("GroceryListAdapter", "In Func Cleaned: ${cleanedExpiryDate}")
                val expiry = sdf.parse(cleanedExpiryDate)
                Log.d("GroceryListAdapter", "In Func Expiry: ${expiry}")
                val currentCalendar = Calendar.getInstance()
                val expiryCalendar = Calendar.getInstance()

                Log.d("GroceryListAdapter", "In Func: ${expiry}")

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