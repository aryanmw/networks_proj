import java.io.Serializable;

public class KeyObject implements Serializable {
    int rows,cols,asciiLow,asciiRange;

    public KeyObject(int rows, int cols, int asciiLow, int asciiRange) {
        this.rows = rows;
        this.cols = cols;
        this.asciiLow = asciiLow;
        this.asciiRange = asciiRange;
    }

    public int getAsciiLow() {
        return asciiLow;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getAsciiRange() {
        return asciiRange;
    }
}
