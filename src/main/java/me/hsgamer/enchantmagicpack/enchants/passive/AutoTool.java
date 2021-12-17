package me.hsgamer.enchantmagicpack.enchants.passive;

import com.sucy.enchant.api.CustomEnchantment;
import com.sucy.enchant.api.ItemSet;
import org.bukkit.Material;
import org.bukkit.Tag;
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
            Material blocktype = event.getClickedBlock().getType();
            String itemtype = event.getMaterial().name();
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

            if (Tag.MINEABLE_AXE.isTagged(blocktype)) {
                event.getItem().setType(Material.valueOf(value + "AXE"));
            } else if (Tag.MINEABLE_PICKAXE.isTagged(blocktype)) {
                event.getItem().setType(Material.valueOf(value + "PICKAXE"));
            } else if (Tag.MINEABLE_SHOVEL.isTagged(blocktype)) {
                event.getItem().setType(Material.valueOf(value + "SHOVEL"));
            } else {
                event.getItem().setType(Material.valueOf(value + "SWORD"));
            }
        }
    }

    public void applyOnHit(LivingEntity user, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(user instanceof Player)) return;
        ItemStack item = ((Player) user).getInventory().getItemInMainHand();
        String itemtype = item.getType().name();
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
        item.setType(Material.valueOf(value + "SWORD"));
    }
}
