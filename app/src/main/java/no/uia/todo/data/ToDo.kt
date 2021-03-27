package no.uia.todo.data

data class ToDo(
    val title: String,
    val items: MutableList<ToDoItem>,
    val created: Long = System.currentTimeMillis())
{
    override fun toString(): String {
        return title
    }

    fun getAmountChecked():Int {
        return items.filter { it.done }.size
    }
}

