package laurenyew.petfindersampleapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import laurenyew.petfindersampleapp.repository.PetSearchRepository
import laurenyew.petfindersampleapp.repository.models.AnimalModel
import javax.inject.Inject

class PetSearchViewModel @Inject constructor(
    private val repository: PetSearchRepository
) : ViewModel() {
    private val _animals = MutableLiveData<List<AnimalModel>>().apply {
        value = emptyList()
    }
    private val _isLoading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val animals: LiveData<List<AnimalModel>> = _animals
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchAnimals(location: String?) {
        location?.let {
            _isLoading.postValue(true)
            viewModelScope.launch {
                _animals.postValue(repository.getNearbyDogs(location))
                _isLoading.postValue(false)
            }
        }
    }
}