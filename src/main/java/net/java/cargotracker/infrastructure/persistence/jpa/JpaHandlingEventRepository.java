package net.java.cargotracker.infrastructure.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import net.java.cargotracker.domain.model.cargo.TrackingId;
import net.java.cargotracker.domain.model.handling.HandlingEvent;
import net.java.cargotracker.domain.model.handling.HandlingEventRepository;
import net.java.cargotracker.domain.model.handling.HandlingHistory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class JpaHandlingEventRepository implements HandlingEventRepository, Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private EntityManager entityManager;

    @Override
    public void store(HandlingEvent event) {
        entityManager.persist(event);
    }

    @Override
    public HandlingHistory lookupHandlingHistoryOfCargo(TrackingId trackingId) {
        return new HandlingHistory(entityManager.createNamedQuery("HandlingEvent.findByTrackingId", HandlingEvent.class).setParameter("trackingId", trackingId).getResultList());
    }
}
