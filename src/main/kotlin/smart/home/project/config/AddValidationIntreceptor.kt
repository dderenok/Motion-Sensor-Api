package smart.home.project.config

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AddValidationIntreceptor : HandlerInterceptorAdapter() {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        print("We are here!")
        return super.preHandle(request, response, handler)
    }
}