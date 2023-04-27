
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

public class Controller implements Initializable {

    Client clientConnection;


    private ObservableList<String> connectedClientsList;


    @FXML
    private ListView<String> ClientList;






    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectedClientsList = FXCollections.observableArrayList();
        ClientList.setItems(connectedClientsList);
        clientConnection = new Client(data -> {
            Platform.runLater(() -> {
                if (data instanceof Server.ClientThread) { // check if the data is a ClientThread
                    Server.ClientThread client = (Server.ClientThread) data;
                    connectedClientsList.add("Client #" + client.count + "\n"); // add the client to the list
                } else {
                    ClientList.getItems().add(data.toString()); // add other data to the list
                }
                // Iterate over the list and print each client's name
                for (String client : connectedClientsList) {
                    System.out.println(client);
                }
            });
        });

        clientConnection.start();
    }

}

