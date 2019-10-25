package bsu.smart.home.controller

import org.springframework.web.bind.annotation.*
import bsu.smart.home.model.Light
import bsu.smart.home.service.LightService

@RestController
@RequestMapping("/light")
class LightController(private val lightService: LightService) {
    @GetMapping
    fun findAll(): List<Light> = lightService.findAllLights()

    @GetMapping("/{name}")
    fun findByName(@PathVariable name: String) = lightService.findLightByName(name)

    @GetMapping("/{status}")
    fun findByLight(@PathVariable status: Boolean) = lightService.findLightByStatus(status)

    @PutMapping("/{name}")
    fun updateLightStatus(@PathVariable name: String) = lightService.updateLightStatus(name)

    @PostMapping
    fun createLight(@RequestBody light: Light) = lightService.createLight(light)

    @DeleteMapping("/{id}")
    fun deleteLight(@PathVariable id: Long) = lightService.deleteLight(id)
}