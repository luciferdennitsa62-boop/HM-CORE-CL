package hmcore.core;

import hmcore.modules.HMTest;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class HMManager {
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;

        initialized = true;

        MeteorClient.LOG.info("[HM] ===============================");
        MeteorClient.LOG.info("[HM] HM CORE initializing...");
        MeteorClient.LOG.info("[HM] Core manager started");

        Modules modules = Modules.get();
        modules.add(new HMTest());
        modules.sortModules();

        MeteorClient.LOG.info("[HM] Registered HM Test module");
        MeteorClient.LOG.info("[HM] ===============================");
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
