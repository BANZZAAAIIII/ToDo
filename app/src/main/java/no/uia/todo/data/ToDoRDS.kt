package no.uia.todo.data

import com.google.firebase.storage.FirebaseStorage



class ToDoRDS {
    private fun uploadList(JSON: String){
        val ref = FirebaseStorage.getInstance().reference.child("ToDo")
    }

    companion object {
        val instance = ToDoRDS()
    }
}
