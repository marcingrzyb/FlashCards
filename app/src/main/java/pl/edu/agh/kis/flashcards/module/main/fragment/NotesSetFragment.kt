package pl.edu.agh.kis.flashcards.module.main.fragment

import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_note_dialog.*
import kotlinx.android.synthetic.main.fragment_notes_list.*
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.api.TranslatorController
import pl.edu.agh.kis.flashcards.database.entity.NoteEntity
import pl.edu.agh.kis.flashcards.module.main.fragment.operation.OperationType
import pl.edu.agh.kis.flashcards.module.main.view.NoteAdapter
import pl.edu.agh.kis.flashcards.module.main.view.NoteHolder
import pl.edu.agh.kis.flashcards.module.main.viewmodels.NoteViewModel
import pl.edu.agh.kis.flashcards.module.playmode.activity.PlayMode
import kotlin.properties.Delegates

private lateinit var noteViewModel: NoteViewModel

class NotesSetFragment : Fragment(), NoteAdapter.OnNoteSetListener {
    private lateinit var translatorController: TranslatorController

    val shownIndex: Int by lazy {
        arguments?.getInt("index", 0) ?: 0
    }

    val targetLang: String by lazy {
        arguments?.getString("targetLang", "") ?: ""
    }

    val sourceLang: String by lazy {
        arguments?.getString("sourceLang", "") ?: ""
    }

    private lateinit var adapter: NoteAdapter

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
        translatorController = TranslatorController()
        if (activity!!.findViewById<View>(R.id.note_list) != null
            || activity!!.findViewById<View>(R.id.note_lists_list) == null
        ) {
            //prevent error on rotating back
            addNewNote.setOnClickListener { view?.let { it1 -> addNote(OperationType.CREATE) } }
            play.setOnClickListener { view?.let { it1 -> playFlashCards(it1) } }

            val recyclerView = notesSetRecyclerView
            adapter = NoteAdapter(activity!!.applicationContext, this)

            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(activity!!.applicationContext, 2)
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

            NoteViewModelFactory.setApplication(
                this.activity!!.application
            )
            NoteViewModelFactory.setIdF(
                shownIndex
            )
            noteViewModel = ViewModelProvider(
                this,
                NoteViewModelFactory
            ).get(NoteViewModel::class.java)
            noteViewModel.notes.observe(this, Observer { noteLists ->
                // Update the cached copy of the words in the adapter.
                noteLists?.let { adapter.setList(it) }
            })
        }
    }

    fun playFlashCards(view: View) {
        val intent = Intent(activity, PlayMode::class.java)
        val b = Bundle()
        b.putInt("id", shownIndex)
        b.putString("sourceLang", sourceLang)
        b.putString("targetLang", targetLang)
        intent.putExtras(b)
        startActivity(intent)
    }

    private fun addNote(operationType: OperationType){
        addNote(operationType, null)
    }

    private fun addNote(operationType: OperationType, noteEntity: NoteEntity?) {
        val mDialogView =
            LayoutInflater.from(activity!!).inflate(R.layout.add_note_dialog, null)
        val builder = AlertDialog.Builder(activity!!)
        with(builder) {
            setView(mDialogView)
            when (operationType) {
                OperationType.CREATE -> setTitle("Add note")
                OperationType.UPDATE -> setTitle("Edit note")
            }
        }

        val dialog = builder.create()
        dialog.show()

        with(dialog) {
            if (operationType == OperationType.UPDATE) {
                fillCurrentSetValues(word, translatedWord, noteEntity!!)
            }
            add_button.isEnabled = operationType != OperationType.CREATE
            cancelButton.setOnClickListener {
                dismiss()
            }
            translateButton.setOnClickListener {
                translatorController.translate(
                    translatedWord,
                    word.text.toString(),
                    sourceLang,
                    targetLang
                )
            }
            add_button.setOnClickListener {
                when (operationType){
                    OperationType.CREATE -> {
                        noteViewModel.insert(
                            NoteEntity(
                                null,
                                shownIndex,
                                word.text.toString(),
                                translatedWord.text.toString(),
                                false,
                                false
                            )
                        )
                    }
                    OperationType.UPDATE -> {
                        noteEntity!!.translatedWord = translatedWord.text.toString()
                        noteEntity!!.word = word.text.toString()
                        noteViewModel.update(noteEntity)
                    }

                }

                dismiss()
            }
            var firstNotEmpty = false
            var secondNotEmpty = false
            word.addTextChangedListener(object : TextWatcher {

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
            translatedWord.addTextChangedListener(object : TextWatcher {

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

    private fun fillCurrentSetValues(
        word: EditText,
        translation: EditText,
        note: NoteEntity
    ) {
        word.setText(note.word)
        translation.setText(note.translatedWord)
    }

    companion object {
        /**
         * Create a new instance of DetailsFragment, initialized to
         * show the text at 'index'.
         */
        fun newInstance(index: Int): NotesSetFragment {
            val f =
                NotesSetFragment()

            // Supply index input as an argument.
            val args = Bundle()
            args.putInt("index", index)
            f.arguments = args

            return f
        }
    }

    override fun onClick(noteHolder: NoteHolder, position: Int) {
        Log.d("SET", "Note clicked")
    }

    override fun onLongClick(noteListHolder: NoteHolder, position: Int) {
        Log.d("SET", "long click")
        val note = noteViewModel.notes.value?.get(position)!!
        addNote(OperationType.UPDATE, note)
    }

    var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteNote(
                    noteViewModel.notes.value?.get(viewHolder.adapterPosition)
                )
            }
        }

    private fun deleteNote(note: NoteEntity?) {
        noteViewModel.delete(note!!)
        adapter.notifyDataSetChanged()
    }


}

object NoteViewModelFactory : ViewModelProvider.Factory {
    lateinit var app: Application
    var id by Delegates.notNull<Int>()
    fun setApplication(application: Application) {
        app = application
    }

    fun setIdF(id_: Int) {
        id = id_
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteViewModel(
            app,
            id
        ) as T
    }
}