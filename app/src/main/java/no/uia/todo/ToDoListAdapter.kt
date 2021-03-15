package no.uia.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.todo_list_card.view.*
import no.uia.todo.data.ToDo
import no.uia.todo.databinding.TodoListCardBinding


class ToDoAdapter(private val todoList: MutableList<ToDo>, private val onClick: (Int) -> Unit) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TodoListCardBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todoList[position], position)
    }

    override fun getItemCount(): Int = todoList.size
}

class ViewHolder(binding: TodoListCardBinding, private val  onClick: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
    private val view = binding.root
    fun bind(todoItem: ToDo, position: Int) {
        view.placeholder.text = todoItem.toString()
        view.setOnClickListener{onClick.invoke(position)}
    }
}