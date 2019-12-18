package models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author Ksenia
 */
public final class Volunteer {

    private String firstName, lastName, phoneNumber, password;
    private LocalDate birthday;
    private File imageFile;
    private int volunteerID;
    private byte[] salt;

    public Volunteer(String firstName, String lastName, String phoneNumber, LocalDate birthday, String password) throws NoSuchAlgorithmException {
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setBirthday(birthday);
        setImageFile(new File("./src/images/default.jpg"));
        salt = PasswordGenerator.getSalt();
        this.password = PasswordGenerator.getSHA512Password(password, salt);
    }

    public Volunteer(String firstName, String lastName, String phoneNumber, LocalDate birthday, File imageFile, String password) throws IOException, NoSuchAlgorithmException {
        this(firstName, lastName, phoneNumber, birthday, password);
        setImageFile(imageFile);
        copyImageFile();
    }

    public int getVolunteerID() {
        return volunteerID;
    }

    public void setVolunteerID(int volunteerID) {
        if (volunteerID >= 0) {
            this.volunteerID = volunteerID;
        } else {
            throw new IllegalArgumentException("VolunteerID must be >= 0");
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * area code city house NXX -XXX -XXXX
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("[2-9]\\d{2}[-.]?\\d{3}[-.]?\\d{4}")) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Phone number must be in the pattern NXX-XXX-XXXX");
        }
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * This will validate that the person is between ages 10 and 100
     *
     * @param birthday
     */
    public void setBirthday(LocalDate birthday) {
        int age = Period.between(birthday, LocalDate.now()).getYears();

        if (age >= 10 && age <= 100) {
            this.birthday = birthday;
        } else {
            throw new IllegalArgumentException("Volunteer must be between ages 10 and 100");
        }
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * This method will copy the file to the images directory on this server and
     * give it a unique name.
     *
     * @throws java.io.IOException
     */
    public void copyImageFile() throws IOException {
        //create a new path to copy the image into a local director
        Path sourcePath = imageFile.toPath();

        String uniqueFileName = getUniqueFileName(imageFile.getName());

        Path targetPath = Paths.get("./src/images/" + uniqueFileName);

        //copy the file to the new directory
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        //update the image to point to the new File
        imageFile = new File(targetPath.toString());
    }

    /**
     * This method will receive a String representing the a file name and return
     * a String with a random, unique set of letters prefixed to it
     */
    private String getUniqueFileName(String oldFileName) {
        String newName;
        int count = 0;
        String fileType = oldFileName.substring(oldFileName.indexOf("."));

        //loop until we have a unique file name
        do {
            newName = "";

            newName = firstName + lastName + "photo";

            if (count > 0) {
                newName += count;
            }

            newName += fileType;
            count++;

        } while (!uniqueFileInDirectory(newName));
        return newName;
    }

    /**
     * This method will search the images directory and ensure that the file
     * name is unique
     *
     * @param fileName
     * @return
     */
    public boolean uniqueFileInDirectory(String fileName) {
        File directory = new File("./src/images/");

        File[] dir_contents = directory.listFiles();

        for (File file : dir_contents) {
            if (file.getName().equals(fileName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method will return a formatted
     *
     * @return String with the person's first name, last name and age
     */
    @Override
    public String toString() {
        return String.format("%s %s is %d years old", firstName, lastName, Period.between(birthday, LocalDate.now()).getYears());
    }

    /**
     * This method will write the instance of the volunteer into the database
     *
     * @throws java.sql.SQLException
     */
    public void insertIntoDataBase() throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?autoReconnect=true&useSSL=false",
                    "root", "MySQLPassword1");
            String sql = "INSERT INTO volunteers (firstName, lastName, phoneNumber, birthday, imageFile, password, salt)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = conn.prepareStatement(sql);

            Date db = Date.valueOf(birthday);

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setDate(4, db);
            preparedStatement.setString(5, imageFile.getName());
            preparedStatement.setString(6, password);
            preparedStatement.setBlob(7, new javax.sql.rowset.serial.SerialBlob(salt));

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * This will update the Volunteer in the database
     */
    public void updateVolunteerDataBase() throws SQLException {

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?autoReconnect=true&useSSL=false",
                    "root", "MySQLPassword1");

            String sql = "UPDATE volunteers SET firstName = ?, lastName = ?, phoneNumber = ?, birthday = ?, imageFile = ?"
                    + "WHERE volunteerID  = ?";

            preparedStatement = conn.prepareStatement(sql);

            Date bd = Date.valueOf(birthday);

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setDate(4, bd);
            preparedStatement.setString(5, imageFile.getName());
            preparedStatement.setInt(6, volunteerID);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
}
