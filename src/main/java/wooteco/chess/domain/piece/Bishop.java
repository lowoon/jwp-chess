package wooteco.chess.domain.piece;

import static wooteco.chess.domain.piece.Direction.*;

import java.util.Arrays;
import java.util.List;

import wooteco.chess.domain.board.Column;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.board.Row;
import wooteco.chess.domain.player.PlayerColor;

public class Bishop extends GamePiece {

    private static final String NAME = "b";
    private static final int SCORE = 3;
    private static final int MOVE_COUNT = 8;
    private static List<Position> originalPositions = Arrays.asList(Position.of(Column.C, Row.ONE),
            Position.of(Column.F, Row.ONE));

    public Bishop(PlayerColor playerColor) {
        super(NAME, SCORE, playerColor, Arrays.asList(NE, SE, NW, SW), MOVE_COUNT);
    }

    @Override
    public List<Position> getOriginalPositions() {
        return playerColor.reviseInitialPositions(originalPositions);
    }
}
