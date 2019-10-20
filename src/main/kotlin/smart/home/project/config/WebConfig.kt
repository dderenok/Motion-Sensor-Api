package smart.home.project.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptors("/home")
    }

    fun InterceptorRegistry.addInterceptors(
            value: String
    ) = addInterceptor(AddValidationIntreceptor()).addPathPatterns(value)
}