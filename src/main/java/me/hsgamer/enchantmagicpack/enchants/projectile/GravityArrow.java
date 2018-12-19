package me.hsgamer.enchantmagicpack.enchants.projectile;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class GravityArrow extends ProjectileShoot {
    public GravityArrow() {
        super("GravityArrow", "Arrows will be no gravity when shoot");

        setMaxLevel(1, 1);
        setWeight(1);
    }

    public void applyProjectileShoot(LivingEntity user, ItemStack bow, int level, EntityShootBowEvent event) {
        event.getProjectile().setGravity(false);
    }
}
