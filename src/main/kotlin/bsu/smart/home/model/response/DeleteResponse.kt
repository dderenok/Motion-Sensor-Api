package bsu.smart.home.model.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DeleteResponse (
    val message: String? = DEFAULT_DELETE_MESSAGE
) {
    companion object {
        private const val DEFAULT_DELETE_MESSAGE = "Light successfully deleted"
    }
}