package bsu.smart.home.config.exception

import javassist.NotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.persistence.NonUniqueResultException

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(NotFoundException::class)
    protected fun handleNotFound(
            ex: Exception,
            request: WebRequest
    ): ResponseEntity<Any> {
        return handleExceptionInternal(ex, "Light not found",
                HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(NonUniqueResultException::class)
    protected fun handleNonUnique(
            ex: Exception,
            request: WebRequest
    ): ResponseEntity<Any> {
        return handleExceptionInternal(ex, "Light with such name already exist",
                HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }
}