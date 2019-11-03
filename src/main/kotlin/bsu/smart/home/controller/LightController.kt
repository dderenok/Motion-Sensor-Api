package bsu.smart.home.controller

import bsu.smart.home.model.Light
import bsu.smart.home.service.LightService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/light")
class LightController(private val lightService: LightService) {
    @GetMapping
    fun findAll(): List<Light> = lightService.findAllLights()

    @GetMapping("/{id}")
    fun findByName(@PathVariable id: Long) = lightService.findLightById(id)

    @GetMapping("/status/{id}")
    fun updateLightStatus(@PathVariable id: Long) = lightService.updateLightStatus(id)

    @PutMapping("/{id}")
    fun updateLightName(@PathVariable id: Long, @RequestBody light: Light) = lightService.updateLightName(id, light)

    @PostMapping
    fun createLight(@RequestBody light: Light) = lightService.createLight(light)

    @DeleteMapping("/{id}")
    fun deleteLight(@PathVariable id: Long) = lightService.deleteLight(id)
}