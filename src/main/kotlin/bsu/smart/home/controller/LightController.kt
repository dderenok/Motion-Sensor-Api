package bsu.smart.home.controller

import bsu.smart.home.model.Light
import bsu.smart.home.model.dto.LightDto
import bsu.smart.home.service.LightService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestMethod.PUT
import org.springframework.web.bind.annotation.RequestMethod.DELETE
import java.util.UUID

@RestController
@RequestMapping("/light")
@CrossOrigin(origins = ["http://localhost:3000"], allowedHeaders = ["*"], methods = [GET, POST, PUT, DELETE])
class LightController(private val lightService: LightService) {
    @GetMapping
    fun findAll(): List<Light> = lightService.findAllLights()

    @GetMapping("/{guid}")
    fun find(@PathVariable guid: UUID) = lightService.findLight(guid)

    @GetMapping("/list")
    fun findAllByGuids(@RequestParam("guids") guids: List<UUID>) = lightService.findAllByGuids(guids)

    @GetMapping("/available-to-attach")
    fun findAvailableToRoomAttach() = lightService.findAvailableToRoomAttach()

    @GetMapping("/filter")
    fun findLightByName(@RequestParam("name") name: String) = lightService.findLightByName(name)

    @PutMapping("/status/{guid}/{roomGuid}")
    fun updateLightStatus(@PathVariable guid: UUID, @PathVariable roomGuid: UUID) =
            lightService.updateStatus(guid, roomGuid)

    @PutMapping("/{guid}")
    fun updateLight(@PathVariable guid: UUID, @RequestBody lightDto: LightDto) = lightService.updateLight(guid, lightDto)

    @PostMapping
    fun createLight(@RequestBody lightDto: LightDto) = lightService.createLight(lightDto)

    @DeleteMapping("/{guid}")
    fun deleteLight(@PathVariable guid: UUID) = lightService.deleteLight(guid)
}