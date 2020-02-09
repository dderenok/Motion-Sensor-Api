package bsu.smart.home.validation

import bsu.smart.home.model.Light
import bsu.smart.home.repository.LightRepository
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.FILE
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

@Retention
@Target(CLASS, FILE)
@MustBeDocumented
@Constraint(validatedBy = [LightNameUniqueValidator::class])
annotation class LightNameUnique(
    val message: String = "Light with such name already exists",
    // TODO: d.derenok MIC-4
    //      check without Suppress annotation
    @Suppress("unused") val groups: Array<KClass<*>> = [],
    @Suppress("unused") val payload: Array<KClass<out Payload>> = []
)

class LightNameUniqueValidator(private val lightRepository: LightRepository) :
        ConstraintValidator<LightNameUnique, Light> {

    override fun initialize(constraintAnnotation: LightNameUnique) = Unit

    override fun isValid(light: Light, context: ConstraintValidatorContext?) : Boolean = true
//             lightRepository.existsByName(lightName)
}