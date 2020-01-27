package views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.sql.*;
import java.time.LocalDate;
import javafx.event.ActionEvent;

/**
 * FXML Controller class
 *
 * @author Ksenia
 */
public class MonthlyHoursViewController implements Initializable {

    @FXML
    private BarChart<?, ?> barChart;
    @FXML
    private NumberAxis hoursWorked;
    @FXML
    private CategoryAxis months;

    private XYChart.Series currentYearSeries;
    private XYChart.Series previousYearSeries;
    private XYChart.Series twoYearsAgoSeries;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentYearSeries = new XYChart.Series<>();
        previousYearSeries = new XYChart.Series<>();
        twoYearsAgoSeries = new XYChart.Series<>();

        currentYearSeries.setName("Current Year");
        previousYearSeries.setName("Previous Year");
        twoYearsAgoSeries.setName("Two Years Ago");

        try {
            populateSeriesFromDB();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        barChart.getData().addAll(currentYearSeries, previousYearSeries, twoYearsAgoSeries);
    }

    /**
     * This will read the user data from the database and populate the series
     */
    private void populateSeriesFromDB() throws SQLException {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?autoReconnect=true&useSSL=false",
                    "root", "MySQLPassword1");

            statement = conn.createStatement();

            String sql = "SELECT year(dateWorked), month(dateWorked), SUM(hoursWorked) "
                    + "FROM hoursWorked "
                    + "GROUP BY YEAR(dateWorked), MONTH(dateWorked) "
                    + "ORDER BY YEAR(dateWorked), MONTH(dateWorked);";

            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                if (resultSet.getInt(1) == LocalDate.now().getYear()) {
                    currentYearSeries.getData().add(new XYChart.Data(resultSet.getString(2), resultSet.getInt(3)));
                } else if (resultSet.getInt(1) == LocalDate.now().getYear()-1) {
                    previousYearSeries.getData().add(new XYChart.Data(resultSet.getString(2), resultSet.getInt(3)));
                } else if (resultSet.getInt(1) == LocalDate.now().getYear()-2) {
                    twoYearsAgoSeries.getData().add(new XYChart.Data(resultSet.getString(2), resultSet.getInt(3)));
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    /**
     * This method will return the scene to the VolunteerTableView
     */
    public void backButtonPushed(ActionEvent event) throws IOException {
        SceneChanger.changeScenes("VolunteerTableView.fxml", "All Volunteers");
    }
}
