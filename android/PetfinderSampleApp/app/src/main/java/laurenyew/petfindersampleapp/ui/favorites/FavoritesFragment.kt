package laurenyew.petfindersampleapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import laurenyew.petfindersampleapp.R
import javax.inject.Inject

class FavoritesFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoritesViewModel =
            ViewModelProvider(this, viewModelFactory)[FavoritesViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)
        val textView: TextView = root.findViewById(R.id.text_favorites)
        favoritesViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}