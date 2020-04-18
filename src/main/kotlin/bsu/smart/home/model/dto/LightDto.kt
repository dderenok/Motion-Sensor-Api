package bsu.smart.home.model.dto

import bsu.smart.home.model.Light
import java.io.Serializable
import java.util.UUID

class LightDto(
    var guid: UUID? = null,
    var name: String? = null,
    var status: Boolean = false,
    // TODO: d.derenok
    //      Add to enum for set type value.
    var type: String? = LIGHT_TYPE,
    var roomGuid: UUID? = null
) : Serializable {
    companion object {
        fun toLight(lightDto: LightDto) = Light().apply {
            guid = lightDto.guid
            name = lightDto.name
            status = lightDto.status
        }

        private const val LIGHT_TYPE = "LIGHT"
        private const val serialVersionUID = 18092353851891936L
    }
}
