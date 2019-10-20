package smart.home.project.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Light(
    @Id
    @GeneratedValue
    private val id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String? = null,

    var lightStatus: Boolean = false
)