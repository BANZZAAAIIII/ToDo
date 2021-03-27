package no.uia.todo.viewmodel

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import no.uia.todo.data.ToDo
import no.uia.todo.data.ToDoItem
import java.io.File
import java.io.FileOutputStream

private var todoList: MutableList<ToDo> = mutableListOf()

class ToDoViewModel : ViewModel() {
    private val fbFolder = "ToDo"
    private val fbPath = "ToDo/"

    private val LOG_TAG = "ToDo:ToDoViewModel"

    private val todo = MutableLiveData<List<ToDo>>()
    val todoListLD: LiveData<List<ToDo>>
        get() = todo

    fun getToDosByIndex(index: Int) = todoList[index]

     fun insertToDo(todoName: String): Boolean {
         return if (todoName != "" && todoName.length < 124) {
             val newToDo = ToDo(todoName, mutableListOf())
             todoList.add(newToDo)
             todo.value = todoList

             uploadToDo(todoToJSON(newToDo), newToDo.created)

             Log.d(LOG_TAG, "Added todo: $todoName")
             true
         } else
             false
    }

    fun insertToDoItem(ToDoListID: Int, item: String, checked: Boolean = false): Boolean {
        return if (item != "") {
            todoList[ToDoListID].items.add(ToDoItem(item, checked))
            uploadToDo(todoToJSON(todoList[ToDoListID]), todoList[ToDoListID].created)

            Log.d(LOG_TAG, "Updated todo: ${todoList[ToDoListID]}. Now has items: ${todoList[ToDoListID].items}")
            true
        } else
            false
    }

    fun insertToDoObject(todoO: ToDo) {
        todoList.add(todoO)
        todoList.sortBy { it.created }
        todo.value = todoList

        uploadToDo(todoToJSON(todoO), todoO.created)
    }

    fun removeToDo(ID: Int) {
        val toRemove:ToDo = getToDosByIndex(ID)
        val todoRef = FirebaseStorage.getInstance().reference.child("$fbPath${toRemove.created}")

        todoList.remove(toRemove)
        todo.value = todoList

        todoRef.delete().addOnCompleteListener {
            Log.d(LOG_TAG, "Removed todo$ID: $toRemove. ToDos left: $todoList")

        }.addOnFailureListener {
            // Adds todo back to list
            todoList.add(toRemove)
            todoList.sortBy { it.created }
            todo.value = todoList

            Log.d(LOG_TAG, "Failed to remove $ID: $toRemove. ToDos left: $todoList")

            // Delete can succeed but not trigger OnCompleteListener (Timeout?)
            downloadToDoList()
        }
    }

    fun removeToDoItem(ToDoListID: Int, ToDoItemID: Int) {
        val item = todoList[ToDoListID].items[ToDoItemID]
        todoList[ToDoListID].items.remove(item)
        uploadToDo(todoToJSON(todoList[ToDoListID]), todoList[ToDoListID].created)
    }

    fun toggleToDoItem(ToDoListID: Int, ToDoItemID: Int, checked: Boolean) {
        todoList[ToDoListID].items[ToDoItemID].done = checked
        uploadToDo(todoToJSON(todoList[ToDoListID]), todoList[ToDoListID].created)
    }

    fun getToDoItemChecked(ToDoListID: Int) : Int {
        return todoList[ToDoListID].getAmountChecked()
    }

    fun getToDoSize(ToDoListID: Int) : Int {
        return todoList[ToDoListID].items.size
    }

    private fun todoToJSON(todo: ToDo): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.toJson(todo)
    }

    fun jsonToToDOList(JSON: String): ToDo {
        val gson = Gson()
        return gson.fromJson(JSON, ToDo::class.java)
    }

    private fun uploadToDo(content: String, ID: Long) {
        val file = File.createTempFile("ToDOList", "JSON")
        FileOutputStream(file, false).bufferedWriter().use { it.write(content) }

        upload(file.toUri(), ID)
        file.delete()
    }

    private fun upload(file: Uri, ID: Long) {
        val ref = FirebaseStorage.getInstance().reference.child("$fbPath$ID")
        val uploadTask = ref.putFile(file)

        uploadTask.addOnSuccessListener {
//            Log.d(LOG_TAG, "successfully uploaded file: $file")
        }.addOnFailureListener{
            Log.d(LOG_TAG, "Failed to upload file: $file", it)
        }
    }

    fun downloadToDoList() {
        val ref = FirebaseStorage.getInstance().reference.child(fbFolder)
        val uploadTask = ref.listAll()

        // resets list
        todoList = mutableListOf()

        // Downloads new list
        uploadTask.addOnSuccessListener { listResult ->
            listResult.items.forEach { item ->
                downloadToDo(item)
            }
        }.addOnFailureListener {
            Log.d(LOG_TAG, "Error downloading all the ToDo lists: ", it)
        }
    }

    private fun downloadToDo(ref: StorageReference) {
        val localFile = File.createTempFile("ToDOList", "JSON")
        ref.getFile(localFile).addOnSuccessListener {
            val jsonToDo: String = localFile.readLines().reduce { acc, s -> acc + s.trim() }
            todoList.add(jsonToToDOList(jsonToDo))
            todoList.sortBy { it.created }

            // Gives live date with new list
            todo.value = todoList

            localFile.delete()
//            Log.d(LOG_TAG, "ToDo ID: ${ref.name}, JSON: $jsonToDo")
        }.removeOnFailureListener { e ->
            Log.d(LOG_TAG, "Exception downloading ToDo with ID: ${ref.name}", e)
        }
    }

    // For uploading test data
    fun uploadToDoList(list: MutableList<ToDo>){
        list.forEach{
            uploadToDo(todoToJSON(it), it.created)
        }
    }
}
