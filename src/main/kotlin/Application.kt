import config.DatabaseFactory
import controller.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import repository.DroneRepository
import service.DroneService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
    .start(wait = true)
}

fun Application.module() {
    val executor = Executors.newSingleThreadScheduledExecutor()

    val koinModules: List<Module> = listOf(module {
        single { DroneRepository() }
        single { DroneService(get()) }
    })

    startKoin {
        modules(koinModules)
    }


    configureRouting()
    DatabaseFactory.init()


    val droneService: DroneService by inject()
    executor.scheduleAtFixedRate({
        droneService.checkDroneBattery()
    }, 0, 5, TimeUnit.SECONDS)

    environment.monitor.subscribe(ApplicationStopPreparing) {
        stopKoin()
    }
}

