package bsu.smart.home.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Light(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    var lightStatus: Boolean = false
)