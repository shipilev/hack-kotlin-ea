package org.sample;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class KotlinEA {

    static final Object MARKER = new Object();

    @Benchmark
    public int test() {
        int sum = 0;
        for (int c = 0; c < 1000; c++) {
            sum += testWith(c);
        }
        return sum;
    }

    private int testWith(int c) {
        Object o = fastProducer(c);
        if (o == MARKER) {
            return -1;
        } else {
            return ((Number)o).intValue();
        }
    }

    // This method may return either MARKER or Integer.
    // MARKER is RARELY produced.
    private Object fastProducer(int c) {
        if (isFast(c)) return Integer.valueOf(c); // fast-path
        return slowProducer(c); // slow-path
    }

    private boolean isFast(int c) {
        return c != 500;
    }

    // it is RARELY invoked
    private Object slowProducer(int c) {
        return MARKER;
    }
}
