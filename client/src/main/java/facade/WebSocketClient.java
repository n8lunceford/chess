package facade;

import com.google.gson.Gson;
import websocket.commands.*;
import websocket.messages.LoadGame;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

public class WebSocketClient extends Endpoint {


    public Session session;

    public WebSocketClient(Observer observer) throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {


                try {
                    ServerMessage myMessage = new Gson().fromJson(message, ServerMessage.class);
                    //String username = getUsername(command.getAuthString());
                    switch (myMessage.getServerMessageType()) {
                        case LOAD_GAME -> observer.loadGame(new Gson().fromJson(message, LoadGame.class));
                        case ERROR -> {
                            MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
                            makeMove(session, username, makeMove);
                        }
                        case NOTIFICATION -> leaveGame(session, username, (Leave) command);
                    }

                    System.out.println(message);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }


}
