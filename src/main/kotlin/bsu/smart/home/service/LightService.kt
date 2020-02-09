package bsu.smart.home.service

import bsu.smart.home.model.Light
import bsu.smart.home.repository.LightRepository
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.UUID.randomUUID
import javax.persistence.NonUniqueResultException
import javax.transaction.Transactional

@Service
class LightService(
        private var lightRepository: LightRepository
) {
    fun findAllLights() = lightRepository.findAll()

    fun findLight(guid: UUID) = lightRepository.findByGuid(guid)

    fun findLightByName(name: String) = lightRepository.findByName(name)

    @Transactional
    fun createLight(light: Light) = light.name?.let {
        if (checkNameUnique(it)) lightRepository.save(
                light.apply { guid = randomUUID() }
        )
        else throw NonUniqueResultException()
    }

    @Transactional
    fun updateStatus(guid: UUID) = lightRepository.findByGuid(guid)?.let {
        lightRepository.save(it.apply {
            status = !status
        })
    }

    @Transactional
    fun updateLight(guid: UUID, light: Light) = lightRepository.findByGuid(guid)?.let {
        lightRepository.save(it.apply {
            name = light.name
            status = light.status
        })
    }

    @Transactional
    fun deleteLight(guid: UUID) = lightRepository.deleteByGuid(guid)

    fun checkNameUnique(lightName: String) = !lightRepository.existsByName(lightName)
}