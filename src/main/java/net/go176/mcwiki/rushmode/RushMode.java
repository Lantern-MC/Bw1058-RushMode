package net.go176.mcwiki.rushmode;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public final class RushMode extends JavaPlugin {

    @Getter
    private static RushMode instance;

    @Getter
    private final Map<java.util.UUID, Boolean> bridgingMode = new HashMap<>();



    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        Bukkit.getOnlinePlayers().forEach((player) -> {
            bridgingMode.put(player.getUniqueId(), false);
        });
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Bukkit.getOnlinePlayers().forEach((player) -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
            });
        }, 0L, 1L);
        getServer().getPluginManager().registerEvents(new BridgingListener(), this);
        getServer().getPluginManager().registerEvents(new BedWarsListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        guanggao();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    void guanggao() {
        getLogger().info("====================");
        getLogger().info("");
        getLogger().info("  欢迎使用青桶社区作品");
        getLogger().info("  Bedwars1058附属插件");
        getLogger().info("  极速起床插件");
        getLogger().info("  青桶Dev-小玄易");
        getLogger().info("  事件注册成功");
        getLogger().info("  mcwiki.go176.net");
        getLogger().info(" ");
        getLogger().info("====================");
    }
}
