package repository

import model.Drone
import model.Medication

interface DroneRepositoryInterface {
    fun getLoadedMedications(serialNumber: String): List<Medication>

    fun getDroneBlocking(serialNumber: String): Drone

    fun getDrone(serialNumber: String): Drone

    fun saveDroneMedication(serialNumber: String, medicationCode: List<String>)

    fun updateDrone(drone: Drone)

    fun saveDrone(drone: Drone)
}