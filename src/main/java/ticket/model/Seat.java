package ticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ticket.model.enums.SeatStatus;

/**
 * Created by: Ganbat Bayar
 * On: 3/14/2019
 * Project: WalHomework
 */
@Data
@AllArgsConstructor
public class Seat {
    private int row;
    private int col;
    private SeatStatus status;
}
