package laurenyew.petadoptsampleapp.ui.features.search.oldviewexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.databinding.PetSearchBinding
import laurenyew.petadoptsampleapp.ui.features.search.PetSearchViewModel

@AndroidEntryPoint
class PetSearchFragment : Fragment() {
    private var _binding: PetSearchBinding? = null
    private val binding get() = _binding!!
    private val petSearchViewModel: PetSearchViewModel by viewModels()
    private val adapter = PetSearchListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PetSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.animalSearchResultsRecyclerView.adapter = adapter
        binding.petLocationSearchView.apply {
            setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    petSearchViewModel.searchAnimals()
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    petSearchViewModel.location.value = p0 ?: ""
                    return true
                }

            })
            setOnSearchClickListener {
                petSearchViewModel.searchAnimals()
            }
            isIconifiedByDefault = false
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                petSearchViewModel.animals
                    .onEach {
                        adapter.setAnimals(it)
                    }.launchIn(this)
                petSearchViewModel.isLoading
                    .onEach { isLoading ->
                        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    }.launchIn(this)
                petSearchViewModel.isError
                    .onEach { isError ->
                        binding.emptySearchResults.visibility =
                            if (isError) View.VISIBLE else View.GONE
                    }.launchIn(this)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}