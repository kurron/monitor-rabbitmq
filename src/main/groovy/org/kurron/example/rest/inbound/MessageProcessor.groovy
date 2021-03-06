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

import static java.nio.charset.StandardCharsets.UTF_8
import org.kurron.example.rest.feedback.ExampleFeedbackContext
import org.kurron.feedback.AbstractFeedbackAware
import org.slf4j.MDC
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

/**
 * Pulls messages of the the queue.
 **/
@SuppressWarnings( 'GroovyUnusedDeclaration' )
@Component
class MessageProcessor extends AbstractFeedbackAware {

    /**
     * Correlation id key into the mapped diagnostic context.
     */
    public static final String CORRELATION_ID = 'correlation-id'

    @RabbitListener( queues = '${example.queue}' )
    void processMessage( @Payload String command, @Header( AmqpHeaders.APP_ID ) String sender,  @Header( value = AmqpHeaders.CORRELATION_ID, required = false ) byte[] correlationBytes  ) {
        String correlationID = correlationBytes ? new String( correlationBytes,UTF_8 ) : 'NOT PROVIDED'
        MDC.put( CORRELATION_ID, correlationID )

        feedbackProvider.sendFeedback( ExampleFeedbackContext.DATA_PROCESSED, command, sender )

        MDC.remove( CORRELATION_ID )
    }
}
