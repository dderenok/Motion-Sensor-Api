package bsu.smart.home.service

import bsu.smart.home.config.exception.LightNotFoundException
import bsu.smart.home.config.exception.LightNotUniqueException
import bsu.smart.home.model.Light
import bsu.smart.home.repository.LightRepository
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.UUID.randomUUID
import javax.transaction.Transactional

@Service
class LightService(
        private var lightRepository: LightRepository
) {
    fun findAllLights() = lightRepository.findAll()

    fun findLight(guid: UUID) =
            lightRepository.findByGuid(guid) ?:
            throw LightNotFoundException(lightNotFoundMessage("guid", guid.toString()))

    fun findLightByName(name: String) =
            lightRepository.findByName(name) ?:
            throw LightNotFoundException(lightNotFoundMessage("name", name))

    @Transactional
    fun createLight(light: Light) = light.name?.let {
        if (checkNameUnique(it)) lightRepository.save(
                light.apply { guid = randomUUID() }
        )
        else throw LightNotUniqueException()
    }

    @Transactional
    fun updateStatus(guid: UUID) = lightRepository.findByGuid(guid)?.let {
        lightRepository.save(it.apply {
            status = !status
        })
    }

    // TODO: d.derenok
    //      Refactor using not-nullable operator for light name
    @Transactional
    fun updateLight(guid: UUID, light: Light) {
        lightRepository.findByGuid(guid)?.let {
            if (!checkNameUnique(light.name!!)) throw LightNotUniqueException()

            lightRepository.save(it.apply {
                name = light.name
                status = light.status
            })
        } ?: throw LightNotFoundException(lightNotFoundMessage("guid", guid.toString()))
    }

    @Transactional
    fun deleteLight(guid: UUID) = lightRepository.findByGuid(guid)?.let {
        lightRepository.deleteByGuid(guid)
    } ?: throw LightNotFoundException(lightNotFoundMessage("guid", guid.toString()))

    fun checkNameUnique(lightName: String) = !lightRepository.existsByName(lightName)

    companion object {
        private fun lightNotFoundMessage(element: String, value: String) = "Light with $element '$value' not found"
    }
}