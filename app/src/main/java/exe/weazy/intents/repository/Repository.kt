package exe.weazy.intents.repository

import exe.weazy.intents.db.AppDatabase
import exe.weazy.intents.db.CategoriesDao
import exe.weazy.intents.db.IntentsDao
import exe.weazy.intents.entity.CategoryEntity
import exe.weazy.intents.entity.IntentEntity
import exe.weazy.intents.network.CategoryResponse
import exe.weazy.intents.network.IntentResponse
import exe.weazy.intents.network.NetworkService
import exe.weazy.intents.util.App
import exe.weazy.intents.util.toCapitalizeString
import io.reactivex.Observable
import retrofit2.Retrofit
import javax.inject.Inject

class Repository {

    @Inject
    lateinit var database : AppDatabase

    @Inject
    lateinit var retrofit : Retrofit

    private val intentsDao : IntentsDao
    private val categoriesDao : CategoriesDao
    private val service : NetworkService

    init {
        App.getComponent().injectRepository(this)
        intentsDao = database.intentsDao()
        categoriesDao = database.categoriesDao()
        service = retrofit.create(NetworkService::class.java)
    }

    fun getIntentsDataSourceFactory(isMarkedUp : Boolean = false) = intentsDao.getAllByMarkedUp(isMarkedUp)

    fun getIntentsObservable(isMarkedUp: Boolean = false, search: String = "", page: Int = 1) = service.getIntents(isMarkedUp.toCapitalizeString(), search, page)

    fun getCategoriesObservable() = service.getCategories()

    fun predictCategories(content: String) : Observable<CategoryResponse> = service.getPredictedCategory(content)

    fun getCategories() = categoriesDao.getAll()

    fun saveIntent(intent: IntentEntity, network : Boolean = false) : Observable<IntentResponse>? {
        if (intentsDao.getById(intent.id) != null) {
            intentsDao.update(intent)
        } else {
            intentsDao.insert(intent)
        }

        return if (network) {
            val categories = intent.categories

            if (categories == null) {
                //return service.updateIntent(id = intent.id, intent = IntentPutBody(content = intent.content))
                return service.updateIntent(id = intent.id, content = intent.content, ids = listOf())
            } else {
                val ids = categories.map { it.id }
                //return service.updateIntent(id = intent.id, intent = IntentPutBody(ids, intent.content))
                return service.updateIntent(id = intent.id, content = intent.content, ids = ids)
            }
        } else {
            null
        }
    }

    fun saveIntents(intents : Collection<IntentEntity>) {
        intents.forEach {
            saveIntent(it)
        }
    }

    fun saveCategories(categories : Collection<CategoryEntity>) {
        categories.forEach {
            if (categoriesDao.getById(it.id) != null) {
                categoriesDao.update(it)
            } else {
                categoriesDao.insert(it)
            }
        }
    }

    fun nukeIntents(isMarkedUp: Boolean) {
        intentsDao.nukeTable(isMarkedUp)
    }
}