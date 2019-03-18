package ticket.service;

import ticket.constants.ResponseConstants;
import ticket.exception.InvalidDataException;
import ticket.model.Seat;
import ticket.model.SeatHold;
import ticket.model.Stage;
import ticket.model.enums.SeatHoldStatus;
import ticket.model.enums.SeatStatus;
import ticket.service.util.SeatHelper;
import ticket.service.util.TicketUnHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by: Ganbat Bayar
 * On: 3/14/2019
 * Project: WalHomework
 */
public class TicketServiceImpl implements TicketService {
    /**
     * The Venue/Stage also will act as In Memory Data Storage
     */
    private Stage stage;
    /**
     * Unique sequential id generator
     */
    private AtomicInteger idGenerator;
    /**
     * Reentrant lock for maintaining thread safety and fairness
     */
    private Lock lock;

    /**
     * Create the stage and start the Scheduled process to automatically free holding tickets once expired
     *
     * @param row       number of rows in the venue
     * @param col       number of cols in each row
     * @param secToHold seconds to hold the ticket before expiring
     */
    public TicketServiceImpl(int row, int col, long secToHold) {
        stage = new Stage(row, col);
        idGenerator = new AtomicInteger();
        lock = new ReentrantLock(true);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        TicketUnHolder unHolder = new TicketUnHolder(stage, secToHold);
        executor.scheduleWithFixedDelay(unHolder, secToHold, 1, TimeUnit.SECONDS);
    }

    @Override
    public int numSeatsAvailable() {
        return (int) stage.getAllSeatByStream().parallel().filter(x -> x.getStatus() == SeatStatus.FREE).count();
    }

    @Override
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        lock.lock();
        try {
            Optional<String> optionalOfEmail = Optional.ofNullable(customerEmail);
            if (numSeats <= 0 || !optionalOfEmail.isPresent())
                throw new InvalidDataException();
            if (numSeatsAvailable() < numSeats)
                return new SeatHold(SeatHoldStatus.FAILED);
            SeatHold seatHold = new SeatHold(idGenerator.incrementAndGet(), customerEmail, SeatHoldStatus.HOLDING);
            List<Seat> seatsToHold = SeatHelper.getBestRow(stage, numSeats);
            seatsToHold.forEach(seat -> seat.setStatus(SeatStatus.HOLD));
            seatHold.setSeatsHeld(seatsToHold);
            stage.getHeldSeats().put(seatHold.getSeatHeldId(), seatHold);
            return seatHold;
        } catch (InvalidDataException e) {
            return new SeatHold(SeatHoldStatus.ERROR);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        Optional<SeatHold> optional = Optional.ofNullable(stage.getHeldSeats().get(seatHoldId));
        if (optional.isPresent()) {
            SeatHold seatHold = optional.get();
            if (!seatHold.getCustomerEmail().equals(customerEmail))
                return ResponseConstants.EMAIL_MISMATCH;
            if (seatHold.getStatus().equals(SeatHoldStatus.REMOVED))
                return ResponseConstants.TICKET_EXPIRED;
            if (seatHold.getStatus().equals(SeatHoldStatus.SUCCESS))
                return ResponseConstants.ALREADY_APPROVED;
            seatHold.setStatus(SeatHoldStatus.SUCCESS);
            seatHold.getSeatsHeld().forEach(x -> x.setStatus(SeatStatus.RESERVED));
            return ResponseConstants.CONFIRMATION_NUMBER + UUID.randomUUID().toString();
        } else
            return ResponseConstants.INVALID_SEAT_ID;
    }
}
