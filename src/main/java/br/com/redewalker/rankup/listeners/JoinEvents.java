package br.com.redewalker.rankup.listeners;

import br.com.redewalker.rankup.Rankup;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Rankup.getRankup().getRankupPlayerManager().handleRankupPlayerCreation(event.getPlayer().getName());
    }
}
