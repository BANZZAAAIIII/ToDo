package no.uia.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.view.*
import no.uia.todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val LOG_TAG = "TODO:MainActivity"
    private lateinit var binding: ActivityMainBinding

    // Behold! My stuff!
    val listOfMyStuff = mutableListOf("vespa", "battle axe", "m16", "statue", "shake Weight", "blarg", "chest", "loot", "more loot")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.d(LOG_TAG, "blarg")
        view.todoRecycler.layoutManager = LinearLayoutManager(this)
        Log.d(LOG_TAG, "Foo")
        view.todoRecycler.adapter = ToDoAdapter(listOfMyStuff)
    }
}