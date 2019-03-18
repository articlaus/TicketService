import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ticket.constants.ResponseConstants;
import ticket.model.SeatHold;
import ticket.model.enums.SeatHoldStatus;
import ticket.service.TicketServiceImpl;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by: Ganbat Bayar
 * On: 3/15/2019
 * Project: WalHomework
 * Test Class for load based tests
 */
class LoadTest {

    private static int row = 50, col = 50, hold = 1;

    private static TicketServiceImpl service;
    private static ArrayList<SeatHold> successHolds;
    private static ArrayList<SeatHold> failedHolds;

    @BeforeAll
    static void init() {
        service = new TicketServiceImpl(row, col, hold);
        successHolds = new ArrayList<>();
        failedHolds = new ArrayList<>();
    }

    /**
     * Concurrently add high user request before each test
     *
     * @throws InterruptedException
     */
    @BeforeEach
    void setup() throws InterruptedException {
        Runnable ticketHold = () -> {
            SeatHold seatHold = service.findAndHoldSeats(10, "test@test.com");
            if (SeatHoldStatus.HOLDING.equals(seatHold.getStatus()))
                successHolds.add(seatHold);
            else
                failedHolds.add(seatHold);
        };
        ExecutorService executor = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 300; i++) {
            executor.execute(ticketHold);
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    /**
     * Test to see if correct number of seats were held and assigned
     */
    @Test
    void concurrentOrder() {
        //Test for correct number of seats held and failed orders due to it being full
        assertEquals(0, service.numSeatsAvailable());
        assertEquals(250, successHolds.size());
        assertEquals(50, failedHolds.size());
    }

    /**
     * Test to see if correct number of seats were released after being held
     * and correct numbers were reserved
     *
     * @throws InterruptedException
     */
    @Test
    void ticketReleaseTest() throws InterruptedException {
        successHolds.stream().limit(200).forEach(e -> {
            service.reserveSeats(e.getSeatHeldId(), e.getCustomerEmail());
        });
        assertEquals(0, service.numSeatsAvailable());
        Thread.sleep(2000); // wait till system finishes freeing up seats
        assertEquals(500, service.numSeatsAvailable());
        assertEquals(ResponseConstants.TICKET_EXPIRED, service.reserveSeats(250, "test@test.com"));
        assertEquals(ResponseConstants.INVALID_SEAT_ID, service.reserveSeats(1231231223, "asdasdasd"));
    }
}
