/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.muni.cz.pa165.travelagency.dao;

import fi.muni.cz.pa165.travelagency.PersistenceSampleApplicationContext;
import fi.muni.cz.pa165.travelagency.entity.User;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.entity.Trip;
import fi.muni.cz.pa165.travelagency.enums.PaymentStateType;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for User Dao implementation.
 *
 * @author (name = "Nermin Jukan", UCO = "<473370>")
 */

@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@Transactional
public class UserDaoTest extends AbstractTestNGSpringContextTests{

    @Autowired
    public UserDao userDao;
    
    @Autowired
    public ExcursionDao excursionDao;
    
    @Autowired
    public ReservationDao reservationDao;
    
    @Autowired
    public TripDao tripDao;
    
    private User userOne;
    private User userTwo;
    private Reservation reservation;
    private Trip trip;
    private Calendar cal;
    
    
    @BeforeMethod
    public void setupEntities(){
        userOne = new User();
        cal = Calendar.getInstance();
        cal.set(2017, 28, 10);
        userOne.setBirthDate(cal.getTime());
        userOne.setEmail("someone@tester.si");
        userOne.setIdCardNumber("123");
        userOne.setSurname("Tester");
        userOne.setPasswordHash("passwordHash1");

        userTwo = new User();
        userTwo.setBirthDate(cal.getTime());
        userTwo.setEmail("someoneNew@testerTwo.cz");
        userTwo.setIdCardNumber("321");
        userTwo.setSurname("TesterTwo");
        userTwo.setPasswordHash("passwordHash2");
        
    }
    
    @Test
    public void testCreate(){
        
        Assert.assertEquals(userDao.findAll().size(), 0);
        
        userDao.create(userOne);
        
        Assert.assertEquals(userDao.findAll().size(), 1);
        Assert.assertEquals(userDao.findById(userOne.getId()).getSurname(), "Tester");
        Assert.assertEquals(userDao.findById(userOne.getId()).getEmail(), "someone@tester.si");
        Assert.assertEquals(userDao.findById(userOne.getId()).getIdCardNumber(), "123");
        Assert.assertEquals(userDao.findById(userOne.getId()).getBirthDate(), cal.getTime());
    }
    
    @Test
    public void testFindAll(){
        
        userDao.create(userOne);
        userDao.create(userTwo);
        
        Assert.assertEquals(userDao.findAll().size(), 2);
    }
    
    @Test(expectedExceptions = Exception.class)
    public void testSameUserCreate(){
        User theDouble = new User();
        theDouble.setBirthDate(cal.getTime());
        theDouble.setEmail("someone@tester.si");
        theDouble.setIdCardNumber("123");
        theDouble.setSurname("Tester");
        
        userDao.create(userOne);
        userDao.create(theDouble);

        //Assert.assertEquals(userDao.findAll().size(), 1);
    }
    
    @Test(expectedExceptions=NullPointerException.class)
    public void testNullUser(){
        User nullBudy = new User();
        userDao.create(nullBudy);
    }
    
    @Test
    public void testFindById(){
        userDao.create(userOne);
        userDao.create(userTwo);
        
        User first = userDao.findById(userOne.getId());
        User second = userDao.findById(userTwo.getId());
        

        Assert.assertNotNull(first);
        Assert.assertEquals(first, userOne);
        
        Assert.assertNotNull(second);
        Assert.assertEquals(second, userTwo);
        
    }
    
    @Test(expectedExceptions = org.springframework.dao.EmptyResultDataAccessException.class)
    public void testFindNonExistingId(){
        User nullBudy = userDao.findById(Long.MIN_VALUE);
        Assert.assertNull(nullBudy);
    }
    
    @Test
    public void testRemove(){
        userDao.create(userOne);
        userDao.create(userTwo);
        
        userDao.remove(userOne);
        
        Assert.assertEquals(userDao.findById(userTwo.getId()), userTwo);
        
        userDao.remove(userTwo);
        
        Assert.assertTrue(userDao.findAll().isEmpty());
    }
    
    @Test
    public void testUpdate(){
        userOne.setPhoneNumber("147");
        userDao.create(userOne);
        
        Assert.assertEquals(userDao.findById(userOne.getId()).getPhoneNumber(), "147");
        
        userOne.setPhoneNumber("741");
        userDao.update(userOne);
        
        Assert.assertEquals(userDao.findById(userOne.getId()).getPhoneNumber(), "741");
    }
    
    @Test
    public void testFindByReservation(){
        userDao.create(userOne);
        
        trip = new Trip();
        trip.setName("Somewhere");
        tripDao.create(trip);
        
        reservation = new Reservation();
        reservation.setPaymentState(PaymentStateType.Paid);
        reservation.setCreated(cal.getTime());
        reservation.setUser(userOne);
        reservation.setTrip(trip);
        reservationDao.create(reservation);
        
        
        User tester = userDao.findByReservation(reservation);
        
        Assert.assertNotNull(tester);
        Assert.assertEquals(tester, userOne);
    }
    
    
}
