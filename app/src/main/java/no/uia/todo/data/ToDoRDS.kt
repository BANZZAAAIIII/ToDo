package no.uia.todo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher



class ToDoRDS {
    // TODO: Get this from firebase
    private val toDoList = mutableListOf<ToDo>()

    // For other classes to observe
    private val toDoLiveData = MutableLiveData<List<ToDo>>()

    // Sets the live data to the data we have
    init {
        toDoLiveData.value = toDoList
    }

    // Updates list and live data to so observers knows its changed
    fun addToDo(toDo: ToDo) {
        toDoList.add(toDo)
        toDoLiveData.value = toDoList
    }

    // returns LiveData as we don't want other classes to change it
    fun getToDos() = toDoLiveData as LiveData<List<ToDo>>

    companion object {
        val instance = ToDoRDS()
    }
}
