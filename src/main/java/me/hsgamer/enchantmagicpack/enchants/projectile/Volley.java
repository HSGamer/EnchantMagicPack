package me.hsgamer.enchantmagicpack.enchants.projectile;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Arrow.PickupStatus;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/*
 * Multiply the arrow
 */
public class Volley extends ProjectileShoot {

    private static final String MULTIPLY = "multiply";

    public Volley() {
        super("Volley", "Multiply the arrow");

        setMaxLevel(5);
        setWeight(2);

        settings.set(MULTIPLY, 2, 1);
    }

    public void applyProjectileShoot(LivingEntity user, ItemStack bow, int level, EntityShootBowEvent event) {
        int amount = (int) (1 + 2 * Math.round(settings.get(MULTIPLY, level)));
        Arrow arrow = (Arrow) event.getProjectile();
        int fireticks = arrow.getFireTicks();
        int knockback = arrow.getKnockbackStrength();
        double damage = arrow.spigot().getDamage();
        boolean crit = arrow.isCritical();
        double anglebetween = (45 / (amount - 1)) * Math.PI / 180;
        double pitch = (user.getLocation().getPitch() + 90) * Math.PI / 180;
        double yaw = (user.getLocation().getYaw() + 90 - 45 / 2) * Math.PI / 180;
        double sZ = Math.cos(pitch);
        for (int i = 0; i < amount; i++) {
            double nX = Math.sin(pitch) * Math.cos(yaw + anglebetween * i);
            double nY = Math.sin(pitch) * Math.sin(yaw + anglebetween * i);
            Vector newVec = new Vector(nX, sZ, nY);
            Arrow newarrow = user.launchProjectile(Arrow.class);
            newarrow.spigot().setDamage(damage);
            newarrow.setShooter(user);
            newarrow.setVelocity(newVec.normalize().multiply(arrow.getVelocity().length()));
            newarrow.setFireTicks(fireticks);
            newarrow.setKnockbackStrength(knockback);
            newarrow.setCritical(crit);
            newarrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);
        }
        arrow.remove();
    }
}
