package solution;

import java.util.Comparator;

/**
 * Comparator for sorting Solution.Time objects.
 *
 * @author Vassili Moskaljov
 * @version 1.0
 */
class TimesComparator implements Comparator<Times> {
    @Override
    public int compare(Times o1, Times o2) {
        if (o1.getIn() == o2.getIn() && o1.getIn() == o2.getOut()) {
            return 0;
        } else if (o1.getIn() > o2.getIn()) {
            return 1;
        } else {
            return -1;
        }
    }
}