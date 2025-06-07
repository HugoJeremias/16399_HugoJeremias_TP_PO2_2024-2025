// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoardModel {
    private Monster monster;
    private List<SnowBall> snowballs;
    private List<Snowman> snowmen;
    private List<List<PositionContent>> board;

    public BoardModel() {}

    public void loadLevelFromFile(String filename) throws IOException {
        List<List<PositionContent>> grid = new ArrayList<>();
        List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(filename));
        this.snowballs = new ArrayList<>();
        this.snowmen = new ArrayList<>();
        int rowIdx = 0;
        for (String line : lines) {
            List<PositionContent> row = new ArrayList<>();
            String[] tokens = line.split(" ");
            for (int colIdx = 0; colIdx < tokens.length; colIdx++) {
                String token = tokens[colIdx];
                switch (token) {
                    case ".":
                        row.add(PositionContent.NO_SNOW);
                        break;
                    case "*":
                        row.add(PositionContent.SNOW);
                        break;
                    case "#":
                        row.add(PositionContent.BLOCK);
                        break;
                    case "S":
                        row.add(PositionContent.NO_SNOW);
                        this.snowballs.add(new SnowBall(new Position(rowIdx, colIdx), SnowBallType.SMALL));
                        break;
                    case "X":
                        row.add(PositionContent.NO_SNOW);
                        this.monster = new Monster(new Position(rowIdx, colIdx));
                        break;
                    case "A":
                        row.add(PositionContent.NO_SNOW);
                        this.snowballs.add(new SnowBall(new Position(rowIdx, colIdx), SnowBallType.AVERAGE));
                        break;
                    case "B":
                        row.add(PositionContent.NO_SNOW);
                        this.snowballs.add(new SnowBall(new Position(rowIdx, colIdx), SnowBallType.BIG));
                        break;
                    default:
                        row.add(PositionContent.SNOW);
                }
            }
            grid.add(row);
            rowIdx++;
        }
        this.board = grid;
    }

    private List<List<PositionContent>> copyBoard(List<List<PositionContent>> board) {
        List<List<PositionContent>> copy = new ArrayList<>();
        for (List<PositionContent> row : board) {
            copy.add(new ArrayList<>(row));
        }
        return copy;
    }

    public PositionContent getPositionContent(int row, int col) {
        if (isOutOfBounds(row, col)) {
            System.out.println("Invalid position");
            return PositionContent.NO_SNOW;
        }
        return board.get(row).get(col);
    }

    private boolean isOutOfBounds(int row, int col) {
        return row < 0 || row >= board.size() || col < 0 || col >= board.get(0).size();
    }

    public void setPositionContent(int row, int col, PositionContent content) {
        if (!isOutOfBounds(row, col)) {
            board.get(row).set(col, content);
        }
    }

    public boolean isValidPosition(Position position) {
        int row = position.getRow();
        int col = position.getCol();
        return !isOutOfBounds(row, col);
    }

    public Monster getMonster() {
        return monster;
    }

    public List<SnowBall> getSnowballs() {
        return snowballs;
    }

    public List<Snowman> getSnowmen() {
        return new ArrayList<>(this.snowmen);
    }

    public void updateBoard() {
        clearSnowballs();
        updateSnowmen();
    }

    private void clearSnowballs() {
        for (SnowBall snowball : snowballs) {
            Position pos = snowball.getPosition();
            setPositionContent(pos.getRow(), pos.getCol(), PositionContent.NO_SNOW);
        }
    }

    private void updateSnowmen() {
        for (Snowman snowman : snowmen) {
            Position pos = snowman.getPosition();
            setPositionContent(pos.getRow(), pos.getCol(), PositionContent.SNOWMAN);
        }
    }

    public SnowBall getSnowballAt(Position position) {
        for (SnowBall snowball : snowballs) {
            if (snowball.getPosition().equals(position)) {
                return snowball;
            }
        }
        return null;
    }

    public boolean hasSnowballAt(Position position) { return getSnowballAt(position) != null; }

    public boolean isPositionEmpty(Position position) {
        if (!isValidPosition(position)) return false;
        if (monster.getPosition().equals(position)) return false;
        if (hasSnowballAt(position)) return false;
        for (Snowman snowman : snowmen) {
            if (snowman.getPosition().equals(position)) return false;
        }
        return true;
    }

    public void checkForSnowman() {
        for (SnowBall base : new ArrayList<>(snowballs)) {
            if (base.getType() != SnowBallType.BIG_AVERAGE) continue;
            SnowBall top = findSmallBallOnBase(base);
            if (top != null) {
                createSnowman(base, top);
            }
        }
    }

    private SnowBall findSmallBallOnBase(SnowBall base) {
        for (SnowBall sb : snowballs) {
            if (sb != base && sb.getType() == SnowBallType.SMALL && sb.getPosition().equals(base.getPosition())) {
                return sb;
            }
        }
        return null;
    }

    private void createSnowman(SnowBall base, SnowBall top) {
        Snowman snowman = new Snowman(base.getPosition(), base, null, top);
        snowmen.add(snowman);
        snowballs.remove(base);
        snowballs.remove(top);
        updateBoard();
    }


}