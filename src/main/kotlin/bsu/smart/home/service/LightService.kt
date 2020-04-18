package bsu.smart.home.service

import bsu.smart.home.config.exception.LightNotFoundException
import bsu.smart.home.config.exception.LightNameException
import bsu.smart.home.model.Light
import bsu.smart.home.model.dto.LightDto
import bsu.smart.home.model.dto.LightDto.Companion.toLight
import bsu.smart.home.model.response.DeleteResponse
import bsu.smart.home.repository.LightRepository
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.UUID.randomUUID
import javax.transaction.Transactional

@Service
class LightService(
    private var lightRepository: LightRepository,
    @Value("\${sensor.create.exchange}") private val createSensorExchange: String,
    @Value("\${sensor.delete.exchange}") private val deleteSensorExchange: String
) {
    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    fun LightDto.createLightNotification() = rabbitTemplate.apply {
        setExchange(createSensorExchange)
    }.convertAndSend(this)

    fun deleteNotification(guid: UUID) = rabbitTemplate.apply {
        setExchange(deleteSensorExchange)
    }.convertAndSend(guid.toString())

    fun findAllLights() = lightRepository.findAll()

    fun findLight(guid: UUID) =
            lightRepository.findByGuid(guid) ?:
            throw LightNotFoundException(lightNotFoundMessage("guid", guid.toString()))

    fun findLightByName(name: String) =
            lightRepository.findByName(name) ?:
            throw LightNotFoundException(lightNotFoundMessage("name", name))

    @Transactional
    fun createLight(lightDto: LightDto) = lightDto.name?.let {
        if (checkNameUnique(it)) {
            lightDto
                .apply { guid = randomUUID() }
                .createLightNotification()

            toLight(lightDto).saveLight()
        }
        else throw LightNameException(lightNameUniqueMessage(it))
    } ?: throw LightNameException(lightNullNameMessage())

    @Transactional
    fun updateStatus(guid: UUID) = lightRepository.findByGuid(guid)?.let {
        lightRepository.save(it.apply {
            status = !status
        })
    }

    @Transactional
    fun updateLight(guid: UUID, light: Light) =
        lightRepository.findByGuid(guid)?.let {
            light.name?.let { name ->
                if (!checkNameUnique(light.name!!)) throw LightNameException(lightNameUniqueMessage(name))
            }
            it.apply {
                light.name?.let { tempName -> name = tempName }
                status = light.status
            }.saveLight()
        } ?: throw LightNotFoundException(lightNotFoundMessage("guid", guid.toString()))

    // TODO: d.derenok
    //      find better variant for response entity returning
    @Transactional
    fun deleteLight(guid: UUID) = lightRepository.findByGuid(guid)?.let {
        lightRepository.deleteByGuid(guid).run {
            ResponseEntity(DeleteResponse(lightDeleteMessage(guid.toString())), OK)
        }
        deleteNotification(guid)
    } ?: throw LightNotFoundException(lightNotFoundMessage("guid", guid.toString()))

    fun checkNameUnique(lightName: String) = !lightRepository.existsByName(lightName)

    fun Light.saveLight() = lightRepository.save(this)

    companion object {
        private fun lightNotFoundMessage(element: String, value: String) = "Light with $element '$value' not found."
        private fun lightNameUniqueMessage(name: String) = "Light with such name $name already exist."
        private fun lightDeleteMessage(guid: String) = "Light with guid '$guid' successfully deleted."
        private fun lightNullNameMessage() = "Light name cannot be null"
    }
}