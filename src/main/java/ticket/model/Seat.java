package ticket.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ticket.model.enums.SeatStatus;

/**
 * Created by: Ganbat Bayar
 * On: 3/14/2019
 * Project: WalHomework
 * <p>
 * This Class implements Builder pattern via @Builder annotation Lombok provides
 */

@Builder
public class Seat {
    @NonNull
    @Getter
    private int row;
    @NonNull
    @Getter
    private int col;
    @NonNull
    @Getter
    @Setter
    private SeatStatus status;

    @Override
    public String toString() {
        return "Seat(row=" + this.getRow() + ", col=" + this.getCol() + ")";
    }
}
