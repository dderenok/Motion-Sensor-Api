package bsu.smart.home.config.exception

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(code = BAD_REQUEST)
class LightNotUniqueException(exceptionMessage: String = REASON) : RuntimeException(exceptionMessage) {

    companion object {
        const val REASON = "Light with such name already exist"

        private const val serialVersionUID = -8457953252981687666L
    }
}