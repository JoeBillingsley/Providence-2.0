package test_helper;

/**
 * Utility to support proper AAA structure in unit tests.
 * @author Frank Appel, Clean JUnit Throwable-Tests with Java 8 Lambdas
 */
public class ThrowableCaptor {

    public static Throwable captureThrowable(ExceptionThrower exceptionThrower) {

        try {
            exceptionThrower.act();
        } catch (Throwable throwable) {
            return throwable;
        }

        return null;
    }

    @FunctionalInterface
    public interface ExceptionThrower {
        void act() throws Throwable;
    }
}

