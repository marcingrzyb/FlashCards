package pl.edu.agh.kis.flashcards.module.playmode.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.NoteListDataBase
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SessionSummary(sessionId: Long) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var sessionId: Long = sessionId

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
        var inflate = inflater.inflate(R.layout.fragment_session_summary, container, false)
        val time: TextView = inflate.findViewById(R.id.word2)
        var toSeconds = 0L
        //change to last index
        Executors.newSingleThreadExecutor().execute(Runnable {
            var sessionDao = NoteListDataBase.getDatabase(this.activity!!.application).sessionDao()
            var load = sessionDao.load(sessionId.toInt())

            var minusMillis = Instant.now().minusMillis(load.startTime!!)
            toSeconds = TimeUnit.MILLISECONDS.toSeconds(minusMillis.toEpochMilli());
        })

        time.r

        time.setText(toSeconds.toString())
        return inflate
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SessionSummary.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SessionSummary(0).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
