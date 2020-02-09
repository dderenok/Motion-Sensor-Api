package bsu.smart.home.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.Column
import javax.validation.constraints.NotBlank

@Entity
@Table(
    name = "light",
    uniqueConstraints = [UniqueConstraint(name = "name", columnNames = ["name"])]
)
data class Light(
    @Id
    @GeneratedValue
    @JsonIgnore
    val id: Long? = null,

    @Column(columnDefinition = "BINARY(16)")
    var guid: UUID? = null,

    @get:NotBlank
    var name: String? = null,

    var status: Boolean = false
)