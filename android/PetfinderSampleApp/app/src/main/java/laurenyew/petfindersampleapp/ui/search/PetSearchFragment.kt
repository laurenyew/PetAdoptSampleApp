package laurenyew.petfindersampleapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_pet_search.*
import laurenyew.petfindersampleapp.R
import laurenyew.petfindersampleapp.repository.models.AnimalModel
import laurenyew.petfindersampleapp.ui.search.views.PetSearchAnimalRecyclerViewAdapter

class PetSearchFragment : Fragment() {
    private var adapter: PetSearchAnimalRecyclerViewAdapter? = null

    private lateinit var petSearchViewModel: PetSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        petSearchViewModel =
            ViewModelProvider(this).get(PetSearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_pet_search, container, false)

        setupAnimalListView()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        petSearchViewModel.animals.observe(viewLifecycleOwner, Observer {
            loadAnimalSearchResults(it)
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
            progress_bar.visibility = View.GONE
        }
    }
}