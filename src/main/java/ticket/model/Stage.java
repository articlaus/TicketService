package ticket.model;

import lombok.Data;
import ticket.model.enums.SeatStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Created by: Ganbat Bayar
 * On: 3/14/2019
 * Project: WalHomework
 */
@Data
public class Stage {
    private static Stage instance;
    private Seat[][] stageSeats;
    private ConcurrentHashMap<Integer, SeatHold> heldSeats = new ConcurrentHashMap<>();

    private int cols, rows;

    /**
     * Creates the Stage based on columns and rows provided
     * and created 2D array map of seats
     *
     * @param rows Number of rows in Stage
     * @param cols Number of Columns per Row
     */
    private Stage(int rows, int cols) {
        this.cols = cols;
        this.rows = rows;
        stageSeats = new Seat[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                stageSeats[i][j] = Seat.builder().row(i).col(j).status(SeatStatus.FREE).build();
            }
        }
    }

    /**
     * Creating the singleton instance of stage object
     *
     * @param rows number of rows in the Stage
     * @param cols number of columns per row
     */
    public static void init(int rows, int cols) {
        instance = new Stage(rows, cols);
    }

    /**
     * Returns the instance of the singleton class, if the class is not properly initiated without
     * calling init() method it will return default 5x5 stage
     *
     * @return instance of Stage class
     */
    public static Stage getInstance() {
        Optional<Stage> optionalStage = Optional.ofNullable(instance);
        if (!optionalStage.isPresent())
            instance = new Stage(5, 5);
        return instance;
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
