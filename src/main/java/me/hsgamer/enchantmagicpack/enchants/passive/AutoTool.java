package me.hsgamer.enchantmagicpack.enchants.passive;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import studio.magemonkey.fabled.enchants.api.CustomEnchantment;
import studio.magemonkey.fabled.enchants.api.ItemSet;

public class AutoTool extends CustomEnchantment {
    public AutoTool() {
        super("Morphing Tool", "Change the type of the tool when interacting specific block");

        setMaxLevel(1);
        setWeight(1.25);

        addNaturalItems(ItemSet.TOOLS.getItems());
        addNaturalItems(ItemSet.SWORDS.getItems());
    }

    @Override
    public void applyInteractBlock(Player user, int level, PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) return;
        Material blockType = block.getType();
        String itemType = event.getMaterial().name();
        String value = getMineralType(itemType);
        if (value == null) return;

        if (Tag.MINEABLE_AXE.isTagged(blockType)) {
            event.getItem().setType(Material.valueOf(value + "AXE"));
        } else if (Tag.MINEABLE_PICKAXE.isTagged(blockType)) {
            event.getItem().setType(Material.valueOf(value + "PICKAXE"));
        } else if (Tag.MINEABLE_SHOVEL.isTagged(blockType)) {
            event.getItem().setType(Material.valueOf(value + "SHOVEL"));
        } else if (Tag.MINEABLE_HOE.isTagged(blockType)) {
            event.getItem().setType(Material.valueOf(value + "HOE"));
        } else {
            event.getItem().setType(Material.valueOf(value + "SWORD"));
        }
    }

    private String getMineralType(String itemType) {
        if (itemType.startsWith("DIAMOND_")) {
            return "DIAMOND_";
        } else if (itemType.startsWith("GOLDEN_")) {
            return "GOLDEN_";
        } else if (itemType.startsWith("IRON_")) {
            return "IRON_";
        } else if (itemType.startsWith("STONE_")) {
            return "STONE_";
        } else if (itemType.startsWith("WOODEN_")) {
            return "WOODEN_";
        } else if (itemType.startsWith("NETHERITE_")) {
            return "NETHERITE_";
        } else return null;
    }

    @Override
    public void applyOnHit(LivingEntity user, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(user instanceof Player player)) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        String itemType = item.getType().name();
        String value = getMineralType(itemType);
        if (value == null) return;
        item.setType(Material.valueOf(value + "SWORD"));
    }
}
