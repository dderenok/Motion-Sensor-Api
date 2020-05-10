package bsu.smart.home.service

import bsu.smart.home.config.exception.LightNotFoundException
import bsu.smart.home.config.exception.LightNameException
import bsu.smart.home.config.rabbitmq.RabbitConfiguration
import bsu.smart.home.model.Light
import bsu.smart.home.model.dto.LightDto
import bsu.smart.home.model.dto.LightDto.Companion.toLight
import bsu.smart.home.model.response.DeleteResponse
import bsu.smart.home.repository.LightRepository
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.UUID.randomUUID
import java.util.logging.Logger
import javax.transaction.Transactional

@Service
class LightService(
    private var lightRepository: LightRepository,
    @Value("\${sensor.create.exchange}") private val createSensorExchange: String,
    @Value("\${sensor.delete.exchange}") private val deleteSensorExchange: String
) {
    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    var logger: Logger = Logger.getLogger(RabbitConfiguration::class.java.toString())

    @RabbitListener(queues = [
        "\${room.attach-light.queue}"
    ])
    @Transactional
    fun attachLightToRoomListener(sensorInfo: List<String>) {
        logger.info { "Attach notification received from Room with guid ${sensorInfo[1]}" }

        unplackAvailabillity(sensorInfo[0])?.let {
            val light = lightRepository.findByGuid(it)
            light?.roomGuid = unplackAvailabillity(sensorInfo[1])
        }
    }

    @RabbitListener(queues = [
        "\${room.remove-light.queue}"
    ])
    @Transactional
    fun removeTemperatureFromRoomListener(lightGuids: List<String>?) {
        logger.info { "Unattach notification from Room for Light sensors with guids: $lightGuids" }

        lightGuids?.forEach { guid ->
            unplackAvailabillity(guid)?.let {
                val light = lightRepository.findByGuid(it)
                light?.roomGuid = null
            }
        }
    }

    /**
     *  Checking light guids transfered through rabbit queues on correction type.
     */
    private fun unplackAvailabillity(roomGuidDto: String): UUID? {
        if (roomGuidDto.length == 36) {
            return UUID.fromString(roomGuidDto)
        }
        return null
    }

    fun createLightNotification(sensorInfo: List<String>) = rabbitTemplate.apply {
        setExchange(createSensorExchange)
    }.convertAndSend(sensorInfo)

    fun deleteNotification(guid: UUID) = rabbitTemplate.apply {
        setExchange(deleteSensorExchange)
    }.convertAndSend(guid.toString())

    fun findAllLights() = lightRepository.findAll()

    fun findAllByGuids(guids: List<UUID>): MutableList<Light> {
        val lights: MutableList<Light> = mutableListOf()
        guids.forEach {
            lightRepository.findByGuid(it)?.let { light ->
                lights.add(light)
            }
        }
        return lights
    }

    fun findAvailableToRoomAttach() = lightRepository.findAll().filter {
        it.roomGuid == null
    }

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

            createLightNotification(listOf(
                lightDto.guid.toString(),
                lightDto.roomGuid.toString(),
                LIGHT_SENSOR
            ))

            toLight(lightDto).saveLight()
        }
        else throw LightNameException(lightNameUniqueMessage(it))
    } ?: throw LightNameException(lightNullNameMessage())

    @Transactional
    fun updateStatus(guid: UUID, roomGuid: UUID) = lightRepository.findByGuid(guid)?.let {
        lightRepository.save(it.apply {
            status = !status
        })
    }

    @Transactional
    fun updateLight(guid: UUID, lightDto: LightDto) =
        lightRepository.findByGuid(guid)?.let {
            lightDto.name?.let { name ->
                if (!checkNameUnique(lightDto.name!!)) throw LightNameException(lightNameUniqueMessage(name))
            }
            it.apply {
                lightDto.name?.let { tempName -> name = tempName }
                status = lightDto.status
                roomGuid = lightDto.roomGuid
                createLightNotification(listOf(
                    guid.toString(),
                    roomGuid.toString(),
                    LIGHT_SENSOR
                ))
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
        private const val LIGHT_SENSOR = "LIGHT"
    }
}