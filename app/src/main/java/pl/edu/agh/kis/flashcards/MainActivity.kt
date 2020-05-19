package pl.edu.agh.kis.flashcards

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import pl.edu.agh.kis.flashcards.activity.TranslationActivity

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.example_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startTranslatorActivity()
        return true
    }

    private fun startTranslatorActivity() {
        val intent = Intent(this, TranslationActivity::class.java)
        startActivity(intent)
    }

}
