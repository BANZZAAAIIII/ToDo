package no.uia.todo.view.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.todo_item_card.view.*
import no.uia.todo.databinding.TodoItemCardBinding


class ToDoItemAdapter(private val todoItem: MutableList<String>) : RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(TodoItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(todoItem[position])

    }

    override fun getItemCount(): Int = todoItem.size
}

class ItemViewHolder(binding: TodoItemCardBinding): RecyclerView.ViewHolder(binding.root) {
    private val view = binding.root
    fun bind(toDo: String) {
        view.item.text = toDo
    }
}