package websocket.messages;

import chess.ChessBoard;
import chess.ChessGame;

public class LoadGame extends ServerMessage {

    private ChessGame game;
    //private boolean isBlack;

    public LoadGame(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
        //this.isBlack = isBlack;
    }

    public ChessGame getGame() {
        return game;
    }
}
