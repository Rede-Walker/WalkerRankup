package br.com.redewalker.rankup.listeners;

import br.com.redewalker.rankup.systems.rank.events.PlayerRankupEvent;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RankupListener implements Listener {

    @EventHandler
    public void rankupEvent(PlayerRankupEvent event){
        Player player = event.getPlayer();

        player.sendMessage(ChatColor.GREEN + "Rank upado com sucesso!");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.5F, 1.5F);
        player.playEffect(EntityEffect.FIREWORK_EXPLODE)                                    ;
    }
}
