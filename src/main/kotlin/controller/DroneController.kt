package controller

import service.DroneService
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import model.Drone
import org.koin.ktor.ext.inject
import utils.DateTimeSerializer

fun Application.configureRouting () {

    install(ContentNegotiation) {
        json(Json {
            serializersModule = SerializersModule {
                contextual(DateTimeSerializer)
            }
        })
    }

    val droneService: DroneService by inject()


    routing {

            get("/drones/{serialNumber}/battery") {
                val serialNumber = call.parameters["serialNumber"] ?: throw IllegalArgumentException("Missing serial number parameter")
                val batteryLevel = droneService.getBatteryLevel(serialNumber)
                call.respond(batteryLevel)
            }

            get("/drones/available") {
                val availableDrones = droneService.getAvailableDrones()
                call.respond(availableDrones)
            }

            post("/drones") {
                val drone = call.receive<Drone>()
                droneService.registerDrone(drone)
                call.respond(HttpStatusCode.OK)
            }

            post("/drones/{serialNumber}/medications") {
                val serialNumber = call.parameters["serialNumber"] ?: throw IllegalArgumentException("Missing serial number parameter")
                val medications = call.receive<List<String>>()
                droneService.loadDrone(serialNumber, medications)
                call.respond(HttpStatusCode.OK)
            }

            get("/drones/{serialNumber}/medications") {
                val serialNumber = call.parameters["serialNumber"] ?: throw IllegalArgumentException("Missing serial number parameter")
                val loadedMedications = droneService.getLoadedMedications(serialNumber)
                call.respond(loadedMedications)
            }
        }
}