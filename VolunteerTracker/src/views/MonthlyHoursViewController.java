/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.sql.*;

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

    private XYChart.Series series;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        series = new XYChart.Series<>();

        try {
            populateSeriesFromDB();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        barChart.getData().addAll(series);
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

            String sql = "SELECT year(dateWorked), monthname(dateWorked), SUM(hoursWorked) "
                    + "FROM hoursWorked "
                    + "GROUP BY YEAR (dateWorked), MONTH(dateWorked);";

            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                series.getData().add(new XYChart.Data(resultSet.getString(2), resultSet.getInt(3)));
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

}
