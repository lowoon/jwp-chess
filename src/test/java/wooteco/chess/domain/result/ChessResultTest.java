package wooteco.chess.domain.result;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Bishop;
import wooteco.chess.domain.piece.GamePiece;
import wooteco.chess.domain.piece.Knight;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Rook;
import wooteco.chess.domain.player.PlayerColor;

class ChessResultTest {

    @Test
    @DisplayName("초기 board score 계산")
    void calculateScore() {
        Board board = BoardFactory.createInitialBoard();
        ChessResult chessResult = ChessResult.from(board.getBoard());
        Map<PlayerColor, Score> scores = chessResult.getResult();
        Map<PlayerColor, Score> expected = new HashMap<>();
        expected.put(PlayerColor.BLACK, Score.from(38));
        expected.put(PlayerColor.WHITE, Score.from(38));

        assertThat(scores).isEqualTo(expected);
    }

    @Test
    @DisplayName("같은 player, 같은 column의 pawn이 여러개 있는 경우 score 계산")
    void calculateScoreWhiteSameColumnPawn() {
        Map<Position, GamePiece> map = new TreeMap<>(
                BoardFactory.EMPTY_BOARD.getBoard());
        map.put(Position.from("d5"), new Pawn(PlayerColor.WHITE));
        map.put(Position.from("d6"), new Pawn(PlayerColor.WHITE));
        map.put(Position.from("f3"), new Pawn(PlayerColor.WHITE));
        map.put(Position.from("f4"), new Pawn(PlayerColor.WHITE));
        map.put(Position.from("f6"), new Pawn(PlayerColor.WHITE));
        map.put(Position.from("h3"), new Pawn(PlayerColor.WHITE));
        ChessResult chessResult = ChessResult.from(map);

        assertThat(chessResult.getResult().get(PlayerColor.WHITE)).isEqualTo(Score.from(3.5));
    }

    @Test
    @DisplayName("여러 말 점수 계산")
    void calculateScoreWithSomePieces() {
        Map<Position, GamePiece> map = new TreeMap<>(
                BoardFactory.EMPTY_BOARD.getBoard());
        map.put(Position.from("d5"), new Pawn(PlayerColor.WHITE));
        map.put(Position.from("d6"), new Pawn(PlayerColor.WHITE));
        map.put(Position.from("f3"), new Pawn(PlayerColor.WHITE));
        map.put(Position.from("f4"), new Pawn(PlayerColor.WHITE));
        map.put(Position.from("f6"), new Pawn(PlayerColor.WHITE));
        map.put(Position.from("h3"), new Pawn(PlayerColor.WHITE));
        map.put(Position.from("d7"), new Bishop(PlayerColor.WHITE));
        map.put(Position.from("d8"), new Rook(PlayerColor.WHITE));
        map.put(Position.from("a8"), new Knight(PlayerColor.WHITE));
        ChessResult chessResult = ChessResult.from(map);

        assertThat(chessResult.getResult().get(PlayerColor.WHITE)).isEqualTo(Score.from(14));
    }
}