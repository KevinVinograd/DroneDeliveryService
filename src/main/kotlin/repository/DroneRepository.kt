package repository

import model.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class DroneRepository : DroneRepositoryInterface{

    override fun saveDrone(drone: Drone) {
         transaction {
             Drones.insert {
                it[serialNumber] = drone.serialNumber
                it[model] = drone.model
                it[weightLimit] = drone.weightLimit
                it[batteryCapacity] = drone.batteryCapacity
                it[state] = drone.state.toString()
            }
        }
    }

    override fun updateDrone(drone: Drone) {
        transaction {
            Drones.update({ Drones.serialNumber eq drone.serialNumber }) {
                it[model] = drone.model
                it[weightLimit] = drone.weightLimit
                it[batteryCapacity] = drone.batteryCapacity
                it[state] = drone.state.toString()
            }
        }

    }

    override fun saveDroneMedication(serialNumber: String, medicationCode: List<String>) {
        transaction {
            medicationCode.map { code ->
                DronesMedications.insert {
                    it[DronesMedications.serialNumber] = serialNumber
                    it[DronesMedications.code] = code
                }
            }
        }
    }

    override fun getLoadedMedications(serialNumber: String): List<Medication> {
        return transaction {
            (DronesMedications innerJoin Medications)
                .select { DronesMedications.serialNumber eq serialNumber }
                .map { toMedication(it) }
        }
    }


    override fun getDroneBlocking(serialNumber: String): Drone {
        return transaction { Drones.select { Drones.serialNumber eq serialNumber }.forUpdate().map { toDrone(it) }.firstOrNull()}
            ?: throw IllegalArgumentException("Drone with serial number $serialNumber does not exist")
    }

    override fun getDrone(serialNumber: String): Drone {
        return transaction {Drones.select { Drones.serialNumber eq serialNumber }.map { toDrone(it) }.firstOrNull()}
            ?: throw IllegalArgumentException("Drone with serial number $serialNumber does not exist")
    }

}