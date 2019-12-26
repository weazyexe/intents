package exe.weazy.intents.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.intents.entity.CategoryEntity
import exe.weazy.intents.network.CategoryResponse
import exe.weazy.intents.repository.Repository
import exe.weazy.intents.state.TestState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TestViewModel : ViewModel() {

    private val repository = Repository()

    var state = MutableLiveData<TestState>(TestState.Input())

    var predicted : MutableLiveData<CategoryEntity> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun predictCategory(content: String) {
        state.postValue(TestState.Loading())

        repository.predictCategories(content)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                if (response.error != null) {
                    handleError(response.error)
                } else {
                    state.postValue(TestState.Done())
                    predicted.postValue(response.result)
                }
            }, { t ->
                val message = t.message

                if (message != null) {
                    handleError(message)
                }
            })
    }

    private fun handleError(message: String) {
        state.postValue(TestState.Error(message))
    }
}