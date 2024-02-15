package net.pitan76.bedrocktools;

import dev.architectury.event.EventResult;
import net.minecraft.util.Identifier;
import net.pitan76.bedrocktools.cmd.BedrockToolsCommand;
import net.pitan76.bedrocktools.item.CreativeShotKillItem;
import net.pitan76.bedrocktools.item.CreativeTabs;
import net.pitan76.mcpitanlib.api.command.CommandRegistry;
import net.pitan76.mcpitanlib.api.event.v0.AttackEntityEventRegistry;
import net.pitan76.mcpitanlib.api.registry.CompatRegistry;

public class BedrockTools {
    public static final String MOD_ID = "bedrocktools76";

    public static final CompatRegistry registry = CompatRegistry.createRegistry(MOD_ID);

    public static void init() {
        registry.registerItemGroup(id("tools"), () -> CreativeTabs.BEDROCK_TOOLS_GROUP);

        registry.registerItem(id("obsidian_sword"), () -> Items.OBSIDIAN_SWORD);
        registry.registerItem(id("obsidian_axe"), () -> Items.OBSIDIAN_AXE);
        registry.registerItem(id("obsidian_pickaxe"), () -> Items.OBSIDIAN_PICKAXE);
        registry.registerItem(id("obsidian_shovel"), () -> Items.OBSIDIAN_SHOVEL);
        registry.registerItem(id("obsidian_hoe"), () -> Items.OBSIDIAN_HOE);
        registry.registerItem(id("bedrock_sword"), () -> Items.BEDROCK_SWORD);
        registry.registerItem(id("bedrock_axe"), () -> Items.BEDROCK_AXE);
        registry.registerItem(id("bedrock_pickaxe"), () -> Items.BEDROCK_PICKAXE);
        registry.registerItem(id("bedrock_shovel"), () -> Items.BEDROCK_SHOVEL);
        registry.registerItem(id("bedrock_hoe"), () -> Items.BEDROCK_HOE);

        CommandRegistry.register("bedrocktools76", new BedrockToolsCommand());

        registry.allRegister();

        AttackEntityEventRegistry.register(
                (player, world, entity, hand, result) -> {
                    if (!player.isCreative()) return EventResult.pass();
                    if (!(player.getStackInHand(hand).getItem() instanceof CreativeShotKillItem)) return EventResult.pass();
                    entity.kill();
                    return EventResult.interruptTrue();
                }
        );
    }

    public static Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }
}