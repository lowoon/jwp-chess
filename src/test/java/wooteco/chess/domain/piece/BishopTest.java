package wooteco.chess.domain.piece;

import static org.assertj.core.api.Assertions.*;
import static wooteco.chess.domain.player.PlayerColor.*;

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

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.exception.InvalidMovementException;
import wooteco.chess.domain.player.PlayerColor;

class BishopTest {

    private GamePiece gamePiece;

    @BeforeEach
    void setUp() {
        gamePiece = new Bishop(PlayerColor.WHITE);
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
            Arguments.of(Position.from("a1"), Position.from("d4"),
                Arrays.asList(Position.from("b2"), Position.from("c3"))),
            Arguments.of(Position.from("d4"), Position.from("a1"),
                Arrays.asList(Position.from("c3"), Position.from("b2"))),
            Arguments.of(Position.from("b2"), Position.from("f6"),
                Arrays.asList(Position.from("c3"), Position.from("d4"), Position.from("e5"))),
            Arguments.of(Position.from("f6"), Position.from("b2"),
                Arrays.asList(Position.from("e5"), Position.from("d4"), Position.from("c3"))),
            Arguments.of(Position.from("g5"), Position.from("f4"), Collections.emptyList())
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
            Arguments.of(Position.from("c5")),
            Arguments.of(Position.from("e5")),
            Arguments.of(Position.from("d6")),
            Arguments.of(Position.from("g3"))
        );
    }

    @Test
    @DisplayName("장애물이 있을 경우")
    void obstacle() {
        Map<Position, GamePiece> boardMap = new TreeMap<>(
            BoardFactory.EMPTY_BOARD.getBoard());
        Position source = Position.from("d5");
        Position target = Position.from("f7");

        Position obstacle = Position.from("e6");

        boardMap.put(source, gamePiece);
        boardMap.put(obstacle, new Pawn(BLACK));

        Board board = BoardFactory.of(boardMap);

        assertThatThrownBy(() -> {
            gamePiece.validateMoveTo(board, source, target);
        }).isInstanceOf(InvalidMovementException.class)
            .hasMessage("이동할 수 없습니다.\n경로에 기물이 존재합니다.");
    }
}