package no.uia.todo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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


    private val _toDoList: MutableLiveData<MutableList<ToDo>> = MutableLiveData()
    val toDoLiveData: LiveData<MutableList<ToDo>> get() = _toDoList

    init {
        _toDoList.value = todoList
    }


    fun getToDos() = todoList
    fun getToDosByID(ID: Int) = todoList[ID]

    fun insertToDo(toDo: String) {
        todoList.add(ToDo(toDo, mutableListOf()))
        Log.d(LOG_TAG, "Added todo: $toDo")
    }

    fun insertToDoItem(ToDoListID: Int, item: String) {
        todoList[ToDoListID].items.add(ToDoItem(item, false))

        Log.d(LOG_TAG, "Updated todo: ${todoList[ToDoListID]}. Now has items: ${todoList[ToDoListID].items}")
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
