package facade;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

public interface Observer {
    void loadGame(LoadGame loadGame);
    void errorMessage(ErrorMessage errorMessage);
    void notification(Notification notification);
}
