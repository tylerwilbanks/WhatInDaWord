import com.minutesock.dawordgame.core.data.DatabaseDriverFactory
import com.minutesock.dawordgame.core.data.DbClient
import com.minutesock.dawordgame.core.data.SqlDelightDbClient
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

fun testDbModule(coroutineDispatcher: CoroutineDispatcher): Module {
    return module {
        single { DatabaseDriverFactory() }
        single { get<DatabaseDriverFactory>().createDriver() }
        single { SqlDelightDbClient(get(), coroutineDispatcher) }.bind<DbClient>()
        single { SqlDelightDbClient(get(), coroutineDispatcher) }.bind<DbClient>()
    }
}