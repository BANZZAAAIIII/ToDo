package no.uia.todo.viewmodel

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
    private val LOG_TAG = "ToDo:ToDoViewModel"

    private val user = MutableLiveData<String>()
    val userLiveData: MutableLiveData<String>
        get() = user

    private val todo = MutableLiveData<List<ToDo>>()
    val liveDataToDoList: LiveData<List<ToDo>>
        get() = todo

    fun getToDosByIndex(index: Int) = todoList[index]

    fun getToDosByID(ID: Long) = todoList.find {
        ID == it.created
    }

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            user.value = currentUser.uid
            Log.d(LOG_TAG, "Current user: ${user.value}")

        } else {
            // In case sign in is not finished
            Log.w(LOG_TAG, "Current user is Null, waiting for AuthStateListener")
            Firebase.auth.addAuthStateListener {
                if (it.currentUser != null) {
                    user.value = it.currentUser?.uid
                    Log.d(LOG_TAG, "Current user: ${user.value}")
                }
            }
        }
    }

     fun insertToDo(todoName: String): Boolean {
         return if (todoName != "" && todoName.length < 124) {
             val newToDo = ToDo(todoName, mutableListOf())
             todoList.add(newToDo)
             todo.value = todoList

             uploadToDo(newToDo)
             Log.v(LOG_TAG, "Added todo: $todoName")
             true
         } else
             false
    }

    fun insertToDoItem(index: Int, item: String, checked: Boolean = false): Boolean {
        return if (item != "" && item.length < 50) {
            todoList[index].items.add(ToDoItem(item, checked))
            uploadToDo(todoList[index])

            Log.v(LOG_TAG, "Updated todo: ${todoList[index]}. Now has items: ${todoList[index].items}")
            true
        } else
            false
    }

    fun insertToDoObject(todoO: ToDo) {
        todoList.addToDo(todoO)
        todo.value = todoList

        uploadToDo(todoO)
    }

    fun removeToDo(index: Int) {
        val toRemove:ToDo = getToDosByIndex(index)

        todoList.remove(toRemove)
        todo.value = todoList

        deleteToDo(toRemove)
    }

    fun removeToDoItem(index: Int, ToDoItemID: Int) {
        val item = todoList[index].items[ToDoItemID]
        todoList[index].items.remove(item)
        uploadToDo(todoList[index])
    }

    fun toggleToDoItem(index: Int, ToDoItemID: Int, checked: Boolean) {
        todoList[index].items[ToDoItemID].done = checked
        uploadToDo(todoList[index])
    }

    fun getToDoItemChecked(index: Int) : Int = todoList[index].getAmountChecked()

    fun getToDoSize(index: Int) : Int = todoList[index].items.size

    private fun todoToJSON(todo: ToDo): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.toJson(todo)
    }

    private fun jsonToToDOList(JSON: String): ToDo = Gson().fromJson(JSON, ToDo::class.java)

    private fun uploadToDo(data: ToDo) {
        val tempFile = File.createTempFile("ToDOList", "JSON")
        FileOutputStream(tempFile, false).bufferedWriter().use { it.write(todoToJSON(data)) }

        val ref = FirebaseStorage.getInstance().reference.child("${user.value}/${data.created}")
        val uploadTask = ref.putFile(tempFile.toUri())

        uploadTask.addOnSuccessListener {
            Log.v(LOG_TAG, "successfully uploaded todo: $data")
            tempFile.delete()
        }.addOnFailureListener{ e ->
            Log.e(LOG_TAG, "Failed to upload todo: $data", e)
            tempFile.delete()
        }
    }

    private fun deleteToDo(data: ToDo) {
        val todoRef = FirebaseStorage.getInstance().reference.child("${user.value}/${data.created}")

        todoRef.delete().addOnCompleteListener {
            Log.v(LOG_TAG, "Removed from fb: todo${data.created}: ${data.title}. ToDos left: $todoList")
        }.addOnFailureListener { e ->
            // Adds todo back to list
            todoList.addToDo(data)
            todo.value = todoList

            Log.e(LOG_TAG, "Failed to remove todo${data.created}: ${data.title}. ToDos left: $todoList", e)

            downloadToDoList()
        }
    }

    fun downloadToDoList() {
        val ref = FirebaseStorage.getInstance().reference.child("${user.value}")
        val uploadTask = ref.listAll()

        // resets list
        todoList = mutableListOf()

        // Downloads new list
        uploadTask.addOnSuccessListener { listResult ->
            listResult.items.forEach { item ->
                downloadToDo(item)
            }
        }.addOnFailureListener { e ->
            Log.e(LOG_TAG, "Error downloading all the ToDo lists: ", e)
        }
    }

    private fun downloadToDo(ref: StorageReference) {
        val tempFile = File.createTempFile("ToDOList", "JSON")
        ref.getFile(tempFile).addOnSuccessListener {
            val jsonToDo: String = tempFile.readLines().reduce { acc, s -> acc + s.trim() }
            todoList.addToDo(jsonToToDOList(jsonToDo))

            // Gives live date the new list
            todo.value = todoList

            tempFile.delete()
            Log.d(LOG_TAG, "Downloaded: ToDo ID: ${ref.name}, JSON: $jsonToDo")
        }.removeOnFailureListener { e ->
            tempFile.delete()
            Log.e(LOG_TAG, "Error downloading ToDo with ID: ${ref.name}", e)
        }
    }

    // For uploading test data
    fun uploadToDoList(list: MutableList<ToDo>){
        list.forEach{
            uploadToDo(it)
        }
    }
}

// sorts ToDos added by time created, so the list is consistent when syncing
fun MutableList<ToDo>.addToDo(data: ToDo) {
    this.add(data)
    this.sortBy { it.created }
}
