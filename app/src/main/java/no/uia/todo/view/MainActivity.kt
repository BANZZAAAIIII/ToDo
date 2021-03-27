package no.uia.todo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import no.uia.todo.R
import no.uia.todo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "TODO:MainActivity"
    private lateinit var binding: ActivityMainBinding

    private lateinit var nav: NavController

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        signInAnonymously()


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setSupportActionBar(binding.root.toolbar)
        nav = Navigation.findNavController(
                this, R.id.nav_fragment)

    }

    private fun signInAnonymously() {
        auth.signInAnonymously().addOnSuccessListener {
            Log.d(LOG_TAG, "Signed in Anonymously with user: ${it.user?.toString()}")
        }.addOnFailureListener {
            Log.d(LOG_TAG, "Signed in Anonymously failed", it)
        }
    }
}