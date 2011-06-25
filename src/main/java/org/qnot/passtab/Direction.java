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
 * Enum for defining valid directions to move around the Tabula Recta when
 * creating passwords
 * 
 * @author Andrew E. Bruno <aeb@qnot.org>
 */
public enum Direction {
    N, S, E, W, NE, NW, SE, SW;
    
    public static Direction fromString(String str) throws SequenceParseException {
        if("N".equalsIgnoreCase(str)) {
            return Direction.N;
        } else if("S".equalsIgnoreCase(str)) {
            return Direction.S;
        } else if("E".equalsIgnoreCase(str)) {
            return Direction.E;
        } else if("W".equalsIgnoreCase(str)) {
            return Direction.W;
        } else if("NE".equalsIgnoreCase(str)) {
            return Direction.NE;
        } else if("NW".equalsIgnoreCase(str)) {
            return Direction.NW;
        } else if("SE".equalsIgnoreCase(str)) {
            return Direction.SE;
        } else if("SW".equalsIgnoreCase(str)) {
            return Direction.SW;
        } else {
            throw new SequenceParseException("Invalid Direction. Must be [n|w|e|w|ne|nw|se|sw]");
        }
    }
    
    public static Direction[] clockwiseCompass() {
        return new Direction[]{N,NW,W,SW,S,SE,E,NE};
    }
    
    public static Direction[] counterclockwiseCompass() {
        return new Direction[]{NE,E,SE,S,SW,W,NW,N};
    }

}
