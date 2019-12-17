package exe.weazy.intents.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.intents.entity.CategoryEntity
import exe.weazy.intents.entity.IntentEntity
import exe.weazy.intents.repository.Repository

class MainViewModel : ViewModel() {
    private val repository = Repository()

    val categories = MutableLiveData<List<CategoryEntity>>(repository.getCategories())
}