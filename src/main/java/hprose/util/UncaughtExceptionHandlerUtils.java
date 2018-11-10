package hprose.util;

/**
 *
 */
public final class UncaughtExceptionHandlerUtils {
    
    private static Thread.UncaughtExceptionHandler sUEH;
    
    public static final Thread.UncaughtExceptionHandler sDefaultUEH =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable throwable) {
                    Thread.UncaughtExceptionHandler ueh = sUEH;
                    if (ueh != null) {
                        ueh.uncaughtException(thread, throwable);
                    }
                }
            };
    
    public static void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
        sUEH = handler;
    }
    
    public static void report(Throwable throwable) {
        report(Thread.currentThread(), throwable);
    }
    
    public static void report(Thread thread, Throwable throwable) {
        Thread.UncaughtExceptionHandler ueh = sUEH;
        if (ueh != null) {
            ueh.uncaughtException(thread, throwable);
        }
    }
    
    private UncaughtExceptionHandlerUtils() {}
    
}
