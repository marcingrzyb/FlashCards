package pl.edu.agh.kis.flashcards.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment

class NotesSetFragment : Fragment() {

    val shownIndex: Int by lazy {
        arguments?.getInt("index", 0) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container == null) {
            return null
        }
        val text = TextView(activity).apply {
            val padding: Int = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4f,
                activity?.resources?.displayMetrics
            ).toInt()
            setPadding(padding, padding, padding, padding)
            text = shownIndex.toString()
        }
        return ScrollView(activity).apply {
            addView(text)
        }
    }

    companion object {
        /**
         * Create a new instance of DetailsFragment, initialized to
         * show the text at 'index'.
         */
        fun newInstance(index: Int): NotesSetFragment {
            val f = NotesSetFragment()

            // Supply index input as an argument.
            val args = Bundle()
            args.putInt("index", index)
            f.arguments = args

            return f
        }
    }


}