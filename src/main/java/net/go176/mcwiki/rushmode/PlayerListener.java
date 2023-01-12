package net.go176.mcwiki.rushmode;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.InvocationTargetException;

public class PlayerListener implements Listener {

      public void sendActionBar(Player player, String message) {
            PacketContainer container = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CHAT);
            container.getChatComponents().write(0, WrappedChatComponent.fromText(message));
            container.getBytes().write(0, (byte) 2);
            try {
                  ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
            } catch (InvocationTargetException exception) {
                  exception.printStackTrace();
            }
      }

      @EventHandler
      public void onInteract(PlayerInteractEvent event) {
            // 模式切换逻辑
            if (event.getAction().name().startsWith("LEFT_")) {
                  if (event.getMaterial() == Material.WOOL) {
                        if (RushMode.getInstance().getBridgingMode().get(event.getPlayer().getUniqueId())) {
                              RushMode.getInstance().getBridgingMode().replace(event.getPlayer().getUniqueId(), false);
                              sendActionBar(event.getPlayer(), "§c搭路模式已关闭");
                        } else {
                              RushMode.getInstance().getBridgingMode().replace(event.getPlayer().getUniqueId(), true);
                              sendActionBar(event.getPlayer(), "§a搭路模式已开启");
                        }
                  }
            }
      }

}
