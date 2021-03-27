package no.uia.todo.view.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.todo_item_fragment.view.*
import no.uia.todo.databinding.TodoItemFragmentBinding
import no.uia.todo.viewmodel.ToDoViewModel

class ToDoItemFragment : Fragment() {
    private val LOG_TAG = "TODO:ToDoItemFragment"

    private var _binding: TodoItemFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ToDoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TodoItemFragmentBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)

        // Args, the ToDoList id, from ToDoListFragment
        val args: ToDoItemFragmentArgs by navArgs()
        val toDoListID = args.toDoItemId

        val todoItem = viewModel.getToDosByIndex(toDoListID)

        // Sets toolbar title
        view.itemTitle.text = todoItem.title

        // Progressbar setup
        updateProgressbar(toDoListID)

        // Updates viewModel on setOnCheckedChangeListener
        val onChecked = { _: CompoundButton, isChecked:Boolean, toDoItemID:Int ->
            viewModel.toggleToDoItem(toDoListID, toDoItemID, isChecked)
            updateProgressbar(toDoListID)
        }

        binding.apply {
            // Recycler view and adapter setup
            val itemAdapter = ToDoItemAdapter(todoItem.items, onChecked)
            ToDoItemRecycler.apply {
                adapter = itemAdapter
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = DefaultItemAnimator()
                setHasFixedSize(true)
            }
            // Sets on swipe action to delete todoitem
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                // onMove returns false as we don't want to use it
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false
                // Deletes item by swiping either direction
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val pos = viewHolder.adapterPosition
                    viewModel.removeToDoItem(toDoListID, pos)
                    updateProgressbar(toDoListID)
                    itemAdapter.notifyItemRemoved(pos)
                }
            }).attachToRecyclerView(ToDoItemRecycler)


            // new item button setup
            addItemButton.setOnClickListener {
                val itemText = view.newItemEditText.text.toString()
                newItemEditText.text.clear()

                if (viewModel.insertToDoItem(toDoListID, itemText)) {
                    itemAdapter.notifyItemInserted(itemAdapter.itemCount)

                    updateProgressbar(toDoListID)

                    // Scrolls down to new item
                    ToDoItemRecycler.smoothScrollToPosition(itemAdapter.itemCount)
                } else {
                    Toast.makeText(context, "Invalid name", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }

    private fun updateProgressbar(ID: Int) {
        binding.apply {
            itemProgressbar.max = viewModel.getToDoSize(ID)
            itemProgressbar.progress = viewModel.getToDoItemChecked(ID)
        }
    }
}