package fi.muni.cz.pa165.travelagency.dao.Impl;

import fi.muni.cz.pa165.travelagency.dao.ReservationDao;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.User;
import fi.muni.cz.pa165.travelagency.entity.Trip;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

/**
 * Implementation of the {@link ReservationDao}. 
 * @author Radoslav Micko <445611>
 */
@Repository
public class ReservationDaoImpl implements ReservationDao {
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public void create(Reservation reservation) {
        em.persist(reservation);
    }

    @Override
    public Reservation update(Reservation reservation) {
        return em.merge(reservation);
    }
    
    @Override
    public List<Reservation> findAll() {
        TypedQuery<Reservation> query = em.createQuery("SELECT q FROM Reservation q",
                Reservation.class);
        return query.getResultList();
    }
    
    @Override
    public List<Reservation> findByUser(User user) {
        TypedQuery<Reservation> query = em.createQuery("Select o from Reservation o "
                + "where o.user = :userid",
                Reservation.class);
        query.setParameter("userid", user);
        return query.getResultList();
    }

    @Override
    public Reservation findById(Long id) {
        return em.find(Reservation.class, id);
    }

    @Override
    public List<Reservation> findByTrip(Trip trip) {
        TypedQuery<Reservation> query = em.createQuery("SELECT o FROM Reservation "
                + "o WHERE o.trip.id = :tripId", Reservation.class);
        query.setParameter("tripId", trip.getId());
        return query.getResultList();
    }
    
    @Override
    public void remove(Reservation reservation) throws IllegalArgumentException {
        em.remove(em.contains(reservation) ? reservation : em.merge(reservation));
    }

    @Override
    public List<Reservation> getReservationsCreatedBetween(Date start, Date end) {
        TypedQuery<Reservation> query = em.createQuery("SELECT o FROM Reservation o "
                + "WHERE o.created BETWEEN :startDate AND :endDate ",Reservation.class);
        query.setParameter("startDate", start);
        query.setParameter("endDate", end);
        return query.getResultList();
    }
}
