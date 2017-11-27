package fi.muni.cz.pa165.travelagency.dto;

import java.util.List;

/**
 * CustomerCreateDTO class
 * @author Martin Sevcik
 */
public class CustomerCreateDTO {

    /**
     * List of reservation ids
     */
    private List<Long> reservationsId;

    public List<Long> getReservationsId() {
        return reservationsId;
    }

    public void setReservationsId(List<Long> reservationsId) {
        this.reservationsId = reservationsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof CustomerCreateDTO)) return false;

        CustomerCreateDTO that = (CustomerCreateDTO) o;

        return this.getReservationsId() != null ?
                this.getReservationsId().equals(that.getReservationsId()) : that.getReservationsId() == null;
    }

    @Override
    public int hashCode() {
        return getReservationsId() != null ? getReservationsId().hashCode() : 0;
    }
}
