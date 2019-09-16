package net.java.cargotracker.application.internal;

import java.util.Date;
import java.util.logging.Logger;
import net.java.cargotracker.application.ApplicationEvents;
import net.java.cargotracker.application.HandlingEventService;
import net.java.cargotracker.domain.model.cargo.TrackingId;
import net.java.cargotracker.domain.model.handling.CannotCreateHandlingEventException;
import net.java.cargotracker.domain.model.handling.HandlingEvent;
import net.java.cargotracker.domain.model.handling.HandlingEventFactory;
import net.java.cargotracker.domain.model.handling.HandlingEventRepository;
import net.java.cargotracker.domain.model.location.UnLocode;
import net.java.cargotracker.domain.model.voyage.VoyageNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultHandlingEventService implements HandlingEventService {

    @Autowired
    private ApplicationEvents applicationEvents;

    @Autowired
    private HandlingEventRepository handlingEventRepository;

    @Autowired
    private HandlingEventFactory handlingEventFactory;

    private static final Logger logger = Logger.getLogger(DefaultHandlingEventService.class.getName());

    @Override
    public void registerHandlingEvent(Date completionTime, TrackingId trackingId, VoyageNumber voyageNumber, UnLocode unLocode, HandlingEvent.Type type) throws CannotCreateHandlingEventException {
        Date registrationTime = new Date();
        /* Using a factory to create a HandlingEvent (aggregate). This is where
         it is determined wether the incoming data, the attempt, actually is capable
         of representing a real handling event. */
        HandlingEvent event = handlingEventFactory.createHandlingEvent(registrationTime, completionTime, trackingId, voyageNumber, unLocode, type);
        /* Store the new handling event, which updates the persistent
         state of the handling event aggregate (but not the cargo aggregate -
         that happens asynchronously!)
         */
        handlingEventRepository.store(event);
        /* Publish an event stating that a cargo has been handled. */
        applicationEvents.cargoWasHandled(event);
        logger.info("Registered handling event");
    }
}
