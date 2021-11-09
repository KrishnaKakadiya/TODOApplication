package com.example.todoapplication.view

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapplication.R
import com.example.todoapplication.databinding.FragmentAddEditTaskBinding
import com.example.todoapplication.util.MyDatePickerDialog
import com.example.todoapplication.util.exhaustive
import com.example.todoapplication.viewmodel.AddEditTaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class AddEditTaskFragment:Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditTaskBinding.bind(view)

        binding.apply {
            editTextTaskTitle.setText(viewModel.taskTitle)
            editTextTaskDescription.setText(viewModel.taskDescription)
            textViewTaskDate.text = viewModel.taskDate

            editTextTaskTitle.addTextChangedListener {
                viewModel.taskTitle = it.toString()
            }
            editTextTaskDescription.addTextChangedListener {
                viewModel.taskDescription = it.toString()
            }

            fabSaveTask.setOnClickListener {
                viewModel.onSaveClick()
            }
            datePicker.setOnClickListener {

                activity?.takeIf { !it.isFinishing && !it.isDestroyed }?.let { activity ->
                    fun showDate(year: Int, month: Int, day: Int) {
                        viewModel.taskDate = "$year/$month/$day"
                        textViewTaskDate.text = viewModel.taskDate
                    }
                    MyDatePickerDialog(activity,textViewTaskDate.text.toString(), ::showDate).show()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when(event){
                    is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                        binding.editTextTaskTitle.clearFocus()
                        binding.editTextTaskDescription.clearFocus()
                        binding.textViewTaskDate.clearFocus()
                        binding.datePicker.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }
    }
}