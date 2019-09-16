package net.java.cargotracker.interfaces.handling.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import net.java.cargotracker.application.ApplicationEvents;
import net.java.cargotracker.domain.model.cargo.TrackingId;
import net.java.cargotracker.domain.model.handling.HandlingEvent;
import net.java.cargotracker.domain.model.location.UnLocode;
import net.java.cargotracker.domain.model.voyage.VoyageNumber;
import net.java.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import javax.ws.rs.core.MediaType;

/**
 * This REST endpoint implementation performs basic validation and parsing of
 * incoming data, and in case of a valid registration attempt, sends an
 * asynchronous message with the information to the handling event registration
 * system for proper registration.
 */
@Service
@RestController
@RequestMapping("/handling")
public class HandlingReportService {

    public static final String ISO_8601_FORMAT = "yyyy-MM-dd HH:mm";

    @Autowired
    private ApplicationEvents applicationEvents;

    public HandlingReportService() {
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @PostMapping(path = "/reports", consumes = MediaType.APPLICATION_JSON)
    public // TODO Better exception handling.
    void submitReport(@NotNull @Valid HandlingReport handlingReport) {
        try {
            Date completionTime = new SimpleDateFormat(ISO_8601_FORMAT).parse(handlingReport.getCompletionTime());
            VoyageNumber voyageNumber = null;
            if (handlingReport.getVoyageNumber() != null) {
                voyageNumber = new VoyageNumber(handlingReport.getVoyageNumber());
            }
            HandlingEvent.Type type = HandlingEvent.Type.valueOf(handlingReport.getEventType());
            UnLocode unLocode = new UnLocode(handlingReport.getUnLocode());
            TrackingId trackingId = new TrackingId(handlingReport.getTrackingId());
            Date registrationTime = new Date();
            HandlingEventRegistrationAttempt attempt = new HandlingEventRegistrationAttempt(registrationTime, completionTime, trackingId, voyageNumber, type, unLocode);
            applicationEvents.receivedHandlingEventRegistrationAttempt(attempt);
        } catch (ParseException ex) {
            throw new RuntimeException("Error parsing completion time", ex);
        }
    }
}
