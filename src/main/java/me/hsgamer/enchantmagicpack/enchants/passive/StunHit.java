package me.hsgamer.enchantmagicpack.enchants.passive;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import studio.magemonkey.fabled.enchants.api.CustomEnchantment;
import studio.magemonkey.fabled.enchants.api.ItemSet;

/*
 * Chance to stun the enemy
 */
public class StunHit extends CustomEnchantment {

    private static final String CHANCE = "chance";

    public StunHit() {
        super("Stun Hit", "Chance to stun the enemy");
        setMaxLevel(6);
        setWeight(1);
        addNaturalItems(ItemSet.SWORDS.getItems());

        settings.set(CHANCE, 7, 4);
    }

    @Override
    public void applyOnHit(LivingEntity user, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (Math.random() * 100 <= settings.get(CHANCE, level)) {
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1, 1);
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1, 1, true, false));
            target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1, 3 * level, true, false));
        }
    }
}
