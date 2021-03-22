package no.uia.todo.data

data class ToDo( var title: String,
                 var items: MutableList<ToDoItem> ) {

    override fun toString(): String {
        return title
    }

    fun getAmountChecked():Int {
        return items.filter { it.done }.size
    }
}

