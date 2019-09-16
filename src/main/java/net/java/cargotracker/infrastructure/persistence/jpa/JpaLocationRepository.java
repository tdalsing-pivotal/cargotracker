package net.java.cargotracker.infrastructure.persistence.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import net.java.cargotracker.domain.model.location.Location;
import net.java.cargotracker.domain.model.location.LocationRepository;
import net.java.cargotracker.domain.model.location.UnLocode;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class JpaLocationRepository implements LocationRepository, Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private EntityManager entityManager;

    @Override
    public Location find(UnLocode unLocode) {
        return entityManager.createNamedQuery("Location.findByUnLocode", Location.class).setParameter("unLocode", unLocode).getSingleResult();
    }

    @Override
    public List<Location> findAll() {
        return entityManager.createNamedQuery("Location.findAll", Location.class).getResultList();
    }
}
