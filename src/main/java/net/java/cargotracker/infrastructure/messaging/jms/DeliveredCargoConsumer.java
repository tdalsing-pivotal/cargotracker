package net.java.cargotracker.infrastructure.messaging.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DeliveredCargoConsumer {

    private static final Logger logger = Logger.getLogger(DeliveredCargoConsumer.class.getName());

    @JmsListener(destination = "DeliveredCargoQueue")
    public void onMessage(Message message) {
        try {
            logger.log(Level.INFO, "Cargo with tracking ID {0} delivered.", message.getBody(String.class));
        } catch (JMSException ex) {
            logger.log(Level.WARNING, "Error processing message.", ex);
        }
    }
}
