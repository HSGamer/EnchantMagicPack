package me.hsgamer.enchantmagicpack.enchants.passive;

import com.sucy.enchant.api.CustomEnchantment;
import com.sucy.enchant.api.ItemSet;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

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

    public void applyDefense(LivingEntity user, LivingEntity target, int level, EntityDamageEvent event) {
        if (Math.random() * 100 <= settings.get(CHANCE, level)) {
            user.getWorld().playSound(user.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1, 1);
            event.setCancelled(true);
        }
    }

}
