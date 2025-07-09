package me.hsgamer.enchantmagicpack.enchants.passive;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import studio.magemonkey.fabled.enchants.api.CustomEnchantment;
import studio.magemonkey.fabled.enchants.api.ItemSet;

/*
 * Chance not to get the damage
 */
public class Dodge extends CustomEnchantment {

    private static final String CHANCE = "chance";

    public Dodge() {

        super("Dodge", "Chance not to get the damage");

        setMaxLevel(5);
        setWeight(1);
        addNaturalItems(ItemSet.CHESTPLATES.getItems());

        settings.set(CHANCE, 5, 5);

    }

    @Override
    public void applyDefense(LivingEntity user, LivingEntity target, int level, EntityDamageEvent event) {
        if (Math.random() * 100 <= settings.get(CHANCE, level)) {
            user.getWorld().playSound(user.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1, 1);
            event.setCancelled(true);
        }
    }

}
