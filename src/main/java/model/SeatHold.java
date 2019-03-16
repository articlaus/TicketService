package model;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Ganbat Bayar
 * On: 3/14/2019
 * Project: WalHomework
 */
@Data
public class SeatHold {
    private int seatHeldId;
    private List<Seat> seatsHeld = new ArrayList<>();
    private String customerEmail;
    private Instant reserveDate = Instant.now();
    private String status;

    public SeatHold(int seatHeldId, String customerEmail, String status) {
        this.seatHeldId = seatHeldId;
        this.customerEmail = customerEmail;
        this.status = status;
    }

    public SeatHold(String status) {
        this.status = status;
    }

}
