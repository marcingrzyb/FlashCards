package pl.edu.agh.kis.flashcards.module.playmode.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.module.playmode.service.EventModel
import pl.edu.agh.kis.flashcards.module.playmode.service.EventSessionHandler


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

//TODO: count add to favourite
//TODO: count remeber
//TODO: implement EventSessionHandler instead Of updating to DB
//TODO: favourite update to DB

class SessionSummary(private var eventSessionHandler: EventSessionHandler) : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var time: TextView? = null
    private var pieChart: PieChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("StaticFieldLeak")
    fun processData() {


        pieChart?.setUsePercentValues(true);
        pieChart?.getDescription()?.setEnabled(true);
        pieChart?.setExtraOffsets(5F,10F,5F,5F);
        pieChart?.setDragDecelerationFrictionCoef(0.9f);
        pieChart?.setTransparentCircleRadius(61f);
        pieChart?.setHoleColor(Color.WHITE);


        val yValues: ArrayList<PieEntry> = ArrayList()
        yValues.add(PieEntry(34f, "Ilala"))
        yValues.add(PieEntry(56f, "Temeke"))
        yValues.add(PieEntry(66f, "Kinondoni"))
        yValues.add(PieEntry(45f, "Kigamboni"))


        val dataSet = PieDataSet(yValues, "Desease Per Regions");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        val pieData = PieData((dataSet));
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);
        pieChart!!.setData(pieData);


        object : AsyncTask<Any?, Any?, Any?>() {
            var processData = EventModel()
            override fun onPostExecute(result: Any?) {
                time?.setText(processData.duration.toString())
            }

            override fun doInBackground(vararg params: Any?) {
                processData = eventSessionHandler.processData()

            }
        }.execute()




        //PieChart Ends Here

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var inflate = inflater.inflate(R.layout.fragment_session_summary, container, false)
        time = inflate.findViewById(R.id.word2)
        pieChart = inflate.findViewById(R.id.piechart_1)

        return inflate
    }

    companion object {
        @JvmStatic
        fun newInstance(eventSessionHandler: EventSessionHandler) =
            SessionSummary(eventSessionHandler).apply {
                arguments = Bundle().apply {
                }
            }
    }
}
