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

    @Setup
    public void setup() {
        // Warm up both branches and mess up the profile.
        for (int c = 0; c < 100_000; c++) {
            testWith(MARKER);
            for (int t = 0; t < 100; t++) {
                testWith(t);
            }
        }
    }

    @Benchmark
    public void test() {
        testWith(Integer.valueOf(42));
    }

    public int testWith(Object o) {
        if (o == MARKER) {
            return -1;
        } else {
            return ((Number)o).intValue();
        }
    }

}
