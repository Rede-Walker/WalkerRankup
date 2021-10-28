package br.com.redewalker.rankup.systems.rank.events;

import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.systems.rank.Rank;
import br.com.redewalker.rankup.systems.rankplayer.RankPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRankupEvent extends Event {

    @Getter private final Player player;
    @Getter private final RankPlayer rankPlayer;
    @Getter private final Rank rank;
    private static final HandlerList handlerList = new HandlerList();


    public PlayerRankupEvent(Player player, Rank rank){
        this.player = player;
        this.rankPlayer = Rankup.getRankup().getRankPlayerManager().getRankupPlayer(player.getName());
        this.rank = rank;
    }


    @Override
    public HandlerList getHandlers(){
        return this.handlerList;
    }

    public static HandlerList getHandlerList() { return handlerList; }
}
