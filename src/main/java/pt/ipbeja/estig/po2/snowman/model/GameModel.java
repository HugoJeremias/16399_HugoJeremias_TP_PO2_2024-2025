// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GameModel {
    private final View view;
    private final BoardModel boardModel = new BoardModel();
    private int level = 1, movesCount = 0;
    private String filename = "Levels/level" + level + ".txt", movesString;
    private String playerName = "";
    private Monster monster;

    public GameModel(View view, String playerName) {
        this.view = view;
        this.playerName = playerName;
        try {
            boardModel.loadLevelFromFile(filename);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public GameModel(View view, int level) {
        this.view = view;
        this.level = level;
        this.filename = "Levels/level" + level + ".txt";
        try {
            boardModel.loadLevelFromFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void stackSnowballs(SnowBall base, SnowBall top) {
        if (base.getType() == SnowBallType.BIG && top.getType() == SnowBallType.AVERAGE) {
            base.setType(SnowBallType.BIG_AVERAGE);
            boardModel.getSnowballs().remove(top);
        } else if (base.getType() == SnowBallType.BIG && top.getType() == SnowBallType.SMALL) {
            base.setType(SnowBallType.BIG_SMAL);
            boardModel.getSnowballs().remove(top);
        } else if (base.getType() == SnowBallType.AVERAGE && top.getType() == SnowBallType.SMALL) {
            base.setType(SnowBallType.AVERAGE_SMALL);
            boardModel.getSnowballs().remove(top);
        }
        else if (base.getType() == SnowBallType.BIG_AVERAGE && top.getType() == SnowBallType.SMALL) {
            top.setPosition(base.getPosition());
            boardModel.checkForSnowman();
            boardModel.updateBoard();
            return;
        }
        boardModel.updateBoard();
    }


     public boolean moveMonster(Direction direction) throws IOException {
        this.monster = boardModel.getMonster();
        Position currentPos = monster.getPosition();
        Position newPos = currentPos.getPositionAt(direction);
        boolean moved = false;

        if (!boardModel.isValidPosition(newPos)) return false;

        PositionContent nextContent = boardModel.getPositionContent(newPos.getRow(), newPos.getCol());

        if (nextContent == PositionContent.BLOCK) return false;

        if (boardModel.isPositionEmpty(newPos)) {
            monster.setPosition(newPos);
            boardModel.updateBoard();
        }
        SnowBall snowball = boardModel.getSnowballAt(newPos);
        if(snowball != null) moved = checkForSnowballs(snowball, newPos, direction);

        if (moved && isGameComplete()) {
            view.showGameCompleteMessage(true, boardModel.getSnowmen().get(0));
        }
        return moved;
    }


    public Boolean checkForSnowballs(SnowBall snowball, Position newPos, Direction direction) {
        if (isAverageSmallSplit(snowball, newPos, direction)) return false;
        if (isBigAverageSplit(snowball, newPos, direction)) return false;

        Position nextPos = newPos.getPositionAt(direction);
        if (!boardModel.isValidPosition(nextPos)) return false;
        if (boardModel.getPositionContent(nextPos.getRow(), nextPos.getCol()) == PositionContent.BLOCK) return false;

        SnowBall target = boardModel.getSnowballAt(nextPos);
        if (target != null) return handleStackOrBlock(snowball, target, newPos);

        if (boardModel.getPositionContent(nextPos.getRow(), nextPos.getCol()) == PositionContent.SNOW) snowball.grow();

        if (boardModel.isPositionEmpty(nextPos)) {
            moveSnowballAndMonster(snowball, nextPos, newPos);
            return true;
        }
        return true;
    }

    private boolean isAverageSmallSplit(SnowBall snowball, Position newPos, Direction direction) {
        if (snowball.getType() == SnowBallType.AVERAGE_SMALL) {
            Position topPos = newPos.getPositionAt(direction);
            if (boardModel.isValidPosition(topPos) && boardModel.isPositionEmpty(topPos)) {
                SnowBall small = new SnowBall(topPos, SnowBallType.SMALL);
                boardModel.getSnowballs().add(small);
                snowball.setType(SnowBallType.AVERAGE);
                boardModel.updateBoard();
            }
            return true;
        }
        return false;
    }

    private boolean isBigAverageSplit(SnowBall snowball, Position newPos, Direction direction) {
        if (snowball.getType() == SnowBallType.BIG_AVERAGE) {
            Position topPos = newPos.getPositionAt(direction);
            if (boardModel.isValidPosition(topPos) && boardModel.isPositionEmpty(topPos)) {
                SnowBall average = new SnowBall(topPos, SnowBallType.AVERAGE);
                boardModel.getSnowballs().add(average);
                snowball.setType(SnowBallType.BIG);
                boardModel.updateBoard();
            }
            return true;
        }
        return false;
    }

    private boolean handleStackOrBlock(SnowBall snowball, SnowBall target, Position newPos) {
        if (snowball.getType().isSmallerThan(target.getType()) ||
                (target.getType() == SnowBallType.BIG_AVERAGE && snowball.getType() == SnowBallType.SMALL)) {
            stackSnowballs(target, snowball);
            this.monster.setPosition(newPos);
            boardModel.updateBoard();
            boardModel.checkForSnowman();
            return true;
        }
        return false;
    }

    private void moveSnowballAndMonster(SnowBall snowball, Position nextPos, Position newPos) {
        snowball.setPosition(nextPos);
        this.monster.setPosition(newPos);
        boardModel.updateBoard();
        boardModel.checkForSnowman();
    }


    public PositionContent getPositionContent(int row, int col) {
        return boardModel.getPositionContent(row, col);
    }

    public Monster getMonster() {
        return boardModel.getMonster();
    }

    public List<SnowBall> getSnowballs() {
        return boardModel.getSnowballs();
    }

    public List<Snowman> getSnowmen() {
        return boardModel.getSnowmen();
    }

    public boolean isGameComplete() throws IOException {
        if (boardModel.getSnowballs().isEmpty() && !boardModel.getSnowmen().isEmpty()) {
            saveGameToFile(String.valueOf(level), movesString, movesCount, this.getSnowmen().get(0).getPosition());
            return true;
        } else {
            return false;
        }
    }

    public boolean hasSnowman(Position position, double CELL_SIZE) {
        for (Snowman snowman : getSnowmen()) {
            Position base = snowman.getPosition();
            if (base.equals(position)) {
                return true;
            }
        }
        return false;
    }

    public void saveGameToFile(String levelFile, String moves, int movesCount, Position snowmanPos) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String filename = "Scores/snowman" + timestamp + ".txt";
        try (PrintWriter out = new PrintWriter(filename)) {
            List<String> mapLines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get("Levels/level" + levelFile + ".txt"));
            out.println("Jogador:" + this.playerName);
            out.println("Mapa:" + this.level);
            for (String line : mapLines) {
                out.println(line);
            }
            out.println("Movimentos:" + moves);
            out.println("Total de movimentos: " + movesCount);
            out.println("Posição do boneco de neve: (" + (snowmanPos.getRow() + 1) + "," + (char)('A' + snowmanPos.getCol()) + ")");
        }
    }

    public String updateMovesValue(int lastRow, int lastCol, int newRow, int newCol) {
        String move = "(" + (lastRow + 1) + "," + (char)('A' + lastCol) + ") -> (" +
                (newRow + 1) + "," + (char)('A' + newCol) + ")";
        this.movesString += ", " + move;
        return move;
    }
}

