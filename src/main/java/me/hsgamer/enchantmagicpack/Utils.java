package me.hsgamer.enchantmagicpack;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.metadata.FixedMetadataValue;

import static me.hsgamer.enchantmagicpack.EnchantMagicPack.getInstance;

public final class Utils {
    public static final String NOT_DAMAGE_META = "notDamageEntity";
    public static final String NOT_BREAK_META = "notBreakBlock";

    private Utils() {
        // EMPTY
    }

    public static boolean spawnTnt(Location location, Entity source, float radius, int fuse, boolean fire, boolean breakBlock, boolean damageEntity) {
        TNTPrimed tnt = location.getWorld().spawn(location, TNTPrimed.class, tntPrimed -> {
            tntPrimed.setFuseTicks(100);
            tntPrimed.setSource(source);
            tntPrimed.setYield(radius);
            tntPrimed.setIsIncendiary(fire);
            if (!damageEntity) {
                tntPrimed.setMetadata(NOT_DAMAGE_META, new FixedMetadataValue(getInstance(), true));
            }
            if (!breakBlock) {
                tntPrimed.setMetadata(NOT_BREAK_META, new FixedMetadataValue(getInstance(), true));
            }
        });
        ExplosionPrimeEvent event = new ExplosionPrimeEvent(tnt);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            tnt.remove();
            return false;
        } else {
            tnt.setFuseTicks(fuse);
            return true;
        }
    }
}
