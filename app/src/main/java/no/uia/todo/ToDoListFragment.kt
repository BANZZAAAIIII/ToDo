package no.uia.todo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.todo_list_fragment.view.*
import no.uia.todo.data.ToDo
import no.uia.todo.databinding.TodoListFragmentBinding
import no.uia.todo.viewmodel.ToDoViewModel

class ToDoListFragment : Fragment() {

    companion object {
        fun newInstance() = ToDoListFragment()
    }

    private val LOG_TAG: String = "piano:PianoLayoutFragment"

    private var _binding: TodoListFragmentBinding? = null
    private val binding get() = _binding!!


    private var listOfToDos: MutableList<ToDo> = mutableListOf(
        ToDo("Watch Thor Ragnarok", null),
        ToDo("Work on TODO app", null),
        ToDo("To something else", null),
        ToDo("Jeg kommer ikke på mer", null),
        ToDo("blarg 1", null),
        ToDo("blarg 2", null),
        ToDo("blarg 3", null)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TodoListFragmentBinding.inflate(layoutInflater, container, false)
        val view = binding.root



        val onClickToDo = {toDo:ToDo -> println("balrg $toDo")}

        view.todoRecycler.layoutManager = LinearLayoutManager(requireActivity())
        view.todoRecycler.adapter = ToDoAdapter(listOfToDos, onClickToDo)

        return view
    }


}