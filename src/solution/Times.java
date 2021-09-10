package solution;

/**
 * Solution.Times class that have 2 fields for holding coming and leaving times in minutes.
 *
 * @author Vassili Moskaljov
 * @version 1.0
 * */
public class Times {
    private int in;
    private int out;

    public Times(int in, int out) {
        this.in = in;
        this.out = out;
    }

    public int getIn() {
        return in;
    }

    public int getOut() {
        return out;
    }

    public void setIn(int in) {
        this.in = in;
    }

    public void setOut(int out) {
        this.out = out;
    }

    /**
     * Checks if timeframe on the right is later and still fits the timeframe.
     * */
    public boolean isGreater(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Times)) {
            return false;
        }
        Times z = (Times) o;

        return in <= z.getIn() && z.getIn() <= out;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Times)) {
            return false;
        }
        Times z = (Times) o;

        return in == z.getIn() && out == z.getOut();
    }
}