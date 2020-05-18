package pl.edu.agh.kis.flashcards.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.add_note.*
import kotlinx.android.synthetic.main.fragment_notes_list.*
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity!!.findViewById<View>(R.id.note_list) != null
            || activity!!.findViewById<View>(R.id.note_lists_list) == null)
            //prevent error on rotating back
            addNewNote.setOnClickListener { view?.let { it1 -> addNote(it1) } }
    }

    fun addNote(view: View) {
        val mDialogView =
            LayoutInflater.from(activity!!).inflate(R.layout.add_note, null)
        val builder = AlertDialog.Builder(activity!!)

        with(builder) {
            setView(mDialogView)
            setTitle("Add note")
        }

        val dialog = builder.create()
        dialog.show()

        with(dialog) {
            add_button.isEnabled = false
            add_button.setOnClickListener {
                //todo add insert impl to room
                dismiss()
            }
            var firstNotEmpty = false
            var secondNotEmpty = false
            editText2.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    firstNotEmpty = s.trim().isNotEmpty()
                    dialog.add_button.isEnabled = firstNotEmpty && secondNotEmpty
                }
            })
            editText3.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    secondNotEmpty = s.trim().isNotEmpty()
                    dialog.add_button.isEnabled = firstNotEmpty && secondNotEmpty
                }
            })
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