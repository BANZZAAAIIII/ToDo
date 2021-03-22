package no.uia.todo.view.item

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.todo_item_card.view.*
import no.uia.todo.data.ToDoItem
import no.uia.todo.databinding.TodoItemCardBinding


class ToDoItemAdapter(
        private val todoItem: MutableList<ToDoItem>,
        private val onChecked: (CompoundButton, Boolean, Int) -> Unit) : RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = TodoItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ItemViewHolder(view, onChecked)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(todoItem[position])

    }

    override fun getItemCount(): Int = todoItem.size
}

class ItemViewHolder(binding: TodoItemCardBinding, private val onChecked: (CompoundButton, Boolean, Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
    private val view = binding.root
    fun bind(toDo: ToDoItem) {
        view.item_name.text = toDo.item
        view.item_checkBox.isChecked = toDo.done

        view.item_checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            onChecked.invoke(buttonView, isChecked, adapterPosition)
        }
    }
}