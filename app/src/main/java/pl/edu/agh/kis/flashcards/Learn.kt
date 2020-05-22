package pl.edu.agh.kis.flashcards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewParent
import android.widget.TextView
import androidx.viewpager.widget.ViewPager

class Learn : AppCompatActivity() {

    private var pager: ViewPager? = null
    private var flashCardCollectionAdapter: FlashCardCollectionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        pager = findViewById(R.id.pager)
        flashCardCollectionAdapter = FlashCardCollectionAdapter(supportFragmentManager, 7);
        pager!!.adapter = flashCardCollectionAdapter
    }
}
