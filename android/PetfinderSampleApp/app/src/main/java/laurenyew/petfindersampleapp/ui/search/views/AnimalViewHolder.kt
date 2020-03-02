package laurenyew.petfindersampleapp.ui.search.views

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import laurenyew.petfindersampleapp.R

class AnimalViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val nameTextView = view.findViewById<TextView>(R.id.name_text_view)
}