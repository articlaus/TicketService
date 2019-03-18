package ticket.service.util;

import ticket.model.Stage;
import ticket.model.enums.SeatHoldStatus;
import ticket.model.enums.SeatStatus;

import java.time.Instant;

/**
 * Created by: Ganbat Bayar
 * On: 3/15/2019
 * Project: WalHomework
 */

public class TicketUnHolder implements Runnable {
    private Long numOfSecondsToHold;
    private Stage stage;

    /**
     * Runnable class which ce ben scheduled to periodically check and free held tickets
     *
     * @param stage              The stage to work on.
     * @param numOfSecondsToHold number of seconds to hold the ticket before releasing
     */
    public TicketUnHolder(Stage stage, Long numOfSecondsToHold) {
        this.stage = stage;
        this.numOfSecondsToHold = numOfSecondsToHold;
    }

    private void unHoldTickets() {
        stage.getHeldSeats().forEach((id, value) -> {
            if (value.getStatus().equals(SeatHoldStatus.HOLDING) && value.getReserveDate().plusSeconds(numOfSecondsToHold).compareTo(Instant.now()) < 0) {
                value.getSeatsHeld().forEach(seat -> seat.setStatus(SeatStatus.FREE));
                value.setStatus(SeatHoldStatus.REMOVED);
            }
        });
    }

    @Override
    public void run() {
        unHoldTickets();
    }
}
