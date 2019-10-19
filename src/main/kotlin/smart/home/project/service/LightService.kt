package smart.home.project.service

import org.springframework.stereotype.Service
import smart.home.project.model.Light
import smart.home.project.repository.LightRepository
import javax.persistence.NonUniqueResultException
import javax.transaction.Transactional

@Service
class LightService(private val lightRepository: LightRepository) {
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
    fun updateLightStatus(id: Long) {
        val light = lightRepository.findById(id)
        light.get().lightStatus = !light.get().lightStatus
        lightRepository.save(light)
    }

    @Transactional
    fun deleteLight(id: Long) {
        lightRepository.deleteById(id)
    }
}