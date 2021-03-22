package no.uia.todo.view.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.todo_item_fragment.view.*
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

        // Args, the ToDoList id, from ToDoListFragment
        val args: ToDoItemFragmentArgs by navArgs()
        val toDoListID = args.toDoItemId

        // Sets toolbar title
        view.item_title.text = viewModel.getToDoListName(toDoListID)

        // Progressbar setup
        view.item_progressbar.max = viewModel.getToDoSize(toDoListID)
        view.item_progressbar.progress = viewModel.getToDoItemChecked(toDoListID)

        // Updates viewModel on setOnCheckedChangeListener
        val onChecked = { _: CompoundButton, isChecked:Boolean, toDoItemID:Int ->
            viewModel.toggleToDoItem(toDoListID, toDoItemID, isChecked)
            view.item_progressbar.progress = viewModel.getToDoItemChecked(toDoListID)
        }

        // Recycler view setup
        val adapter = ToDoItemAdapter(viewModel.getToDosByID(toDoListID).items, onChecked)
        view.todoItemRecycler.adapter = adapter
        view.todoItemRecycler.layoutManager = LinearLayoutManager(requireActivity())
        view.todoItemRecycler.itemAnimator = DefaultItemAnimator()


        view.add_item_btn.setOnClickListener {
            val item = view.new_item_editText.text.toString()
            view.new_item_editText.text.clear()

            viewModel.insertToDoItem(toDoListID, item)
            adapter.notifyItemChanged(toDoListID)

            view.item_progressbar.max = viewModel.getToDoSize(toDoListID)

            // TODO Fix this
            // Scrolls down to new item
            view.todoItemRecycler.adapter?.let { it1 ->
                view.todoItemRecycler.scrollToPosition(it1.itemCount)
            }
        }

        return view
    }
}