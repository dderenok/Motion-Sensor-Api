package bsu.smart.home.repository

import bsu.smart.home.model.Light
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface LightRepository : CrudRepository<Light, Long> {
    override fun findAll(): List<Light>

    fun findByGuid(guid: UUID): Light?

    fun findByName(name: String): Light?

    fun existsByName(name: String): Boolean

    fun deleteByGuid(guid: UUID)
}