package pl.edu.agh.kis.flashcards.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import pl.edu.agh.kis.flashcards.NoteListDetailsActivity
import pl.edu.agh.kis.flashcards.R
import pl.edu.agh.kis.flashcards.database.entities.NoteEntity
import java.io.Serializable

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FlashCard.newInstance] factory method to
 * create an instance of this fragment.
 */
class FlashCard : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val entity: Serializable by lazy {
        arguments?.getSerializable("note")!!
    }

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
        val inflate = inflater.inflate(R.layout.fragment_flash_card, container, false)
        var value: NoteEntity = entity as NoteEntity;
        val txtView: TextView = inflate.findViewById(R.id.word)
        val hiddenContent: TextView = inflate.findViewById(R.id.word1)
        val favourite: ImageButton = inflate.findViewById(R.id.favouriteButton)

        txtView.setText(value.word)
        hiddenContent.setText("click")

        var heart = R.drawable.heart
        favourite.setImageResource(heart)

        hiddenContent.setOnClickListener {
            hiddenContent.setText(value.translatedWord)
        }

        return inflate
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FlashCard.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FlashCard().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
