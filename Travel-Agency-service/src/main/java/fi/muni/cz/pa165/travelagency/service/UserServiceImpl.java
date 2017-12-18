package fi.muni.cz.pa165.travelagency.service;

import fi.muni.cz.pa165.travelagency.dao.UserDao;
import fi.muni.cz.pa165.travelagency.entity.User;
import fi.muni.cz.pa165.travelagency.entity.Excursion;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.enums.PaymentStateType;
import fi.muni.cz.pa165.travelagency.exceptions.TravelAgencyServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

/**
 * UserService implementation
 * @author Martin Sevcik <422365>
 */
@Service
public class UserServiceImpl implements UserService {

    private static final int SALT_BYTE_SIZE = 24;
    private static final int HASH_BYTE_SIZE = 24;
    private static final int PBKDF2_ITERATIONS = 1000;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ReservationService reservationService;

    @Override
    public void registerUser(User user, String password) {
        try {
            user.setPasswordHash(createHash(password));
            userDao.create(user);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public List<User> findAll() {
       try {
            return userDao.findAll();
       } catch (Exception e){
           throw new TravelAgencyServiceException(e);
       }
    }

    @Override
    public User findById(Long id) {
        try {
            return userDao.findById(id);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }

    }

    @Override
    public User changeUserOnReservation(User newUser, Reservation reservation) {
        try {
            User oldUser = reservation.getUser();
            oldUser.removeReservation(reservation);
            userDao.update(oldUser);

            reservation.setUser(newUser);
            reservationService.updateReservation(reservation);

            newUser.addReservation(reservation);
            return userDao.update(newUser);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e) {
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public BigDecimal getTotalPriceUsersReservations(User user) {
        try {
            List<Reservation> reservationList =  reservationService.findByUser(user);
            BigDecimal totalSum = BigDecimal.ZERO;
            for (Reservation reservation: reservationList) {
                if (reservation.getPaymentState() != PaymentStateType.Paid) {
                    continue;
                }
                totalSum = totalSum.add(reservation.getTrip().getPrice());

                if (reservation.getExcursions() != null) {
                    for (Excursion excursion: reservation.getExcursions()) {
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
    public void removeUser(User user) {
        try {
            userDao.remove(user);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public User updateUser(User user) {
        try {
            return userDao.update(user);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public User findByReservation(Reservation reservation) {
        try {
            return userDao.findByReservation(reservation);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public User findByIdCardNumber(String idCardNumber) {
        try{
            return userDao.findByIdCardNumber(idCardNumber);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public User findByEmail(String email) {
        try {
            return userDao.findByEmail(email);
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public boolean authenticate(User user, String password) {
        try {
            return validatePassword(password, user.getPasswordHash());
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }
    }

    @Override
    public boolean isAdmin(User user) {
        try {
            return userDao.findById(user.getId()).getIsAdmin();
        } catch (NullPointerException npe){
            throw npe;
        } catch (Exception e){
            throw new TravelAgencyServiceException(e);
        }
    }

    //see  https://crackstation.net/hashing-security.htm#javasourcecode
    private static String createHash(String password) {


        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);
        // Hash the password
        byte[] hash = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        // format iterations:salt:hash
        return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * validates password
     * @param password password
     * @param correctHash hash
     * @return true if hash is correct, false otherwise
     */
    public static boolean validatePassword(String password, String correctHash) {
        if(password==null) return false;
        if(correctHash==null) throw new IllegalArgumentException("password hash is null");
        String[] params = correctHash.split(":");
        int iterations = Integer.parseInt(params[0]);
        byte[] salt = fromHex(params[1]);
        byte[] hash = fromHex(params[2]);
        byte[] testHash = pbkdf2(password.toCharArray(), salt, iterations, hash.length);
        return slowEquals(hash, testHash);
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line
     * system using a timing attack and then attacked off-line.
     *
     * @param a the first byte array
     * @param b the second byte array
     * @return true if both byte arrays are the same, false if not
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        return paddingLength > 0 ? String.format("%0" + paddingLength + "d", 0) + hex : hex;
    }
    
}
