package no.uia.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.todo_list_fragment.view.*
import no.uia.todo.databinding.ActivityMainBinding
import no.uia.todo.viewmodel.ToDoViewModel
import no.uia.todo.data.ToDo


class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "TODO:MainActivity"
    private lateinit var binding: ActivityMainBinding

    private lateinit var nav: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.root.toolbar)
        nav = Navigation.findNavController(
            this, R.id.nav_fragment)
    }
}