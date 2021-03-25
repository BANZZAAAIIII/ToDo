package no.uia.todo.viewmodel

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import no.uia.todo.data.ToDo
import no.uia.todo.data.ToDoItem
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream


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
    val fbPath = "ToDo/ToDoList"
    private val LOG_TAG = "ToDo:ToDoViewModel"

    // TODO: This should be in a constructor!
    var path: File? = null


    fun getToDos(): MutableList<ToDo> {
        downloadFile()
        return todoList
    }
    fun getToDosByID(ID: Int) = todoList[ID]

     fun insertToDo(todoName: String): Boolean {
         return if (todoName != "") {
             todoList.add(ToDo(todoName, mutableListOf()))
             Log.d(LOG_TAG, "Added todo: $todoName")
             saveToDoList(toDoListToJSON())
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
            saveToDoList(toDoListToJSON())
            true
        } else
            false
    }

    fun removeToDo(ToDoListID: Int) {
        val todoItem = todoList[ToDoListID]
        todoList.remove(todoItem)
        Log.d(LOG_TAG, "Removed todo: $todoItem. ToDos left: $todoList")

        saveToDoList(toDoListToJSON())
    }

    fun removeToDo(todo: ToDo) {
        todoList.remove(todo)
        Log.d(LOG_TAG, "Removed todo: $todo. ToDos left: $todoList")

        saveToDoList(toDoListToJSON())
    }

    fun removeToDoItem(ToDoListID: Int, ToDoItemID: Int) {
        val item = todoList[ToDoListID].items[ToDoItemID]
        todoList[ToDoListID].items.remove(item)

        saveToDoList(toDoListToJSON())
    }

    fun toggleToDoItem(ToDoListID: Int, ToDoItemID: Int, checked: Boolean) {
        todoList[ToDoListID].items[ToDoItemID].done = checked

        saveToDoList(toDoListToJSON())
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

    private fun toDoListToJSON(): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.toJson(todoList)
    }

//    private fun JSONToToDOList(JSON: String): MutableList<ToDo>? {
//        val gson = Gson()
//
//    }

    private fun saveToDoList(content: String) {

        val file = File.createTempFile("ToDOList", "JSON")
        FileOutputStream(file, false).bufferedWriter().use { writer ->
            writer.write(content)
        }

        uploadFile(file.toUri())
    }

    private fun uploadFile(file: Uri) {
        Log.d(LOG_TAG, "Uploading file to firebase")

        val ref = FirebaseStorage.getInstance().reference.child(fbPath)
        val uploadTask = ref.putFile(file)

        uploadTask.addOnSuccessListener {
            Log.d(LOG_TAG, "successfully uploaded file: $it")
        }.addOnFailureListener{
            Log.d(LOG_TAG, "Failed to upload file: $file", it)
        }
    }

    private fun downloadFile() {
        Log.d(LOG_TAG, "Downloading file from firebase")
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val todoRef = storageRef.child(fbPath)

        val localFile = File.createTempFile("ToDOList", "JSON")

        todoRef.getFile(localFile).addOnSuccessListener {
            val file: List<String> = localFile.readLines()
            localFile.delete()
        }.removeOnFailureListener {
            Log.d(LOG_TAG, "Error downloading todo list", it)
        }
    }
}
