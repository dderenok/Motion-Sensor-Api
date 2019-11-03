package bsu.smart.home.repository

import bsu.smart.home.model.Light
import org.springframework.data.repository.CrudRepository
import java.util.*

interface LightRepository : CrudRepository<Light, Long> {
    override fun findAll(): List<Light>

    override fun findById(id: Long): Optional<Light>

    fun save(light: Optional<Light>)

    fun findByName(name: String): Light

    fun findByLightStatus(status: Boolean): List<Light>
}