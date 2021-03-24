package no.uia.todo.view.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import no.uia.todo.data.ToDo
import no.uia.todo.databinding.TodoListCardBinding


class ToDoListAdapter(
        private var todoList: List<ToDo>,
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

    fun updateAdapter(newToDos: List<ToDo>) {
        todoList = newToDos
    }
}

class ListViewHolder(
        private val binding: TodoListCardBinding,
        private val onClick: (Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onClick.invoke(adapterPosition)
            }
        }
    }

    fun bind(todoItem: ToDo) {
        binding.apply {
            listName.text = todoItem.title
            listProgressBar.max = todoItem.items.size
            listProgressBar.progress = todoItem.getAmountChecked()
        }
    }
}