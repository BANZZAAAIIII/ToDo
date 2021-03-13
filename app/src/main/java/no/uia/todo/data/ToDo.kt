package no.uia.todo.data

data class ToDo( val title: String,
                 val items: MutableList<String>? ) {

    override fun toString(): String {
        return title
    }
}

