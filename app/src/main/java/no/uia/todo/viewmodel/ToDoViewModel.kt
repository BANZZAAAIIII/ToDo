package no.uia.todo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.uia.todo.data.ToDo

class ToDoViewModel : ViewModel() {
    private val LOG_TAG = "ToDo:ToDoViewModel"

    private val todoList: MutableList<ToDo> = mutableListOf(
        ToDo("Watch Thor Ragnarok", mutableListOf("10 min inn", "20 min inn", "30 min inn", "sv")),
        ToDo("Work on TODO app", mutableListOf("Hva er en var?", "Hva er en String", "Hva er menigen med livet?")),
        ToDo("To something else", null),
        ToDo("Jeg kommer ikke på mer", null),
        ToDo("blarg 1", null),
        ToDo("blarg 2", null),
        ToDo("blarg 3", null)
    )
    //private val _toDoList: MutableLiveData<ToDo> = MutableLiveData()
    //val toDoLiveData: LiveData<ToDo> get() = _toDoList


    fun getToDos() = todoList
    fun getToDosByID(ID: Int) = todoList[ID]
}
