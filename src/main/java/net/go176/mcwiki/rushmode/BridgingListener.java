package net.go176.mcwiki.rushmode;

// #Debug: 注释
import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("all")
public class BridgingListener implements Listener {

      public void playSound(Player player, Location location) {
            PacketContainer container = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.NAMED_SOUND_EFFECT);
            container.getStrings().write(0, "dig.cloth");
            container.getIntegers().write(0, (int) (location.getX() * 8.0D));
            container.getIntegers().write(1, (int) (location.getY() * 8.0D));
            container.getIntegers().write(2, (int) (location.getZ() * 8.0D));
            container.getFloat().write(0, 4f);
            container.getIntegers().write(3, (int) (1f * 63f));
            try {
                  ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
            } catch (InvocationTargetException exception) {
                  exception.printStackTrace();
            }
      }

      // #Debug: 注释

      @EventHandler
      public void onBlockPlace(BlockPlaceEvent event) {
            BedWars bedwarsAPI = Bukkit.getServicesManager().getRegistration(BedWars .class).getProvider();
            // 搭路逻辑
            if (event.getBlockPlaced().getType() == Material.WOOL) {
                  if (bedwarsAPI.getArenaUtil().isPlaying(event.getPlayer())) {
                        if (RushMode.getInstance().getBridgingMode().get(event.getPlayer().getUniqueId())) {
                              IArena arena = bedwarsAPI.getArenaUtil().getArenaByPlayer(event.getPlayer());

                              BlockFace face = event.getBlockPlaced().getFace(event.getBlockAgainst());
                              AtomicInteger distance = new AtomicInteger(1);
                              Location playerLocation = event.getPlayer().getLocation();
                              AtomicBoolean isTaskEnd = new AtomicBoolean(false);
                              arena.getRegionsList().forEach(region -> region.isInRegion(event.getBlockPlaced().getLocation()));
                              Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                                    if (event.getBlockPlaced().getLocation().getBlock().getType() == Material.AIR)
                                          isTaskEnd.set(true);
                                    Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                                    for (Player player : nextBlock.getWorld().getEntitiesByClass(Player.class)) {
                                          Location feetLoc = player.getLocation();
                                          Location eyeLoc = player.getEyeLocation();
                                          Location checkLoc = nextBlock.clone().add(.5, 0, .5);
                                          if (feetLoc.distance(checkLoc) <= 0.6 || eyeLoc.distance(checkLoc.add(0, 1, 0)) <= 0.6)
                                                isTaskEnd.set(true);
                                    }
                                    if (nextBlock.getBlock().getType() != Material.AIR
                                            || nextBlock.distance(arena.getTeam(event.getPlayer()).getSpawn()) < arena.getIslandRadius())
                                          isTaskEnd.set(true);
                                    arena.getTeams().forEach(team -> {
                                          if (nextBlock.distance(team.getSpawn()) < arena.getIslandRadius()) isTaskEnd.set(true);
                                    });

                                    if (!isTaskEnd.get()) {
                                          for (Player player : playerLocation.getWorld().getEntitiesByClass(Player.class)) {
                                                if (player.getLocation().distance(playerLocation) <= 4) {
                                                      playSound(player, playerLocation);
                                                }
                                          }
                                          nextBlock.getBlock().setType(Material.WOOL);
                                          nextBlock.getBlock().setData(event.getBlockPlaced().getData());

                                          distance.addAndGet(1);
                                          arena.addPlacedBlock(nextBlock.getBlock());
                                    }
                              }, 2);
                              Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                                    Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                                    for (Player player : nextBlock.getWorld().getEntitiesByClass(Player.class)) {
                                          Location feetLoc = player.getLocation();
                                          Location eyeLoc = player.getEyeLocation();
                                          Location checkLoc = nextBlock.clone().add(.5, 0, .5);
                                          if (feetLoc.distance(checkLoc) <= 0.6 || eyeLoc.distance(checkLoc.add(0, 1, 0)) <= 0.6)
                                                isTaskEnd.set(true);
                                    }
                                    if (nextBlock.getBlock().getType() != Material.AIR
                                            || nextBlock.distance(arena.getTeam(event.getPlayer()).getSpawn()) < arena.getIslandRadius())
                                          isTaskEnd.set(true);

                                    if (!isTaskEnd.get()) {
                                          for (Player player : playerLocation.getWorld().getEntitiesByClass(Player.class)) {
                                                if (player.getLocation().distance(playerLocation) <= 4) {
                                                      playSound(player, playerLocation);
                                                }
                                          }
                                          nextBlock.getBlock().setType(Material.WOOL);
                                          nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                                          distance.addAndGet(1);
                                          arena.addPlacedBlock(nextBlock.getBlock());
                                    }
                              }, 4);
                              Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                                    Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                                    for (Player player : nextBlock.getWorld().getEntitiesByClass(Player.class)) {
                                          Location feetLoc = player.getLocation();
                                          Location eyeLoc = player.getEyeLocation();
                                          Location checkLoc = nextBlock.clone().add(.5, 0, .5);
                                          if (feetLoc.distance(checkLoc) <= 0.6 || eyeLoc.distance(checkLoc.add(0, 1, 0)) <= 0.6)
                                                isTaskEnd.set(true);
                                    }
                                    if (nextBlock.getBlock().getType() != Material.AIR
                                            || nextBlock.distance(arena.getTeam(event.getPlayer()).getSpawn()) < arena.getIslandRadius())
                                          isTaskEnd.set(true);

                                    if (!isTaskEnd.get()) {
                                          for (Player player : playerLocation.getWorld().getEntitiesByClass(Player.class)) {
                                                if (player.getLocation().distance(playerLocation) <= 4) {
                                                      playSound(player, playerLocation);
                                                }
                                          }
                                          nextBlock.getBlock().setType(Material.WOOL);
                                          nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                                          distance.addAndGet(1);
                                          arena.addPlacedBlock(nextBlock.getBlock());
                                    }
                              }, 6);
                              Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                                    Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                                    for (Player player : nextBlock.getWorld().getEntitiesByClass(Player.class)) {
                                          Location feetLoc = player.getLocation();
                                          Location eyeLoc = player.getEyeLocation();
                                          Location checkLoc = nextBlock.clone().add(.5, 0, .5);
                                          if (feetLoc.distance(checkLoc) <= 0.6 || eyeLoc.distance(checkLoc.add(0, 1, 0)) <= 0.6)
                                                isTaskEnd.set(true);

                                    }
                                    if (nextBlock.getBlock().getType() != Material.AIR
                                            || nextBlock.distance(arena.getTeam(event.getPlayer()).getSpawn()) < arena.getIslandRadius())
                                          isTaskEnd.set(true);

                                    if (!isTaskEnd.get()) {
                                          for (Player player : playerLocation.getWorld().getEntitiesByClass(Player.class)) {
                                                if (player.getLocation().distance(playerLocation) <= 4) {
                                                      playSound(player, playerLocation);
                                                }
                                          }
                                          nextBlock.getBlock().setType(Material.WOOL);
                                          nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                                          distance.addAndGet(1);
                                          arena.addPlacedBlock(nextBlock.getBlock());
                                    }
                              }, 8);
                              Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                                    Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                                    for (Player player : nextBlock.getWorld().getEntitiesByClass(Player.class)) {
                                          Location feetLoc = player.getLocation();
                                          Location eyeLoc = player.getEyeLocation();
                                          Location checkLoc = nextBlock.clone().add(.5, 0, .5);
                                          if (feetLoc.distance(checkLoc) <= 0.6 || eyeLoc.distance(checkLoc.add(0, 1, 0)) <= 0.6)
                                                isTaskEnd.set(true);
                                    }
                                    if (nextBlock.getBlock().getType() != Material.AIR
                                            || nextBlock.distance(arena.getTeam(event.getPlayer()).getSpawn()) < arena.getIslandRadius())
                                          isTaskEnd.set(true);

                                    if (!isTaskEnd.get()) {
                                          for (Player player : playerLocation.getWorld().getEntitiesByClass(Player.class)) {
                                                if (player.getLocation().distance(playerLocation) <= 4) {
                                                      playSound(player, playerLocation);
                                                }
                                          }
                                          nextBlock.getBlock().setType(Material.WOOL);
                                          nextBlock.getBlock().setData(event.getBlockPlaced().getData());

                                          distance.addAndGet(1);
                                          arena.addPlacedBlock(nextBlock.getBlock());
                                    }
                              }, 10);
                              Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                                    Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                                    for (Player player : nextBlock.getWorld().getEntitiesByClass(Player.class)) {
                                          Location feetLoc = player.getLocation();
                                          Location eyeLoc = player.getEyeLocation();
                                          Location checkLoc = nextBlock.clone().add(.5, 0, .5);
                                          if (feetLoc.distance(checkLoc) <= 0.6 || eyeLoc.distance(checkLoc.add(0, 1, 0)) <= 0.6)
                                                isTaskEnd.set(true);
                                    }
                                    if (nextBlock.getBlock().getType() != Material.AIR
                                            || nextBlock.distance(arena.getTeam(event.getPlayer()).getSpawn()) < arena.getIslandRadius())
                                          isTaskEnd.set(true);

                                    if (!isTaskEnd.get()) {
                                          for (Player player : playerLocation.getWorld().getEntitiesByClass(Player.class)) {
                                                if (player.getLocation().distance(playerLocation) <= 4) {
                                                      playSound(player, playerLocation);
                                                }
                                          }
                                          nextBlock.getBlock().setType(Material.WOOL);
                                          nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                                          distance.addAndGet(1);
                                          arena.addPlacedBlock(nextBlock.getBlock());
                                    }
                              }, 12);
                        }
                  }
            }
      }

/*
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // 搭路逻辑
        if (event.getBlockPlaced().getType() == Material.WOOL) {
            if (BwdProgram.getInstance().getBridgingMode().get(event.getPlayer().getUniqueId())) {

                BlockFace face = event.getBlockPlaced().getFace(event.getBlockAgainst());
                AtomicInteger distance = new AtomicInteger(1);
                Location playerLocation = event.getPlayer().getLocation();
                AtomicBoolean isTaskEnd = new AtomicBoolean(false);
                Bukkit.getScheduler().runTaskLater(BwdProgram.getInstance(), () -> {
                    if (event.getBlockPlaced().getLocation().getBlock().getType() == Material.AIR)
                        isTaskEnd.set(true);
                    Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                    for (Player player : nextBlock.getWorld().getEntitiesByClass(Player.class)) {
                        Location feetLoc = player.getLocation();
                        Location eyeLoc = player.getEyeLocation();
                        Location checkLoc = nextBlock.clone().add(.5, 0, .5);
                        if (feetLoc.distance(checkLoc) <= 0.6 || eyeLoc.distance(checkLoc.add(0, 1, 0)) <= 0.6)
                            isTaskEnd.set(true);
                    }
                    if (nextBlock.getBlock().getType() != Material.AIR)
                        isTaskEnd.set(true);

                    if (!isTaskEnd.get()) {
                        for (Player player : playerLocation.getWorld().getEntitiesByClass(Player.class)) {
                            if (player.getLocation().distance(playerLocation) <= 4) {
                                playSound(player, playerLocation);
                            }
                        }
                        nextBlock.getBlock().setType(Material.WOOL);
                        nextBlock.getBlock().setData(event.getBlockPlaced().getData());

                        distance.addAndGet(1);
                    }
                }, 2);
                Bukkit.getScheduler().runTaskLater(BwdProgram.getInstance(), () -> {
                    Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                    for (Player player : nextBlock.getWorld().getEntitiesByClass(Player.class)) {
                        Location feetLoc = player.getLocation();
                        Location eyeLoc = player.getEyeLocation();
                        Location checkLoc = nextBlock.clone().add(.5, 0, .5);
                        if (feetLoc.distance(checkLoc) <= 0.6 || eyeLoc.distance(checkLoc.add(0, 1, 0)) <= 0.6)
                            isTaskEnd.set(true);
                    }
                    if (nextBlock.getBlock().getType() != Material.AIR)
                        isTaskEnd.set(true);

                    if (!isTaskEnd.get()) {
                        for (Player player : playerLocation.getWorld().getEntitiesByClass(Player.class)) {
                            if (player.getLocation().distance(playerLocation) <= 4) {
                                playSound(player, playerLocation);
                            }
                        }
                        nextBlock.getBlock().setType(Material.WOOL);
                        nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                        distance.addAndGet(1);
                    }
                }, 4);
                Bukkit.getScheduler().runTaskLater(BwdProgram.getInstance(), () -> {
                    Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                    for (Player player : nextBlock.getWorld().getEntitiesByClass(Player.class)) {
                        Location feetLoc = player.getLocation();
                        Location eyeLoc = player.getEyeLocation();
                        Location checkLoc = nextBlock.clone().add(.5, 0, .5);
                        if (feetLoc.distance(checkLoc) <= 0.6 || eyeLoc.distance(checkLoc.add(0, 1, 0)) <= 0.6)
                            isTaskEnd.set(true);
                    }
                    if (nextBlock.getBlock().getType() != Material.AIR)
                        isTaskEnd.set(true);

                    if (!isTaskEnd.get()) {
                        for (Player player : playerLocation.getWorld().getEntitiesByClass(Player.class)) {
                            if (player.getLocation().distance(playerLocation) <= 4) {
                                playSound(player, playerLocation);
                            }
                        }
                        nextBlock.getBlock().setType(Material.WOOL);
                        nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                        distance.addAndGet(1);
                    }
                }, 6);
                Bukkit.getScheduler().runTaskLater(BwdProgram.getInstance(), () -> {
                    Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                    for (Player player : nextBlock.getWorld().getEntitiesByClass(Player.class)) {
                        Location feetLoc = player.getLocation();
                        Location eyeLoc = player.getEyeLocation();
                        Location checkLoc = nextBlock.clone().add(.5, 0, .5);
                        if (feetLoc.distance(checkLoc) <= 0.6 || eyeLoc.distance(checkLoc.add(0, 1, 0)) <= 0.6)
                            isTaskEnd.set(true);

                    }
                    if (nextBlock.getBlock().getType() != Material.AIR)
                        isTaskEnd.set(true);

                    if (!isTaskEnd.get()) {
                        for (Player player : playerLocation.getWorld().getEntitiesByClass(Player.class)) {
                            if (player.getLocation().distance(playerLocation) <= 4) {
                                playSound(player, playerLocation);
                            }
                        }
                        nextBlock.getBlock().setType(Material.WOOL);
                        nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                        distance.addAndGet(1);
                    }
                }, 8);
                Bukkit.getScheduler().runTaskLater(BwdProgram.getInstance(), () -> {
                    Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                    for (Player player : nextBlock.getWorld().getEntitiesByClass(Player.class)) {
                        Location feetLoc = player.getLocation();
                        Location eyeLoc = player.getEyeLocation();
                        Location checkLoc = nextBlock.clone().add(.5, 0, .5);
                        if (feetLoc.distance(checkLoc) <= 0.6 || eyeLoc.distance(checkLoc.add(0, 1, 0)) <= 0.6)
                            isTaskEnd.set(true);
                    }
                    if (nextBlock.getBlock().getType() != Material.AIR)
                        isTaskEnd.set(true);

                    if (!isTaskEnd.get()) {
                        for (Player player : playerLocation.getWorld().getEntitiesByClass(Player.class)) {
                            if (player.getLocation().distance(playerLocation) <= 4) {
                                playSound(player, playerLocation);
                            }
                        }
                        nextBlock.getBlock().setType(Material.WOOL);
                        nextBlock.getBlock().setData(event.getBlockPlaced().getData());

                        distance.addAndGet(1);
                    }
                }, 10);
                Bukkit.getScheduler().runTaskLater(BwdProgram.getInstance(), () -> {
                    Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                    for (Player player : nextBlock.getWorld().getEntitiesByClass(Player.class)) {
                        Location feetLoc = player.getLocation();
                        Location eyeLoc = player.getEyeLocation();
                        Location checkLoc = nextBlock.clone().add(.5, 0, .5);
                        if (feetLoc.distance(checkLoc) <= 0.6 || eyeLoc.distance(checkLoc.add(0, 1, 0)) <= 0.6)
                            isTaskEnd.set(true);
                    }
                    if (nextBlock.getBlock().getType() != Material.AIR)
                        isTaskEnd.set(true);

                    if (!isTaskEnd.get()) {
                        for (Player player : playerLocation.getWorld().getEntitiesByClass(Player.class)) {
                            if (player.getLocation().distance(playerLocation) <= 4) {
                                playSound(player, playerLocation);
                            }
                        }
                        nextBlock.getBlock().setType(Material.WOOL);
                        nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                        distance.addAndGet(1);
                    }
                }, 12);
            }
        }
    }*/
}

