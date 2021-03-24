package no.uia.todo.data

data class ToDoItem (var name: String,
                     var done: Boolean ) {

    override fun toString(): String {
        return name
    }
}