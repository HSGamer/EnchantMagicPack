package me.hsgamer.enchantmagicpack.enchants;

import com.rit.sucy.player.Protection;
import com.sucy.enchant.api.Cooldowns;
import com.sucy.enchant.api.CustomEnchantment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class SpreadLaser extends CustomEnchantment {
    private static final String MULTIPLY = "multiply";
    private static final String LENGTH = "length";
    private static final String SPACE = "space";
    private static final String DAMAGE = "damage";

    public SpreadLaser() {
        super("Spread Laser", "Fire a laser that spreads when hit");

        setMaxLevel(3, 3);
        setWeight(0.75);
        Cooldowns.configure(settings, 7, 2);

        settings.set(MULTIPLY, 3, 1);
        settings.set(LENGTH, 10, 3);
        settings.set(SPACE, 1);
        settings.set(DAMAGE, 3, 1);

        addNaturalItems(Material.STICK, Material.BLAZE_ROD);
    }

    public void applyInteractBlock(Player user, int level, PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) return;
        if (Cooldowns.onCooldown(this, user, settings, level)) return;
        Location loc = user.getLocation().add(0.0D, 1.2D, 0.0D);
        Vector dir = loc.getDirection();
        double range = settings.get(LENGTH, level);
        double space = settings.getDouble(SPACE);
        int amount = (int) (1 + 2 * Math.round(settings.get(MULTIPLY, level)));
        spreadLaser(range, space, settings.get(DAMAGE, level), user, loc, dir, amount);
        Cooldowns.start(this, user);
    }

    private void spreadLaser(double range, double space, double damage, Player user, Location loc, Vector dir, int amount) {
        for (double i = 0.0D; i < range; i += space) {
            Block tempblock = loc.getBlock();
            if (!(tempblock.isEmpty() || tempblock.getType() == Material.GLASS || tempblock.getType() == Material.STAINED_GLASS || tempblock.getType() == Material.STAINED_GLASS_PANE || tempblock.getType() == Material.THIN_GLASS))
                break;
            loc = loc.add(dir.multiply(1).normalize());
            loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 5, 0, 0, 0, 0);
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, .3, .3, .3)) {
                if ((entity instanceof LivingEntity) && (entity.getEntityId() != user.getEntityId()) && !(entity instanceof ArmorStand) && !(Protection.isAlly(user, (LivingEntity) entity))) {
                    ((Damageable) entity).damage(damage, user);
                    double pitch = (user.getLocation().getPitch() + 90) * Math.PI / 180;
                    double yaw = (user.getLocation().getYaw() + 90 - 45 / 2) * Math.PI / 180;
                    double sZ = Math.cos(pitch);
                    for (int a = 0; a < amount; a++) {
                        double anglebetween = (45 / (amount - 1)) * Math.PI / 180;
                        double nX = Math.sin(pitch) * Math.cos(yaw + anglebetween * a);
                        double nY = Math.sin(pitch) * Math.sin(yaw + anglebetween * a);
                        Vector newVec = new Vector(nX, sZ, nY);
                        spreadLaser(range, space, damage, user, loc, newVec, amount);
                    }
                }
            }
        }
    }
}
