package model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime
import java.time.LocalDateTime


object Drones : Table() {
    val serialNumber: Column<String> = varchar("serial_number", 100).primaryKey()
    val model: Column<String> = varchar("model", 100)
    val weightLimit: Column<Int> = integer("weight_limit")
    var batteryCapacity: Column<Int> = integer("battery_capacity")
    var state: Column<String> = varchar("state", 100)
    val createdAt: Column<DateTime> = datetime("created_at").default( DateTime.now())
    val updatedAt: Column<DateTime?> = datetime("updated_at").default( DateTime.now()).nullable()
}

object Medications : Table() {
    val name: Column<String> = varchar("name", 100).primaryKey()
    val weight: Column<Int> = integer("weight")
    val code: Column<String> = varchar("code", 100)
    val image: Column<String> = varchar("image", 100)
    val createdAt: Column<DateTime> = datetime("created_at").default( DateTime.now())
    val updatedAt: Column<DateTime?> = datetime("updated_at").default( DateTime.now()).nullable()
}

object DronesMedications : Table() {
    val serialNumber: Column<String> = varchar("serial_number", 100).references(Drones.serialNumber).primaryKey()
    val code: Column<String> = varchar("code", 100).references(Medications.code).primaryKey()
    val createdAt: Column<DateTime> = datetime("created_at").default( DateTime.now())
    val updatedAt: Column<DateTime?> = datetime("updated_at").default( DateTime.now()).nullable()
}

data class BatteryLevelEvent(
    val timestamp: LocalDateTime,
    val droneSerialNumber: String,
    val batteryLevel: Int
)

@Serializable
data class Drone(
    val serialNumber: String,
    val model: String,
    val weightLimit: Int,
    var batteryCapacity: Int,
    var state: DroneState,
    @Contextual
    val createdAt: DateTime = DateTime.now(),
    @Contextual
    val updatedAt: DateTime? = DateTime.now()
    )

@Serializable
enum class DroneState {
    IDLE,
    LOADING,
    LOADED,
    DELIVERING,
    DELIVERED,
    RETURNING
}

@Serializable
data class Medication(
    val name: String,
    val weight: Int,
    val code: String,
    val image: String,
    @Contextual
    val createdAt: DateTime = DateTime.now(),
    @Contextual
    val updatedAt: DateTime? = DateTime.now()

)

@Serializable
data class DroneMedication(
    val serialNumber: String,
    val code: String,
    @Contextual
    val createdAt: DateTime = DateTime.now(),
    @Contextual
    val updatedAt: DateTime? = DateTime.now()
)

fun toDrone(it: ResultRow) = Drone(
    serialNumber = it[Drones.serialNumber],
    model = it[Drones.model],
    weightLimit = it[Drones.weightLimit],
    batteryCapacity = it[Drones.batteryCapacity],
    state = DroneState.valueOf(it[Drones.state])
)

fun toMedication(it: ResultRow) = Medication(
    name = it[Medications.name],
    weight = it[Medications.weight],
    code = it[Medications.code],
    image = it[Medications.image]
)

private fun toDronesMedications(it: ResultRow) = DroneMedication (
    serialNumber = it[DronesMedications.serialNumber],
    code = it[DronesMedications.code]
)