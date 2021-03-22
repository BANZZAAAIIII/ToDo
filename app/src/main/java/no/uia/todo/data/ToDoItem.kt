package no.uia.todo.data

data class ToDoItem ( var item: String,
                      var done: Boolean ) {

    override fun toString(): String {
        return item
    }
}