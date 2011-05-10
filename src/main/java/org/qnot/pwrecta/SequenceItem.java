package org.qnot.pwrecta;

public class SequenceItem {
    private Direction direction;
    private int length;
    
    public SequenceItem(Direction direction, int length) {
        this.direction = direction;
        this.length = length;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
