package laurenyew.petadoptsampleapp.ui.features.search.oldviewexample

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import laurenyew.petadoptsampleapp.R

@AndroidEntryPoint
class PetSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_search)
        val toolbar = findViewById<Toolbar>(R.id.pet_search_toolbar)
        toolbar.setTitle(R.string.title_pet_search_page)
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
    }
}