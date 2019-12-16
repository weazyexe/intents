package exe.weazy.intents.repository

import exe.weazy.intents.db.AppDatabase
import exe.weazy.intents.db.CategoriesDao
import exe.weazy.intents.db.IntentsDao
import exe.weazy.intents.entity.CategoryEntity
import exe.weazy.intents.entity.IntentEntity
import exe.weazy.intents.util.App
import javax.inject.Inject

class Repository {

    @Inject
    lateinit var database : AppDatabase

    private val intentsDao : IntentsDao
    private val categoriesDao : CategoriesDao

    init {
        App.getComponent().injectRepository(this)
        intentsDao = database.intentsDao()
        categoriesDao = database.categoriesDao()
    }

    fun getIntentsDataSourceFactory(isMarkedUp : Boolean = false) = intentsDao.getAllByMarkedUp(isMarkedUp)

    fun getCategories() = categoriesDao.getAll()

    fun saveIntents(intents : Collection<IntentEntity>) {
        intents.forEach {
            if (intentsDao.getById(it.id) != null) {
                intentsDao.update(it)
            } else {
                intentsDao.insert(it)
            }
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

    fun putPlaceholderData() {
        val categories = listOf(
            CategoryEntity(0, "Операционные системы"),
            CategoryEntity(1, "Еда"),
            CategoryEntity(2, "Погода"),
            CategoryEntity(3, "Настроение"),
            CategoryEntity(4, "Факт"),
            CategoryEntity(5, "Наглая ложь"),
            CategoryEntity(6, "iOS-разработчик"),
            CategoryEntity(7, "Люди"),
            CategoryEntity(8, "Тело"),
            CategoryEntity(9, "Наука"),
            CategoryEntity(10, "Политика"),
            CategoryEntity(11, "Животные"),
            CategoryEntity(12, "Журналистика"),
            CategoryEntity(13, "История"),
            CategoryEntity(14, "Интернет"),
            CategoryEntity(15, "Предметы")
        )

        val intents = listOf(
            IntentEntity(0, "Андроид", true, listOf(categories[0])),
            IntentEntity(1, "Жульен - это вкусно!", true, listOf(categories[1], categories[4])),
            IntentEntity(2, "Никита", true, listOf(categories[6])),
            IntentEntity(3, "Android лучше iOS, а iOS лучше Android", true, listOf(categories[4], categories[0], categories[5])),
            IntentEntity(4, "Владимир Путин - молодец", true, listOf(categories[10], categories[4], categories[7])),
            IntentEntity(5, "Надпись не имеет смысла", true, listOf(categories[4])),
            IntentEntity(6, "На улице холодно - от того и грустно", true, listOf(categories[2], categories[3])),
            IntentEntity(7, "Spring Framework > Mobile", true, listOf(categories[5])),
            IntentEntity(8, "Было взято интервью", true, listOf(categories[12])),
            IntentEntity(9, "Людвиг XIV", true, listOf(categories[13], categories[7], categories[10])),
            IntentEntity(10, "Костя гений", true, listOf(categories[4])),
            IntentEntity(11, "Длинное предложение для проверки, едет ли верстка или нет? Lorem ipsum, " +
                    "or lipsum as it is sometimes known, is dummy text used in laying out print, graphic or web designs.",
                true, listOf(categories[0])),

            IntentEntity(12, "iOS", true, listOf(categories[0])),
            IntentEntity(13, "Windows", true, listOf(categories[0])),
            IntentEntity(14, "macOS", true, listOf(categories[0])),
            IntentEntity(15, "macOS > Windows", true, listOf(categories[0], categories[4], categories[5])),
            IntentEntity(16, "Мемыыы", true, listOf(categories[14])),
            IntentEntity(17, "Руки в ноги", true, listOf(categories[8])),
            IntentEntity(18, "Тарелка", true, listOf(categories[15])),
            IntentEntity(19, "Надень шарф и шапку, на улице холодно", true, listOf(categories[15], categories[2])),
            IntentEntity(20, "Дизайн этого приложения содержит множество зеленых оттенков", true, listOf(categories[4])),
            IntentEntity(21, "Слишком много фактов", true, listOf(categories[5])),

            IntentEntity(22, "Носорог", false, null),
            IntentEntity(23, "Gravity Falls", false, null),
            IntentEntity(24, "Острые козырьки", false, null),
            IntentEntity(25, "Длинная строка, которая не относится ни к одной категории", false, null),
            IntentEntity(26, "Рандом", false, null),
            IntentEntity(27, "Зебра, лев, жираф, бегемот", false, null),
            IntentEntity(28, "Кот Леопольд", false, null),
            IntentEntity(29, "о май гад данила ты что крейзи", false, null),
            IntentEntity(30, "Франция", false, null),
            IntentEntity(31, "Окулов Станислав Михайлович", false, null),
            IntentEntity(32, "Ъуь", false, null),
            IntentEntity(33, "Криптография", false, null),
            IntentEntity(34, "Конфессия", false, null),
            IntentEntity(35, "Шарф", false, null),
            IntentEntity(36, "Съешь ещё этих мягких французских булок, да выпей чаю!", false, null),
            IntentEntity(37, "The quick brown fox jumps over lazy dog", false, null),
            IntentEntity(38, "Sample text", false, null),
            IntentEntity(39, "Incremental annotation processing requested, but support is disabled because the following processors are not incremental", false, null),
            IntentEntity(40, "Psh", false, null),
            IntentEntity(41, "Кек", false, null),
            IntentEntity(42, "Динамика", false, null),
            IntentEntity(43, "Терминал", false, null),
            IntentEntity(44, "Run tasks", false, null)
        )

        saveIntents(intents)
        saveCategories(categories)
    }
}