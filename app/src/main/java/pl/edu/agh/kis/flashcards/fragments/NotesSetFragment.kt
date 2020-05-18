package pl.edu.agh.kis.flashcards.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.edu.agh.kis.flashcards.R

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
        return inflater.inflate(R.layout.fragment_notes_list, container, false)
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