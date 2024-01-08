package org.example.bazedate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javafx.scene.control.cell.MapValueFactory;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public class ApplicationController {
    int OP;
    boolean PAGE = true;

    @FXML
    private Label intro;

    @FXML
    private Pane main_menu;

    @FXML
    private Pane show_result;

    @FXML
    private Button showContent;

    private TableView<Map<String, Object>> tableView;

    ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();



    @FXML
    protected void onShowContent() {

        tableView = new TableView<>();
        show_result.getChildren().add(tableView);

        if(PAGE){
            main_menu.setVisible(false);
            show_result.setVisible(true);

            showResults(show_result);

            PAGE = !PAGE;
            showContent.setText("Go back");

            intro.setVisible(false);
        }else{
            show_result.getChildren().clear();
            show_result.setVisible(false);
            main_menu.setVisible(true);


            PAGE = !PAGE;
            showContent.setText("Show results");

            intro.setVisible(true);
        }
    }

    protected void showResults(Pane x){
        x.autosize();

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();

        String connectQuerry = new String();

        tableView.setPrefSize(300, 200); // Set preferred width and height

        tableView.setLayoutX(575);
        tableView.setLayoutY(0);



        switch (OP){
            case 1:
                System.out.println("idf                  numef               stare            oras");

                connectQuerry = "SELECT * FROM Furnizori\n" +
                        "ORDER BY stare DESC, numef ASC;";

                tableView.getColumns().clear();
                data = FXCollections.observableArrayList();

                try{

                    Statement statement = connectDB.createStatement();
                    ResultSet querryOutput = statement.executeQuery(connectQuerry);

                    ResultSetMetaData metaData = querryOutput.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    // Dynamically create table columns
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);

                        // Explicitly specifying the type for MapValueFactory
                        column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String, Object>, Object>, ObservableValue<Object>>() {
                            @Override
                            public ObservableValue<Object> call(TableColumn.CellDataFeatures<Map<String, Object>, Object> p) {
                                return new SimpleObjectProperty<>(p.getValue().get(columnName));
                            }
                        });

                        tableView.getColumns().add(column);
                    }


                    while(querryOutput.next()){

                        String idf = querryOutput.getString("idf");
                        String numef = querryOutput.getString("numef");
                        boolean stare = querryOutput.getBoolean("stare");
                        String oras = querryOutput.getString("oras");

                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            row.put(metaData.getColumnName(i), querryOutput.getObject(i));
                        }
                        data.add(row);

                        int intStare = stare ? 1 : 0;

                        System.out.println(idf + "             " + numef + "                " + intStare + "                " + oras);
                    }

                } catch(Exception e){
                    e.printStackTrace();
                }

                tableView.setItems(data);

                break;

                case 2:
                    System.out.println("idc                  numec               culoare            masa             oras");

                    connectQuerry = "SELECT * FROM Componente\n" +
                            "WHERE masa BETWEEN 100 AND 500\n" +
                            "AND oras = 'Cluj-Napoca';";

                    tableView.getColumns().clear();
                    data = FXCollections.observableArrayList();


                    try{

                        Statement statement = connectDB.createStatement();
                        ResultSet querryOutput = statement.executeQuery(connectQuerry);

                        ResultSetMetaData metaData = querryOutput.getMetaData();
                        int columnCount = metaData.getColumnCount();

                        // Dynamically create table columns
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);

                            // Explicitly specifying the type for MapValueFactory
                            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String, Object>, Object>, ObservableValue<Object>>() {
                                @Override
                                public ObservableValue<Object> call(TableColumn.CellDataFeatures<Map<String, Object>, Object> p) {
                                    return new SimpleObjectProperty<>(p.getValue().get(columnName));
                                }
                            });

                            tableView.getColumns().add(column);
                        }

                        while(querryOutput.next()){

                            String idc = querryOutput.getString("idc");
                            String numec = querryOutput.getString("numec");
                            String culoare = querryOutput.getString("culoare");
                            int masa = querryOutput.getInt("masa");
                            String oras = querryOutput.getString("oras");

                            System.out.println(idc + "             " + numec + "                " + culoare + "                " + masa + "                " + oras);

                            Map<String, Object> row = new HashMap<>();
                            for (int i = 1; i <= columnCount; i++) {
                                row.put(metaData.getColumnName(i), querryOutput.getObject(i));
                            }
                            data.add(row);
                        }

                    } catch(Exception e){
                        e.printStackTrace();
                    }

                    tableView.setItems(data);

                    break;


                    case 3:

                        System.out.println("numep                  numec               oras");

                        connectQuerry = "SELECT Proiecte.numep, Componente.numec, Proiecte.oras\n" +
                                "FROM Proiecte\n" +
                                "JOIN Livrari ON Proiecte.idp = Livrari.idp\n" +
                                "JOIN Componente ON Livrari.idc = Componente.idc\n" +
                                "WHERE Proiecte.oras = Componente.oras;";

                        tableView.getColumns().clear();
                        data = FXCollections.observableArrayList();

                        try{

                            Statement statement = connectDB.createStatement();
                            ResultSet querryOutput = statement.executeQuery(connectQuerry);

                            ResultSetMetaData metaData = querryOutput.getMetaData();
                            int columnCount = metaData.getColumnCount();

                            for (int i = 1; i <= columnCount; i++) {
                                String columnName = metaData.getColumnName(i);
                                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);

                                // Explicitly specifying the type for MapValueFactory
                                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String, Object>, Object>, ObservableValue<Object>>() {
                                    @Override
                                    public ObservableValue<Object> call(TableColumn.CellDataFeatures<Map<String, Object>, Object> p) {
                                        return new SimpleObjectProperty<>(p.getValue().get(columnName));
                                    }
                                });

                                tableView.getColumns().add(column);
                            }


                            while(querryOutput.next()){

                                String numep = querryOutput.getString("numep");
                                String numec = querryOutput.getString("numec");
                                String oras = querryOutput.getString("oras");

                                System.out.println(numep + "             " + numec + "                " + oras);

                                Map<String, Object> row = new HashMap<>();
                                for (int i = 1; i <= columnCount; i++) {
                                    row.put(metaData.getColumnName(i), querryOutput.getObject(i));
                                }
                                data.add(row);
                            }

                        } catch(Exception e){
                            e.printStackTrace();
                        }

                        tableView.setItems(data);

                        break;


                    case 4:

                        System.out.println("idp1             idp2                ");

                        connectQuerry = "SELECT DISTINCT\n" +
                                "    a.idp AS idp1, \n" +
                                "    b.idp AS idp2\n" +
                                "FROM \n" +
                                "    Livrari a\n" +
                                "INNER JOIN \n" +
                                "    Livrari b ON a.idf = b.idf AND a.idc = b.idc\n" +
                                "WHERE \n" +
                                "    a.idp < b.idp;";

                        tableView.getColumns().clear();
                        data = FXCollections.observableArrayList();

                        try{

                            Statement statement = connectDB.createStatement();
                            ResultSet querryOutput = statement.executeQuery(connectQuerry);

                            ResultSetMetaData metaData = querryOutput.getMetaData();
                            int columnCount = metaData.getColumnCount();

                            for (int i = 1; i <= columnCount; i++) {
                                String columnName = metaData.getColumnName(i);
                                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);

                                // Explicitly specifying the type for MapValueFactory
                                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String, Object>, Object>, ObservableValue<Object>>() {
                                    @Override
                                    public ObservableValue<Object> call(TableColumn.CellDataFeatures<Map<String, Object>, Object> p) {
                                        return new SimpleObjectProperty<>(p.getValue().get(columnName));
                                    }
                                });

                                tableView.getColumns().add(column);
                            }


                            while(querryOutput.next()){

                                String idp1 = querryOutput.getString("idp1");
                                String idp2 = querryOutput.getString("idp2");

                                System.out.println(idp1 + "             " + idp2 + "                ");

                                Map<String, Object> row = new HashMap<>();
                                for (int i = 1; i <= columnCount; i++) {
                                    row.put(metaData.getColumnName(i), querryOutput.getObject(i));
                                }
                                data.add(row);
                            }

                        } catch(Exception e){
                            e.printStackTrace();
                        }

                        tableView.setMaxWidth(100);
                        tableView.setLayoutX(680);

                        tableView.setItems(data);

                        break;


                    case 5:
                        System.out.println("numec");

                        /** PROCEDURA **/

                        tableView.getColumns().clear();
                        data = FXCollections.observableArrayList();

                        try (CallableStatement statement = connectDB.prepareCall("{call Componente_Bistrita()}")) {
                            ResultSet querryOutput = statement.executeQuery();

                            ResultSetMetaData metaData = querryOutput.getMetaData();
                            int columnCount = metaData.getColumnCount();

                            for (int i = 1; i <= columnCount; i++) {
                                String columnName = metaData.getColumnName(i);
                                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);

                                // Explicitly specifying the type for MapValueFactory
                                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String, Object>, Object>, ObservableValue<Object>>() {
                                    @Override
                                    public ObservableValue<Object> call(TableColumn.CellDataFeatures<Map<String, Object>, Object> p) {
                                        return new SimpleObjectProperty<>(p.getValue().get(columnName));
                                    }
                                });

                                tableView.getColumns().add(column);
                            }

                            while (querryOutput.next()) {
                                String numec = querryOutput.getString("numec");

                                Map<String, Object> row = new HashMap<>();
                                for (int i = 1; i <= columnCount; i++) {
                                    row.put(metaData.getColumnName(i), querryOutput.getObject(i));
                                }
                                data.add(row);

                                System.out.println(numec);

                            }

                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                        tableView.setItems(data);

                        break;


                    case 6:
                        System.out.println("numep");

                        connectQuerry = "SELECT numep \n" +
                                "FROM Proiecte \n" +
                                "WHERE EXISTS (\n" +
                                "    SELECT 1\n" +
                                "    FROM Furnizori\n" +
                                "    WHERE Furnizori.oras = Proiecte.oras AND Furnizori.idf = 'F001'\n" +
                                ");";

                        tableView.getColumns().clear();
                        data = FXCollections.observableArrayList();

                        try{

                            Statement statement = connectDB.createStatement();
                            ResultSet querryOutput = statement.executeQuery(connectQuerry);

                            ResultSetMetaData metaData = querryOutput.getMetaData();
                            int columnCount = metaData.getColumnCount();

                            for (int i = 1; i <= columnCount; i++) {
                                String columnName = metaData.getColumnName(i);
                                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);

                                // Explicitly specifying the type for MapValueFactory
                                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String, Object>, Object>, ObservableValue<Object>>() {
                                    @Override
                                    public ObservableValue<Object> call(TableColumn.CellDataFeatures<Map<String, Object>, Object> p) {
                                        return new SimpleObjectProperty<>(p.getValue().get(columnName));
                                    }
                                });

                                tableView.getColumns().add(column);
                            }


                            while(querryOutput.next()){

                                String numep = querryOutput.getString("numep");

                                Map<String, Object> row = new HashMap<>();
                                for (int i = 1; i <= columnCount; i++) {
                                    row.put(metaData.getColumnName(i), querryOutput.getObject(i));
                                }
                                data.add(row);

                                System.out.println(numep);
                            }

                        } catch(Exception e){
                            e.printStackTrace();
                        }

                        tableView.setItems(data);

                        break;


                    case 7:

                        System.out.println("oras           numar proiecte           numar componente           numar furnizori");

                        connectQuerry = "SELECT \n" +
                                "    oras,\n" +
                                "    (SELECT COUNT(*) FROM Proiecte WHERE oras = P.oras) AS număr_proiecte,\n" +
                                "    (SELECT COUNT(*) FROM Componente WHERE oras = P.oras) AS număr_componente,\n" +
                                "    (SELECT COUNT(*) FROM Furnizori WHERE oras = P.oras) AS număr_furnizori\n" +
                                "FROM \n" +
                                "    (SELECT oras FROM Proiecte\n" +
                                "     UNION \n" +
                                "     SELECT oras FROM Componente\n" +
                                "     UNION \n" +
                                "     SELECT oras FROM Furnizori) P;";

                        tableView.getColumns().clear();
                        data = FXCollections.observableArrayList();

                        try{

                            Statement statement = connectDB.createStatement();
                            ResultSet querryOutput = statement.executeQuery(connectQuerry);

                            ResultSetMetaData metaData = querryOutput.getMetaData();
                            int columnCount = metaData.getColumnCount();

                            for (int i = 1; i <= columnCount; i++) {
                                String columnName = metaData.getColumnName(i);
                                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);

                                // Explicitly specifying the type for MapValueFactory
                                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String, Object>, Object>, ObservableValue<Object>>() {
                                    @Override
                                    public ObservableValue<Object> call(TableColumn.CellDataFeatures<Map<String, Object>, Object> p) {
                                        return new SimpleObjectProperty<>(p.getValue().get(columnName));
                                    }
                                });

                                tableView.getColumns().add(column);
                            }

                            tableView.autosize();

                            while(querryOutput.next()){

                                String oras = querryOutput.getString("oras");
                                String numar_proiecte = querryOutput.getString("număr_proiecte");
                                String numar_componente = querryOutput.getString("număr_componente");
                                String numar_furnizori = querryOutput.getString("număr_furnizori");

                                System.out.println(oras + "           " + numar_proiecte + "           " + numar_componente + "           " + numar_furnizori);

                                Map<String, Object> row = new HashMap<>();
                                for (int i = 1; i <= columnCount; i++) {
                                    row.put(metaData.getColumnName(i), querryOutput.getObject(i));
                                }
                                data.add(row);
                            }

                        } catch(Exception e){
                            e.printStackTrace();
                        }

                        tableView.setMinHeight(290);
                        tableView.setMinWidth(425);

                        tableView.setLayoutX(525);
                        tableView.setLayoutY(-50);

                        tableView.setItems(data);

                        break;


                    case 8:

                        System.out.println("cantitate minima          cantitate maxima");

                        connectQuerry = "SELECT \n" +
                                "    MIN(cantitate) AS cantitate_minimă,\n" +
                                "    MAX(cantitate) AS cantitate_maximă\n" +
                                "FROM \n" +
                                "    Livrari\n" +
                                "WHERE \n" +
                                "    idc = 'C12';";

                        tableView.getColumns().clear();
                        data = FXCollections.observableArrayList();

                        try{

                            Statement statement = connectDB.createStatement();
                            ResultSet querryOutput = statement.executeQuery(connectQuerry);

                            ResultSetMetaData metaData = querryOutput.getMetaData();
                            int columnCount = metaData.getColumnCount();

                            for (int i = 1; i <= columnCount; i++) {
                                String columnName = metaData.getColumnName(i);
                                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);

                                // Explicitly specifying the type for MapValueFactory
                                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String, Object>, Object>, ObservableValue<Object>>() {
                                    @Override
                                    public ObservableValue<Object> call(TableColumn.CellDataFeatures<Map<String, Object>, Object> p) {
                                        return new SimpleObjectProperty<>(p.getValue().get(columnName));
                                    }
                                });

                                tableView.getColumns().add(column);
                            }


                            while(querryOutput.next()){

                                String cantitateMinimă = querryOutput.getString("cantitate_minimă");
                                String cantitateMaximă = querryOutput.getString("cantitate_maximă");

                                System.out.println(cantitateMinimă + "                       " + cantitateMaximă);

                                Map<String, Object> row = new HashMap<>();
                                for (int i = 1; i <= columnCount; i++) {
                                    row.put(metaData.getColumnName(i), querryOutput.getObject(i));
                                }
                                data.add(row);
                            }

                        } catch(Exception e){
                            e.printStackTrace();
                        }

                        tableView.setItems(data);

                        break;

                    default:
                        System.out.println("Eroare");
            }
    }

    private void adjustTableViewHeight(TableView<Map<String, Object>> tableView, int rowCount) {
        // Base height for table header
        double baseHeight = 30.0;

        // Height of each row (adjust as needed)
        double rowHeight = 24.0;

        // Calculate total height
        double totalHeight = baseHeight + rowCount * rowHeight;

        // Set the preferred height
        tableView.setPrefHeight(totalHeight);
    }

    @FXML
    protected void firstOP() {
        OP = 1;
        System.out.println(OP);
    }

    @FXML
    protected void secondOP() {
        OP = 2;
        System.out.println(OP);
    }

    @FXML
    protected void thirdOP() {
        OP = 3;
        System.out.println(OP);
    }

    @FXML
    protected void fourthOP() {
        OP = 4;
        System.out.println(OP);
    }

    @FXML
    protected void fifthOP() {
        OP = 5;
        System.out.println(OP);
    }

    @FXML
    protected void sixthOP() {
        OP = 6;
        System.out.println(OP);
    }

    @FXML
    protected void seventhOP() {
        OP = 7;
        System.out.println(OP);
    }

    @FXML
    protected void eighthOP() {
        OP = 8;
        System.out.println(OP);
    }
}