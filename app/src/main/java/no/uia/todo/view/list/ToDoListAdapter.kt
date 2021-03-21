package no.uia.todo.view.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.todo_list_card.view.*
import no.uia.todo.data.ToDo
import no.uia.todo.databinding.TodoListCardBinding


class ToDoListAdapter(
        private val todoList: MutableList<ToDo>,
        private val onClick: (Int) -> Unit
): RecyclerView.Adapter<ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = TodoListCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(todoList[position])
    }

    override fun getItemCount(): Int = todoList.size
}

class ListViewHolder(
        binding: TodoListCardBinding,
        private val  onClick: (Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    private val view = binding.root

    fun bind(todoItem: ToDo) {
        view.placeholder.text = todoItem.toString()
        view.setOnClickListener{onClick.invoke(adapterPosition)}
    }
}