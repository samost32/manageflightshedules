package com.example.manageflightshedules;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.example.manageflightshedules.database.connectDb;

public class ApplicationController {
    @FXML
    public TextField search_priceEdit;
    @FXML
    public TextField search_Time;
    @FXML
    public DatePicker search_date;
    @FXML
    public Button searchButton;
    @FXML
    private TableView<Flight> addEmployee_tableView;
    @FXML
    private TableColumn<Flight, String> date;
    @FXML
    private TableColumn<Flight, String> time;
    @FXML
    private TableColumn<Flight, String> from;
    @FXML
    private TableColumn<Flight, String> to;
    @FXML
    private TableColumn<Flight, String> flightNumber;
    @FXML
    private TableColumn<Flight, String> aircraft;
    @FXML
    private TableColumn<Flight, Double> economyPrice;
    @FXML
    private TableColumn<Flight, Double> businessPrice;
    @FXML
    private TableColumn<Flight, Double> firstClassPrice;

    @FXML
    private Button cancelFlightBtn;

    @FXML
    private Button editFlightBtn;


    @FXML
    private Button minimize;

    @FXML
    private TextField search_FlightNumber;

    @FXML
    private ComboBox<String> search_From;

    @FXML
    private DatePicker search_Outbound;

    @FXML
    private ComboBox<String> search_To;

    private ObservableList<Flight> flightData;
    private Set<Integer> selectedIndexes;


    @FXML
    void close() {
        System.exit(0);
    }

    @FXML
    void minimize(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    private void filterTable(String fromValue, String flightNumberValue) {
        // Создание фильтрованного списка для хранения соответствующих объектов Flight
        ObservableList<Flight> filteredList = FXCollections.observableArrayList();

        // Проверка каждого объекта Flight в flightData и добавление соответствующих объектов в filteredList
        for (Flight flight : flightData) {
            boolean match = true;

            if (fromValue != null && !fromValue.isEmpty()) {
                // Проверка соответствия значения "from"
                if (!flight.getFrom().equals(fromValue)) {
                    match = false;
                }
            }

            if (flightNumberValue != null && !flightNumberValue.isEmpty()) {
                // Проверка соответствия значения "flightNumber"
                if (!flight.getFlightNumber().equals(flightNumberValue)) {
                    match = false;
                }
            }

            if (match) {
                filteredList.add(flight);
            }
        }

        // Установка фильтрованного списка в таблицу
        addEmployee_tableView.setItems(filteredList);
    }


    @FXML
    void initialize() {

        ObservableList<String> fromOptions = FXCollections.observableArrayList("AUH", "CAI", "BAH", "ADE", "DOH", "RUH",null);
        search_From.setItems(fromOptions);
        search_From.getSelectionModel().selectFirst();

        // ...

        // Установка обработчика события для кнопки searchButton
        searchButton.setOnAction(event -> {
            String selectedFrom = search_From.getValue();
            String selectedFlightNumber = search_FlightNumber.getText();



            // Фильтрация данных в таблице
            filterTable(selectedFrom, selectedFlightNumber);
        });


        selectedIndexes = new HashSet<>();
        // Установка соединения с базой данных
        Connection connection = connectDb();
        if (connection == null) {
            System.out.println("Ошибка подключения к базе данных");
            return;
        }

        try {
            // Запрос на получение данных из таблицы (пример)
            Statement statement = connection.createStatement();
            String selectQuery = "SELECT * FROM flight";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // Создание списка объектов Flight для хранения данных таблицы
            flightData = FXCollections.observableArrayList();

            // Заполнение списка данными из результата запроса
            while (resultSet.next()) {
                Flight flight = new Flight(resultSet.getString("date"), resultSet.getString("time"), resultSet.getString("from"), resultSet.getString("to"), resultSet.getString("flightNumber"), resultSet.getString("aircraft"), resultSet.getDouble("economyPrice"), resultSet.getDouble("businessPrice"), resultSet.getDouble("firstClassPrice"));
                flightData.add(flight);
            }

            // Закрытие результата и оператора
            resultSet.close();
            statement.close();

            // Привязка данных к столбцам таблицы
            date.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
            time.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
            from.setCellValueFactory(cellData -> cellData.getValue().fromProperty());
            to.setCellValueFactory(cellData -> cellData.getValue().toProperty());
            flightNumber.setCellValueFactory(cellData -> cellData.getValue().flightNumberProperty());
            aircraft.setCellValueFactory(cellData -> cellData.getValue().aircraftProperty());
            economyPrice.setCellValueFactory(cellData -> cellData.getValue().economyPriceProperty().asObject());
            businessPrice.setCellValueFactory(cellData -> cellData.getValue().businessPriceProperty().asObject());
            firstClassPrice.setCellValueFactory(cellData -> cellData.getValue().firstClassPriceProperty().asObject());

            // Установка данных в таблицу
            addEmployee_tableView.setItems(flightData);

            Set<Integer> selectedIndexes = new HashSet<>();

            // Установка обработчика события для кнопки editFlightBtn
            cancelFlightBtn.setOnAction(event -> {
                // Получение выбранных строк из таблицы
                ObservableList<Flight> selectedFlights = addEmployee_tableView.getSelectionModel().getSelectedItems();

                // Очистка множества выбранных индексов
                selectedIndexes.clear();

                // Добавление индексов выбранных строк в множество
                for (Flight flight : selectedFlights) {
                    selectedIndexes.add(flightData.indexOf(flight));
                }

                // Обновление отображения таблицы
                addEmployee_tableView.refresh();
            });

            // Установка стилей для строк в таблице
            addEmployee_tableView.setRowFactory(tv -> new TableRow<>() {
                @Override
                protected void updateItem(Flight item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        // Стиль для пустых строк
                        setStyle("");
                    } else {
                        // Проверка, является ли индекс строки выбранным
                        if (selectedIndexes.contains(getIndex())) {
                            // Стиль для выбранных строк (красный цвет)
                            setStyle("-fx-background-color: red;");
                        } else {
                            // Стиль для остальных строк
                            setStyle("");
                        }
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void filterTableByFrom(String fromValue) {
        // Создайте отфильтрованный список для хранения соответствующих объектов полета
        ObservableList<Flight> filteredList = FXCollections.observableArrayList();

        // Выполните итерацию по данным рейса и проверьте, соответствует ли свойство "from" выбранному значению
        for (Flight flight : flightData) {
            if (flight.getFrom().equals(fromValue)) {
                filteredList.add(flight);
            }
        }

        // Установите отфильтрованный список в качестве элементов в TableView
        addEmployee_tableView.setItems(filteredList);
    }

    @FXML
    void editFlight() {
        // Считывание данных из элементов управления
        LocalDate date = search_date.getValue();
        String time = search_Time.getText();
        double economyPrice = Double.parseDouble(search_priceEdit.getText());

        // Получение выбранного объекта Flight из таблицы
        Flight selectedFlight = addEmployee_tableView.getSelectionModel().getSelectedItem();
        if (selectedFlight == null) {
            System.out.println("Не выбрана строка для обновления");
            return;
        }

        // Обновление свойств выбранного объекта Flight
        selectedFlight.setDate(date.toString());
        selectedFlight.setTime(time);
        selectedFlight.setEconomyPrice(economyPrice);

        // Установка соединения с базой данных
        Connection connection = connectDb();
        if (connection == null) {
            System.out.println("Ошибка подключения к базе данных");
            return;
        }

        try {
            // Подготовка SQL-запроса для обновления данных в таблице
            String updateQuery = "UPDATE flight SET date = ?, time = ?, economyPrice = ?";

            // Создание объекта PreparedStatement для выполнения запроса
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setString(1, selectedFlight.getDate());
            statement.setString(2, selectedFlight.getTime());
            statement.setDouble(3, selectedFlight.getEconomyPrice());


            // Выполнение запроса
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Данные успешно обновлены в базе данных");
            } else {
                System.out.println("Не удалось обновить данные в базе данных");
            }

            // Закрытие оператора
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void searchByOutboundDate(ActionEvent event) {
        LocalDate selectedDate = search_Outbound.getValue();

        // Создайте отфильтрованный список для хранения соответствующих объектов 
        ObservableList<Flight> filteredList = FXCollections.observableArrayList();

        // Выполните итерацию по данным рейса и проверьте, соответствует ли свойство "дата" выбранной дате
        for (Flight flight : flightData) {
            LocalDate flightDate = LocalDate.parse(flight.getDate());
            if (flightDate.isEqual(selectedDate)) {
                filteredList.add(flight);
            }
        }

        // Обновите табличцу с отфильтрованным списком
        addEmployee_tableView.setItems(filteredList);

        String flightNumber = search_FlightNumber.getText().trim();

        // Создайте отфильтрованный список для хранения соответствующих объектов 
        ObservableList<Flight> filteredList2 = FXCollections.observableArrayList();

        // Повторите данные рейса и проверьте, соответствует ли свойство "Номер рейса" введенному значению
        for (Flight flight : flightData) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                filteredList2.add(flight);
            }
        }

        addEmployee_tableView.setItems(filteredList2);
    }

    @FXML
    void clearFields() {

        search_From.getSelectionModel().selectFirst();

        // Сброс фильтрации и отображение всех данных в таблице
        addEmployee_tableView.setItems(flightData);
        // Очистите поле выбора даты
        search_date.setValue(null);
        // Очистите поля текстового поля и установите значения по умолчанию
        search_Time.setText("00:00:00");
        search_priceEdit.clear();


    }


}

    





