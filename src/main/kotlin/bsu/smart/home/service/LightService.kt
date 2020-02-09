package bsu.smart.home.service

import bsu.smart.home.model.Light
import bsu.smart.home.repository.LightRepository
import org.springframework.stereotype.Service
import javax.persistence.NonUniqueResultException
import javax.transaction.Transactional

@Service
class LightService(
        private var lightRepository: LightRepository
) {
    fun findAllLights() = lightRepository.findAll()

    fun findLightById(id: Long) = lightRepository.findById(id).get()

    fun findLightByStatus(status: Boolean) = lightRepository.findByLightStatus(status)

    @Transactional
    fun createLight(light: Light) {
        light.name?.let {
            if (checkNameUnique(it)) lightRepository.save(light)
            else throw NonUniqueResultException()
        }
    }

    @Transactional
    fun updateLightStatus(id: Long) {
        val updateLight = lightRepository.findById(id).get()
        updateLight.lightStatus = !updateLight.lightStatus
        lightRepository.save(updateLight)
    }

    @Transactional
    fun updateLightName(id: Long, light: Light) {
        val updateLight = lightRepository.findById(id).get()
        updateLight.name = light.name
        lightRepository.save(updateLight)
    }

    @Transactional
    fun deleteLight(id: Long) {
        lightRepository.deleteById(id)
    }

    fun checkNameUnique(lightName: String) = !lightRepository.existsByName(lightName)
}