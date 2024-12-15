package com.nirmal.baby.grocy.ui.dashboardActivity

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.nirmal.baby.grocy.R
import com.nirmal.baby.grocy.data.model.GroceryItem
import com.nirmal.baby.grocy.databinding.AddGroceryDialogBinding
import com.nirmal.baby.grocy.viewmodel.MainActivityViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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
            val item = GroceryItem(
                name = binding.itemNameEditText.text.toString(),
                quantity = binding.itemQuantityEditText.text.toString(),
                unit = binding.quantityUnitSpinner.selectedItem.toString(),
                expiryDate = binding.expiryDateTextView.text.toString(),
                price = binding.itemPriceEditText.text.toString().toDoubleOrNull(),
                description = binding.itemDescriptionEditText.text.toString()
            )

            if (item.name.isBlank() || item.quantity.isBlank()) {
                Toast.makeText(context, getString(R.string.error_invalid_input), Toast.LENGTH_SHORT).show()
            } else {
                groceryViewModel.addGroceryItem(item)
                dismiss()
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
}