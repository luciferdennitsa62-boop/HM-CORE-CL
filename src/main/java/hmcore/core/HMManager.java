package hmcore.core;

import meteordevelopment.meteorclient.MeteorClient;

public class HMManager {

    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;

        initialized = true;

        MeteorClient.LOG.info("[HM] Manager initialized");
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
