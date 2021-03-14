package no.uia.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.todo_card.view.*
import no.uia.todo.data.ToDo
import no.uia.todo.databinding.TodoCardBinding


class ToDoAdapter(private val todoItems: MutableList<ToDo>, private val onClick: (ToDo) -> Unit): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TodoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todoItems[position])
    }

    override fun getItemCount(): Int = todoItems.size
}

class ViewHolder(binding: TodoCardBinding, private val  onClick: (ToDo) -> Unit): RecyclerView.ViewHolder(binding.root) {
    private val view = binding.root
    fun bind(todoItem: ToDo) {
        view.placeholder.text = todoItem.toString()
        view.setOnClickListener{onClick.invoke(todoItem)}
    }
}