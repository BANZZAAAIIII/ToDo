package no.uia.todo.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.todo_list_add_dialog.view.*
import no.uia.todo.R
import no.uia.todo.viewmodel.ToDoViewModel


class ToDoListAddDialog : BottomSheetDialogFragment() {
    private lateinit var viewModel: ToDoViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)
        // TODO: This should be in a constructor in ToDoViewModel
        viewModel.path = this.activity?.getExternalFilesDir(null)

        view.ToDo_add_list_btn.setOnClickListener {
            if (viewModel.insertToDo(view.ToDo_list_editText.text.toString())) {
                dismiss()
            } else {
                Toast.makeText(context, "Invalid name", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.todo_list_add_dialog, container, false)
    }
}
