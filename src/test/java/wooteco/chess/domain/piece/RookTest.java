package chess.domain.piece;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.exception.InvalidMovementException;
import chess.domain.player.PlayerColor;

class RookTest {

    private GamePiece gamePiece;

    @BeforeEach
    void setUp() {
        gamePiece = new Rook(PlayerColor.WHITE);
    }

    @ParameterizedTest
    @DisplayName("이동 경로 찾기")
    @MethodSource("createSourceToTarget")
    void findMovePath(Position source, Position target, List<Position> expected) {
        Map<Position, GamePiece> boardMap = new TreeMap<>(
                BoardFactory.EMPTY_BOARD.getBoard());
        boardMap.put(source, gamePiece);

        Board board = BoardFactory.of(boardMap);

        assertThatCode(() -> {
            gamePiece.validateMoveTo(board, source, target);
        }).doesNotThrowAnyException();

    }

    static Stream<Arguments> createSourceToTarget() {
        return Stream.of(
                Arguments.of(Position.from("a1"), Position.from("a4"),
                        Arrays.asList(Position.from("a2"), Position.from("a3"))),
                Arguments.of(Position.from("a4"), Position.from("a1"),
                        Arrays.asList(Position.from("a3"), Position.from("a2"))),
                Arguments.of(Position.from("b2"), Position.from("f2"),
                        Arrays.asList(Position.from("c2"), Position.from("d2"), Position.from("e2"))),
                Arguments.of(Position.from("f2"), Position.from("b2"),
                        Arrays.asList(Position.from("e2"), Position.from("d2"), Position.from("c2"))),
                Arguments.of(Position.from("g5"), Position.from("g4"), Collections.emptyList())
        );
    }

    @ParameterizedTest
    @DisplayName("이동할 수 없는 source, target")
    @MethodSource("createInvalidTarget")
    void invalidMovementException(Position target) {
        Position source = Position.from("d5");
        Map<Position, GamePiece> boardMap = new TreeMap<>(
                BoardFactory.EMPTY_BOARD.getBoard());
        boardMap.put(source, gamePiece);

        Board board = BoardFactory.of(boardMap);

        assertThatThrownBy(() -> {
            gamePiece.validateMoveTo(board, source, target);
        }).isInstanceOf(InvalidMovementException.class)
                .hasMessage("이동할 수 없습니다.\n이동할 수 없는 경로입니다.");
    }

    static Stream<Arguments> createInvalidTarget() {
        return Stream.of(
                Arguments.of(Position.from("e7")),
                Arguments.of(Position.from("f6")),
                Arguments.of(Position.from("f4")),
                Arguments.of(Position.from("e3")),
                Arguments.of(Position.from("e4")),
                Arguments.of(Position.from("c4")),
                Arguments.of(Position.from("c6")),
                Arguments.of(Position.from("c7"))
        );
    }

    @Test
    @DisplayName("장애물이 있을 경우")
    void obstacle() {
        Map<Position, GamePiece> boardMap = new TreeMap<>(
                BoardFactory.EMPTY_BOARD.getBoard());
        Position source = Position.from("d5");
        Position target = Position.from("g5");

        Position obstacle = Position.from("e5");

        boardMap.put(source, gamePiece);
        boardMap.put(obstacle, new Pawn(PlayerColor.BLACK));

        Board board = BoardFactory.of(boardMap);

        assertThatThrownBy(() -> {
            gamePiece.validateMoveTo(board, source, target);
        }).isInstanceOf(InvalidMovementException.class)
                .hasMessage("이동할 수 없습니다.\n경로에 기물이 존재합니다.");
    }
}