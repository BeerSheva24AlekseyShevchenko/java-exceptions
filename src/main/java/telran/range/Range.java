package telran.range;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import telran.range.exceptions.OutOfRangeMaxValueException;
import telran.range.exceptions.OutOfRangeMinValueException;

public class Range implements Iterable<Integer> {
    private static final String ERROR_MESSAGE = "max less or equal min";
    private int min;
    private int max;
    private Predicate<Integer> predicate = null;

    private Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static Range getRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }

        return new Range(min, max);
    }

    public void checkNumber(int number) throws OutOfRangeMaxValueException, OutOfRangeMinValueException {
        if (number > max) {
            throw new OutOfRangeMaxValueException(max, number);
        }

        if (number < min) {
            throw new OutOfRangeMinValueException(min, number);
        }
    }

    public void setPredicate(Predicate<Integer> predicate) {
        this.predicate = predicate;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new RangeIterator();
    }

    private class RangeIterator implements Iterator<Integer> {
        private Integer current = getNext(min);

        private Integer getNext(int value) {
            Integer result = null;
            while (value <= max && result == null) {
                if (predicate == null || predicate.test(value)) {
                    result = value;
                } else {
                    value++;
                }
            }

            return result;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Integer next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }

            Integer tmp = current;
            current = getNext(++current);
            return tmp;
        }
    }
}
