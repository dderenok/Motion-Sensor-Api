package smart.home.project.controller

import org.springframework.web.bind.annotation.*
import smart.home.project.model.Light
import smart.home.project.service.LightService

@RestController
@RequestMapping("/light")
class LightController(private val lightService: LightService) {
    @GetMapping
    fun findAll(): List<Light> = lightService.findAllLights()

    @GetMapping("/{name}")
    fun findByName(@PathVariable name: String) = lightService.findLightByName(name)

    @GetMapping("/{status}")
    fun findByLight(@PathVariable status: Boolean) = lightService.findLightByStatus(status)

    @GetMapping("/{id}")
    fun updateLightStatus(@PathVariable id: Long) = lightService.updateLightStatus(id)

    @PostMapping
    fun createLight(@RequestBody light: Light) = lightService.createLight(light)

    @DeleteMapping("/{id}")
    fun deleteLight(@PathVariable id: Long) = lightService.deleteLight(id)
}