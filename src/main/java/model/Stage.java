package model;

import lombok.Data;
import model.enums.SeatStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by: Ganbat Bayar
 * On: 3/14/2019
 * Project: WalHomework
 */
@Data
public class Stage {
    private List<Seat> seats = new ArrayList<>();
    private Map<Integer, SeatHold> heldSeats = new HashMap<>();

    List<Integer> holdList= new ArrayList<>();
    List<Integer> unHoldedList =new ArrayList<>();

    public Stage(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                seats.add(new Seat(i, j, SeatStatus.FREE));
            }
        }
    }
}
