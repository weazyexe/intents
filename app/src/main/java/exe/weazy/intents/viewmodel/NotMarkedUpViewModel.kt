package exe.weazy.intents.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import exe.weazy.intents.entity.IntentEntity
import exe.weazy.intents.repository.Repository
import exe.weazy.intents.state.State
import exe.weazy.intents.util.DEFAULT_PAGE_SIZE
import exe.weazy.intents.util.DEFAULT_PREFETCH_DISTANCE
import exe.weazy.intents.util.NO_CONTENT_TAG
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NotMarkedUpViewModel : ViewModel() {

    var page = 1

    var state = MutableLiveData<State>()

    private val repository = Repository()

    val intents by lazy {
        val dataSourceFactory = repository.getIntentsDataSourceFactory(false)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(DEFAULT_PAGE_SIZE)
            .setPrefetchDistance(DEFAULT_PREFETCH_DISTANCE)
            .setInitialLoadSizeHint(DEFAULT_PAGE_SIZE)
            .build()

        val boundaryCallback = object : PagedList.BoundaryCallback<IntentEntity>() {
            override fun onItemAtEndLoaded(itemAtEnd: IntentEntity) {
                page++
                loadIntents()
            }

            override fun onZeroItemsLoaded() {
                loadIntents()
            }
        }

        LivePagedListBuilder(dataSourceFactory, config)
            .setBoundaryCallback(boundaryCallback)
            .build()
    }


    init {
        state.postValue(State.Loading())
    }


    fun refresh() {
        repository.nukeIntents(false)
        state.postValue(State.Loading())
        page = 1
        loadIntents()
    }



    @SuppressLint("CheckResult")
    fun loadIntents() {
        repository.getIntentsObservable(isMarkedUp = false, page = page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({response ->
                if (response.error == null) {
                    repository.saveIntents(response.result)
                    intents.value?.dataSource?.invalidate()
                    state.postValue(State.Loaded(if (response.result.isEmpty()) NO_CONTENT_TAG else null))
                } else {
                    handleError(response.error)
                }
            }, { t ->
                val message = t.message

                if (message != null) {
                    handleError(message)
                }
            })
    }

    @SuppressLint("CheckResult")
    fun saveIntent(intent : IntentEntity) {
        repository.saveIntent(intent, network = true)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ response ->
                if (response.error == null) {
                    state.postValue(State.Loaded("Запись обновлена"))
                } else {
                    handleError(response.error)
                }
            }, { t ->
                val message = t.message

                if (message != null) {
                    handleError(message)
                }
            })
    }

    private fun handleError(message: String) {
        val size = intents.value?.size ?: 0

        if (size > 0) {
            state.postValue(State.Loaded(message))
        } else {
            state.postValue(State.Error(message))
        }
    }
}