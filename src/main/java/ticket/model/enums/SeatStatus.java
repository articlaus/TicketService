package ticket.model.enums;

/**
 * Created by: Ganbat Bayar
 * On: 3/14/2019
 * Project: WalHomework
 * Possible states the seat can be in
 */
public enum SeatStatus {
    /**
     * Free status, available for holding
     */
    FREE,
    /**
     * Held status, cannot be held again but can be reserved and freed
     */
    HOLD,
    /**
     * Reserved status, once reserved cannot change state again
     */
    RESERVED
}
