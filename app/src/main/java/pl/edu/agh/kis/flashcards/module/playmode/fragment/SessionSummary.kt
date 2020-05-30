package pl.edu.agh.kis.flashcards.module.playmode.fragment

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.module.playmode.service.EventModel
import pl.edu.agh.kis.flashcards.module.playmode.service.EventSessionService

//TODO: updateEntity
class SessionSummary(private var eventSessionHandler: EventSessionService) : Fragment() {

    private var time: TextView? = null
    private var words: TextView? = null
    private var rembered: ProgressBar? = null
    private var favourite: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @SuppressLint("StaticFieldLeak")
    fun processData() {
        object : AsyncTask<Any?, Any?, Any?>() {

            var processData = EventModel()

            override fun onPostExecute(result: Any?) {
                var seconds = processData.duration
                time?.setText(transformSecondsToText(seconds!!))

                words?.setText(processData.count.toString())

                rembered?.setProgress(processData.addToRemebered!!)
                rembered?.setMax(processData.count!!);

                favourite?.setProgress(processData.addToFavourite!!)
                favourite?.setMax(processData.count!!);
            }

            override fun doInBackground(vararg params: Any?) {
                processData = eventSessionHandler.processData()!!
            }

        }.execute()
    }

    fun transformSecondsToText(seconds: Long): String {
        var text = ""
        var restSeconds = seconds
        if (seconds / 60 > 0) {
            text = text + (seconds / 60) + " Minutes "
            restSeconds = seconds % 60
        }
        return text + restSeconds.toString() + " Seconds"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var inflate = inflater.inflate(R.layout.fragment_session_summary, container, false)
        time = inflate.findViewById(R.id.time)
        words = inflate.findViewById(R.id.words)
        favourite = inflate.findViewById(R.id.progressBarFavourite)
        rembered = inflate.findViewById(R.id.progressBarRemember)

        return inflate
    }

    companion object {
        @JvmStatic
        fun newInstance(eventSessionHandler: EventSessionService) =
            SessionSummary(eventSessionHandler).apply {
                arguments = Bundle().apply {
                }
            }
    }

}
