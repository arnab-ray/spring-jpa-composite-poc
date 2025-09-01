package io.arnab.spring_jpa_poc;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Jitter utility class providing various jitter algorithms commonly used
 * in networking, retry mechanisms, and distributed systems to avoid
 * thundering herd problems and improve system resilience.
 */
public class Jitter {

    private final Random random;

    /**
     * Creates a new Jitter instance with a thread-safe random number generator.
     */
    public Jitter() {
        this.random = ThreadLocalRandom.current();
    }

    /**
     * Creates a new Jitter instance with a custom Random instance.
     * @param random Custom Random instance to use
     */
    public Jitter(Random random) {
        this.random = random;
    }

    /**
     * Applies full jitter to a base delay.
     * Returns a random value between 0 and the base delay.
     *
     * @param baseDelayMs Base delay in milliseconds
     * @return Jittered delay between 0 and baseDelayMs
     */
    public long fullJitter(long baseDelayMs) {
        if (baseDelayMs <= 0) {
            return 0;
        }
        return (long) (random.nextDouble() * (baseDelayMs + 1));
    }

    /**
     * Applies equal jitter to a base delay.
     * Returns the base delay divided by 2, plus a random value
     * between 0 and half the base delay.
     *
     * @param baseDelayMs Base delay in milliseconds
     * @return Jittered delay between baseDelayMs/2 and baseDelayMs
     */
    public long equalJitter(long baseDelayMs) {
        if (baseDelayMs <= 0) {
            return 0;
        }
        long halfDelay = baseDelayMs / 2;
        return halfDelay + (long) (random.nextDouble() * (halfDelay + 1));
    }

    /**
     * Applies decorrelated jitter using the previous delay as a factor.
     * This helps spread out retries over time in a more natural pattern.
     *
     * @param baseDelayMs Base delay in milliseconds
     * @param previousDelayMs Previous delay value in milliseconds
     * @return Jittered delay based on previous delay
     */
    public long decorrelatedJitter(long baseDelayMs, long previousDelayMs) {
        if (baseDelayMs <= 0) {
            return 0;
        }
        long upperBound = Math.max(baseDelayMs, previousDelayMs * 3);
        return baseDelayMs + (long) (random.nextDouble() * (upperBound - baseDelayMs + 1));
    }

    /**
     * Applies exponential backoff with full jitter.
     * Combines exponential backoff (2^attempt * baseDelay) with full jitter.
     *
     * @param baseDelayMs Base delay in milliseconds
     * @param attempt Current attempt number (0-based)
     * @param maxDelayMs Maximum allowed delay in milliseconds
     * @return Jittered exponential backoff delay
     */
    public long exponentialBackoffWithJitter(long baseDelayMs, int attempt, long maxDelayMs) {
        if (baseDelayMs <= 0 || attempt < 0) {
            return 0;
        }

        // Calculate exponential backoff: 2^attempt * baseDelay
        long exponentialDelay = baseDelayMs * (1L << Math.min(attempt, 30)); // Prevent overflow

        // Cap at maximum delay
        long cappedDelay = Math.min(exponentialDelay, maxDelayMs);

        // Apply full jitter
        return fullJitter(cappedDelay);
    }

    /**
     * Applies a simple random jitter by adding or subtracting a random
     * percentage of the base delay.
     *
     * @param baseDelayMs Base delay in milliseconds
     * @param jitterPercentage Jitter percentage (e.g., 20 for ±20%)
     * @return Jittered delay
     */
    public long percentageJitter(long baseDelayMs, double jitterPercentage) {
        if (baseDelayMs <= 0 || jitterPercentage < 0) {
            return baseDelayMs;
        }

        double jitterAmount = baseDelayMs * (jitterPercentage / 100.0);
        double randomFactor = (random.nextDouble() * 2 - 1); // Random between -1 and 1
        long jitter = (long) (jitterAmount * randomFactor);

        return Math.max(0, baseDelayMs + jitter);
    }

    /**
     * Builder class for creating more complex jitter configurations.
     */
    public static class JitterBuilder {
        private long baseDelay = 1000;
        private long maxDelay = 30000;
        private JitterType type = JitterType.FULL;
        private double percentage = 20.0;
        private Random random = ThreadLocalRandom.current();

        public JitterBuilder baseDelay(long baseDelayMs) {
            this.baseDelay = baseDelayMs;
            return this;
        }

        public JitterBuilder maxDelay(long maxDelayMs) {
            this.maxDelay = maxDelayMs;
            return this;
        }

        public JitterBuilder type(JitterType type) {
            this.type = type;
            return this;
        }

        public JitterBuilder percentage(double percentage) {
            this.percentage = percentage;
            return this;
        }

        public JitterBuilder random(Random random) {
            this.random = random;
            return this;
        }

        public JitterStrategy build() {
            return new JitterStrategy(baseDelay, maxDelay, type, percentage, random);
        }
    }

    /**
     * Enumeration of different jitter types.
     */
    public enum JitterType {
        FULL, EQUAL, PERCENTAGE, EXPONENTIAL
    }

    /**
     * A configured jitter strategy that can be reused.
     */
    public static class JitterStrategy {
        private final long baseDelay;
        private final long maxDelay;
        private final JitterType type;
        private final double percentage;
        private final Jitter jitter;

        private long previousDelay = 0;

        JitterStrategy(long baseDelay, long maxDelay, JitterType type,
                       double percentage, Random random) {
            this.baseDelay = baseDelay;
            this.maxDelay = maxDelay;
            this.type = type;
            this.percentage = percentage;
            this.jitter = new Jitter(random);
        }

        /**
         * Calculates the next delay based on the configured jitter strategy.
         *
         * @param attempt Current attempt number (0-based)
         * @return Next delay in milliseconds
         */
        public long nextDelay(int attempt) {
            long delay;

            switch (type) {
                case FULL:
                    delay = jitter.fullJitter(baseDelay);
                    break;
                case EQUAL:
                    delay = jitter.equalJitter(baseDelay);
                    break;
                case PERCENTAGE:
                    delay = jitter.percentageJitter(baseDelay, percentage);
                    break;
                case EXPONENTIAL:
                    delay = jitter.exponentialBackoffWithJitter(baseDelay, attempt, maxDelay);
                    break;
                default:
                    delay = baseDelay;
            }

            previousDelay = delay;
            return delay;
        }

        /**
         * Gets decorrelated jitter based on the previous delay.
         *
         * @return Decorrelated jitter delay
         */
        public long nextDecorrelatedDelay() {
            long delay = jitter.decorrelatedJitter(baseDelay, previousDelay);
            previousDelay = delay;
            return delay;
        }
    }

    // Example usage and testing
    public static void main(String[] args) {
        Jitter jitter = new Jitter();

        System.out.println("=== Jitter Examples ===");

        // Example 1: Full Jitter
        System.out.println("\nFull Jitter (0 to 1000ms):");
        for (int i = 0; i < 5; i++) {
            System.out.println("Attempt " + i + ": " + jitter.fullJitter(1000) + "ms");
        }

        // Example 2: Equal Jitter
        System.out.println("\nEqual Jitter (500 to 1000ms):");
        for (int i = 0; i < 5; i++) {
            System.out.println("Attempt " + i + ": " + jitter.equalJitter(1000) + "ms");
        }

        // Example 3: Exponential Backoff with Jitter
        System.out.println("\nExponential Backoff with Jitter:");
        for (int i = 0; i < 6; i++) {
            long delay = jitter.exponentialBackoffWithJitter(100, i, 10000);
            System.out.println("Attempt " + i + ": " + delay + "ms");
        }

        // Example 4: Percentage Jitter
        System.out.println("\nPercentage Jitter (±20% of 1000ms):");
        for (int i = 0; i < 5; i++) {
            System.out.println("Attempt " + i + ": " + jitter.percentageJitter(1000, 20) + "ms");
        }

        // Example 5: Using JitterStrategy
        System.out.println("\nJitter Strategy (Exponential):");
        JitterStrategy strategy = new JitterBuilder()
                .baseDelay(100)
                .maxDelay(5000)
                .type(JitterType.EXPONENTIAL)
                .build();

        for (int i = 0; i < 6; i++) {
            System.out.println("Attempt " + i + ": " + strategy.nextDelay(i) + "ms");
        }

        // Example 6: Decorrelated Jitter
        System.out.println("\nDecorrelated Jitter:");
        JitterStrategy decorrelated = new JitterBuilder()
                .baseDelay(100)
                .build();

        for (int i = 0; i < 6; i++) {
            System.out.println("Attempt " + i + ": " + decorrelated.nextDecorrelatedDelay() + "ms");
        }
    }
}
