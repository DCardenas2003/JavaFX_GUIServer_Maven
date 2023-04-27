
import com.sun.javafx.collections.ElementObservableListDecorator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class Controller implements Initializable {


    Client clientConnection;


    static ObservableList<String> connectedClientsList;


    @FXML
    private ListView<String> ClientList;







    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ClientList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        connectedClientsList = FXCollections.observableArrayList();
        ClientList.setItems(connectedClientsList);
        for (Server.ClientThread client : Server.clients) { // iterate over the clients array list
            connectedClientsList.add("Client #" + client.count + "\n"); // add each client to the observable list
        }
        clientConnection = new Client(data -> {
            Platform.runLater(() -> {
                if (data instanceof Server.ClientThread) { // check if the data is a ClientThread
                    Server.ClientThread client = (Server.ClientThread) data;
                    connectedClientsList.add("Client #" + client.count + "\n"); // add the client to the list
                } else {
                    ClientList.getItems().add(data.toString()); // add other data to the list
                }
            });
        });
        clientConnection.start();
    }


    public void DeleteUser(ActionEvent actionEvent) {
    }

    public void ChooseUsers(ActionEvent actionEvent) {
    }
}

