package controller


import kotlin.test.*
import io.mockk.*
import kotlinx.coroutines.runBlocking
import model.Drone
import model.DroneState
import model.Medication
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import repository.DroneRepository
import service.DroneService


class ApplicationTest {

    private val droneRepository = mockk<DroneRepository>()
    private val droneService = DroneService(droneRepository)


    @Test
    fun `registerDrone should not throw an exception`() = runBlocking {
        // Arrange
        val serialNumber = "serial-1"
        val drone = Drone(serialNumber, "TEST", 100, 1000, DroneState.DELIVERED)
        coEvery { droneRepository.saveDrone(any()) } just runs

        // Act and Assert
        assertDoesNotThrow { droneService.registerDrone(drone) }
        coVerify { droneRepository.saveDrone(drone) }
    }

    @Test
    fun `registerDrone should throw an exception when drone with same serial number already exists`() = runBlocking {
        // Arrange
        val serialNumber = "serial-1"
        val drone = Drone(serialNumber, "TEST", 100, 1000, DroneState.DELIVERED)
        coEvery { droneRepository.saveDrone(any()) } throws IllegalArgumentException()

        // Act and Assert
        assertThrows<IllegalArgumentException> { droneService.registerDrone(drone) }
        coVerify { droneRepository.saveDrone(drone) }
    }


    @Test
    fun `loadDrone should throw IllegalStateException when drone is not in idle state`() = runBlocking {
        // Arrange
        val serialNumber = "serial-1"
        val medicationCode = listOf("med-1", "med-2")
        val drone = Drone(serialNumber, "TEST",   100, 1000, DroneState.DELIVERED)

        every { droneRepository.getDroneBlocking(serialNumber) } returns drone

        // Act and Assert
        assertThrows<IllegalStateException> {
            droneService.loadDrone(serialNumber, medicationCode)
        }

        verify(exactly = 1) { droneRepository.getDroneBlocking(serialNumber) }
        verify(exactly = 0) { droneRepository.getLoadedMedications(serialNumber) }
        verify(exactly = 0) { droneRepository.saveDroneMedication(serialNumber, medicationCode) }
        verify(exactly = 0) { droneRepository.updateDrone(drone) }
    }

    @Test
    fun `loadDrone should throw IllegalStateException when drone battery level is below 25`() = runBlocking {
        // Arrange
        val serialNumber = "serial-1"
        val medicationCode = listOf("med-1", "med-2")
        val drone = Drone(serialNumber, "TEST",   20, 10, DroneState.LOADING)

        every { droneRepository.getDroneBlocking(serialNumber) } returns drone

        // Act and Assert
        assertThrows<IllegalStateException> {
            droneService.loadDrone(serialNumber, medicationCode)
        }

        verify(exactly = 1) { droneRepository.getDroneBlocking(serialNumber) }
        verify(exactly = 0) { droneRepository.getLoadedMedications(serialNumber) }
        verify(exactly = 0) { droneRepository.saveDroneMedication(serialNumber, medicationCode) }
        verify(exactly = 0) { droneRepository.updateDrone(drone) }
    }

    @Test
    fun `loadDrone should throw IllegalArgumentException when medication is already loaded`() = runBlocking {
        // Arrange
        val serialNumber = "serial-1"
        val medicationCode = listOf("JKL-456")
        val loadedMedication = listOf(
            Medication("Medication1", 15, "JKL-456", "https://example.com/image4.png"),
            Medication("Medication2", 25, "15", "https://example.com/image4.png"),
        )
        val drone = Drone(serialNumber, "TEST",   80, 1000, DroneState.LOADING)

        every { droneRepository.getDroneBlocking(serialNumber) } returns drone
        every { droneRepository.getLoadedMedications(serialNumber) } returns loadedMedication

        // Act and Assert
        assertThrows<IllegalArgumentException> {
            droneService.loadDrone(serialNumber, medicationCode)
        }

        verify(exactly = 1) { droneRepository.getDroneBlocking(serialNumber) }
        verify(exactly = 1) { droneRepository.getLoadedMedications(serialNumber) }
        verify(exactly = 0) { droneRepository.saveDroneMedication(serialNumber, medicationCode) }
        verify(exactly = 0) { droneRepository.updateDrone(drone) }
    }
}
