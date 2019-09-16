package net.java.cargotracker.infrastructure.messaging.jms;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.jms.annotation.JmsListener;

@Service
public class MisdirectedCargoConsumer {

    private static final Logger logger = Logger.getLogger(MisdirectedCargoConsumer.class.getName());

    @JmsListener(destination = "MisdirectedCargoQueue")
    public void onMessage(Message message) {
        try {
            logger.log(Level.INFO, "Cargo with tracking ID {0} misdirected.", message.getBody(String.class));
        } catch (JMSException ex) {
            logger.log(Level.WARNING, "Error processing message.", ex);
        }
    }
}
