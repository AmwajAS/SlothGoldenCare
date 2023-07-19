package com.example.slothgoldencare.sudoko;

import android.os.Bundle;

import com.example.slothgoldencare.R;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import android.content.Intent;

/**
 * The Game activity represents the Sudoku game board and provides functionality to play the game.
 */
public class Game extends Activity {
    private static final String TAG = "Sudoku";
    public static final String KEY_DIFFICULTY = "org.example.sudoku.difficulty";
    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_MEDIUM = 1;
    public static final int DIFFICULTY_HARD = 2;
    public static final int DIFFICULTY_CONTINUE = -1;
    private static final String key = "puzzle";

    private final String easyPuzzle =
            "362581479914237856785694231" +
                    "170462583823759614546813927" +
                    "431925768657148392298376140";
    private final String mediumPuzzle =
            "650000070000506000014000005" +
                    "007009000002314700000700800" +
                    "500000630000201000030000097";
    private final String hardPuzzle =
            "009000000080605020501078000" +
                    "000000700706040102004000000" +
                    "000720903090301080000000600";

    private int puzzle[] = new int[9 * 9];

    private PuzzleView puzzleView;

    /**
     * Called when the activity is first created.
     * Sets up the game board and initializes the puzzle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
        if (savedInstanceState != null) {
            puzzle = fromPuzzleString(savedInstanceState.getString(key));
        } else
            puzzle = getPuzzle(diff);

        calculateUsedTiles();

        //defines tile that was already filled by the game
        calculateUsedTilesIndex(question);
        //-------------------------------------------------------------------------
        puzzleView = new PuzzleView(this);
        setContentView(puzzleView);
        puzzleView.requestFocus();
    }


    /**
     * Displays the keypad or an error message when a tile is clicked.
     *
     * @param x The x-coordinate of the clicked tile.
     * @param y The y-coordinate of the clicked tile.
     */
    public void showKeypadOrError(int x, int y) {
        int tiles[] = getUsedTiles(x, y);
        if (tiles.length == 9) {
            Toast toast = Toast.makeText(this, R.string.no_moves_label, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Log.d(TAG, "showKeypad: used=" + toPuzzleString(tiles));
            Dialog v = new Keypad(this, tiles, puzzleView);
            v.show();
        }
    }

    /**
     * Sets the tile value if it's a valid move.
     *
     * @param x     The x-coordinate of the tile.
     * @param y     The y-coordinate of the tile.
     * @param value The value to be set.
     * @return True if the move is valid and the tile is set, false otherwise.
     */
    public boolean setTileIfValid(int x, int y, int value) {
        int tiles[] = getUsedTiles(x, y);
        if (value != 0) {
            for (int tile : tiles) {
                if (tile == value) {
                    return false;
                }
            }
        }
        setTile(x, y, value);
        calculateUsedTiles();
        return true;
    }

    private final int used[][][] = new int[9][9][];

    /**
     * Retrieves the array of used tiles for the given coordinates.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     * @return An array containing the used tiles.
     */
    protected int[] getUsedTiles(int x, int y) {
        return used[x][y];
    }

    private int[] calculateUsedTiles(int x, int y) {
        int c[] = new int[9];
        //horizontal
        for (int i = 0; i < 9; i++) {
            if (i == y) {
                continue;
            }
            int t = getTile(x, i);
            if (t != 0) {
                c[t - 1] = t;
            }
        }
        //vertical
        for (int i = 0; i < 9; i++) {
            if (i == x) {
                continue;
            }
            int t = getTile(i, y);
            if (t != 0) {
                c[t - 1] = t;
            }
        }

        // same cell block
        int startx = (x / 3) * 3;
        int starty = (y / 3) * 3;
        for (int i = startx; i < startx + 3; i++) {
            for (int j = starty; j < starty + 3; j++) {
                if (i == x && j == y)
                    continue;
                int t = getTile(i, j);
                if (t != 0)
                    c[t - 1] = t;
            }
        }

        // compress
        int nused = 0;
        for (int t : c) {
            if (t != 0) {
                nused++;
            }
        }
        int c1[] = new int[nused];
        nused = 0;
        for (int t : c) {
            if (t != 0) {
                c1[nused++] = t;
            }
        }
        return c1;
    }

    /**
     * Calculates the used tiles for each tile on the board.
     */
    private void calculateUsedTiles() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                used[x][y] = calculateUsedTiles(x, y);
            }
        }
    }

    /**
     * Retrieves the initial puzzle based on the selected difficulty level.
     *
     * @param diff The selected difficulty level.
     * @return An array representing the initial puzzle.
     */
    private int[] getPuzzle(int diff) {
        String puz;
        // TODO: Continue last game
        switch (diff) {
            case DIFFICULTY_CONTINUE:
                puz = getPreferences(MODE_PRIVATE).getString(key, easyPuzzle);
                break;
            case DIFFICULTY_HARD:
                puz = hardPuzzle;
                break;
            case DIFFICULTY_MEDIUM:
                puz = mediumPuzzle;
                break;
            case DIFFICULTY_EASY:
            default:
                puz = easyPuzzle;
                break;
        }
        return fromPuzzleString(puz);
    }

    /**
     * Converts the puzzle array to a string representation.
     *
     * @param puz The puzzle array.
     * @return A string representation of the puzzle.
     */
    static private String toPuzzleString(int[] puz) {
        StringBuilder buf = new StringBuilder();
        for (int element : puz) {
            buf.append(element);
        }
        return buf.toString();
    }


    /**
     * Converts the puzzle string to an array representation.
     *
     * @param string The puzzle string.
     * @return An array representation of the puzzle.
     */
    static protected int[] fromPuzzleString(String string) {
        int[] puz = new int[string.length()];
        for (int i = 0; i < puz.length; i++) {
            puz[i] = string.charAt(i) - '0';
        }
        return puz;
    }


    private int getTile(int x, int y) {
        return puzzle[y * 9 + x];
    }

    private void setTile(int x, int y, int value) {
        puzzle[y * 9 + x] = value;
    }

    protected String getTileString(int x, int y) {
        int v = getTile(x, y);
        if (v == 0) {
            return "";
        } else {
            return String.valueOf(v);
        }
    }
    //--ma nguon them boi Tunglx-----------------------------------------------
    //----chuc nang: khong cho phep sua cac o da co san trong de bai-----------

    protected final int question[][] = new int[81][2];
    protected int usedNum = 0;

    private void calculateUsedTilesIndex(int[][] c) // Ham nay duoc sua de co the tinh toan cac index
    // cua cac o da su dung.
    {
        int k = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                final int t = getTile(i, j);
                if (t != 0) {
                    c[k][0] = i;
                    c[k][1] = j;
                    k++;
                }
            }
        }
        usedNum = k; // dem so luong o da duoc su dung, phuc vu cho ham` isFinish()
    }

    protected int[][] getUsedIndex() {
        return question;
    }
    //-------------------------------------------------------------------------


    // Ma nguon them boi Tunglx------------------------------------------------
    // Xac dinh khi nao thi het game (tat ca cac o deu duoc dien.)
    protected boolean isFinish() {
        for (int j : puzzle) {
            if (j == 0) {
                return false; // There is at least one empty tile
            }
        }
        return true; // All tiles are filled
    }


    protected void callCongratScreen() {
        Intent intent = new Intent(Game.this, CongratScreen.class);
        startActivity(intent);
        finish();
    }

    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        getPreferences(MODE_PRIVATE).edit().putString(key, toPuzzleString(puzzle)).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(key, toPuzzleString(puzzle));
        super.onSaveInstanceState(outState);
    }
}