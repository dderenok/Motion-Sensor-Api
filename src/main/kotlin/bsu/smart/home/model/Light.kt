package bsu.smart.home.model

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(
    name = "light",
    uniqueConstraints = [UniqueConstraint(name = "name", columnNames = ["name"])]
)
data class Light(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @get:NotBlank
    var name: String? = null,

    var lightStatus: Boolean = false
)