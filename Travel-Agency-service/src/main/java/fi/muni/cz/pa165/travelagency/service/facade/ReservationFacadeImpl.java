package fi.muni.cz.pa165.travelagency.service.facade;

import fi.muni.cz.pa165.travelagency.dto.ReservationCreateDTO;
import fi.muni.cz.pa165.travelagency.dto.ReservationDTO;
import fi.muni.cz.pa165.travelagency.entity.Customer;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.exceptions.TravelAgencyServiceException;
import fi.muni.cz.pa165.travelagency.facade.ReservationFacade;
import fi.muni.cz.pa165.travelagency.service.BeanMappingService;
import fi.muni.cz.pa165.travelagency.service.CustomerService;
import fi.muni.cz.pa165.travelagency.service.ExcursionService;
import fi.muni.cz.pa165.travelagency.service.ReservationService;
import fi.muni.cz.pa165.travelagency.service.TripService;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Radoslav Micko <445611>
 */
@Service
@Transactional
public class ReservationFacadeImpl implements ReservationFacade {

    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private CustomerService customerService;

    @Autowired
    private TripService tripService;

    @Autowired
    private ExcursionService excursionService;
    
    @Autowired
    private BeanMappingService beanMappingService;
    
    @Override
    public Long createReservation(ReservationCreateDTO reservation) {
        
        Customer customer = customerService.findById(reservation.getCustomerId());
        if (customer == null ) {
            throw new TravelAgencyServiceException("Not existing customer");
        } 
        
        Trip trip = tripService.findTripWithId(reservation.getTripId());
        if (trip == null) {
            throw new TravelAgencyServiceException("Not existing trip");
        }
        
        Reservation newReservation = new Reservation();
        newReservation.setCustomer(customer);
        newReservation.setReservedTrip(trip);
        newReservation.setCreated(new Date());
        if (reservation.getExcursionsId() != null) {
            for (Long excursionId : reservation.getExcursionsId()) {
                Excursion excursion = excursionService.findById(excursionId);
                if (excursion != null) {
                    newReservation.addReservedExcursion(excursion);
                }
            }
        }
        
        customer.addReservation(newReservation);
        customerService.updateCustomer(customer);
        reservationService.createReservation(newReservation);
        return newReservation.getId();
    }

    @Override
    public void updateReservation(ReservationDTO reservation) {
        Reservation newReservation = beanMappingService.mapTo(
                reservation, Reservation.class);
        
        newReservation.setCustomer(
                beanMappingService.mapTo(reservation.getCustomer(), Customer.class));
        
        newReservation.addAllReservedExcursions(beanMappingService.
                mapTo(reservation.getExcursions(), Excursion.class));
        
        reservationService.updateReservation(newReservation);
    }

    @Override
    public void removeReservation(Long reservationId) {
        reservationService.removeReservation(new Reservation(reservationId));
    }

    @Override
    public void addExcursionToReservation(Long reservationId, Long excursionId) {
        reservationService.findById(reservationId).addReservedExcursion(
                excursionService.findById(excursionId));
        
    }

    @Override
    public void addExcrusionsToReservation(Long reservationId, List<Long> excursionsId) {
        Reservation reservation = reservationService.findById(reservationId);
        
        Set<Excursion> excursions = new HashSet<>();
                
        for (Long excursionId : excursionsId) {
            Excursion excursion = excursionService.findById(excursionId); 
            if (excursion != null) {
                    excursions.add(excursion);
            }
        }
        
        reservation.addAllReservedExcursions(excursions);
    }

    @Override
    public List<ReservationDTO> findAllReservations() {
        List<ReservationDTO> reservations = beanMappingService.mapTo(
                        reservationService.findAll(),
                        ReservationDTO.class);
        return setPrice(reservations);
    }

    @Override
    public ReservationDTO findReservationById(Long id) {
        ReservationDTO reservation = beanMappingService.mapTo(
                        reservationService.findById(id), ReservationDTO.class);
        return setPrice(reservation);
    }

    @Override
    public List<ReservationDTO> findReservationByCustomer(Long customerId) {
        List<ReservationDTO> reservations = beanMappingService.mapTo(
                reservationService.findByCustomer(
                        customerService.findById(customerId)),
                ReservationDTO.class);
        return setPrice(reservations);
    }

    @Override
    public List<ReservationDTO> findReservationByTrip(Long tripId) {
        List<ReservationDTO> reservations = beanMappingService.mapTo(
                reservationService.findByTrip(tripService.findTripWithId(tripId)),
                ReservationDTO.class);
        return setPrice(reservations);
    }
    
    @Override
    public List<ReservationDTO> getReservationsCreatedBetween(Date start, Date end) {
        List<ReservationDTO> reservations = beanMappingService.mapTo(
                reservationService.getReservationsCreatedBetween(start, end),
                ReservationDTO.class);
        return setPrice(reservations);
    }

    @Override
    public void setReservationPaid(Long reservationId) {
        reservationService.setPaidState(reservationService.findById(reservationId));
    }

    @Override
    public void setReservationNotPaid(Long reservationId) {
        reservationService.setNotPaidState(reservationService.findById(reservationId));
    }
    
    private List<ReservationDTO> setPrice(List<ReservationDTO> reservations) {
        for (ReservationDTO reservation: reservations) {
            setPrice(reservation);
        }
        return reservations;
    }
    
    private ReservationDTO setPrice(ReservationDTO reservation) {
         reservation.setTotalPrice(reservationService.getTotalPrice(
                beanMappingService.mapTo(reservation, Reservation.class)));
         return reservation;
    }
}
