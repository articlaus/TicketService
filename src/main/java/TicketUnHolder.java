import model.SeatHold;
import model.Stage;
import model.enums.SeatStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Ganbat Bayar
 * On: 3/15/2019
 * Project: WalHomework
 */

public class TicketUnHolder implements Runnable {
    private Long numOfSecondsToHold;
    private Stage stage;

    TicketUnHolder(Stage stage, Long numOfSecondsToHold) {
        this.stage = stage;
        this.numOfSecondsToHold = numOfSecondsToHold;
    }


    private synchronized void unHoldTickets() {
        stage.getHoldList().stream()
                .filter(e -> stage.getHeldSeats().get(e).getReserveDate().plusSeconds(numOfSecondsToHold).compareTo(Instant.now()) < 0)
                .peek(e -> stage.getUnHoldedList().add(e))
                .flatMap(e -> stage.getHeldSeats().get(e).getSeatsHeld().stream())
                .forEach(e -> {
                    if (e.getStatus() == SeatStatus.HOLD)
                        e.setStatus(SeatStatus.FREE);
                });
    }

    @Override
    public void run() {
        unHoldTickets();
    }
}
