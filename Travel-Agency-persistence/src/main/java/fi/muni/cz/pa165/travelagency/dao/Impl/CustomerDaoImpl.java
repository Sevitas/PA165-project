package fi.muni.cz.pa165.travelagency.dao.Impl;

import fi.muni.cz.pa165.travelagency.dao.CustomerDao;
import fi.muni.cz.pa165.travelagency.entity.Customer;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by martin on 22.10.2017.
 * @author Martin Sevcik <422365>
 */
@Repository
public class CustomerDaoImpl implements CustomerDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Customer customer) {
        em.persist(customer);
    }

    @Override
    public List<Customer> findAll() {
        return em.createQuery("select c from Customer c", Customer.class).getResultList();
    }

    @Override
    public Customer findById(Long id) {
        return em.createQuery("select c from Customer c where c.id = :id", Customer.class)
                .setParameter("id", id).getSingleResult();
    }

    @Override
    public void remove(Customer customer) {
        em.remove(em.contains(customer) ? customer : em.merge(customer));
    }

    @Override
    public Customer update(Customer customer) {
        return em.merge(customer);
    }

    @Override
    public Customer findByReservation(Reservation reservation){
        return em.createQuery("select c from Customer c join c.reservations r" +
                " where r.id = :reservationId", Customer.class)
                .setParameter("reservationId", reservation.getId()).getSingleResult();
    }

    @Override
    public Customer findByIdCardNumber(String idCardNumber) {
        return em.createQuery("select c from Customer c where c.idCardNumber = :idCardNumber", Customer.class)
                .setParameter("idCardNumber", idCardNumber).getSingleResult();
    }

    @Override
    public Customer findByEmail(String email) {
        return em.createQuery("select c from Customer c where c.email = :email", Customer.class)
                .setParameter("email", email).getSingleResult();
    }
}
