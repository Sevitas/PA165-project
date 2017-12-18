package fi.muni.cz.pa165.travelagency.service;

import fi.muni.cz.pa165.travelagency.dao.ReservationDao;
import fi.muni.cz.pa165.travelagency.entity.User;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.enums.PaymentStateType;
import fi.muni.cz.pa165.travelagency.exceptions.TravelAgencyServiceException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Radoslav Micko <445611>
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationDao reservationDao;
    
    @Autowired
    private TripService tripService;
    
    @Override
    public Reservation createReservation(Reservation reservation) {
        try {
            if (reservation.getTrip().getAvailableSpots() == 0) {
                throw new TravelAgencyServiceException("Cannot create new "
                        + "reservation. There is no more free slot for this trip.");
            }
            Trip trip = tripService.findTripWithId(reservation.getTrip().getId());
            trip.setAvailableSpots(trip.getAvailableSpots() - 1);
            tripService.updateTrip(trip);
            reservationDao.create(reservation);
            return reservation;
        } catch (EntityExistsException e) {
            throw new TravelAgencyServiceException("the entity already exists.", e);
        } catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("Not an entity.", e);
        } catch (TransactionRequiredException e) {
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public Reservation updateReservation(Reservation reservation) {
        try {
            return reservationDao.update(reservation);
        } catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("Not entity or removed.", e);
        } catch (TransactionRequiredException e) {
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public void removeReservation(Reservation reservation) {
        try {
            Trip trip = tripService.findTripWithId(reservation.getTrip().getId());
            reservationDao.remove(reservation);
            trip.setAvailableSpots(trip.getAvailableSpots() + 1);
            tripService.updateTrip(trip);
        }  catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("Not entity or removed.", e);
        } catch (TransactionRequiredException e) {
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public void addExcursionToReservation(Reservation reservation, Excursion excursion) {
        try {
            if (!reservation.getTrip().getExcursions().contains(excursion)) { 
                throw new TravelAgencyServiceException("Cannot add excrusion to "
                        + "reservation. There is no such excrusion available for this trip.");
            }
            reservation = reservationDao.findById(reservation.getId());
            reservation.addReservedExcursion(excursion);
            reservationDao.update(reservation);
        } catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("Not entity.", e);
        } catch (TransactionRequiredException e) {
            throw new TravelAgencyServiceException(e);
        }
    }
    
    @Override
    public void addExcrusionsToReservation(Reservation reservation, List<Excursion> excursions) {
        try {
            if (!reservation.getTrip().getExcursions().containsAll(excursions)) { 
                throw new TravelAgencyServiceException("Cannot add excrusions to "
                        + "reservation. One of excursions is not available for this trip.");
            }
            reservation = reservationDao.findById(reservation.getId());
            reservation.addAllReservedExcursions(excursions);
            reservationDao.update(reservation);
        } catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("Not entity.", e);
        } catch (TransactionRequiredException e) {
            throw new TravelAgencyServiceException(e);
        }
    }
    

    @Override
    public List<Reservation> findAll() {
        try {
            return reservationDao.findAll();
        } catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("Invalid Query.", e);
        } catch (Exception e) {
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public Reservation findById(Long id) {
        try {
            return reservationDao.findById(id);
        } catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("not valid key or null.", e);
        }
    }

    @Override
    public List<Reservation> findByUser(User user) {
        try {
            return reservationDao.findByUser(user);
        } catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("Invalid Query.", e);
        } catch (Exception e) {
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public List<Reservation> findByTrip(Trip trip) {
        try {
            return reservationDao.findByTrip(trip);
        } catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("Invalid Query.", e);
        } catch (Exception e) {
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public BigDecimal getTotalPrice(Reservation reservation) {
        try {
            Reservation r = reservationDao.findById(reservation.getId());
            BigDecimal price = r.getTrip().getPrice();
            for (Excursion e: r.getExcursions()) {
                price = price.add(e.getPrice());
            }
            return price;
        } catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("not valid key or null.", e);
        }
    }


    @Override
    public List<Reservation> getReservationsCreatedBetween(Date start, Date end) {
        if (start.after(end)) { 
            throw new TravelAgencyServiceException("Wrong order of dates.");
        }
        try {
            return reservationDao.getReservationsCreatedBetween(start, end);
        } catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("Invalid Query.", e);
        } catch (Exception e) {
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public void setPaidState(Reservation reservation) {
        try {
            reservation = reservationDao.findById(reservation.getId());
            reservation.setPaymentState(PaymentStateType.Paid);
        } catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("not valid key or null.", e);
        }
    }
    
    @Override
    public void setNotPaidState(Reservation reservation) {
        try {
            reservation = reservationDao.findById(reservation.getId());
            reservation.setPaymentState(PaymentStateType.NotPaid);
        } catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("not valid key or null.", e);
        }
    } 

    @Override
    public List<Reservation> findAllSortedByDate() {
        try {
            List<Reservation> all = reservationDao.findAll();
            all.sort((o1, o2) -> o1.getCreated().compareTo(o2.getCreated()));
            return all;
        } catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("Invalid Query.", e);
        } catch (Exception e) {
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public List<Reservation> findAllNotPaid() {
        try {
            List<Reservation> notPaid = new ArrayList<>();
            for (Reservation r : reservationDao.findAll()) {
                if (r.getPaymentState() == PaymentStateType.NotPaid) {
                    notPaid.add(r);
                }
            }
            return notPaid;
        } catch (IllegalArgumentException e) {
            throw new TravelAgencyServiceException("Invalid Query.", e);
        } catch (Exception e) {
            throw new TravelAgencyServiceException(e);
        }
    }
}
