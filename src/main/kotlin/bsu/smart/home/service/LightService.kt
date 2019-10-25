package bsu.smart.home.service

import org.springframework.stereotype.Service
import bsu.smart.home.model.Light
import bsu.smart.home.repository.LightRepository
import javax.persistence.NonUniqueResultException
import javax.transaction.Transactional

@Service
class LightService(
    private val lightRepository: LightRepository
) {
    fun findAllLights() = lightRepository.findAll()

    fun findLightByName(name: String) = lightRepository.findByName(name)

    fun findLightByStatus(status: Boolean) = lightRepository.findByLightStatus(status)

    @Transactional
    fun createLight(light: Light) {
        try {
            lightRepository.findByName(light.name!!)
        } catch(ex: Exception) { throw NonUniqueResultException() }

        lightRepository.save(light)
    }

    @Transactional
    fun updateLightStatus(name: String) {
        val light = lightRepository.findByName(name)
        light.lightStatus = !light.lightStatus
        lightRepository.save(light)
    }

    @Transactional
    fun deleteLight(id: Long) {
        lightRepository.deleteById(id)
    }
}