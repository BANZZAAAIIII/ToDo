package no.uia.todo.view.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.todo_list_add_dialog.view.*
import kotlinx.android.synthetic.main.todo_list_fragment.view.*
import no.uia.todo.R
import no.uia.todo.databinding.TodoListFragmentBinding
import no.uia.todo.viewmodel.ToDoViewModel

class ToDoListFragment : Fragment() {
    private val LOG_TAG: String = "ToDo:ToDoListFragment"

    private var _binding: TodoListFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ToDoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TodoListFragmentBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)


        val onClickToDo = { pos:Int ->
            val arg = ToDoListFragmentDirections.actionToDoListFragmentToToDoItemFragment(pos)
            findNavController().navigate(arg)
        }

        // Recycler view and adapter setup
        val toDoAdapter = ToDoListAdapter(onClickToDo)
        binding.apply {
            ToDoListRecycler.apply {
                adapter = toDoAdapter
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = DefaultItemAnimator()
                setHasFixedSize(true)
            }
            // Sets on swipe action to delete ToDoitem
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT ) {
                // onMove returns false as we don't want to use it
                override fun onMove(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val pos = viewHolder.adapterPosition
                    val todo = viewModel.getToDosByIndex(pos)
                    viewModel.removeToDo(pos)

                    // Undo Snackbar when deleting
                    Snackbar.make(requireView(), "ToDo Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo") {
                                viewModel.insertToDoObject(todo)
                            }.show()
                }
            }).attachToRecyclerView(ToDoListRecycler)
        }

        // Gets all the ToDos from firebase
        viewModel.downloadToDoList()
        viewModel.liveDataToDoList.observe(viewLifecycleOwner, {
            toDoAdapter.submitList(it.toMutableList())
//            Log.d(LOG_TAG, "Live Data, ToDo: $it")
        })

        // Dialog to add new todoList
        view.add_ToDo_fab.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.todo_list_add_dialog, container, false)

            dialogView.ToDo_add_list_btn.setOnClickListener {
                val newToDoName = dialogView.ToDo_list_editText.text.toString()
                if (viewModel.insertToDo(newToDoName)) {
                    toDoAdapter.notifyItemInserted(toDoAdapter.itemCount)
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "Invalid name", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.setContentView(dialogView)
            dialog.show()
        }

        return view
    }
}