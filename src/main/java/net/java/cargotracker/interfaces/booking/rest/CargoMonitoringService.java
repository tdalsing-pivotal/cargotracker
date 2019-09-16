package net.java.cargotracker.interfaces.booking.rest;

import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.core.MediaType;
import net.java.cargotracker.domain.model.cargo.Cargo;
import net.java.cargotracker.domain.model.cargo.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@RestController
@RequestMapping("/cargo")
public class CargoMonitoringService {

    public static final String ISO_8601_FORMAT = "yyyy-MM-dd HH:mm";

    @Autowired
    private CargoRepository cargoRepository;

    public CargoMonitoringService() {
    }

    @GetMapping
    public JsonArray getAllCargo() {
        List<Cargo> cargos = cargoRepository.findAll();
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (Cargo cargo : cargos) {
            builder.add(Json.createObjectBuilder().add("trackingId", cargo.getTrackingId().getIdString()).add("routingStatus", cargo.getDelivery().getRoutingStatus().toString()).add("misdirected", cargo.getDelivery().isMisdirected()).add("transportStatus", cargo.getDelivery().getTransportStatus().toString()).add("atDestination", cargo.getDelivery().isUnloadedAtDestination()).add("origin", cargo.getOrigin().getUnLocode().getIdString()).add("lastKnownLocation", cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString().equals("XXXXX") ? "Unknown" : cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString()));
        }
        return builder.build();
    }
}
