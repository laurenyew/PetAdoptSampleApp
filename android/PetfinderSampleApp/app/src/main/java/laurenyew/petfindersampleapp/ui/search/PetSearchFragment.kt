package laurenyew.petfindersampleapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_pet_search.*
import laurenyew.petfindersampleapp.R
import laurenyew.petfindersampleapp.repository.models.AnimalModel
import laurenyew.petfindersampleapp.ui.search.views.PetSearchAnimalRecyclerViewAdapter
import javax.inject.Inject

class PetSearchFragment : DaggerFragment() {
    private var adapter: PetSearchAnimalRecyclerViewAdapter? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var petSearchViewModel: PetSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        petSearchViewModel =
            ViewModelProvider(this, viewModelFactory)[PetSearchViewModel::class.java]
        return inflater.inflate(R.layout.fragment_pet_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchView()
        setupAnimalListView()
        petSearchViewModel.animals.observe(viewLifecycleOwner, Observer { results ->
            loadAnimalSearchResults(results)
        })
        petSearchViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progress_bar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        })
    }

    private fun setupSearchView() {
        pet_location_search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                petSearchViewModel.searchAnimals(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun setupAnimalListView() {
        animal_search_results_recycler_view.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            //Add separator lines between rows
            val dividerItemDecoration =
                DividerItemDecoration(context, linearLayoutManager.orientation)
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun loadAnimalSearchResults(results: List<AnimalModel>?) {
        if (isAdded && isVisible) {
            if (adapter == null) {
                adapter = PetSearchAnimalRecyclerViewAdapter()
                animal_search_results_recycler_view.adapter = adapter
            }
            adapter?.updateData(results)
            if (results == null || results.isEmpty()) {
                empty_search_results.visibility = View.VISIBLE
            } else {
                empty_search_results.visibility = View.GONE
            }
        }
    }
}