package no.uia.todo.view.item

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.todo_item_card.view.*
import no.uia.todo.data.ToDo
import no.uia.todo.data.ToDoItem
import no.uia.todo.databinding.TodoItemCardBinding


class ToDoItemAdapter(
        private var todoItem: List<ToDoItem>,
        private val onChecked: (CompoundButton, Boolean, Int) -> Unit,
) : RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = TodoItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ItemViewHolder(view, onChecked)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(todoItem[position])
    }

    override fun getItemCount(): Int = todoItem.size
}

class ItemViewHolder(
        private val binding: TodoItemCardBinding,
        private val onChecked: (CompoundButton, Boolean, Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    init {
        binding.itemCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onChecked.invoke(buttonView, isChecked, position)
            }
        }
    }
    fun bind(toDo: ToDoItem) {
        binding.apply {
            itemName.text = toDo.name
            itemCheckBox.isChecked = toDo.done
        }
    }
}