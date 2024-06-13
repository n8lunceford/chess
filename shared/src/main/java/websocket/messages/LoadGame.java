package websocket.messages;

import chess.ChessBoard;

public class LoadGame extends ServerMessage {

    private ChessBoard game;
    //private boolean isBlack;

    public LoadGame(ServerMessageType type, ChessBoard game/**, boolean isBlack*/) {
        super(type);
        this.game = game;
        //this.isBlack = isBlack;
    }

    public ChessBoard getGame() {
        return game;
    }

//    public boolean getIsBlack() {
//        return isBlack;
//    }
}
