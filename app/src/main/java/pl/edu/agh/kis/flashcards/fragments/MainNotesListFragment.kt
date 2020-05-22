package pl.edu.agh.kis.flashcards.fragments

import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.add_new_list_dialog.*
import kotlinx.android.synthetic.main.fragment_main_notes_list.*
import pl.edu.agh.kis.flashcards.NoteListDetailsActivity
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.entities.NoteListEntity
import pl.edu.agh.kis.flashcards.recyclerView.NoteHolder
import pl.edu.agh.kis.flashcards.recyclerView.NoteListAdapterRecycler
import pl.edu.agh.kis.flashcards.recyclerView.NoteListHolder
import pl.edu.agh.kis.flashcards.viewmodels.NoteListViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var noteListViewModel: NoteListViewModel


class MainNotesListFragment : Fragment(), NoteListAdapterRecycler.OnNoteSetListener {

    private var param1: String? = null
    private var param2: String? = null

    private var dualPane: Boolean = false
    private var curCheckPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_notes_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val detailsFrame: View? = activity?.findViewById(R.id.note_list)
        dualPane = detailsFrame?.visibility == View.VISIBLE
        curCheckPosition = savedInstanceState?.getInt("curChoice", 0) ?: 0

        if (dualPane) {
            showDetails(curCheckPosition)
        }
        val recyclerView = MainNotesRecyclerView
        val adapter = NoteListAdapterRecycler(activity!!.applicationContext, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        ViewModelFactory.setApplication(this.activity!!.application)
        noteListViewModel = ViewModelProvider(
            this,
            ViewModelFactory
        ).get(NoteListViewModel::class.java)
        noteListViewModel.allNoteLists.observe(this, Observer { noteLists ->
            // Update the cached copy of the words in the adapter.
            noteLists?.let { adapter.setList(it) }
        })
        AddNewNotesList.setOnClickListener { view?.let { it1 -> basicAlert(it1) } }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("curChoice", curCheckPosition)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainNotesListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainNotesListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun basicAlert(view: View) {
        val mDialogView =
            LayoutInflater.from(activity!!).inflate(R.layout.add_new_list_dialog, null)
        val builder = AlertDialog.Builder(activity!!)

        with(builder) {
            setView(mDialogView)
            setTitle("Create new FlashNotes list!")
        }

        val dialog = builder.create()
        dialog.show()

        ArrayAdapter.createFromResource(
            activity!!,
            R.array.languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialog.baseLangSpinner.adapter = adapter
            dialog.targetLangSpinner.adapter = adapter
        }


        with(dialog) {
            cancel_button.setOnClickListener { dialog.dismiss() }
            confirm_button.isEnabled = false
            confirm_button.setOnClickListener {
                noteListViewModel.insert(
                    NoteListEntity(
                        null,
                        editText1.text.toString(),
                        baseLangSpinner.selectedItem.toString(),
                        targetLangSpinner.selectedItem.toString()
                    )
                )
                dismiss()
            }
            editText1.addTextChangedListener(object : TextWatcher {

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
                    dialog.confirm_button.isEnabled = s.trim().isNotEmpty()
                }
            })
        }
    }

    override fun onClick(noteListHolder: NoteListHolder, position: Int) {
        curCheckPosition = position
        showDetails(position)
    }

    private fun showDetails(index: Int) {
        curCheckPosition = index
        if (dualPane) {
            var details = fragmentManager?.findFragmentById(R.id.note_list) as? NotesSetFragment
            if (details?.shownIndex != index) {
                details =
                    NotesSetFragment.newInstance(noteListViewModel.allNoteLists.value?.get(index)!!.id!!)
                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.note_list, details)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    commit()
                }
            }
        } else {
            val intent = Intent().apply {
                setClass(context!!, NoteListDetailsActivity::class.java)
                putExtra("index", noteListViewModel.allNoteLists.value?.get(index)!!.id)
                putExtra("sourceLang",noteListViewModel.allNoteLists.value?.get(index)!!.baseLanguage)
                putExtra("targetLang",noteListViewModel.allNoteLists.value?.get(index)!!.targetLanguage)
            }
            startActivity(intent)
        }
    }
}


object ViewModelFactory : ViewModelProvider.Factory {
    lateinit var app: Application

    fun setApplication(application: Application) {
        app = application
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteListViewModel(app) as T
    }
}