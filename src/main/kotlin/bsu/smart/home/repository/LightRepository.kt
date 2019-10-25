package bsu.smart.home.repository

import org.springframework.data.repository.CrudRepository
import bsu.smart.home.model.Light
import java.util.*

interface LightRepository : CrudRepository<Light, Long> {
    override fun findAll(): List<Light>

    fun save(light: Optional<Light>)

    fun findByName(name: String): Light

    fun findByLightStatus(status: Boolean): List<Light>
}