package org.qnot.pwrecta;

public class Position {
    private int prevRow;
    private int prevCol;
    private int row;
    private int col;
    
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
        this.prevRow = row;
        this.prevCol = col;
    }
    
    public void move(Direction direction) {
        prevRow = row;
        prevCol = col;
        switch (direction) {
        case N:
            row--;
            break;
        case S:
            row++;
            break;
        case E:
            col--;
            break;
        case W:
            col++;
            break;
        case NE:
            row--;
            col--;
            break;
        case NW:
            row--;
            col++;
            break;
        case SE:
            row++;
            col--;
            break;
        case SW:
            row++;
            col++;
            break;
        }
    }
    
    public void backup() {
        row = prevRow;
        col = prevCol;
    }
    
    public void setWithinBounds(int maxRow, int maxCol) {
        if (row > maxRow) {
            row = maxRow;
        }
        
        if (col > maxCol) {
            col = maxCol;
        }
    }
    
    public boolean isOutOfBounds(int maxRow, int maxCol) {
        boolean collision = false;
        if (row < 0 || row > maxRow) {
            collision = true;
        }
        
        if (col < 0 || col > maxCol) {
            collision = true;
        }
        
        return collision;
    }

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
