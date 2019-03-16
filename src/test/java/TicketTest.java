import model.SeatHold;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by: Ganbat Bayar
 * On: 3/15/2019
 * Project: WalHomework
 */
public class TicketTest {

    int row = 50, col = 50, hold = 5;

    private AtomicInteger int1 = new AtomicInteger();


    @Test
    public void orderTest() throws InterruptedException {
        TicketServiceImpl service = new TicketServiceImpl(row, col, hold);
        ArrayList<SeatHold> seatHolds = new ArrayList<>();
        Runnable ticketHold = () ->
                seatHolds.add(service.findAndHoldSeats(10, int1.incrementAndGet() + ""));

        for (int i = 0; i < 200; i++) {
            new Thread(ticketHold).run();
        }
        assertEquals(500, service.numSeatsAvailable());

        seatHolds.stream().forEach(e -> {
            service.reserveSeats(e.getSeatHeldId(), e.getCustomerEmail());
        });
        assertEquals(500, service.numSeatsAvailable());
        System.out.println("Number of seats free after holding - " + service.numSeatsAvailable());
        Thread.sleep(6000);
        assertEquals(500, service.numSeatsAvailable());
    }

    private void book(TicketService service) {

    }
}
