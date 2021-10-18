package br.com.redewalker.rankup.listeners;

import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.objects.RankPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class VirtualChestListeners implements Listener {

    @EventHandler
    void inventoryClose(InventoryCloseEvent event){
        RankPlayer rankPlayer = Rankup.getRankup().getRankPlayerManager().getRankupPlayer(event.getPlayer().getName());
        if(rankPlayer.getVirtualChests().isEmpty()) return;
        rankPlayer.getVirtualChests().forEach(c->{
            if(c.getChestName().equalsIgnoreCase(event.getInventory().getName())){
                c.setChestInventory(Rankup.getRankup().getVirtualChestManager().InventoryToString(event.getInventory()));
            }
        });
    }
}
