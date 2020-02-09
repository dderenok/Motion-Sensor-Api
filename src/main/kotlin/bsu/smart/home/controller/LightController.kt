package bsu.smart.home.controller

import bsu.smart.home.model.Light
import bsu.smart.home.service.LightService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestBody
import java.util.UUID

@RestController
@RequestMapping("/light")
class LightController(private val lightService: LightService) {
    @GetMapping
    fun findAll(): List<Light> = lightService.findAllLights()

    @GetMapping("/{guid}")
    fun find(@PathVariable guid: UUID) = lightService.findLight(guid)

    @GetMapping("filter")
    fun findLightByName(@RequestParam("name") name: String) = lightService.findLightByName(name)

    @PutMapping("/status/{guid}")
    fun updateLightStatus(@PathVariable guid: UUID) = lightService.updateStatus(guid)

    @PutMapping("/{guid}")
    fun updateLight(@PathVariable guid: UUID, @RequestBody light: Light) = lightService.updateLight(guid, light)

    @PostMapping
    fun createLight(@RequestBody light: Light) = lightService.createLight(light)

    @DeleteMapping("/{guid}")
    fun deleteLight(@PathVariable guid: UUID) = lightService.deleteLight(guid)
}