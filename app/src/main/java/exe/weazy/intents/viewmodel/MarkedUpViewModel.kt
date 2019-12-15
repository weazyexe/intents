package exe.weazy.intents.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import exe.weazy.intents.entity.IntentEntity
import exe.weazy.intents.repository.Repository
import exe.weazy.intents.state.State
import exe.weazy.intents.util.DEFAULT_PAGE_SIZE
import exe.weazy.intents.util.DEFAULT_PREFETCH_DISTANCE

class MarkedUpViewModel : ViewModel() {

    var page = 0

    var state = MutableLiveData<State>()

    private val repository = Repository()

    val intents by lazy {
        val dataSourceFactory = repository.getIntentsDataSourceFactory(true)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(DEFAULT_PAGE_SIZE)
            .setPrefetchDistance(DEFAULT_PREFETCH_DISTANCE)
            .setInitialLoadSizeHint(DEFAULT_PAGE_SIZE)
            .build()

        val boundaryCallback = object : PagedList.BoundaryCallback<IntentEntity>() {
            override fun onItemAtEndLoaded(itemAtEnd: IntentEntity) {
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
        repository.nukeIntents(true)
        state.postValue(State.Loading())
        page = 0
        loadIntents()
    }

    fun loadIntents() {
        // FIXME: waiting for API
    }
}