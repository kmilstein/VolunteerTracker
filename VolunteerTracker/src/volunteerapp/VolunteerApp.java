package volunteerapp;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import models.Volunteer;

/**
 *
 * @author Ksenia
 */
public class VolunteerApp {
    public static void main(String[] args) throws IOException, SQLException {
        Volunteer volunteer2 = new Volunteer("Ash", "Ketchum2", "313-123-1234", LocalDate.of(1984, Month.MAY, 5), new File("./src/images/Ash.jpg"));
        System.out.printf("Our volunteer is: %s%n", volunteer2);
        
        volunteer2.insertIntoDataBase();
    }
}
