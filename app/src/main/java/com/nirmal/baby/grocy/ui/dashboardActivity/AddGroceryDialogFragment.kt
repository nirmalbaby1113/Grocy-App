package com.nirmal.baby.grocy.ui.dashboardActivity

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.nirmal.baby.grocy.R
import com.nirmal.baby.grocy.data.model.GroceryItem
import com.nirmal.baby.grocy.databinding.AddGroceryDialogBinding
import com.nirmal.baby.grocy.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class AddGroceryDialogFragment : DialogFragment() {

    private var _binding: AddGroceryDialogBinding? = null
    private val binding get() = _binding!!

    private val groceryViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddGroceryDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up expiry date picker
        binding.expiryDateTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    // Set the selected date in DD MonthName YYYY format
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, day)

                    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)

                    // Update the TextView with formatted date
                    binding.expiryDateTextView.text = "Expiry : $formattedDate"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Disable earlier dates
            datePickerDialog.datePicker.minDate = calendar.timeInMillis

            datePickerDialog.show()

        }


        // Handle Add Button
        binding.addButton.setOnClickListener {
            // Safely access text fields and provide default values
            val item = GroceryItem(
                name = binding.itemNameEditText.text?.toString()?.trim() ?: "",
                quantity = binding.itemQuantityEditText.text?.toString()?.trim() ?: "",
                unit = binding.quantityUnitSpinner.selectedItem?.toString() ?: "",
                expiryDate = binding.expiryDateTextView.text?.toString()?.trim() ?: "",
                price = binding.itemPriceEditText.text?.toString()?.toDoubleOrNull(),
                description = binding.itemDescriptionEditText.text?.toString()?.trim() ?: ""
            )

            // Validate input fields
            if (item.name.isBlank() || item.quantity.isBlank()) {
                Toast.makeText(context, getString(R.string.error_invalid_input), Toast.LENGTH_SHORT).show()
            } else {
                setUiLoadingState(true)

                // Set up timeout
                val timeoutHandler = Handler(Looper.getMainLooper())
                timeoutHandler.postDelayed({
                    if (binding.progressBar.isVisible) {
                        //setUiLoadingState(false)
                        binding.progressBar.visibility = View.GONE
                        binding.linearLayoutErrorMessage.visibility = View.VISIBLE
                        binding.errorMessageTrayAgainButton.setOnClickListener {
                            binding.linearLayoutErrorMessage.visibility = View.GONE
                            setUiLoadingState(false)
                        }
                    }
                }, 10000) // 10 seconds timeout

                // Add the item
                groceryViewModel.addGroceryItem(
                    item.copy(
                        expiryDate = if (binding.expiryDateTextView.text.contains("Expiry :")) {
                            binding.expiryDateTextView.text.toString().replace("Expiry : ", "").trim()
                        } else null
                    ),
                    onSuccess = {
                        timeoutHandler.removeCallbacksAndMessages(null) // Clear timeout handler
                        //setUiLoadingState(false)
                        binding.progressBar.visibility = View.GONE
                        binding.linearLayoutSuccessMessage.visibility = View.VISIBLE

                        binding.successMessageOkButton.setOnClickListener {
                            dismiss()
                        }
                        //binding.root.postDelayed({ dismiss() }, 2000)
                    },
                    onFailure = { exception ->
                        timeoutHandler.removeCallbacksAndMessages(null) // Clear timeout handler
                        //setUiLoadingState(false)
                        binding.progressBar.visibility = View.GONE
                        binding.linearLayoutErrorMessage.visibility = View.VISIBLE
                        binding.errorMessageTrayAgainButton.setOnClickListener {
                            setUiLoadingState(false)
                        }
                    }
                )
            }
        }


        // Handle Close Button
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(), // 90% of screen width
            ViewGroup.LayoutParams.WRAP_CONTENT // Height adjusts based on content
        )
        dialog?.window?.setGravity(Gravity.CENTER) // Center the dialog
    }

    // Helper function to toggle UI state during loading
    private fun setUiLoadingState(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.addButton.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.closeButton.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.itemNameTitle.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.itemNameEditText.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.itemQuantityTitle.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.itemQuantityEditText.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.quantityUnitSpinner.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.expiryDateTextView.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.itemPriceTitle.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.itemPriceEditText.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.itemDescriptionTitle.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.itemDescriptionEditText.visibility = if (!isLoading) View.VISIBLE else View.GONE


        // Hide messages while loading
        if (isLoading) {
            binding.linearLayoutSuccessMessage.visibility = View.GONE
            binding.linearLayoutErrorMessage.visibility = View.GONE
        }
    }
}