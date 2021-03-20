package no.uia.todo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.todo_item_fragment.view.*
import no.uia.todo.data.ToDo
import no.uia.todo.databinding.TodoItemFragmentBinding
import no.uia.todo.viewmodel.ToDoViewModel

class ToDoItemFragment : Fragment() {
    private val LOG_TAG = "TODO:ToDoItemFragment"

    private var _binding: TodoItemFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ToDoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TodoItemFragmentBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)

        val args: ToDoItemFragmentArgs by navArgs()
        val toDo: ToDo = viewModel.getToDosByID(args.toDoItemId)

        view.Header1.text = "$toDo"

        view.add_new_ToDo_btn.setOnClickListener {
            val item = view.add_new_ToDo_editText.text.toString()
            toDo.items?.add(item)

            viewModel.updateToDoItem(args.toDoItemId, item)
        }

        view.todoItemRecycler.layoutManager = LinearLayoutManager(requireActivity())
        view.todoItemRecycler.adapter = ToDoItemAdapter(toDo.items)

        return view
    }
}