package net.go176.mcwiki.rushmode;

// #Debug: 注释
import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.NextEvent;
import com.andrei1058.bedwars.api.arena.generator.GeneratorType;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import com.andrei1058.bedwars.api.upgrades.MenuContent;
import com.andrei1058.bedwars.api.upgrades.TeamUpgrade;
import net.go176.mcwiki.rushmode.RushMode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.material.Bed;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("all")
public class BedWarsListener implements Listener {


      @EventHandler
      public void onJoin(PlayerJoinEvent event) {
            // 玩家进服设置搭路模式数据
            RushMode.getInstance().getBridgingMode().put(event.getPlayer().getUniqueId(), false);
      }

      @EventHandler
      public void onQuit(PlayerQuitEvent event) {
            // 玩家退服删除搭路模式数据
            RushMode.getInstance().getBridgingMode().remove(event.getPlayer().getUniqueId());
      }

      // #Debug: 注释

      @EventHandler
      public void onGameStart(GameStateChangeEvent event) {

            BedWars bedwarsAPI = Bukkit.getServicesManager().getRegistration(BedWars .class).getProvider();
            // 游戏开始判定逻辑
            if (event.getNewState() == GameState.playing) {
                  IArena arena = event.getArena();
                  Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                        // 修改事件
                        arena.setNextEvent(NextEvent.BEDS_DESTROY);

                        for (ITeam team : arena.getTeams()) {
                              // 设置队伍升级
                              team.getTeamUpgradeTiers().put("upgrade-forge", 3);
                              team.getTeamUpgradeTiers().put("upgrade-miner", 0);
                              team.getTeamUpgradeTiers().put("upgrade-heal-pool", 0);


//                              Class<TeamUpgrade> teamUpgrade = TeamUpgrade.class;
//                              try {
//                                    Field field = teamUpgrade.getDeclaredField("menuContentByName");
//                                    field.setAccessible(true);
//                                    HashMap<String, MenuContent> hashMap = (HashMap<String, MenuContent>) field.get(teamUpgrade);
//                                    hashMap.forEach((s, menuContent) -> {
//                                          TeamUpgrade upgrade;
//                                          switch (s) {
//                                                case "upgrade-miner":
//                                                      upgrade = (MenuUpgrade) menuContent;
//                                                      upgrade.getTiers().get(0).getUpgradeActions().forEach(upgradeAction -> upgradeAction.onBuy(team));
//                                                case "upgrade-forge":
//                                                case "upgrade-heal-pool":
//                                                      upgrade = (MenuUpgrade) menuContent;
//                                                      List<UpgradeTier> tiers = upgrade.getTiers();
//                                                      tiers.get(tiers.size() - 1).getUpgradeActions().forEach(upgradeAction -> upgradeAction.onBuy(team));
//                                          }
//                                    });
//                              } catch (ReflectiveOperationException e) {
//                                    e.printStackTrace();
//                              }

                              Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                                    // 放置防御
                                    if (team.isBedDestroyed()) return;
                                    Bed bed = (Bed) team.getBed().getBlock().getState().getData();
                                    Location bedLoc = team.getBed();
                                    int deltaX = bed.getFacing().getModX();
                                    int deltaZ = bed.getFacing().getModZ();
                                    if (bed.isHeadOfBed()) {
                                          bedLoc.subtract(deltaX, 0, deltaZ);
                                    }
                                    if (deltaX != 0) {
                                          ArrayList<Location> woods = new ArrayList<>(Arrays.asList(
                                                  bedLoc.clone().add(-deltaX, 0, 0),
                                                  bedLoc.clone().add(0, 0, 1),
                                                  bedLoc.clone().add(0, 0, -1),
                                                  bedLoc.clone().add(0, 1, 0),
                                                  bedLoc.clone().add(deltaX + deltaX, 0, 0),
                                                  bedLoc.clone().add(deltaX, 0, 1),
                                                  bedLoc.clone().add(deltaX, 0, -1),
                                                  bedLoc.clone().add(deltaX, 1, 0)));
                                          ArrayList<Location> wools = new ArrayList<>(Arrays.asList(
                                                  bedLoc.clone().add(-deltaX * 2, 0, 0),
                                                  bedLoc.clone().add(-deltaX, 1, 0),
                                                  bedLoc.clone().add(-deltaX, 0, 1),
                                                  bedLoc.clone().add(-deltaX, 0, -1),
                                                  bedLoc.clone().add(0, 2, 0),
                                                  bedLoc.clone().add(0, 1, 1),
                                                  bedLoc.clone().add(0, 1, -1),
                                                  bedLoc.clone().add(0, 0, 2),
                                                  bedLoc.clone().add(0, 0, -2),
                                                  bedLoc.clone().add(deltaX * 2 + deltaX, 0, 0),
                                                  bedLoc.clone().add(deltaX + deltaX, 1, 0),
                                                  bedLoc.clone().add(deltaX + deltaX, 0, 1),
                                                  bedLoc.clone().add(deltaX + deltaX, 0, -1),
                                                  bedLoc.clone().add(deltaX, 2, 0),
                                                  bedLoc.clone().add(deltaX, 1, 1),
                                                  bedLoc.clone().add(deltaX, 1, -1),
                                                  bedLoc.clone().add(deltaX, 0, 2),
                                                  bedLoc.clone().add(deltaX, 0, -2)
                                          ));
                                          ArrayList<Location> glasses = new ArrayList<>(Arrays.asList(
                                                  bedLoc.clone().add(-deltaX * 3, 0, 0),
                                                  bedLoc.clone().add(-deltaX * 2, 1, 0),
                                                  bedLoc.clone().add(-deltaX * 2, 0, -1),
                                                  bedLoc.clone().add(-deltaX * 2, 0, 1),
                                                  bedLoc.clone().add(-deltaX, 1, 1),
                                                  bedLoc.clone().add(-deltaX, 1, -1),
                                                  bedLoc.clone().add(-deltaX, 0, 2),
                                                  bedLoc.clone().add(-deltaX, 0, -2),
                                                  bedLoc.clone().add(-deltaX, 2, 0),
                                                  bedLoc.clone().add(0, 0, -3),
                                                  bedLoc.clone().add(0, 0, 3),
                                                  bedLoc.clone().add(0, 1, 2),
                                                  bedLoc.clone().add(0, 1, -2),
                                                  bedLoc.clone().add(0, 2, 1),
                                                  bedLoc.clone().add(0, 2, -1),
                                                  bedLoc.clone().add(0, 3, 0),
                                                  bedLoc.clone().add(deltaX * 3 + deltaX, 0, 0),
                                                  bedLoc.clone().add(deltaX * 2 + deltaX, 1, 0),
                                                  bedLoc.clone().add(deltaX * 2 + deltaX, 0, -1),
                                                  bedLoc.clone().add(deltaX * 2 + deltaX, 0, 1),
                                                  bedLoc.clone().add(deltaX + deltaX, 1, 1),
                                                  bedLoc.clone().add(deltaX + deltaX, 1, -1),
                                                  bedLoc.clone().add(deltaX + deltaX, 0, 2),
                                                  bedLoc.clone().add(deltaX + deltaX, 0, -2),
                                                  bedLoc.clone().add(deltaX + deltaX, 2, 0),
                                                  bedLoc.clone().add(deltaX, 0, -3),
                                                  bedLoc.clone().add(deltaX, 0, 3),
                                                  bedLoc.clone().add(deltaX, 1, 2),
                                                  bedLoc.clone().add(deltaX, 1, -2),
                                                  bedLoc.clone().add(deltaX, 2, 1),
                                                  bedLoc.clone().add(deltaX, 2, -1),
                                                  bedLoc.clone().add(deltaX, 3, 0)
                                          ));

                                          woods.forEach(location -> {
                                                if (location.getBlock().getType() == Material.AIR && location.distance(team.getSpawn()) >
                                                        arena.getIslandRadius()) {
                                                      location.getBlock().setType(Material.WOOD);
                                                      arena.addPlacedBlock(location.getBlock());
                                                }
                                          });
                                          wools.forEach(location -> {
                                                if (location.getBlock().getType() == Material.AIR && location.distance(team.getSpawn()) >
                                                        arena.getIslandRadius()) {
                                                      location.getBlock().setType(Material.WOOL);
                                                      location.getBlock().setData(team.getColor().itemByte());
                                                      arena.addPlacedBlock(location.getBlock());
                                                }
                                          });
                                          glasses.forEach(location -> {
                                                if (location.getBlock().getType() == Material.AIR && location.distance(team.getSpawn()) >
                                                        arena.getIslandRadius()) {
                                                      location.getBlock().setType(Material.STAINED_GLASS);
                                                      location.getBlock().setData(team.getColor().itemByte());
                                                      arena.addPlacedBlock(location.getBlock());
                                                }
                                          });
                                    } else if (deltaZ != 0) {
                                          ArrayList<Location> woods = new ArrayList<>(Arrays.asList(
                                                  bedLoc.clone().add(0, 0, -deltaZ),
                                                  bedLoc.clone().add(1, 0, 0),
                                                  bedLoc.clone().add(-1, 0, 0),
                                                  bedLoc.clone().add(0, 1, 0),
                                                  bedLoc.clone().add(0, 0, deltaZ + deltaZ),
                                                  bedLoc.clone().add(1, 0, deltaZ),
                                                  bedLoc.clone().add(-1, 0, deltaZ),
                                                  bedLoc.clone().add(0, 1, deltaZ)));
                                          ArrayList<Location> wools = new ArrayList<>(Arrays.asList(
                                                  bedLoc.clone().add(0, 0, -deltaZ * 2),
                                                  bedLoc.clone().add(0, 1, -deltaZ),
                                                  bedLoc.clone().add(1, 0, -deltaZ),
                                                  bedLoc.clone().add(-1, 0, -deltaZ),
                                                  bedLoc.clone().add(0, 2, 0),
                                                  bedLoc.clone().add(1, 1, 0),
                                                  bedLoc.clone().add(-1, 1, 0),
                                                  bedLoc.clone().add(2, 0, 0),
                                                  bedLoc.clone().add(-2, 0, 0),
                                                  bedLoc.clone().add(0, 0, deltaZ * 2 + deltaZ),
                                                  bedLoc.clone().add(0, 1, deltaZ + deltaZ),
                                                  bedLoc.clone().add(1, 0, deltaZ + deltaZ),
                                                  bedLoc.clone().add(-1, 0, deltaZ + deltaZ),
                                                  bedLoc.clone().add(0, 2, deltaZ),
                                                  bedLoc.clone().add(1, 1, deltaZ),
                                                  bedLoc.clone().add(-1, 1, deltaZ),
                                                  bedLoc.clone().add(2, 0, deltaZ),
                                                  bedLoc.clone().add(-2, 0, deltaZ)
                                          ));
                                          ArrayList<Location> glasses = new ArrayList<>(Arrays.asList(
                                                  bedLoc.clone().add(0, 0, -deltaZ * 3),
                                                  bedLoc.clone().add(0, 1, -deltaZ * 2),
                                                  bedLoc.clone().add(-1, 0, -deltaZ * 2),
                                                  bedLoc.clone().add(1, 0, -deltaZ * 2),
                                                  bedLoc.clone().add(1, 1, -deltaZ),
                                                  bedLoc.clone().add(-1, 1, -deltaZ),
                                                  bedLoc.clone().add(2, 0, -deltaZ),
                                                  bedLoc.clone().add(-2, 0, -deltaZ),
                                                  bedLoc.clone().add(0, 2, -deltaZ),
                                                  bedLoc.clone().add(-3, 0, 0),
                                                  bedLoc.clone().add(3, 0, 0),
                                                  bedLoc.clone().add(2, 1, 0),
                                                  bedLoc.clone().add(-2, 1, 0),
                                                  bedLoc.clone().add(1, 2, 0),
                                                  bedLoc.clone().add(-1, 2, 0),
                                                  bedLoc.clone().add(0, 3, 0),
                                                  bedLoc.clone().add(0, 0, deltaZ * 3 + deltaZ),
                                                  bedLoc.clone().add(0, 1, deltaZ * 2 + deltaZ),
                                                  bedLoc.clone().add(-1, 0, deltaZ * 2 + deltaZ),
                                                  bedLoc.clone().add(1, 0, deltaZ * 2 + deltaZ),
                                                  bedLoc.clone().add(1, 1, deltaZ + deltaZ),
                                                  bedLoc.clone().add(-1, 1, deltaZ + deltaZ),
                                                  bedLoc.clone().add(2, 0, deltaZ + deltaZ),
                                                  bedLoc.clone().add(-2, 0, deltaZ + deltaZ),
                                                  bedLoc.clone().add(0, 2, deltaZ + deltaZ),
                                                  bedLoc.clone().add(-3, 0, deltaZ),
                                                  bedLoc.clone().add(3, 0, deltaZ),
                                                  bedLoc.clone().add(2, 1, deltaZ),
                                                  bedLoc.clone().add(-2, 1, deltaZ),
                                                  bedLoc.clone().add(1, 2, deltaZ),
                                                  bedLoc.clone().add(-1, 2, deltaZ),
                                                  bedLoc.clone().add(0, 3, deltaZ)
                                          ));

                                          woods.forEach(location -> {
                                                if (location.getBlock().getType() == Material.AIR && location.distance(team.getSpawn()) >
                                                        arena.getIslandRadius()) {
                                                      location.getBlock().setType(Material.WOOD);
                                                      arena.addPlacedBlock(location.getBlock());
                                                }
                                          });
                                          wools.forEach(location -> {
                                                if (location.getBlock().getType() == Material.AIR && location.distance(team.getSpawn()) >
                                                        arena.getIslandRadius()) {
                                                      location.getBlock().setType(Material.WOOL);
                                                      location.getBlock().setData(team.getColor().itemByte());
                                                      arena.addPlacedBlock(location.getBlock());
                                                }
                                          });
                                          glasses.forEach(location -> {
                                                if (location.getBlock().getType() == Material.AIR && location.distance(team.getSpawn()) >
                                                        arena.getIslandRadius()) {
                                                      location.getBlock().setType(Material.STAINED_GLASS);
                                                      location.getBlock().setData(team.getColor().itemByte());
                                                      arena.addPlacedBlock(location.getBlock());
                                                }
                                          });
                                    }

                              }, 4L);
                        }
                  }, 2);
                  // 资源点升级
                  Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> arena.getOreGenerators().forEach(iGenerator -> {
                        if (iGenerator.getType() == GeneratorType.DIAMOND || iGenerator.getType() == GeneratorType.EMERALD) {
                              iGenerator.upgrade();
                              iGenerator.upgrade();
                        }
                  }), 100L);
            }
      }

/*
    @EventHandler
    public void handle(BlockPlaceEvent event) {

        // 放置防御
        Bed bed = (Bed) event.getBlockPlaced().getState().getData();
        Location bedLoc = event.getBlockPlaced().getLocation();
        int deltaX = bed.getFacing().getModX();
        int deltaZ = bed.getFacing().getModZ();
        if (deltaX != 0) {
            ArrayList<Location> woods = new ArrayList<>(Arrays.asList(
                    bedLoc.clone().add(-deltaX, 0, 0),
                    bedLoc.clone().add(0, 0, 1),
                    bedLoc.clone().add(0, 0, -1),
                    bedLoc.clone().add(0, 1, 0),
                    bedLoc.clone().add(deltaX + deltaX, 0, 0),
                    bedLoc.clone().add(deltaX, 0, 1),
                    bedLoc.clone().add(deltaX, 0, -1),
                    bedLoc.clone().add(deltaX, 1, 0)));
            ArrayList<Location> wools = new ArrayList<>(Arrays.asList(
                    bedLoc.clone().add(-deltaX * 2, 0, 0),
                    bedLoc.clone().add(-deltaX, 1, 0),
                    bedLoc.clone().add(-deltaX, 0, 1),
                    bedLoc.clone().add(-deltaX, 0, -1),
                    bedLoc.clone().add(0, 2, 0),
                    bedLoc.clone().add(0, 1, 1),
                    bedLoc.clone().add(0, 1, -1),
                    bedLoc.clone().add(0, 0, 2),
                    bedLoc.clone().add(0, 0, -2),
                    bedLoc.clone().add(deltaX * 2 + deltaX, 0, 0),
                    bedLoc.clone().add(deltaX + deltaX, 1, 0),
                    bedLoc.clone().add(deltaX + deltaX, 0, 1),
                    bedLoc.clone().add(deltaX + deltaX, 0, -1),
                    bedLoc.clone().add(deltaX, 2, 0),
                    bedLoc.clone().add(deltaX, 1, 1),
                    bedLoc.clone().add(deltaX, 1, -1),
                    bedLoc.clone().add(deltaX, 0, 2),
                    bedLoc.clone().add(deltaX, 0, -2)
            ));
            ArrayList<Location> glasses = new ArrayList<>(Arrays.asList(
                    bedLoc.clone().add(-deltaX * 3, 0, 0),
                    bedLoc.clone().add(-deltaX * 2, 1, 0),
                    bedLoc.clone().add(-deltaX * 2, 0, -1),
                    bedLoc.clone().add(-deltaX * 2, 0, 1),
                    bedLoc.clone().add(-deltaX, 1, 1),
                    bedLoc.clone().add(-deltaX, 1, -1),
                    bedLoc.clone().add(-deltaX, 0, 2),
                    bedLoc.clone().add(-deltaX, 0, -2),
                    bedLoc.clone().add(-deltaX, 2, 0),
                    bedLoc.clone().add(0, 0, -3),
                    bedLoc.clone().add(0, 0, 3),
                    bedLoc.clone().add(0, 1, 2),
                    bedLoc.clone().add(0, 1, -2),
                    bedLoc.clone().add(0, 2, 1),
                    bedLoc.clone().add(0, 2, -1),
                    bedLoc.clone().add(0, 3, 0),
                    bedLoc.clone().add(deltaX * 3 + deltaX, 0, 0),
                    bedLoc.clone().add(deltaX * 2 + deltaX, 1, 0),
                    bedLoc.clone().add(deltaX * 2 + deltaX, 0, -1),
                    bedLoc.clone().add(deltaX * 2 + deltaX, 0, 1),
                    bedLoc.clone().add(deltaX + deltaX, 1, 1),
                    bedLoc.clone().add(deltaX + deltaX, 1, -1),
                    bedLoc.clone().add(deltaX + deltaX, 0, 2),
                    bedLoc.clone().add(deltaX + deltaX, 0, -2),
                    bedLoc.clone().add(deltaX + deltaX, 2, 0),
                    bedLoc.clone().add(deltaX, 0, -3),
                    bedLoc.clone().add(deltaX, 0, 3),
                    bedLoc.clone().add(deltaX, 1, 2),
                    bedLoc.clone().add(deltaX, 1, -2),
                    bedLoc.clone().add(deltaX, 2, 1),
                    bedLoc.clone().add(deltaX, 2, -1),
                    bedLoc.clone().add(deltaX, 3, 0)
            ));

            woods.forEach(location -> {
                if (location.getBlock().getType() == Material.AIR) {
                    location.getBlock().setType(Material.WOOD);
                }
            });
            wools.forEach(location -> {
                if (location.getBlock().getType() == Material.AIR) {
                    location.getBlock().setType(Material.WOOL);
                    location.getBlock().setData((byte) 14);
                }
            });
            glasses.forEach(location -> {
                if (location.getBlock().getType() == Material.AIR) {
                    location.getBlock().setType(Material.STAINED_GLASS);
                    location.getBlock().setData((byte) 14);
                }
            });
        } else if (deltaZ != 0) {
            ArrayList<Location> woods = new ArrayList<>(Arrays.asList(
                    bedLoc.clone().add(0, 0, -deltaZ),
                    bedLoc.clone().add(1, 0, 0),
                    bedLoc.clone().add(-1, 0, 0),
                    bedLoc.clone().add(0, 1, 0),
                    bedLoc.clone().add(0, 0, deltaZ + deltaZ),
                    bedLoc.clone().add(1, 0, deltaZ),
                    bedLoc.clone().add(-1, 0, deltaZ),
                    bedLoc.clone().add(0, 1, deltaZ)));
            ArrayList<Location> wools = new ArrayList<>(Arrays.asList(
                    bedLoc.clone().add(0, 0, -deltaZ * 2),
                    bedLoc.clone().add(0, 1, -deltaZ),
                    bedLoc.clone().add(1, 0, -deltaZ),
                    bedLoc.clone().add(-1, 0, -deltaZ),
                    bedLoc.clone().add(0, 2, 0),
                    bedLoc.clone().add(1, 1, 0),
                    bedLoc.clone().add(-1, 1, 0),
                    bedLoc.clone().add(2, 0, 0),
                    bedLoc.clone().add(-2, 0, 0),
                    bedLoc.clone().add(0, 0, deltaZ * 2 + deltaZ),
                    bedLoc.clone().add(0, 1, deltaZ + deltaZ),
                    bedLoc.clone().add(1, 0, deltaZ + deltaZ),
                    bedLoc.clone().add(-1, 0, deltaZ + deltaZ),
                    bedLoc.clone().add(0, 2, deltaZ),
                    bedLoc.clone().add(1, 1, deltaZ),
                    bedLoc.clone().add(-1, 1, deltaZ),
                    bedLoc.clone().add(2, 0, deltaZ),
                    bedLoc.clone().add(-2, 0, deltaZ)
            ));
            ArrayList<Location> glasses = new ArrayList<>(Arrays.asList(
                    bedLoc.clone().add(0, 0, -deltaZ * 3),
                    bedLoc.clone().add(0, 1, -deltaZ * 2),
                    bedLoc.clone().add(-1, 0, -deltaZ * 2),
                    bedLoc.clone().add(1, 0, -deltaZ * 2),
                    bedLoc.clone().add(1, 1, -deltaZ),
                    bedLoc.clone().add(-1, 1, -deltaZ),
                    bedLoc.clone().add(2, 0, -deltaZ),
                    bedLoc.clone().add(-2, 0, -deltaZ),
                    bedLoc.clone().add(0, 2, -deltaZ),
                    bedLoc.clone().add(-3, 0, 0),
                    bedLoc.clone().add(3, 0, 0),
                    bedLoc.clone().add(2, 1, 0),
                    bedLoc.clone().add(-2, 1, 0),
                    bedLoc.clone().add(1, 2, 0),
                    bedLoc.clone().add(-1, 2, 0),
                    bedLoc.clone().add(0, 3, 0),
                    bedLoc.clone().add(0, 0, deltaZ * 3 + deltaZ),
                    bedLoc.clone().add(0, 1, deltaZ * 2 + deltaZ),
                    bedLoc.clone().add(-1, 0, deltaZ * 2 + deltaZ),
                    bedLoc.clone().add(1, 0, deltaZ * 2 + deltaZ),
                    bedLoc.clone().add(1, 1, deltaZ + deltaZ),
                    bedLoc.clone().add(-1, 1, deltaZ + deltaZ),
                    bedLoc.clone().add(2, 0, deltaZ + deltaZ),
                    bedLoc.clone().add(-2, 0, deltaZ + deltaZ),
                    bedLoc.clone().add(0, 2, deltaZ + deltaZ),
                    bedLoc.clone().add(-3, 0, deltaZ),
                    bedLoc.clone().add(3, 0, deltaZ),
                    bedLoc.clone().add(2, 1, deltaZ),
                    bedLoc.clone().add(-2, 1, deltaZ),
                    bedLoc.clone().add(1, 2, deltaZ),
                    bedLoc.clone().add(-1, 2, deltaZ),
                    bedLoc.clone().add(0, 3, deltaZ)
            ));


            woods.forEach(location -> {
                if (location.getBlock().getType() == Material.AIR) {
                    location.getBlock().setType(Material.WOOD);
                }
            });
            wools.forEach(location -> {
                if (location.getBlock().getType() == Material.AIR) {
                    location.getBlock().setType(Material.WOOL);
                    location.getBlock().setData((byte) 14);
                }
            });
            glasses.forEach(location -> {
                if (location.getBlock().getType() == Material.AIR) {
                    location.getBlock().setType(Material.STAINED_GLASS);
                    location.getBlock().setData((byte) 14);
                }
            });
        }
    }*/
}
