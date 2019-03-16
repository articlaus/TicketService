import model.SeatHold;
import model.Stage;
import model.enums.SeatStatus;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by: Ganbat Bayar
 * On: 3/14/2019
 * Project: WalHomework
 */
public class TicketServiceImpl implements TicketService {

    private Stage stage;
    private AtomicInteger idGenerator = new AtomicInteger();


    TicketServiceImpl(int row, int col, long secToHold) {
        stage = new Stage(row, col);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        TicketUnHolder unHolder = new TicketUnHolder(stage, secToHold);
        executor.scheduleWithFixedDelay(unHolder, secToHold, 1, TimeUnit.SECONDS);
    }

    @Override
    public int numSeatsAvailable() {
        return (int) stage.getSeats().stream().filter(x -> x.getStatus() == SeatStatus.FREE).count();
    }

    @Override
    public synchronized SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        if (numSeatsAvailable() < numSeats)
            return new SeatHold("Sorry we are don't enough seats for your order.\n we only have - ");//todo
        SeatHold seatHold = new SeatHold(idGenerator.incrementAndGet(), customerEmail, "Success");
        stage.getSeats().stream().filter(x -> x.getStatus() == SeatStatus.FREE).limit(numSeats).forEachOrdered(x -> {
            x.setStatus(SeatStatus.HOLD);
            seatHold.getSeatsHeld().add(x);
        });
        stage.getHoldList().add(seatHold.getSeatHeldId());
        stage.getHeldSeats().put(seatHold.getSeatHeldId(), seatHold);
        return seatHold;
    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        SeatHold seatHold = stage.getHeldSeats().get(seatHoldId);
        if (!seatHold.getCustomerEmail().equals(customerEmail))
            return "Unauthorized";
        seatHold.getSeatsHeld().forEach(x -> x.setStatus(SeatStatus.RESERVED));
        stage.getHoldList().remove(Integer.valueOf(seatHoldId));
        return "Conformation Number = " + UUID.fromString(customerEmail).toString();
    }

}
