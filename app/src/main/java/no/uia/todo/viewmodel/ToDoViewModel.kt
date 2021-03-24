package no.uia.todo.viewmodel

import android.util.Log
import androidx.lifecycle.*
import no.uia.todo.data.ToDo
import no.uia.todo.data.ToDoItem


private val todoList: MutableList<ToDo> = mutableListOf(
        ToDo("Watch Thor Ragnarok", mutableListOf(ToDoItem("10 min inn", true))),
        ToDo("Work on TODO app",  mutableListOf(ToDoItem("Hva er en var?", false), ToDoItem("Hva er en String", false), ToDoItem("Hva er menigen med livet?", false))),
        ToDo("Do something else", mutableListOf(ToDoItem("ja?", false), ToDoItem("nei?", true))),
        ToDo("Jeg kommer ikke på mer", mutableListOf()),
        ToDo("blarg 1", mutableListOf()),
        ToDo("blarg 2", mutableListOf()),
        ToDo("blarg 3", mutableListOf())
)

class ToDoViewModel : ViewModel() {
    private val LOG_TAG = "ToDo:ToDoViewModel"

    fun getToDos() = todoList
    fun getToDosByID(ID: Int) = todoList[ID]

     fun insertToDo(todoName: String): Boolean {
         return if (todoName != "") {
             todoList.add(ToDo(todoName, mutableListOf()))
             Log.d(LOG_TAG, "Added todo: $todoName")
             true
         } else
             false
    }

    fun insertToDoObject(toDo: ToDo) {
        todoList.add(toDo)
    }

    fun insertToDoItem(ToDoListID: Int, item: String): Boolean {
        return if (item != "") {
            todoList[ToDoListID].items.add(ToDoItem(item, false))
            Log.d(LOG_TAG, "Updated todo: ${todoList[ToDoListID]}. Now has items: ${todoList[ToDoListID].items}")
            true
        } else
            false
    }

    fun removeToDo(ToDoListID: Int) {
        val todoItem = todoList[ToDoListID]
        todoList.remove(todoItem)
        Log.d(LOG_TAG, "Removed todo: $todoItem. ToDos left: $todoList")
    }

    fun removeToDoItem(ToDoListID: Int, ToDoItemID: Int) {
        val item = todoList[ToDoListID].items[ToDoItemID]
        todoList[ToDoListID].items.remove(item)
    }

    fun toggleToDoItem(ToDoListID: Int, ToDoItemID: Int, checked: Boolean) {
        todoList[ToDoListID].items[ToDoItemID].done = checked
    }

    fun getToDoListName(ToDoListID: Int) : String {
        return todoList[ToDoListID].toString()
    }

    fun getToDoItemChecked(ToDoListID: Int) : Int {
        return todoList[ToDoListID].getAmountChecked()
    }

    fun getToDoSize(ToDoListID: Int) : Int {
        return todoList[ToDoListID].items.size
    }
}
