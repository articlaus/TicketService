package ticket.model.enums;

/**
 * Created by: Ganbat Bayar
 * On: 3/16/2019
 * Project: WalHomework
 * Possible state the SeatHold can be in
 */
public enum SeatHoldStatus {
    /**
     * Successfully Reserved state
     */
    SUCCESS,
    /**
     * Failed state, was unable to book any seat due to lack of seats to be boocked
     */
    FAILED,
    /**
     * Removed state, Expired bookings ie didn't get reserved before the expiration
     */
    REMOVED,
    /**
     * State of being either removed or being reserved
     */
    HOLDING,
    /**
     * State when the input data has error in it
     */
    ERROR
}
