package smart.home.project.repository

import org.springframework.data.repository.CrudRepository
import smart.home.project.model.Light
import java.util.*

interface LightRepository : CrudRepository<Light, Long> {
    override fun findAll(): List<Light>

    override fun findById(id: Long): Optional<Light>

    fun save(light: Optional<Light>)

    fun findByName(name: String): Light

    fun findByLightStatus(status: Boolean): List<Light>
}