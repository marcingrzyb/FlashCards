package pl.edu.agh.kis.flashcards.module.playmode.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.NoteListDataBase
import java.time.Instant
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
    private var time: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    @SuppressLint("StaticFieldLeak")
    fun processData() {
        object : AsyncTask<Any?, Any?, Any?>() {
            var toSeconds = 0L
            override fun onPostExecute(result: Any?) {
                time?.setText(toSeconds.toString())
            }

            override fun doInBackground(vararg params: Any?) {
                var sessionDao = NoteListDataBase.getDatabase(params.get(0) as Context).sessionDao()
                var load = sessionDao.load(sessionId.toInt())

                var minusMillis = Instant.now().minusMillis(load.startTime!!)
                toSeconds = TimeUnit.MILLISECONDS.toSeconds(minusMillis.toEpochMilli());
            }
        }.execute(this.activity!!.application)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var inflate = inflater.inflate(R.layout.fragment_session_summary, container, false)
        time = inflate.findViewById(R.id.word2)

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
