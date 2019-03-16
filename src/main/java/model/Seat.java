package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.enums.SeatStatus;

/**
 * Created by: Ganbat Bayar
 * On: 3/14/2019
 * Project: WalHomework
 */
@Data
@AllArgsConstructor
public class Seat {
    private Integer row;
    private Integer col;
    private SeatStatus status;

}
