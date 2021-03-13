package no.uia.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import no.uia.todo.databinding.TodoCardBinding

class ToDoAdapter(private val todoItems: MutableList<String>): RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    class ViewHolder(private val binding: TodoCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(todoItem: String){
            println("TODO adapter tingemajig")
            binding.placeholder.text = todoItem
        }
    }

    override fun getItemCount(): Int = todoItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todoItems[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TodoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

}