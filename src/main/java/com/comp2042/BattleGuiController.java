package com.comp2042;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class BattleGuiController implements Initializable {

    private static final int BRICK_SIZE = 18;

    //player1 panel
    @FXML private GridPane gamePanel1;
    @FXML private GridPane brickPanel1;
    @FXML private GridPane nextPanel1;
    @FXML private GridPane holdPanel1;
    @FXML private Label scoreLabel1;
    @FXML private Label linesLabel1;
    @FXML private Label levelLabel1;
    @FXML private Label player1Label;
    @FXML private Group notification1;

    //player2 panel
    @FXML private GridPane gamePanel2;
    @FXML private GridPane brickPanel2;
    @FXML private GridPane nextPanel2;
    @FXML private GridPane holdPanel2;
    @FXML private Label scoreLabel2;
    @FXML private Label linesLabel2;
    @FXML private Label levelLabel2;
    @FXML private Label player2Label;
    @FXML private Group notification2;

    @FXML private Label gameStatusLabel;
    @FXML private Pane rootPane;
    @FXML private StackPane overlayPane;
    @FXML private Label overlayTitle;
    @FXML private Button resumeBtn;
    @FXML private Button restartBtn;
    @FXML private Button menuBtn;


    //Game display matricx
    private Rectangle[][] displayMatrix1;
    private Rectangle[][] displayMatrix2;
    private Rectangle[][] brickRectangles1;
    private Rectangle[][] brickRectangles2;

    //game timeline
    private Timeline timeLine1;
    private Timeline timeLine2;
    private Timeline hardDropTimeline1;
    private Timeline hardDropTimeline2;


    //board
    private Board board1;
    private Board board2;

    //game state
    private boolean isGameOver1 = false;
    private boolean isGameOver2 = false;
    private boolean isPaused = false;
    private boolean gameEnded = false;

    //Gamemode
    private GameMode gameMode;
    private AIDifficulty aiDifficulty;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initBattleGame(GameMode mode, AIDifficulty difficulty) {
        this.gameMode = mode;
        this.aiDifficulty = difficulty;

        //Initialize the Game
        board1 = new SimpleBoard(25, 10);
        board2 = new SimpleBoard(25, 10);

        board1.createNewBrick();
        board2.createNewBrick();

        //player2 Label
        if (mode == GameMode.VS_AI) {
            player2Label.setText("AI - " + difficulty.name());
        }

        //Initialize Display
        initGamePanel(1);
        initGamePanel(2);

        bindLabels();

        Platform.runLater(() -> {
            rootPane.getScene().setOnKeyPressed(this::handleKeyPress);
            rootPane.requestFocus();
        });

        startGameLoops();

        //Play Background Music
        SoundManager.getInstance().playBgm();
        //button
        resumeBtn.setOnAction(e -> togglePause());
        restartBtn.setOnAction(e -> restartGame());
        menuBtn.setOnAction(e -> returnToMenu());

    }



    private void initGamePanel(int playerNum) {
        Board board = (playerNum == 1) ? board1 : board2;
        GridPane gamePanel = (playerNum == 1) ? gamePanel1 : gamePanel2;
        GridPane brickPanel = (playerNum == 1) ? brickPanel1 : brickPanel2;

        int[][] boardMatrix = board.getBoardMatrix();
        Rectangle[][] displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        // 初始化游戏背景格子
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rect.setFill(Color.TRANSPARENT);
                rect.setArcHeight(4);
                rect.setArcWidth(4);
                displayMatrix[i][j] = rect;
                gamePanel.add(rect, j, i - 2);
            }
        }

        // 初始化当前方块面板
        ViewData viewData = board.getViewData();
        int[][] brickData = viewData.getBrickData();
        Rectangle[][] brickRects = new Rectangle[brickData.length][brickData[0].length];

        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rect.setFill(getFillColor(brickData[i][j]));
                rect.setArcHeight(4);
                rect.setArcWidth(4);
                brickRects[i][j] = rect;
                brickPanel.add(rect, j, i);
            }
        }

        // 设置方块面板位置
        brickPanel.setLayoutX(gamePanel.getLayoutX() + viewData.getxPosition() * (BRICK_SIZE + 1));
        brickPanel.setLayoutY(-38 + gamePanel.getLayoutY() + viewData.getyPosition() * (BRICK_SIZE + 1));

        // 保存引用
        if (playerNum == 1) {
            displayMatrix1 = displayMatrix;
            brickRectangles1 = brickRects;
        } else {
            displayMatrix2 = displayMatrix;
            brickRectangles2 = brickRects;
        }

        // 显示下一个方块
        showNextBrick(playerNum);
    }

    private void bindLabels() {
        scoreLabel1.textProperty().bind(board1.getScore().scoreProperty().asString());
        linesLabel1.textProperty().bind(board1.getScore().linesProperty().asString("Lines: %d"));
        levelLabel1.textProperty().bind(board1.getScore().levelProperty().asString("Lv: %d"));

        scoreLabel2.textProperty().bind(board2.getScore().scoreProperty().asString());
        linesLabel2.textProperty().bind(board2.getScore().linesProperty().asString("Lines: %d"));
        levelLabel2.textProperty().bind(board2.getScore().levelProperty().asString("Lv: %d"));
    }

    private void startGameLoops() {
        // 玩家1的游戏循环
        timeLine1 = new Timeline(new KeyFrame(
                Duration.millis(500),
                e -> moveDown(1)
        ));
        timeLine1.setCycleCount(Timeline.INDEFINITE);
        timeLine1.play();

        // 玩家2/AI的游戏循环
        timeLine2 = new Timeline(new KeyFrame(
                Duration.millis(500),
                e -> {
                    if (gameMode == GameMode.VS_AI && !isGameOver2 && !isPaused) {
                        performAIMove();
                    }
                    moveDown(2);
                }
        ));
        timeLine2.setCycleCount(Timeline.INDEFINITE);
        timeLine2.play();
    }

    private void performAIMove() {
        // AI简单实现：根据难度随机决定是否做出最优移动
        if (Math.random() < aiDifficulty.getAccuracy()) {
            // 尝试找一个好位置（简单策略：随机左右移动和旋转）
            int moves = (int) (Math.random() * 5) - 2; // -2 到 2
            int rotations = (int) (Math.random() * 4); // 0 到 3

            for (int i = 0; i < rotations; i++) {
                board2.rotateLeftBrick();
            }
            for (int i = 0; i < Math.abs(moves); i++) {
                if (moves < 0) {
                    board2.moveBrickLeft();
                } else {
                    board2.moveBrickRight();
                }
            }
            refreshBrick(2);
        }
    }

    private void moveDown(int playerNum) {
        moveDownInternal(playerNum, false);
    }

    private void moveDownInternal(int playerNum, boolean isUserInput) {
        if (isPaused || gameEnded) return;

        Board board = (playerNum == 1) ? board1 : board2;
        boolean isGameOver = (playerNum == 1) ? isGameOver1 : isGameOver2;

        if (isGameOver) return;

        boolean canMove = board.moveBrickDown();

        if (!canMove) {
            board.mergeBrickToBackground();
            ClearRow clearRow = board.clearRows();

            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
                board.getScore().addLines(clearRow.getLinesRemoved());
                SoundManager.getInstance().playClearSound();

                // 显示加分飘字
                Group notification = (playerNum == 1) ? notification1 : notification2;
                NotificationPanel bonus = new NotificationPanel("+" + clearRow.getScoreBonus());
                notification.getChildren().add(bonus);
                bonus.showScore(notification.getChildren());

                // 攻击机制：消4行让对手升级（加速）
                if (clearRow.getLinesRemoved() >= 4) {
                    attackOpponent(playerNum);
                }

                updateSpeed(playerNum);
            }

            if (board.createNewBrick()) {
                handleGameOver(playerNum);
            }

            refreshGameBackground(playerNum);
        } else {
            // 用户按下键软降加1分
            if (isUserInput) {
                board.getScore().add(1);
            }
        }

        refreshBrick(playerNum);
        showNextBrick(playerNum);
    }


    private void attackOpponent(int attackerPlayer) {
        int targetPlayer = (attackerPlayer == 1) ? 2 : 1;
        Board targetBoard = (targetPlayer == 1) ? board1 : board2;

        // 让对手强制升一级
        targetBoard.getScore().addLines(10);
        updateSpeed(targetPlayer);

        // 显示攻击提示
        Platform.runLater(() -> {
            Group targetNotification = (targetPlayer == 1) ? notification1 : notification2;
            NotificationPanel attackNotice = new NotificationPanel("ATTACK!");
            targetNotification.getChildren().add(attackNotice);
            attackNotice.showScore(targetNotification.getChildren());
        });
    }

    private void updateSpeed(int playerNum) {
        Timeline timeLine = (playerNum == 1) ? timeLine1 : timeLine2;
        Board board = (playerNum == 1) ? board1 : board2;

        int level = board.getScore().getLevel();
        double newRate = 1.0 + (level - 1) * 0.15;
        timeLine.setRate(newRate);
    }

    private void handleGameOver(int playerNum) {
        if (gameEnded) return;
        gameEnded = true;

        if (playerNum == 1) {
            isGameOver1 = true;
            player1Label.getStyleClass().add("loser-text");
        } else {
            isGameOver2 = true;
            player2Label.getStyleClass().add("loser-text");
        }

        // 停止两边游戏
        timeLine1.stop();
        timeLine2.stop();

        SoundManager.getInstance().playGameOverSound();

        // 显示胜者
        String winner;
        if (playerNum == 1) {
            winner = (gameMode == GameMode.VS_AI) ? "AI Wins!" : "Player 2 Wins!";
            player2Label.getStyleClass().add("winner-text");
        } else {
            winner = (gameMode == GameMode.VS_AI) ? "You Win!" : "Player 1 Wins!";
            player1Label.getStyleClass().add("winner-text");
        }
        gameStatusLabel.setText(winner);
        // 显示结束面板
        overlayTitle.setText(winner);
        resumeBtn.setVisible(false);  // 游戏结束不能继续
        overlayPane.setVisible(true);

    }

    private void refreshBrick(int playerNum) {
        Board board = (playerNum == 1) ? board1 : board2;
        GridPane brickPanel = (playerNum == 1) ? brickPanel1 : brickPanel2;
        GridPane gamePanel = (playerNum == 1) ? gamePanel1 : gamePanel2;
        Rectangle[][] brickRects = (playerNum == 1) ? brickRectangles1 : brickRectangles2;

        ViewData viewData = board.getViewData();
        int[][] brickData = viewData.getBrickData();

        brickPanel.setLayoutX(gamePanel.getLayoutX() + viewData.getxPosition() * (BRICK_SIZE + 1));
        brickPanel.setLayoutY(-38 + gamePanel.getLayoutY() + viewData.getyPosition() * (BRICK_SIZE + 1));

        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                brickRects[i][j].setFill(getFillColor(brickData[i][j]));
            }
        }
    }

    private void refreshGameBackground(int playerNum) {
        Board board = (playerNum == 1) ? board1 : board2;
        Rectangle[][] displayMatrix = (playerNum == 1) ? displayMatrix1 : displayMatrix2;
        int[][] boardMatrix = board.getBoardMatrix();

        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                displayMatrix[i][j].setFill(getFillColor(boardMatrix[i][j]));
            }
        }
    }

    private void showNextBrick(int playerNum) {
        Board board = (playerNum == 1) ? board1 : board2;
        GridPane nextPanel = (playerNum == 1) ? nextPanel1 : nextPanel2;

        int[][] nextData = board.getViewData().getNextBrickData();
        if (nextData == null) return;

        nextPanel.getChildren().clear();
        for (int i = 0; i < nextData.length; i++) {
            for (int j = 0; j < nextData[i].length; j++) {
                Rectangle rect = new Rectangle(BRICK_SIZE - 4, BRICK_SIZE - 4);
                rect.setFill(getFillColor(nextData[i][j]));
                rect.setArcHeight(4);
                rect.setArcWidth(4);
                nextPanel.add(rect, j, i);
            }
        }
    }

    private void showHoldBrick(int playerNum) {
        Board board = (playerNum == 1) ? board1 : board2;
        GridPane holdPanel = (playerNum == 1) ? holdPanel1 : holdPanel2;

        int[][] holdData = board.getViewData().getHoldBrickData();
        holdPanel.getChildren().clear();

        if (holdData == null) return;

        for (int i = 0; i < holdData.length; i++) {
            for (int j = 0; j < holdData[i].length; j++) {
                if (holdData[i][j] != 0) {
                    Rectangle rect = new Rectangle(BRICK_SIZE - 4, BRICK_SIZE - 4);
                    rect.setFill(getFillColor(holdData[i][j]));
                    rect.setArcHeight(4);
                    rect.setArcWidth(4);
                    holdPanel.add(rect, j, i);
                }
            }
        }
    }

    private void handleKeyPress(KeyEvent event) {
        // ESC 返回主菜单（任何时候都能用）
        if (event.getCode() == KeyCode.ESCAPE) {
            returnToMenu();
            event.consume();
            return;
        }

        // N 重新开始（任何时候都能用）
        if (event.getCode() == KeyCode.N) {
            restartGame();
            event.consume();
            return;
        }

        // 暂停键（只在游戏进行中有效）
        if (event.getCode() == KeyCode.P) {
            if (!gameEnded) {
                togglePause();
            }
            event.consume();
            return;
        }

        if (gameEnded) return;
        if (isPaused) return;

        // 玩家1控制 - 方向键
        if (!isGameOver1) {
            switch (event.getCode()) {
                case LEFT -> { board1.moveBrickLeft(); refreshBrick(1); }
                case RIGHT -> { board1.moveBrickRight(); refreshBrick(1); }
                case UP -> { board1.rotateLeftBrick(); refreshBrick(1); }
                case DOWN -> moveDownInternal(1, true);
                case SPACE -> hardDrop(1);
                case C -> { board1.holdCurrentBrick(); refreshBrick(1); showHoldBrick(1); showNextBrick(1); }
            }
        }

        // 玩家2控制 - WASD（仅双人模式）
        if (gameMode == GameMode.TWO_PLAYER && !isGameOver2) {
            switch (event.getCode()) {
                case A -> { board2.moveBrickLeft(); refreshBrick(2); }
                case D -> { board2.moveBrickRight(); refreshBrick(2); }
                case W -> { board2.rotateLeftBrick(); refreshBrick(2); }
                case S -> moveDownInternal(2, true);
                case E -> hardDrop(2);
                case Q -> { board2.holdCurrentBrick(); refreshBrick(2); showHoldBrick(2); showNextBrick(2); }
            }
        }

        event.consume();
    }

    private void hardDrop(int playerNum) {
        boolean isGameOver = (playerNum == 1) ? isGameOver1 : isGameOver2;
        if (isGameOver || isPaused) return;

        Timeline timeLine = (playerNum == 1) ? timeLine1 : timeLine2;
        timeLine.stop();

        Timeline hardDropTimeline = new Timeline(
                new KeyFrame(Duration.millis(30), event -> {
                    Board board = (playerNum == 1) ? board1 : board2;
                    boolean currentGameOver = (playerNum == 1) ? isGameOver1 : isGameOver2;

                    if (board.moveBrickDown()) {
                        board.getScore().add(1);  // 速降每格加2分
                        refreshBrick(playerNum);
                    } else {
                        // 落地了
                        if (playerNum == 1) {
                            hardDropTimeline1.stop();
                        } else {
                            hardDropTimeline2.stop();
                        }
                        moveDownInternal(playerNum, false);

                        if (!isPaused && !currentGameOver && !gameEnded) {
                            Timeline tl = (playerNum == 1) ? timeLine1 : timeLine2;
                            tl.play();
                        }
                    }
                })
        );
        hardDropTimeline.setCycleCount(Timeline.INDEFINITE);

        if (playerNum == 1) {
            if (hardDropTimeline1 != null) hardDropTimeline1.stop();
            hardDropTimeline1 = hardDropTimeline;
        } else {
            if (hardDropTimeline2 != null) hardDropTimeline2.stop();
            hardDropTimeline2 = hardDropTimeline;
        }

        hardDropTimeline.play();
    }


    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            timeLine1.pause();
            timeLine2.pause();
            if (hardDropTimeline1 != null) hardDropTimeline1.pause();
            if (hardDropTimeline2 != null) hardDropTimeline2.pause();
            overlayTitle.setText("PAUSED");
            resumeBtn.setVisible(true);
            overlayPane.setVisible(true);
            SoundManager.getInstance().pauseBgm();
        } else {
            if (!isGameOver1) timeLine1.play();
            if (!isGameOver2) timeLine2.play();
            overlayPane.setVisible(false);
            SoundManager.getInstance().resumeBgm();
        }
    }

    private void restartGame() {
        // 停止所有时间线
        timeLine1.stop();
        timeLine2.stop();
        if (hardDropTimeline1 != null) hardDropTimeline1.stop();
        if (hardDropTimeline2 != null) hardDropTimeline2.stop();

        // 重置状态
        isGameOver1 = false;
        isGameOver2 = false;
        isPaused = false;
        gameEnded = false;

        // 清除面板
        gamePanel1.getChildren().clear();
        gamePanel2.getChildren().clear();
        brickPanel1.getChildren().clear();
        brickPanel2.getChildren().clear();
        overlayPane.setVisible(false);
        gameStatusLabel.setText("");

        // 移除胜负样式
        player1Label.getStyleClass().removeAll("winner-text", "loser-text");
        player2Label.getStyleClass().removeAll("winner-text", "loser-text");

        // 重新初始化
        board1 = new SimpleBoard(25, 10);
        board2 = new SimpleBoard(25, 10);
        board1.createNewBrick();
        board2.createNewBrick();

        initGamePanel(1);
        initGamePanel(2);
        bindLabels();
        startGameLoops();

        SoundManager.getInstance().playBgm();
        rootPane.requestFocus();
    }

    private void returnToMenu() {
        // 停止所有时间线
        timeLine1.stop();
        timeLine2.stop();
        if (hardDropTimeline1 != null) hardDropTimeline1.stop();
        if (hardDropTimeline2 != null) hardDropTimeline2.stop();
        SoundManager.getInstance().stopBgm();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 500));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private Paint getFillColor(int colorCode) {
        return switch (colorCode) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.CYAN;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.LIMEGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BLUE;
            case 7 -> Color.ORANGE;
            default -> Color.WHITE;
        };
    }
}
