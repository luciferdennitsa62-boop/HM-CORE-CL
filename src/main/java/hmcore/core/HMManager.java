package hmcore.core;

import meteordevelopment.meteorclient.MeteorClient;

public class HMManager {
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;

        initialized = true;

        MeteorClient.LOG.info("[HM] ===============================");
        MeteorClient.LOG.info("[HM] HM CORE initializing...");
        MeteorClient.LOG.info("[HM] Core manager started");
        MeteorClient.LOG.info("[HM] ===============================");
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
