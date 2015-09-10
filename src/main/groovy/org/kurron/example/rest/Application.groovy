/*
 * Copyright (c) 2015. Ronald D. Kurr kurr@jvmguy.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kurron.example.rest

import groovy.util.logging.Slf4j
import org.kurron.feedback.FeedbackAwareBeanPostProcessor
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * This is the main entry into the application. Running from the command-line using embedded Tomcat will invoke
 * the main() method.
 */
@SpringBootApplication
@EnableConfigurationProperties( ApplicationProperties )
@Slf4j
@SuppressWarnings( 'GStringExpressionWithinString' )
class Application {

    /**
     * Called to start the entire application. Typically, java -jar foo.jar.
     * @param args any arguments to the program.
     */
    static void main( String[] args ) {
        log.info '--- Running embedded web container ----'
        SpringApplication.run( Application, args )
    }

    /**
     * Indicates the type of service emitting the messages.
     */
    @Value( '${info.app.name}' )
    String serviceCode

    /**
     * Indicates the instance of the service emitting the messages.
     */
    @Value( '${PID}' )
    String serviceInstance

    /**
     * Indicates the logical group of the service emitting the messages.
     */
    @Value( '${info.app.realm}' )
    String realm

    @Bean
    FeedbackAwareBeanPostProcessor feedbackAwareBeanPostProcessor() {
        new FeedbackAwareBeanPostProcessor( serviceCode, serviceInstance, realm )
    }

    @Bean
    RabbitTemplate rabbitTemplate( ApplicationProperties configuration, ConnectionFactory factory  ) {
        def bean = new RabbitTemplate( factory )
        bean.exchange = configuration.exchange
        bean
    }

    @Bean
    DirectExchange exchange( ApplicationProperties configuration ) {
        new DirectExchange( configuration.exchange )
    }

    @Bean
    Queue queue( ApplicationProperties configuration ) {
        new Queue( configuration.queue, true, false, false )
    }

    @Bean
    Binding binding( Queue queue, DirectExchange exchange ) {
        new Binding(queue.name, Binding.DestinationType.QUEUE, exchange.name, '', [:] )
    }
}
