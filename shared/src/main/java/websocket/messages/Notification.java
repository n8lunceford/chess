package websocket.messages;

public class Notification extends ServerMessage {

    private String message;

    public Notification(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }
}
