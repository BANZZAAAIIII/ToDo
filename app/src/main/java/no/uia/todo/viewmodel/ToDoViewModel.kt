package no.uia.todo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.uia.todo.data.ToDo

private val todoList: MutableList<ToDo> = mutableListOf(
        ToDo("Watch Thor Ragnarok", mutableListOf("10 min inn", "20 min inn", "30 min inn", "sv")),
        ToDo("Work on TODO app", mutableListOf("Hva er en var?", "Hva er en String", "Hva er menigen med livet?")),
        ToDo("To something else", mutableListOf()),
        ToDo("Jeg kommer ikke på mer", mutableListOf()),
        ToDo("blarg 1", mutableListOf()),
        ToDo("blarg 2", mutableListOf()),
        ToDo("blarg 3", mutableListOf())
)

class ToDoViewModel : ViewModel() {
    private val LOG_TAG = "ToDo:ToDoViewModel"


    //private val _toDoList: MutableLiveData<ToDo> = MutableLiveData()
    //val toDoLiveData: LiveData<ToDo> get() = _toDoList


    fun getToDos() = todoList
    fun getToDosByID(ID: Int) = todoList[ID]

    fun insertToDo(toDo: ToDo) = todoList.add(toDo)
    fun updateToDoItem(ID: Int, item: String) {
        todoList[ID].items?.add(item)

        Log.d(LOG_TAG, "Updated todo: ${todoList[ID]}. Now had items: ${todoList[ID].items}")
    }
}
