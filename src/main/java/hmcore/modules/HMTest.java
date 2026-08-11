package hmcore.modules;

import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;

public class HMTest extends Module {
    public HMTest() {
        super(
            Categories.Misc,
            "hm-test",
            "HM Core test module used to verify that HM Core modules are loaded."
        );
    }

    @Override
    public void onActivate() {
        info("HM Core test module is active.");
    }

    @Override
    public void onDeactivate() {
        info("HM Core test module is inactive.");
    }
}
