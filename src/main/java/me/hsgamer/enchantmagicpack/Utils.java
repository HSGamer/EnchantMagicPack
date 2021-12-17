package me.hsgamer.enchantmagicpack;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.metadata.FixedMetadataValue;

import static me.hsgamer.enchantmagicpack.EnchantMagicPack.getInstance;

public final class Utils {
    public static final String DAMAGE_META = "damageEntity";
    public static final String BREAK_META = "breakBlock";

    private Utils() {
        // EMPTY
    }

    public static boolean spawnTnt(Location location, Entity source, float radius, int fuse, boolean fire, boolean breakBlock, boolean damageEntity) {
        TNTPrimed tnt = location.getWorld().spawn(location, TNTPrimed.class, tntPrimed -> {
            tntPrimed.setFuseTicks(100);
            tntPrimed.setSource(source);
            tntPrimed.setYield(radius);
            tntPrimed.setIsIncendiary(fire);
            tntPrimed.setMetadata(DAMAGE_META, new FixedMetadataValue(getInstance(), damageEntity));
            tntPrimed.setMetadata(BREAK_META, new FixedMetadataValue(getInstance(), breakBlock));
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
