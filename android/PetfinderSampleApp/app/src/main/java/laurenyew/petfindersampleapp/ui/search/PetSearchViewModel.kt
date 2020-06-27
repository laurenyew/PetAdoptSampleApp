package laurenyew.petfindersampleapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import laurenyew.petfindersampleapp.repository.PetSearchRepository
import laurenyew.petfindersampleapp.repository.models.AnimalModel
import laurenyew.petfindersampleapp.repository.responses.SearchPetsRepoResponse
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
    private val _isError = MutableLiveData<Boolean>().apply {
        value = false
    }
    val animals: LiveData<List<AnimalModel>> = _animals
    val isLoading: LiveData<Boolean> = _isLoading
    val isError: LiveData<Boolean> = _isError

    fun searchAnimals(location: String?) {
        location?.let {
            _isLoading.postValue(true)
            viewModelScope.launch {
                when(val response = repository.getNearbyDogs(location)){
                    is SearchPetsRepoResponse.Success ->{
                        _animals.postValue(response.animals)
                        _isError.postValue(false)
                    }
                    else ->{
                        _animals.postValue(null)
                        _isError.postValue(true)
                    }
                }
                _isLoading.postValue(false)
            }
        }
    }
}