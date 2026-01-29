package kr.pyke.displayname.mixin.server;

import kr.pyke.displayname.DisplayName;
import kr.pyke.displayname.data.DisplayNameData;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntitySelectorParser.class)
public class EntitySelectorParserMixin {
    @ModifyVariable(method = "parseNameOrUUID", at = @At("STORE"))
    private String replaceDisplayNameWithRealName(String name) {
        if (null == name || name.isEmpty()) { return ""; }
        if (DisplayName.SERVER_INSTANCE == null) { return name; }

        return DisplayNameData.getServerState(DisplayName.SERVER_INSTANCE).getRealName(name);
    }
}