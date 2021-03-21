package no.uia.todo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import no.uia.todo.R
import no.uia.todo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "TODO:MainActivity"
    private lateinit var binding: ActivityMainBinding

    private lateinit var nav: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setSupportActionBar(binding.root.toolbar)
        nav = Navigation.findNavController(
                this, R.id.nav_fragment)
    }
}