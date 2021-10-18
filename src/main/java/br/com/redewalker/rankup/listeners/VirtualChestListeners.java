package br.com.redewalker.rankup.listeners;

import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.objects.RankPlayer;
import br.com.redewalker.rankup.objects.VirtualChest;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class VirtualChestListeners implements Listener {

    public static HashMap<Player, VirtualChest> renameVirtualChest = new HashMap<>();


    @EventHandler
    void inventoryClose(InventoryCloseEvent event){
        RankPlayer rankPlayer = Rankup.getRankup().getRankPlayerManager().getRankupPlayer(event.getPlayer().getName());
        if(rankPlayer.getVirtualChests().isEmpty()) return;
        rankPlayer.getVirtualChests().forEach(c->{
            if(c.getChestTitle().equalsIgnoreCase(event.getInventory().getName())){
                c.setChestInventory(Rankup.getRankup().getVirtualChestManager().InventoryToString(event.getInventory()));
                Rankup.getRankup().getRankPlayerManager().getDao().saveEntity(rankPlayer);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        RankPlayer rankPlayer = Rankup.getRankup().getRankPlayerManager().getRankupPlayer(player.getName());

        if(renameVirtualChest.containsKey(player)){
            event.setCancelled(true);
            if(event.getMessage().equalsIgnoreCase("cancelar")){
                player.sendMessage(ChatColor.RED + "Operação cancelada!");
                renameVirtualChest.remove(player);
                return;
            }

            if(event.getMessage().length() > 32){
                player.sendMessage(ChatColor.RED + "O tamanho limite do nome de um baú é de 32 caracteres.");
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                return;
            }

            VirtualChest virtualChest = renameVirtualChest.get(player);
            virtualChest.setName(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
            Rankup.getRankup().getRankPlayerManager().getDao().saveEntity(rankPlayer);
            player.sendMessage(ChatColor.GREEN + "Baú renomeado com sucesso!");
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);
            renameVirtualChest.remove(player);
        }
    }

    @EventHandler
    void onQuit(PlayerQuitEvent event){
        renameVirtualChest.remove(event.getPlayer());
    }

    @EventHandler
    void openInventory(InventoryOpenEvent event){
        if(!(event.getPlayer() instanceof Player)) return;
        if(renameVirtualChest.containsKey((Player)event.getPlayer())){
            event.getPlayer().sendMessage(ChatColor.RED + "Operação cancelada!");
            renameVirtualChest.remove((Player) event.getPlayer());
        }
    }
}
