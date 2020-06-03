package pl.edu.agh.kis.flashcards.module.main.fragment

import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_new_list_dialog.*
import kotlinx.android.synthetic.main.fragment_main_notes_list.*
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.entity.NoteListEntity
import pl.edu.agh.kis.flashcards.module.main.activity.NoteListDetailsActivity
import pl.edu.agh.kis.flashcards.module.main.view.NoteListAdapterRecycler
import pl.edu.agh.kis.flashcards.module.main.view.NoteListHolder
import pl.edu.agh.kis.flashcards.module.main.viewmodels.NoteListViewModel
import pl.edu.agh.kis.flashcards.module.main.fragment.operation.OperationType
import java.util.Objects.nonNull


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var noteListViewModel: NoteListViewModel

class MainNotesListFragment : Fragment(), NoteListAdapterRecycler.OnNoteSetListener {

    private val TAG: String = "MAIN_NOTES_LIST_FR"

    private var param1: String? = null
    private var param2: String? = null

    private var dualPane: Boolean = false
    private var curCheckPosition = 0

    private lateinit var adapter: NoteListAdapterRecycler
    private var details = fragmentManager?.findFragmentById(R.id.note_list) as? NotesSetFragment

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

        val recyclerView = MainNotesRecyclerView
        adapter =
            NoteListAdapterRecycler(
                activity!!.applicationContext,
                this
            )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        ViewModelFactory.setApplication(
            this.activity!!.application
        )
        noteListViewModel = ViewModelProvider(
            this,
            ViewModelFactory
        ).get(NoteListViewModel::class.java)
        noteListViewModel.allNoteLists.observe(this, Observer { noteLists ->
            // Update the cached copy of the words in the adapter.
            noteLists?.let { adapter.setList(it) }
        })
        AddNewNotesList.setOnClickListener { view?.let { operationDialog(OperationType.CREATE) } }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        if (dualPane && nonNull(noteListViewModel.allNoteLists.value)) {
            showDetails(curCheckPosition)
        }
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

    private fun operationDialog(operationType: OperationType) {
        operationDialog(operationType, null)
    }

    private fun operationDialog(operationType: OperationType, noteSet: NoteListEntity?) {
        val mDialogView =
            LayoutInflater.from(activity!!).inflate(R.layout.add_new_list_dialog, null)
        val builder = AlertDialog.Builder(activity!!)

        with(builder) {
            setView(mDialogView)
            when (operationType) {
                OperationType.CREATE -> {
                    setTitle("Create new FlashNotes list!")
                }
                OperationType.UPDATE -> {
                    setTitle("Edit existing list info")
                }
            }
        }

        val dialog = builder.create()
        dialog.show()

        val arrayAdapter = ArrayAdapter.createFromResource(
            activity!!,
            R.array.languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialog.baseLangSpinner.adapter = adapter
            dialog.targetLangSpinner.adapter = adapter
        }

        with(dialog) {
            if (operationType == OperationType.UPDATE) {
                fillCurrentSetValues(dialog, arrayAdapter, noteSet!!)
                editText1.setText(noteSet.listName)
            }
            val langs=resources.getStringArray(R.array.languages)
            if(langs.contains<String>(Resources.getSystem().getConfiguration().locales[0].language)) {
                baseLangSpinner.setSelection(
                    langs.indexOf(
                        Resources.getSystem().getConfiguration().locales[0].language
                    )
                )
            }else{
                baseLangSpinner.setSelection(
                    langs.indexOf(
                        "en"
                    )
                )
            }

            cancel_button.setOnClickListener { dialog.dismiss() }
            confirm_button.isEnabled = operationType != OperationType.CREATE
            confirm_button.setOnClickListener {
                when (operationType) {
                    OperationType.CREATE -> {
                        noteListViewModel.insert(
                            NoteListEntity(
                                null,
                                editText1.text.toString(),
                                baseLangSpinner.selectedItem.toString(),
                                targetLangSpinner.selectedItem.toString()
                            )
                        )
                    }

                    OperationType.UPDATE -> {
                        updateSetInfo(noteSet!!, baseLangSpinner, targetLangSpinner, editText1)
                    }
                }
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

    override fun onLongClick(noteListHolder: NoteListHolder, position: Int) {
        val noteSet = noteListViewModel.allNoteLists.value?.get(position)!!
        operationDialog(OperationType.UPDATE, noteSet)
    }

    private fun updateSetInfo(
        noteSet: NoteListEntity,
        baseLangSpinner: Spinner,
        targetLangSpinner: Spinner,
        editText1: EditText
    ) {
        noteSet.baseLanguage = baseLangSpinner.selectedItem.toString()
        noteSet.targetLanguage = targetLangSpinner.selectedItem.toString()
        noteSet.listName = editText1.text.toString()
        noteListViewModel.update(noteSet)
    }

    private fun fillCurrentSetValues(
        dialog: AlertDialog,
        adapter: ArrayAdapter<CharSequence>,
        noteSet: NoteListEntity
    ) {
        dialog.baseLangSpinner.setSelection(adapter.getPosition(noteSet.baseLanguage))
        dialog.targetLangSpinner.setSelection(adapter.getPosition(noteSet.targetLanguage))
    }

    private fun showDetails(index: Int) {
        curCheckPosition = index
        if (noteListViewModel.allNoteLists.value!!.isNotEmpty() &&
            noteListViewModel.allNoteLists.value?.size!! > curCheckPosition
        ) {
            if (dualPane) {
                updateFragmentNextToList(index)
            } else {
                startSetListActivity(index)
            }
        }
    }

    private fun startSetListActivity(index: Int) {
        val intent = Intent().apply {
            setClass(context!!, NoteListDetailsActivity::class.java)
            putExtra("index", noteListViewModel.allNoteLists.value?.get(index)!!.id)
            putExtra(
                "sourceLang",
                noteListViewModel.allNoteLists.value?.get(index)!!.baseLanguage
            )
            putExtra(
                "targetLang",
                noteListViewModel.allNoteLists.value?.get(index)!!.targetLanguage
            )
        }
        startActivity(intent)
    }

    private fun updateFragmentNextToList(index: Int) {
        details =
            NotesSetFragment.newInstance(
                noteListViewModel.allNoteLists.value?.get(index)!!.id!!
            )

        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.note_list, details!!)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            commit()
        }
    }

    private fun deleteSet(
        note: NoteListEntity?,
        adapterPosition: Int
    ) {
        noteListViewModel.delete(note!!)
        adapter.notifyDataSetChanged()
        if (curCheckPosition == adapterPosition && dualPane) {
            fragmentManager?.beginTransaction()?.apply {
                remove(details!!)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                commit()
            }
        }
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
                deleteSet(
                    noteListViewModel.allNoteLists.value?.get(viewHolder.adapterPosition),
                    viewHolder.adapterPosition
                )
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