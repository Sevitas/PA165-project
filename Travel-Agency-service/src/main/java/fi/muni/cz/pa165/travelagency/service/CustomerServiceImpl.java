package fi.muni.cz.pa165.travelagency.service;

import fi.muni.cz.pa165.travelagency.dao.CustomerDao;
import fi.muni.cz.pa165.travelagency.entity.Customer;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.enums.PaymentStateType;
import fi.muni.cz.pa165.travelagency.exceptions.TravelAgencyServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * CustomerService implementation
 * @author Martin Sevcik <422365>
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private ReservationService reservationService;

    @Override
    public void createCustomer(Customer customer) {
        try {
            customerDao.create(customer);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public List<Customer> findAll() {
       try {
            return customerDao.findAll();
       } catch (NullPointerException npe){
           throw npe;
       } catch (Exception e){
           throw new TravelAgencyServiceException(e);
       }
    }

    @Override
    public Customer findById(Long id) {
        try {
            return customerDao.findById(id);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }

    }

    @Override
    public Customer changeCustomerOnReservation(Customer newCustomer, Reservation reservation) {
        try {
            Customer oldCustomer = reservation.getCustomer();
            oldCustomer.removeReservation(reservation);
            customerDao.update(oldCustomer);

            reservation.setCustomer(newCustomer);
            reservationService.updateReservation(reservation);

            newCustomer.addReservation(reservation);
            return customerDao.update(newCustomer);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e) {
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public BigDecimal getTotalPriceCustomersReservations(Customer customer) {
        try {
            List<Reservation> reservationList =  reservationService.findByCustomer(customer);
            BigDecimal totalSum = BigDecimal.ZERO;
            for (Reservation reservation: reservationList) {
                if (reservation.getPaymentState() != PaymentStateType.Paid) {
                    continue;
                }
                totalSum = totalSum.add(reservation.getReservedTrip().getPrice());

                if (reservation.getReservedExcursions() != null) {
                    for (Excursion excursion: reservation.getReservedExcursions()) {
                        totalSum = totalSum.add(excursion.getPrice());
                    }
                }
            }

            return totalSum;
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e) {
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public void removeCustomer(Customer customer) {
        try {
            customerDao.remove(customer);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        try {
            return customerDao.update(customer);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public Customer findByReservation(Reservation reservation) {
        try {
            return customerDao.findByReservation(reservation);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public Customer findByIdCardNumber(String idCardNumber) {
        try{
            return customerDao.findByIdCardNumber(idCardNumber);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public Customer findByEmail(String email) {
        try {
            return customerDao.findByEmail(email);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }
    }
}
