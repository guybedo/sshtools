package com.akalea.sshtools.utils;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class ThreadUtils {

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {

        }
    }

    public static Boolean waitForCondition(
        Supplier<Boolean> condition,
        int maxWaitSecs,
        int checkIntervalMillis) {
        LocalDateTime limit = LocalDateTime.now().plusSeconds(maxWaitSecs);
        while (LocalDateTime.now().isBefore(limit)) {
            if (condition.get())
                return true;
            sleep(checkIntervalMillis);
        }
        return false;
    }
}
