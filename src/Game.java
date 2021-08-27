import java.util.Random;
import java.util.Scanner;

public class Game {
    public static void main(String[] args) {

        /*for (int i = 0; i < 100; i++) {
            Random rand = new Random();
            int num = rand.nextInt(9);
            if (num < 1 || num > 8)
                System.out.println(num+1);
        }*/
        Matrix matrix = new Matrix();
        //matrix.print();

        //getUserInput(matrix);
    }

    private static void getUserInput(Matrix matrix) {
        Scanner sc = new Scanner(System.in);
        boolean continueGame = true;

        while (continueGame) {
            matrix.print();
            Coordinates coordinates = getCoordinates(sc);
            int newValue = getNewValue(sc);
            matrix.switchValue(coordinates, newValue);
            continueGame = getContinueGame(sc);
        }
        sc.close();
    }

    private static boolean getContinueGame(Scanner sc) {
        System.out.println("Continue (C) or Submit (S): ");
        String str = sc.nextLine();
        return str.equals("C");
    }

    private static int getNewValue(Scanner sc) {
        System.out.print("Enter new value: ");
        String str = sc.nextLine();
        return Integer.parseInt(str);
    }

    private static Coordinates getCoordinates(Scanner sc) {
        System.out.print("Enter coordinates to change: ");
        String strCoordinates = sc.nextLine(); //reads string
        return getMatrixCoordinates(strCoordinates);
    }

    private static Coordinates getMatrixCoordinates(String strCoordinates) {
        Coordinates coordinates = new Coordinates();
        coordinates.setCol(strCoordinates.charAt(0) - 65);
        coordinates.setRow(Integer.parseInt(strCoordinates.substring(1)) - 1);
        return coordinates;
    }
}

class Matrix {
    private static final int FIRST_ROW = 1;
    private static final int MAX_PREFILL_FAILS = 50000;
    private static final int MAX_PREFILLS = 75;
    private final Cell[][] grid;
    private int emptyCount;

    public Matrix() {
        grid = new Cell[9][9];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell();
            }
        }

        //assignValues();
        //preFillValuesRandomly(preFilledCount);
        preFillValuesSystematically(MAX_PREFILLS);
        emptyCount = countEmptyCells();
        System.out.println("emptyCount:" + emptyCount);

    }

    private void preFillValuesSystematically(int preFilledCount) {
        Random rand = new Random();

        int num;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int boxCount = 0;
                print();
                System.out.println("Filling box #" + ((i * 3) + j + 1));
                //num = rand.nextInt(9) + 1;

                while (boxCount < 9) {
                    num = boxCount + 1;
                    boolean numberFilled = false;
                    int fillFailCount = 0;
                    while (!numberFilled && fillFailCount < 10) {
                        int row = (i * 3) + rand.nextInt(3);
                        int col = (j * 3) + rand.nextInt(3);

                        if (isFillableCell(row, col, num)) {
                            grid[row][col].setValue(num);
                            numberFilled = true;
                            boxCount++;
                            //print();
                        } else
                            fillFailCount++;
                    }
                    if (!numberFilled) {
                        System.out.println("could not place the number: " + num + " in box: " + boxCount+1);
                        return;
                    }
                }
            }
        }
    }

    private void preFillValuesRandomly(int preFilledCount) {
        Random rand = new Random();
        int count = 0;
        int attempt = 0;
        int fillFailedCount = 0;
        while (count < preFilledCount && fillFailedCount < MAX_PREFILL_FAILS) {
            //System.out.println("attempt:" + attempt++ + " Filled cells:" + count + " EmptyCells:" + countEmptyCells());
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            int num = rand.nextInt(9) + 1;
            if (isFillableCell(row, col, num)) {
                grid[row][col].setOriginal(true);
                grid[row][col].setValue(num);
                count++;
                System.out.println("Filled " + row + " " + col + " " + num);
                //print();
                System.out.println("Iteration #" + count + " EmptyCells:" + countEmptyCells());
            } else {
                fillFailedCount++;
                //System.out.println("Not fillable " + row + " " + col + " " + num);
            }
        }
    }

    private boolean isFillableCell(int row, int col, int num) {
        //System.out.println("fillable? " + row + " " + col + " " + num);
        if (isBlockConflict(row, col, num)) {
            return false;
        }
        if (grid[row][col].getValue() != 0) {
            //System.out.println("row:" + row + " col:" + col + " Already populated!");
            //print();
            return false;
        }
        for (int j = 0; j < 9; j++) {
            if (grid[row][j].getValue() == num) {
                //System.out.println("row:" + row + " conflict");
                return false;
            }
        }
        for (int i = 0; i < 9; i++) {
            if (grid[i][col].getValue() == num) {
                //System.out.println("col:" + col + " conflict");
                return false;
            }
        }

        return true;
    }

    private boolean isBlockConflict(int row, int col, int num) {
        int rowBlock = row / 3;
        int colBlock = col / 3;
        //System.out.println("Row:" + row + " Col:" + col + " rowBlock:" + rowBlock + " colBlock:" + colBlock);
        for (int i = rowBlock * 3; i < rowBlock + 3; i++) {
            for (int j = colBlock * 3; j < colBlock + 3; j++) {
                if (grid[i][j].getValue() == num) {
                    //System.out.println("block conflict");
                    return true;
                }
            }
        }
        return false;
    }

    private void assignValues() {
        grid[0][0].setOriginal(true);
        grid[0][0].setValue(1);
        grid[0][2].setOriginal(true);
        grid[0][2].setValue(6);
        grid[0][4].setOriginal(true);
        grid[0][4].setValue(2);
        grid[0][5].setOriginal(true);
        grid[0][5].setValue(8);
        grid[0][6].setOriginal(true);
        grid[0][6].setValue(9);
        grid[1][1].setOriginal(true);
        grid[1][1].setValue(8);
        grid[1][2].setOriginal(true);
        grid[1][2].setValue(4);
        grid[1][3].setOriginal(true);
        grid[1][3].setValue(1);
        grid[1][4].setOriginal(true);
        grid[1][4].setValue(6);
        grid[1][5].setOriginal(true);
        grid[1][5].setValue(9);
        grid[1][7].setOriginal(true);
        grid[1][7].setValue(2);
        grid[1][8].setOriginal(true);
        grid[1][8].setValue(7);
        grid[2][1].setOriginal(true);
        grid[2][1].setValue(3);
        grid[2][3].setOriginal(true);
        grid[2][3].setValue(4);
        grid[2][6].setOriginal(true);
        grid[2][6].setValue(1);
        grid[2][7].setOriginal(true);
        grid[2][7].setValue(6);
        grid[3][1].setOriginal(true);
        grid[3][1].setValue(2);
        grid[3][2].setOriginal(true);
        grid[3][2].setValue(7);
        grid[3][4].setOriginal(true);
        grid[3][4].setValue(9);
        grid[3][5].setOriginal(true);
        grid[3][5].setValue(4);
        grid[3][6].setOriginal(true);
        grid[3][6].setValue(6);
        grid[3][8].setOriginal(true);
        grid[3][8].setValue(1);
        grid[4][0].setOriginal(true);
        grid[4][0].setValue(4);
        grid[4][1].setOriginal(true);
        grid[4][1].setValue(1);
        grid[4][2].setOriginal(true);
        grid[4][2].setValue(8);
        grid[4][3].setOriginal(true);
        grid[4][3].setValue(2);
        grid[4][7].setOriginal(true);
        grid[4][7].setValue(7);
        grid[4][8].setOriginal(true);
        grid[4][8].setValue(9);
        grid[5][0].setOriginal(true);
        grid[5][0].setValue(6);
        grid[5][4].setOriginal(true);
        grid[5][4].setValue(1);
        grid[5][5].setOriginal(true);
        grid[5][5].setValue(7);
        grid[5][6].setOriginal(true);
        grid[5][6].setValue(2);
        grid[5][7].setOriginal(true);
        grid[5][7].setValue(3);
        grid[6][1].setOriginal(true);
        grid[6][1].setValue(5);
        grid[6][2].setOriginal(true);
        grid[6][2].setValue(3);
        grid[6][3].setOriginal(true);
        grid[6][3].setValue(6);
        grid[6][4].setOriginal(true);
        grid[6][4].setValue(4);
        grid[6][5].setOriginal(true);
        grid[6][5].setValue(1);
        grid[6][6].setOriginal(true);
        grid[6][6].setValue(7);
        grid[6][7].setOriginal(true);
        grid[6][7].setValue(9);
        grid[6][8].setOriginal(true);
        grid[6][8].setValue(2);
        grid[7][0].setOriginal(true);
        grid[7][0].setValue(7);
        grid[7][2].setOriginal(true);
        grid[7][2].setValue(1);
        grid[7][3].setOriginal(true);
        grid[7][3].setValue(9);
        grid[7][6].setOriginal(true);
        grid[7][6].setValue(4);
        grid[8][2].setOriginal(true);
        grid[8][2].setValue(2);
        grid[8][4].setOriginal(true);
        grid[8][4].setValue(5);
        grid[8][5].setOriginal(true);
        grid[8][5].setValue(3);
        grid[8][6].setOriginal(true);
        grid[8][6].setValue(8);
        grid[8][8].setOriginal(true);
        grid[8][8].setValue(6);
    }

    public void print() {
        int count = FIRST_ROW;

        System.out.println("       A    B    C  |  D    E    F  |  G    H    I");
        //System.out.println("       0    1    2  |  3    4    5  |  6    7    8");
        System.out.println();
        for (Cell[] row : grid) {
            System.out.print(count++ + "   | ");

            int colCount = 1;
            for (Cell c : row) {
                System.out.print(c.isOriginal() ? " " : "*");
                System.out.print(c.getValue());
                System.out.print(c.isOriginal() ? " " : "*");
                System.out.print(" " + (((colCount++ % 3) == 0) ? "|" : "") + " ");
            }
            System.out.println();
            System.out.println();
            if (count == FIRST_ROW + 3 || count == FIRST_ROW + 6) {
                System.out.println("--------------------------------------------------");
            }
        }
    }

    private int countEmptyCells() {
        int count = 0;
        for (Cell[] row : grid) {
            for (Cell c : row) {
                count += (c.getValue() == 0) ? 1 : 0;
            }
        }
        return count;
    }

    public void switchValue(Coordinates coordinates, int newValue) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (grid[row][col].getValue() == 0 && newValue != 0) {
            emptyCount--;
            grid[row][col].setValue(newValue);
        }
    }
}

class Cell {
    private boolean isOriginal;
    private int value;

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

class Coordinates {
    private int row;
    private int col;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}