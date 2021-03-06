package exe.weazy.intents.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.intents.entity.CategoryEntity
import exe.weazy.intents.entity.IntentEntity
import exe.weazy.intents.repository.Repository
import exe.weazy.intents.state.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {

    var state = MutableLiveData<State>(State.Loading())

    private val repository = Repository()

    lateinit var categories : MutableLiveData<List<CategoryEntity>>

    @SuppressLint("CheckResult")
    fun loadCategories() {
        categories = MutableLiveData()

        repository.getCategoriesObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                if (response.error == null) {
                    categories.postValue(response.result)
                    state.postValue(State.Loaded())
                } else {
                    handleError(response.error)
                }
            }, { t->
                val message = t.message
                if (message != null) {
                    handleError(message)
                }
            })
    }

    private fun handleError(message: String) {
        val size = categories.value?.size ?: 0

        if (size != 0) {
            state.postValue(State.Loaded(message))
        } else {
            state.postValue(State.Error(message))
        }
    }
}