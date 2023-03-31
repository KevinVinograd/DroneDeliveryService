package service

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import repository.DroneRepository
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

class DroneService constructor(private val droneRepository: DroneRepository){

    private val droneLocks = ConcurrentHashMap<String, Mutex>()

    fun registerDrone(drone: Drone) {
        try {
            droneRepository.saveDrone(drone)
        } catch (e: Exception) {
            throw IllegalArgumentException("Drone with serial number ${drone.serialNumber} already exists")
        }
    }

    fun checkDroneBattery() {
        try {
            val drones = getAvailableDrones()
            drones.forEach { drone ->
                if (drone.state != DroneState.IDLE) {
                    val event = BatteryLevelEvent(LocalDateTime.now(), drone.serialNumber, drone.batteryCapacity)
                    println(event)
                }
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    suspend fun loadDrone(serialNumber: String, medicationCode: List<String>) {
        val droneLock = droneLocks.getOrPut(serialNumber) { Mutex() }
        droneLock.withLock {
            val drone = droneRepository.getDroneBlocking(serialNumber)
            if (drone.state != DroneState.IDLE && drone.state != DroneState.LOADING) {
                throw IllegalStateException("Drone is not in idle state")
            }
            if (drone.batteryCapacity < 25) {
                throw IllegalStateException("Drone battery level is below 25%")
            }
            val loadedMedication = droneRepository.getLoadedMedications(serialNumber)
            loadedMedication.forEach {
                if (it.code in medicationCode) {
                    throw IllegalArgumentException("Medication ${it.code} is already loaded")
                }
            }
            val totalWeight = loadedMedication.sumOf { it.weight }
            if (totalWeight > drone.weightLimit) {
                throw IllegalArgumentException("Total weight of medications exceeds drone weight limit")
            }
            droneRepository.saveDroneMedication(serialNumber, medicationCode)
            droneRepository.updateDrone(drone)
        }
    }

    fun getLoadedMedications(serialNumber: String): List<Medication> {
        return droneRepository.getLoadedMedications(serialNumber)
    }


    fun getAvailableDrones(): List<Drone> {
        return transaction{
            Drones.selectAll().map { toDrone(it) }
        }
    }

    fun getBatteryLevel(serialNumber: String): Int {
        val drone = droneRepository.getDrone(serialNumber)
        return drone.batteryCapacity
    }

}