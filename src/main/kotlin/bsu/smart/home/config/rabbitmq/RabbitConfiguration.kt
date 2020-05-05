package bsu.smart.home.config.rabbitmq

import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(prefix = "spring.rabbitmq", name = ["username", "password"])
class RabbitConfiguration(
    @Value("\${spring.rabbitmq.host}") private val rabbitmqHost: String,
    @Value("\${sensor.create.exchange}") private val sensorCreateExchange: String,
    @Value("\${sensor.create.queue}") private val sensorCreateQueue: String,
    @Value("\${sensor.delete.exchange}") private val sensorDeleteExchange: String,
    @Value("\${sensor.delete.queue}") private val sensorDeleteQueue: String
) {
    @Bean
    fun connectionFactory() = CachingConnectionFactory(rabbitmqHost)

    @Bean
    fun jsonMessageConvertor(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun ampqAdmin() = RabbitAdmin(connectionFactory())

    @Bean
    fun rabbitTemplate() = RabbitTemplate(connectionFactory()).apply {
        messageConverter = jsonMessageConvertor()
    }

    @Bean
    fun sensorCreateExchange() = FanoutExchange(sensorCreateExchange)

    @Bean
    fun sensorCreateQueue() = Queue(sensorCreateQueue)

    @Bean
    fun sensorDeleteExchange() = FanoutExchange(sensorDeleteExchange)

    @Bean
    fun sensorDeleteQueue() = Queue(sensorDeleteQueue)

    @Bean
    fun bindCreatingQueues() = BindingBuilder.bind(sensorCreateQueue()).to(sensorCreateExchange())

    @Bean
    fun bindDeletionQueues() = BindingBuilder.bind(sensorDeleteQueue()).to(sensorDeleteExchange())
}