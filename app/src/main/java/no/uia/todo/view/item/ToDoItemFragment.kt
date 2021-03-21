package no.uia.todo.view.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
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
        val toDoID = args.toDoItemId

        val toDo: ToDo = viewModel.getToDosByID(toDoID)

        val adapter = ToDoItemAdapter(toDo.items)
        view.todoItemRecycler.adapter = adapter
        view.todoItemRecycler.layoutManager = LinearLayoutManager(requireActivity())
        view.todoItemRecycler.itemAnimator = DefaultItemAnimator()

        view.item_toolbar.title = "$toDo"

        view.add_item_btn.setOnClickListener {
            val item = view.ToDo_editText.text.toString()
            view.ToDo_editText.text.clear()

            viewModel.updateToDoItem(toDoID, item)
            adapter.notifyItemChanged(toDoID)

            // Scrolls down to new item
            view.todoItemRecycler.adapter?.let { it1 ->
                view.todoItemRecycler.scrollToPosition(it1.itemCount)
            }
        }


        return view
    }
}