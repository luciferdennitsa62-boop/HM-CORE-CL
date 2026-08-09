package hmcore;

import meteordevelopment.meteorclient.MeteorClient;
import hmcore.core.HMManager;

public class HMCore {

    public static final String NAME = "HM Core Client";
    public static final String VERSION = "1.0";

    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;

        initialized = true;

        MeteorClient.LOG.info("================================");
        MeteorClient.LOG.info("{} v{} loaded", NAME, VERSION);

        HMManager.init();

        MeteorClient.LOG.info("HM Core system initialized");
        MeteorClient.LOG.info("================================");
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
