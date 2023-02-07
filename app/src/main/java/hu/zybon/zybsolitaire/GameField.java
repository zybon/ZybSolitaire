package hu.zybon.zybsolitaire;

import android.content.Context;
import android.view.MotionEvent;

import java.util.ArrayList;

import hu.zybon.zybsolitaire.names.MeshesNames;
import zybandroid.opengl.blenderdatas.MeshDataCollector;
import zybandroid.opengl.blenderdatas.MeshDataCollectors;
import zybandroid.opengl.gui.GUI_Button;
import zybandroid.opengl.gui.TexturedQuad;
import zybandroid.opengl.text.TextObject;
import zybandroid.opengl.util.Constants;
import zybandroid.opengl.util.SaveManager;
import zybandroid.opengl.util.TextureHelper;
import zybandroid.opengl.util.ZybColor;
import zybandroid.util.touchevents.ZybMultiTouchEvent;

/**
 * @author zybon
 * Created 2021. 09. 09. 21:06
 */
public class GameField implements SaveManager.Saveable {
    public static final String TAG = "GameField";

    private static final int NON_USED_PLACE = 0;
    private static final int BALL_IN_PLACE = 1;
    private static final int EMPTY_PLACE = 2;
    private static final int BALL_SELECTED = 3;

    private TexturedQuad background;

    private TexturedQuad golyo;

    private ZybColor selectColor = new ZybColor(0x66ffffff);
    private ZybColor remainColor = new ZybColor(0x44ff0000);

    private final ArrayList<int[]> moves = new ArrayList<>();

    private final int[] startState = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0,
            0, 0, 1, 1, 1, 2, 1, 1, 1, 0, 0,
            0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0,
            0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    private final int STATE_COLUMN_NUMBER = 11;
    private final int STATE_ROW_NUMBER = 11;

    private final int[] gameState = new int[startState.length];

    private final float X_OFFSET = 0.263f;
    private final float Y_OFFSET = 0.263f/MainRenderer.getScreenAspectRatio();

    private final float START_X = -5f*X_OFFSET;
    private final float START_Y = 5f*Y_OFFSET;

    private int selectedIndex = -1;

    private final TextObject timeText;
    private final TextObject gameOverText;
    private long timeWhenLastPausedInMs;
    private long timerStartTimeInMs;
    private long currentTimeInMs;

    private boolean timerActive = false;

    private boolean playable = true;
    private boolean gameOver = true;




    private final Context context;

    public GameField(Context context) {
        this.context = context;
        background = new TexturedQuad(context);
        background.setScaleY(1/MainRenderer.getScreenAspectRatio());
        background.setGlTextureId(TextureHelper.getGlTextureIdFromResID(context, "main", R.drawable.jatekter));

        golyo = new TexturedQuad(context);
        golyo.setScaleX(0.11f);
        golyo.setScaleY(0.11f/MainRenderer.getScreenAspectRatio());
        golyo.setGlTextureId(TextureHelper.getGlTextureIdFromResID(context, "main", R.drawable.golyo));

        MeshDataCollector dashBoardCollector = MeshDataCollectors.getCollector(MeshesNames.Dashboard.collector_file_name);
        GUI_Button timeField = new GUI_Button(dashBoardCollector.getMeshData(MeshesNames.Dashboard.time_field));
        timeText = GameRenderer.textRenderer.createTextObject("00:00");
        timeText.setAlign(TextObject.Align.TOP_RIGHT);
        timeText.setPositionInGL(timeField.getGlPosX(), timeField.getGlPosY());
        timeText.setScale2(0.1f, 0.1f/MainRenderer.getScreenAspectRatio());
        timeText.setAlwaysDrawable(true);
        timeText.setColor(ZybColor.WHITE);
        timeText.setBackgroundColorTop(ZybColor.DARKGREY);
        timeText.setBackgroundColorBottom(ZybColor.LIGHTGREY);
        timeText.showBackground();

        gameOverText = GameRenderer.textRenderer.createTextObject("");
        gameOverText.setAlign(TextObject.Align.TOP_CENTER);
        gameOverText.setPositionInGL(0, 0.85f);
        gameOverText.setScale2(0.1f, 0.1f/MainRenderer.getScreenAspectRatio());
        gameOverText.setAlwaysDrawable(false);
        gameOverText.setColor(ZybColor.WHITE);
    }

    public void startNewGame(){
        System.arraycopy(startState, 0, gameState, 0, startState.length);
        timerActive = false;
        timeText.setText("00:00");
        playable = true;
        gameOver = false;
        gameOverText.setAlwaysDrawable(false);
        currentTimeInMs = 0;
        timeWhenLastPausedInMs = 0;
    }

    public void onResume(){
//        if (timeWhenLastPausedInMs>0){
//            startTimer();
//        }
    }

    public void pause(){
        stopTimer();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private boolean isPlayable(){
        if (selectedIndex>0){return true;}
        for (int i = 0; i < gameState.length; i++) {
            if (isSelecteable(i)) {
                return true;
            }
        }
        return false;
    }

    private void startTimer(){
        timerStartTimeInMs = System.currentTimeMillis();
        timerActive = true;
    }

    public void stopTimer(){
        timerActive = false;
        timeWhenLastPausedInMs = currentTimeInMs;
    }

    private void updateTime(){
        currentTimeInMs = timeWhenLastPausedInMs+(System.currentTimeMillis()-timerStartTimeInMs);
    }

    private String getTimeString(){
        int sec = (int)(currentTimeInMs/ 1000);
        int minute = sec/60;
        return toDigitFormat(minute)+":"+toDigitFormat(sec%60);
    }

    private String toDigitFormat(int number){
        return (number<10?"0"+number:""+number);
    }

    private void checkGame(){
        playable = isPlayable();
        if (!playable) {
            stopTimer();
            int remain = 0;
            for (int i = 0; i < gameState.length; i++) {
                if (gameState[i] == 1) {
                    remain++;
                }
            }
            gameOverText.setText(context.getString(R.string.game_over)+"\n" +
                    context.getString(R.string.time)+": "+getTimeString()+"\n"+
                    context.getString(R.string.remain)+": "+remain);
            gameOverText.setAlwaysDrawable(true);
            moves.clear();
            gameOver = true;
        }
        else {
            updateTime();
        }
    }

    public void draw(){
        background.draw();
        if (playable) {
            checkGame();
            drawInGame();
        }
        else {
            drawEndGame();
        }


    }

    private void drawInGame(){
        if (timerActive) {
            timeText.setText(getTimeString());
        }

        float x, y;
        for (int i = 0; i < gameState.length; i++) {
            if (gameState[i]==BALL_IN_PLACE){
                x = START_X + ((float)(i%STATE_COLUMN_NUMBER))*X_OFFSET;
                y = START_Y - ((float)(i/STATE_COLUMN_NUMBER))*Y_OFFSET;
                golyo.setBaseColor(ZybColor.EMPTY);
                golyo.setOffsetX(x);
                golyo.setOffsetY(y);
                golyo.draw();
            }
            if (gameState[i]==BALL_SELECTED){
                x = START_X + ((float)(i%STATE_COLUMN_NUMBER))*X_OFFSET;
                y = START_Y - ((float)(i/STATE_COLUMN_NUMBER))*Y_OFFSET;
                golyo.setBaseColor(selectColor);
                golyo.setOffsetX(x);
                golyo.setOffsetY(y);
                golyo.draw();
            }
        }
    }

    private void drawEndGame(){
        float x, y;
        for (int i = 0; i < gameState.length; i++) {
            if (gameState[i]==BALL_IN_PLACE){
                x = START_X + ((float)(i%STATE_COLUMN_NUMBER))*X_OFFSET;
                y = START_Y - ((float)(i/STATE_COLUMN_NUMBER))*Y_OFFSET;
                golyo.setBaseColor(remainColor);
                golyo.setOffsetX(x);
                golyo.setOffsetY(y);
                golyo.draw();
            }
        }
    }


    public void onTouchEvent(ZybMultiTouchEvent zybMultiTouchEvent) {
        if (!playable) {
            return;
        }
        MotionEvent e = zybMultiTouchEvent.getCurrentEvent();
        if (e.getAction() == MotionEvent.ACTION_DOWN){
            float norm_x = MainRenderer.calcNormalizedX(e.getX());
            float norm_y = MainRenderer.calcNormalizedY(e.getY());
            int c = (int)((norm_x+X_OFFSET/2-START_X)/X_OFFSET);
            int r = -(int)((norm_y-Y_OFFSET/2-START_Y)/Y_OFFSET);
            int index = r*STATE_COLUMN_NUMBER+c;
            if (c<0 || r<0 || c>STATE_COLUMN_NUMBER-1 || r>STATE_ROW_NUMBER-1) {
//                MainRenderer.sendDebugInfo("none", 1000);
                clearSelection();
            }
            else {
                checkTable(index);
            }

        }
    }

    private void clearSelection(){
        if (selectedIndex>0) {
            gameState[selectedIndex] = 1;
            selectedIndex = -1;
        }
    }

    private void checkTable(int index){
        if (selectedIndex>0){
            if (index == selectedIndex){
                clearSelection();
                return;
            }
            if (index == selectedIndex-2*STATE_COLUMN_NUMBER){
                if (gameState[selectedIndex-STATE_COLUMN_NUMBER] == BALL_IN_PLACE && gameState[index] == EMPTY_PLACE){
                    saveLastMove();
                    gameState[selectedIndex] = EMPTY_PLACE;
                    gameState[selectedIndex-STATE_COLUMN_NUMBER] = EMPTY_PLACE;
                    gameState[index] = BALL_IN_PLACE;
                    selectedIndex = -1;
                    return;
                }
            }
            if (index == selectedIndex+2*STATE_COLUMN_NUMBER){
                if (gameState[selectedIndex+STATE_COLUMN_NUMBER] == BALL_IN_PLACE && gameState[index] == EMPTY_PLACE){
                    saveLastMove();
                    gameState[selectedIndex] = EMPTY_PLACE;
                    gameState[selectedIndex+STATE_COLUMN_NUMBER] = EMPTY_PLACE;
                    gameState[index] = BALL_IN_PLACE;
                    selectedIndex = -1;
                    return;
                }
            }
            if (index == selectedIndex-2){
                if (gameState[selectedIndex-1] == BALL_IN_PLACE && gameState[index] == EMPTY_PLACE){
                    saveLastMove();
                    gameState[selectedIndex] = EMPTY_PLACE;
                    gameState[selectedIndex-1] = EMPTY_PLACE;
                    gameState[index] = BALL_IN_PLACE;
                    selectedIndex = -1;
                    return;
                }
            }
            if (index == selectedIndex+2){
                if (gameState[selectedIndex+1] == BALL_IN_PLACE && gameState[index] == EMPTY_PLACE){
                    saveLastMove();
                    gameState[selectedIndex] = EMPTY_PLACE;
                    gameState[selectedIndex+1] = EMPTY_PLACE;
                    gameState[index] = BALL_IN_PLACE;
                    selectedIndex = -1;
                    return;
                }
            }
            clearSelection();
        }
        if (index != selectedIndex && selectedIndex>0){
            clearSelection();
        }


        if (isSelecteable(index)) {
            if (!timerActive){
                startTimer();
            }
            gameState[index] = BALL_SELECTED;
            selectedIndex = index;
        }
    }

    private boolean isSelecteable(int index){
        if (gameState[index]!=BALL_IN_PLACE) {
            return false;
        }
        if (gameState[index-1] == BALL_IN_PLACE && gameState[index-2] == EMPTY_PLACE){
            return true;
        }
        if (gameState[index+1] == BALL_IN_PLACE && gameState[index+2] == EMPTY_PLACE){
            return true;
        }
        if (gameState[index-STATE_COLUMN_NUMBER] == BALL_IN_PLACE && gameState[index-2*STATE_COLUMN_NUMBER] == EMPTY_PLACE){
            return true;
        }
        if (gameState[index+STATE_COLUMN_NUMBER] == BALL_IN_PLACE && gameState[index+2*STATE_COLUMN_NUMBER] == EMPTY_PLACE){
            return true;
        }
        return false;
    }

    private void saveLastMove(){
        int[] move = new int[gameState.length];
        System.arraycopy(gameState, 0, move, 0, gameState.length);
        for (int i = 0; i < gameState.length; i++) {
            if (move[i] == BALL_SELECTED){
                move[i] = BALL_IN_PLACE;
            }
        }
        moves.add(move);
    }

    public void undoLastMove(){
        if (moves.size() > 0) {
            int[] lastMove = moves.remove(moves.size() - 1);
            System.arraycopy(lastMove, 0, gameState, 0, gameState.length);
        }
    }

    public boolean isUndoable(){
        if (playable){
            if (moves.size()>0){
                return true;
            }
        }
        return false;
    }



    @Override
    public String getSaveLabel() {
        return TAG;
    }

    @Override
    public String getSavedData() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gameState.length; i++) {
            sb.append(gameState[i]);
        }
        sb.append('|');
        sb.append(currentTimeInMs);
        return sb.toString();
    }

    @Override
    public void loadSavedData(String savedData) {

        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = Integer.parseInt(""+savedData.charAt(i));
        }
        String[] saved = savedData.split("\\|");
        currentTimeInMs = Long.parseLong(saved[1]);
        timeText.setText(getTimeString());
        stopTimer();
    }
}
