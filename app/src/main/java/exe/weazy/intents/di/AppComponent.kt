package exe.weazy.intents.di

import dagger.Component
import exe.weazy.intents.repository.Repository

@Component(modules = [DatabaseModule::class, NetworkModule::class])
interface AppComponent {
    fun injectRepository(repository: Repository)
}