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

package org.kurron.example.rest.inbound

import java.nio.charset.StandardCharsets
import org.kurron.example.rest.Application
import org.kurron.traits.GenerationAbility
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageBuilder
import org.springframework.amqp.core.MessageDeliveryMode
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.core.MessagePropertiesBuilder
import org.springframework.amqp.rabbit.core.RabbitOperations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * Integration test for the MessageProcessor object.
 **/
@ContextConfiguration( loader = SpringApplicationContextLoader, classes = [Application] )
@WebIntegrationTest( randomPort = true )
class AmqpInboundGatewayIntegrationTest extends Specification implements GenerationAbility {

    @Autowired
    private RabbitOperations theTemplate

    def 'exercise happy path'() {

        given: 'a valid environment'
        assert theTemplate

        and: 'a message'
        def message = newMessage( randomHexString() )

        when: 'the answer comes back'
        theTemplate.send( message )

        then: 'I cannot think of anything clever to assert'
        true
    }

    private MessageProperties newProperties() {
        MessagePropertiesBuilder.newInstance().setAppId( 'monitor-rabbitmq-test' )
                .setContentType( 'text/plain' )
                .setMessageId( randomUUID().toString() )
                .setDeliveryMode( MessageDeliveryMode.PERSISTENT )
                .setTimestamp( Calendar.instance.time )
                .setCorrelationId( randomUUID().toString().getBytes( StandardCharsets.UTF_8 ) )
                .build()
    }

    private Message newMessage( String data ) {
        def properties = newProperties()
        MessageBuilder.withBody( data.getBytes( StandardCharsets.UTF_8  ) )
                      .andProperties( properties )
                      .build()
    }
}
