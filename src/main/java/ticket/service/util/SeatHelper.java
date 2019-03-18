package ticket.service.util;

import ticket.model.Seat;
import ticket.model.Stage;
import ticket.model.enums.SeatStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by: Ganbat Bayar
 * On: 3/16/2019
 * Project: WalHomework
 */
public class SeatHelper {

    /**
     * Simple helper method to find a good choice of seats for the given request
     * by first looking to book the seats in single row, if not found book seats from ordered free seats
     * where it will have a good chance of having to seat near etc.
     *
     * @param stage         The Stage/Venue
     * @param numberOfSeats number of seats to book
     * @return seats to hold
     */
    public static List<Seat> getBestRow(Stage stage, int numberOfSeats) {
        List<Seat> returnSeats = new ArrayList<>();
        for (int i = 0; i < stage.getRows(); i++) {
            returnSeats.clear();
            for (int j = 0; j < stage.getCols(); j++) {
                if (!stage.getStageSeats()[i][j].getStatus().equals(SeatStatus.FREE)) {
                    returnSeats.clear();
                    continue;
                }
                returnSeats.add(stage.getStageSeats()[i][j]);
                if (returnSeats.size() == numberOfSeats) {
                    return returnSeats;
                }
            }
        }
        return stage.getAllSeatByStream().filter(seat -> seat.getStatus().equals(SeatStatus.FREE)).limit(numberOfSeats).collect(Collectors.toList());
    }

    /**
     * Checks if given string is proper mail
     *
     * @param email given string
     * @return whether is mail or not
     * <p>
     * Credits: geeksforgeeks.com
     */
    public static boolean isValidMail(String email) {
        Optional<String> optional = Optional.ofNullable(email);
        if (optional.isPresent()) {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";
            Pattern pat = Pattern.compile(emailRegex);
            return pat.matcher(email).matches();
        } else
            return false;
    }

}
