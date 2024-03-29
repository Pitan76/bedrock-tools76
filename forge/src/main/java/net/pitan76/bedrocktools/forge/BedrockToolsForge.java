package net.pitan76.bedrocktools.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.pitan76.bedrocktools.BedrockTools;
import net.pitan76.bedrocktools.Config;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;

@Mod(BedrockTools.MOD_ID)
public class BedrockToolsForge {
    public BedrockToolsForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(BedrockTools.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Config.init(FMLPaths.CONFIGDIR.get().toFile());

        BedrockTools.init();

        if (PlatformUtil.isClient()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(BedrockToolsClientForge::clientInit);
        }
    }
}