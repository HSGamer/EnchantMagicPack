package me.hsgamer.enchantmagicpack.enchants.passive;

import com.sucy.enchant.api.CustomEnchantment;
import com.sucy.enchant.api.ItemSet;
import me.hsgamer.enchantmagicpack.utils.ToolsUtils;
import me.hsgamer.enchantmagicpack.utils.XMaterial;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AutoTool extends CustomEnchantment {
    public AutoTool() {
        super("Morphing Tool", "Change the type of the tool when interacting specific block");

        setMaxLevel(1);
        setWeight(1.25);

        addNaturalItems(ItemSet.TOOLS.getItems());
        addNaturalItems(ItemSet.SWORDS.getItems());
    }

    public void applyInteractBlock(Player user, int level, PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            XMaterial blocktype = XMaterial.fromString(event.getClickedBlock().getType().toString());
            String itemtype = XMaterial.fromString(event.getMaterial().toString()).toString();
            String value;
            if (itemtype.startsWith("DIAMOND_")) {
                value = "DIAMOND_";
            } else if (itemtype.startsWith("GOLDEN_")) {
                value = "GOLDEN_";
            } else if (itemtype.startsWith("IRON_")) {
                value = "IRON_";
            } else if (itemtype.startsWith("STONE_")) {
                value = "STONE_";
            } else if (itemtype.startsWith("WOODEN_")) {
                value = "WOODEN_";
            } else return;

            if (ToolsUtils.AXES_BLOCKS.contains(blocktype)) {
                event.getItem().setType(XMaterial.valueOf(value + "AXE").parseMaterial());
            } else if (ToolsUtils.PICKAXES_BLOCKS.contains(blocktype)) {
                event.getItem().setType(XMaterial.valueOf(value + "PICKAXE").parseMaterial());
            } else if (ToolsUtils.SHOVELS_BLOCKS.contains(blocktype)) {
                event.getItem().setType(XMaterial.valueOf(value + "SHOVEL").parseMaterial());
            } else if (ToolsUtils.SWORDS_BLOCKS.contains(blocktype)) {
                event.getItem().setType(XMaterial.valueOf(value + "SWORD").parseMaterial());
            }
        }
    }

    public void applyOnHit(LivingEntity user, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(user instanceof Player)) return;
        ItemStack item = ((Player) user).getInventory().getItemInMainHand();
        String itemtype = XMaterial.fromString(item.getType().toString()).toString();
        String value;
        if (itemtype.startsWith("DIAMOND_")) {
            value = "DIAMOND_";
        } else if (itemtype.startsWith("GOLDEN_")) {
            value = "GOLDEN_";
        } else if (itemtype.startsWith("IRON_")) {
            value = "IRON_";
        } else if (itemtype.startsWith("STONE_")) {
            value = "STONE_";
        } else if (itemtype.startsWith("WOODEN_")) {
            value = "WOODEN_";
        } else return;
        item.setType(XMaterial.valueOf(value + "SWORD").parseMaterial());
    }
}
