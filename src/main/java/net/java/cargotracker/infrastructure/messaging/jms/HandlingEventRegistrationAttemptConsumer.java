package net.java.cargotracker.infrastructure.messaging.jms;

import net.java.cargotracker.application.HandlingEventService;
import net.java.cargotracker.domain.model.handling.CannotCreateHandlingEventException;
import net.java.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

/**
 * Consumes handling event registration attempt messages and delegates to proper
 * registration.
 */
@Service
public class HandlingEventRegistrationAttemptConsumer {

    @Autowired
    private HandlingEventService handlingEventService;

    // private static final Logger logger = Logger.getLogger(
    // HandlingEventRegistrationAttemptConsumer.class.getName());
    @JmsListener(destination = "HandlingEventRegistrationAttemptQueue")
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            HandlingEventRegistrationAttempt attempt = (HandlingEventRegistrationAttempt) objectMessage.getObject();
            handlingEventService.registerHandlingEvent(attempt.getCompletionTime(), attempt.getTrackingId(), attempt.getVoyageNumber(), attempt.getUnLocode(), attempt.getType());
        } catch (JMSException | CannotCreateHandlingEventException e) {
            // Poison messages will be placed on dead-letter queue.
            throw new RuntimeException("Error occurred processing message", e);
        // } catch (JMSException e) {
        // logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
