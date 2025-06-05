// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {

    private View view;

    @Test
    void testMonsterToTheLeft() throws IOException {
        GameModel gameModel = new GameModel(null, "AAA");
        System.out.println("Original Monster position: " + gameModel.getMonster().getPosition());
        boolean moved = gameModel.moveMonster(Direction.LEFT);
        assertTrue(moved);
        System.out.println("New Monster position: " + gameModel.getMonster().getPosition());
    }

    @Test
    void testCreateAverageSnowball() throws IOException {
        GameModel gameModel = new GameModel(null,"AAA");
        System.out.println("Monster position: " + gameModel.getMonster().getPosition());
        System.out.println(gameModel.getSnowballs().get(0).getType() +  " position: " + gameModel.getSnowballs().get(0).getPosition());
        System.out.println(gameModel.getSnowballs().get(1).getType() +  " position: " + gameModel.getSnowballs().get(1).getPosition());
        System.out.println(gameModel.getSnowballs().get(2).getType() +  " position: " + gameModel.getSnowballs().get(2).getPosition());
        System.out.println(gameModel.getSnowballs().get(3).getType() +  " position: " + gameModel.getSnowballs().get(3).getPosition());

        gameModel.moveMonster(Direction.RIGHT);
        System.out.println("New type: " + gameModel.getSnowballs().get(0).getType() + " in position: " + gameModel.getSnowballs().get(0).getPosition());

        assertEquals(SnowBallType.AVERAGE, gameModel.getSnowballs().get(0).getType());
    }

    @Test
    void testCreateBigSnowball() throws IOException {

        GameModel gameModel = new GameModel(null, "AAA");
        System.out.println("Monster position: " + gameModel.getMonster().getPosition());
        System.out.println(gameModel.getSnowballs().get(0).getType() +  " position: " + gameModel.getSnowballs().get(0).getPosition());
        System.out.println(gameModel.getSnowballs().get(1).getType() +  " position: " + gameModel.getSnowballs().get(1).getPosition());
        System.out.println(gameModel.getSnowballs().get(2).getType() +  " position: " + gameModel.getSnowballs().get(2).getPosition());
        System.out.println(gameModel.getSnowballs().get(3).getType() +  " position: " + gameModel.getSnowballs().get(3).getPosition());
        gameModel.moveMonster(Direction.LEFT);
        gameModel.moveMonster(Direction.DOWN);
        gameModel.moveMonster(Direction.RIGHT);
        System.out.println("New type: " + gameModel.getSnowballs().get(1).getType() + " in position: " + gameModel.getSnowballs().get(1).getPosition());

        assertEquals(SnowBallType.BIG, gameModel.getSnowballs().get(1).getType());
    }

    @Test
    void testMaintainBigSnowball() throws IOException {
        GameModel gameModel = new GameModel(null, "AAA");

        System.out.println("Monster position: " + gameModel.getMonster().getPosition());
        System.out.println(gameModel.getSnowballs().get(0).getType() +  " position: " + gameModel.getSnowballs().get(0).getPosition());
        System.out.println(gameModel.getSnowballs().get(1).getType() +  " position: " + gameModel.getSnowballs().get(1).getPosition());
        System.out.println(gameModel.getSnowballs().get(2).getType() +  " position: " + gameModel.getSnowballs().get(2).getPosition());
        System.out.println(gameModel.getSnowballs().get(3).getType() +  " position: " + gameModel.getSnowballs().get(3).getPosition());

        gameModel.moveMonster(Direction.LEFT);
        gameModel.moveMonster(Direction.DOWN);
        gameModel.moveMonster(Direction.RIGHT);

        System.out.println("New type: " + gameModel.getSnowballs().get(1).getType() + " in position: " + gameModel.getSnowballs().get(1).getPosition());

        gameModel.moveMonster(Direction.RIGHT);

        System.out.println("Same type: " + gameModel.getSnowballs().get(1).getType() + " in position: " + gameModel.getSnowballs().get(1).getPosition());

        assertEquals(SnowBallType.BIG, gameModel.getSnowballs().get(1).getType());
    }

    @Test
    void testBigAverage() throws IOException {
        GameModel gameModel = new GameModel(null, "AAA");

        System.out.println("Monster position: " + gameModel.getMonster().getPosition());
        System.out.println(gameModel.getSnowballs().get(0).getType() +  " position: " + gameModel.getSnowballs().get(0).getPosition());
        System.out.println(gameModel.getSnowballs().get(1).getType() +  " position: " + gameModel.getSnowballs().get(1).getPosition());
        System.out.println(gameModel.getSnowballs().get(2).getType() +  " position: " + gameModel.getSnowballs().get(2).getPosition());
        System.out.println(gameModel.getSnowballs().get(3).getType() +  " position: " + gameModel.getSnowballs().get(3).getPosition());

        gameModel.moveMonster(Direction.DOWN);

        System.out.println("New type: " + gameModel.getSnowballs().get(2).getType() + " in position: " + gameModel.getSnowballs().get(2).getPosition());

        assertEquals(SnowBallType.BIG_AVERAGE, gameModel.getSnowballs().get(2).getType());
    }

    @Test
    void testCompleteSnowman() throws IOException {
        GameModel gameModel = new GameModel(null, "AAA");

        System.out.println("Monster position: " + gameModel.getMonster().getPosition());
        System.out.println(gameModel.getSnowballs().get(0).getType() +  " position: " + gameModel.getSnowballs().get(0).getPosition());
        System.out.println(gameModel.getSnowballs().get(1).getType() +  " position: " + gameModel.getSnowballs().get(1).getPosition());
        System.out.println(gameModel.getSnowballs().get(2).getType() +  " position: " + gameModel.getSnowballs().get(2).getPosition());
        System.out.println(gameModel.getSnowballs().get(3).getType() +  " position: " + gameModel.getSnowballs().get(3).getPosition());

        gameModel.moveMonster(Direction.DOWN);
        gameModel.moveMonster(Direction.LEFT);
        gameModel.moveMonster(Direction.LEFT);
        gameModel.moveMonster(Direction.DOWN);
        gameModel.moveMonster(Direction.RIGHT);
        gameModel.moveMonster(Direction.RIGHT);

        System.out.println("New snowman in position: " + gameModel.getSnowmen().get(0).getPosition());

        assertTrue(gameModel.getSnowmen().get(0).isValid());
    }
}