package kr.pyke.displayname.registry;

import kr.pyke.displayname.registry.creativetab.ModCreativeTab;
import kr.pyke.displayname.registry.item.ModItem;

public class ModRegistry {
    private ModRegistry() { }

    public static void register() {
        ModItem.register();

        ModCreativeTab.register();
    }
}
