package smart.home.project.config.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(LightNotFoundException::class)
    protected fun handleNotFound(
            ex: Exception,
            request: WebRequest
    ): ResponseEntity<Any> {
        return handleExceptionInternal(ex, "Light not found",
                HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }
}