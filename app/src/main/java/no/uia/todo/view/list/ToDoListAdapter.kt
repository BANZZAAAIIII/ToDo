package no.uia.todo.view.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import no.uia.todo.data.ToDo
import no.uia.todo.databinding.TodoListCardBinding

class ToDoListAdapter(
    private val onClick: (Int) -> Unit
) : ListAdapter<ToDo, ToDoListAdapter.ListViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = TodoListCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
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

    class DiffCallback : DiffUtil.ItemCallback<ToDo>() {
        override fun areItemsTheSame(oldItem: ToDo, newItem: ToDo) = oldItem.created == newItem.created
        override fun areContentsTheSame(oldItem: ToDo, newItem: ToDo) = oldItem == newItem
    }
}