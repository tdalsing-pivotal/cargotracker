package net.java.cargotracker.infrastructure.messaging.jms;

import net.java.cargotracker.application.CargoInspectionService;
import net.java.cargotracker.domain.model.cargo.TrackingId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Consumes JMS messages and delegates notification of misdirected cargo to the
 * tracking service.
 *
 * This is a programmatic hook into the JMS infrastructure to make cargo
 * inspection message-driven.
 */
@Service
public class CargoHandledConsumer {

    @Autowired
    private CargoInspectionService cargoInspectionService;

    private static final Logger logger = Logger.getLogger(CargoHandledConsumer.class.getName());

    @JmsListener(destination = "CargoHandledQueue")
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String trackingIdString = textMessage.getText();
            cargoInspectionService.inspectCargo(new TrackingId(trackingIdString));
        } catch (JMSException e) {
            logger.log(Level.SEVERE, "Error procesing JMS message", e);
        }
    }
}
