package dev.nullpointercoding.zdeatharcade.Zombies;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;


import dev.nullpointercoding.zdeatharcade.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ZombieLevel1 {
    
    private Main plugin = Main.getInstance();
    private final Component level = Component.text("LVL 1",TextColor.color(255, 255, 255)).decorate(TextDecoration.BOLD);

    public LivingEntity convertToLevel1Zombie(final Zombie z){
        z.customName(Component.text("Zombie").color(TextColor.color(10, 214, 68)).appendSpace().append(level));
        z.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(4.0);
        z.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
        z.setCustomNameVisible(true);
        z.setShouldBurnInDay(false);
        return z;
    }
}
