package no.uia.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.view.*
import no.uia.todo.databinding.ActivityMainBinding
import no.uia.todo.viewmodel.ToDoViewModel
import no.uia.todo.data.ToDo


class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "TODO:MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ToDoViewModel

    private var listOfToDos: MutableList<ToDo> = mutableListOf(
        ToDo("Watch Thor Ragnarok", null),
        ToDo("Work on TODO app", null),
        ToDo("To something else", null),
        ToDo("Jeg kommer ikke på mer", null),
        ToDo("blarg 1", null),
        ToDo("blarg 2", null),
        ToDo("blarg 3", null)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        val onClickToDo = {toDo:ToDo -> println("balrg $toDo")}

        view.todoRecycler.layoutManager = LinearLayoutManager(this)
        view.todoRecycler.adapter = ToDoAdapter(listOfToDos, onClickToDo)

        setSupportActionBar(view.toolbar)

    }

}