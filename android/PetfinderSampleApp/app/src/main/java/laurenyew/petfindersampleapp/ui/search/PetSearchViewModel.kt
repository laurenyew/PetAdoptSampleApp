package laurenyew.petfindersampleapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import laurenyew.petfindersampleapp.repository.models.AnimalModel

class PetSearchViewModel : ViewModel() {
    private val _animals = MutableLiveData<List<AnimalModel>>().apply {
        value = emptyList()
    }
    val animals: LiveData<List<AnimalModel>> = _animals
}