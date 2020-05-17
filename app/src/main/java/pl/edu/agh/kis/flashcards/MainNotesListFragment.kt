package pl.edu.agh.kis.flashcards

import android.app.AlertDialog
import android.app.Application
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.add_new_list_dialog.*
import kotlinx.android.synthetic.main.add_new_list_dialog.view.*
import kotlinx.android.synthetic.main.add_new_list_dialog.view.cancel_button
import kotlinx.android.synthetic.main.fragment_main_notes_list.*
import pl.edu.agh.kis.flashcards.database.entities.NoteListEntity
import pl.edu.agh.kis.flashcards.recyclerView.ListAdapter
import pl.edu.agh.kis.flashcards.viewmodels.NoteListViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var noteListViewModel: NoteListViewModel


class MainNotesListFragment : Fragment() {
    // ????
    private var param1: String? = null
    private var param2: String? = null

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
        val recyclerView=MainNotesRecyclerView
        val adapter=ListAdapter(activity!!.applicationContext)
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(activity!!.applicationContext)
        viewModelFactory.setApplication(this.activity!!.application)
        noteListViewModel = ViewModelProvider(this,viewModelFactory).get(NoteListViewModel::class.java)
        noteListViewModel.allNoteLists.observe(this, Observer { noteLists ->
            // Update the cached copy of the words in the adapter.
            noteLists?.let { adapter.setList(it) }
        })
        AddNewNotesList.setOnClickListener{ view?.let { it1 -> basicAlert(it1) } }
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

    fun basicAlert(view: View){
        val mDialogView = LayoutInflater.from(activity!!).inflate(R.layout.add_new_list_dialog, null)
        val builder = AlertDialog.Builder(activity!!)

        with(builder){
            setView(mDialogView)
            setTitle("Create new FlashNotes list!")
        }

        var dialog=builder.create()
        dialog.show()

        ArrayAdapter.createFromResource(
            activity!!,
            R.array.languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialog.langSpinner.adapter = adapter
        }


        with(dialog) {
            cancel_button.setOnClickListener { dialog.dismiss() }
            confirm_button.isEnabled = false
            confirm_button.setOnClickListener {
                noteListViewModel.insert(NoteListEntity(null, editText1.text.toString(),langSpinner.selectedItem.toString()))
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
                    dialog.confirm_button.isEnabled = s.trim().length != 0
                }
            })
        }
    }
}


object viewModelFactory : ViewModelProvider.Factory {
    lateinit var app : Application

    fun setApplication(application: Application) {
        app = application
    }
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteListViewModel(app) as T }}