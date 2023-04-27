import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public class Client extends Thread{


	Socket socketClient;

	ObjectOutputStream out;
	ObjectInputStream in;

	private Consumer<Serializable> callback;

	Client(Consumer<Serializable> call){

		callback = call;
	}

	public void run() {
		try {
			socketClient = new Socket("127.0.0.1", 5555);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		} catch (Exception e) {
			callback.accept("Connection Failed");
		}

		while (true) {
			try {
				String data = in.readObject().toString();
				callback.accept(data);
				Object obj = in.readObject();
				if (obj instanceof String) {
					String clientList = (String) obj;
					if (!clientList.isEmpty()) {
						String[] clients = clientList.split("\n");
						Platform.runLater(() -> {
							Controller.connectedClientsList.clear();
							Controller.connectedClientsList.addAll(clients);
						});
					} else {
						Platform.runLater(() -> {
							Controller.connectedClientsList.clear();
						});
					}
				}
			} catch (Exception e) {
				callback.accept("Connection closed");
				break;
			}
		}
	}


	public void send(String data) {

		try {
			out.writeObject(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void startListening() {
		Thread listener = new Thread(() -> {
			while (true) {
				try {
					String message = in.readObject().toString();
					Platform.runLater(() -> Controller.chatView.getItems().add(message));
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		listener.setDaemon(true);
		listener.start();
	}


}
