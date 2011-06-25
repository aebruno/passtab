/*
 * Copyright (C) 2011  Andrew E. Bruno <aeb@qnot.org>
 *
 * This file is part of passtab.
 *
 * passtab is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * passtab is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with passtab.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.qnot.passtab;

/**
 * Class which encapsulates a Position on the Tabula Recta
 * 
 * @author Andrew E. Bruno <aeb@qnot.org>
 */
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
            col++;
            break;
        case W:
            col--;
            break;
        case NE:
            row--;
            col++;
            break;
        case NW:
            row--;
            col--;
            break;
        case SE:
            row++;
            col++;
            break;
        case SW:
            row++;
            col--;
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
        } else if (row < 0) {
            row = 0;
        }
        
        if (col > maxCol) {
            col = maxCol;
        } else if(col < 0) {
            col = 0;
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
