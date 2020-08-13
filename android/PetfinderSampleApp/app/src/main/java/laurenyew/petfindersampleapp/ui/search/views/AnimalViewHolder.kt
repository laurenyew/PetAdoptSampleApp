package laurenyew.petfindersampleapp.ui.search.views

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import laurenyew.petfindersampleapp.R

class AnimalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imageView = view.findViewById<ImageView>(R.id.animal_image_view)
    val nameTextView = view.findViewById<TextView>(R.id.name_text_view)
    val basicInfoTextView = view.findViewById<TextView>(R.id.info_text_view)
    val descriptionTextView = view.findViewById<TextView>(R.id.description_text_view)
}