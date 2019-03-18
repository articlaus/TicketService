package ticket.model;

import lombok.Data;
import ticket.model.enums.SeatStatus;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Created by: Ganbat Bayar
 * On: 3/14/2019
 * Project: WalHomework
 */
@Data
public class Stage {
    private Seat[][] stageSeats;
    private ConcurrentHashMap<Integer, SeatHold> heldSeats = new ConcurrentHashMap<>();

    private int cols, rows;

    /**
     * Creates the Stage based on columns and rows provided
     * and created 2D array map of seats
     *
     * @param rows Number of rows in Stage
     * @param cols Number of Colums per Row
     */
    public Stage(int rows, int cols) {
        this.cols = cols;
        this.rows = rows;
        stageSeats = new Seat[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                stageSeats[i][j] = new Seat(i, j, SeatStatus.FREE);
            }
        }
    }

    /**
     * Returns stream object for easier manipulation of 2D seating array
     *
     * @return Stream object of 2D seat array
     */
    public Stream<Seat> getAllSeatByStream() {
        return Arrays.stream(stageSeats).flatMap(Arrays::stream);
    }
}
