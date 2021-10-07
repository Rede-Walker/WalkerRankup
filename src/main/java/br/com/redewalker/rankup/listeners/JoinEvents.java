package br.com.redewalker.rankup.listeners;

import br.com.redewalker.api.events.PlayerPreQuitEvent;
import br.com.redewalker.common.database.exceptions.JsonSerializeException;
import br.com.redewalker.common.database.objects.ObjectForSave;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.dao.RankPlayerDAO;
import br.com.redewalker.rankup.objects.RankPlayer;
import com.mongodb.client.model.Filters;
import lombok.SneakyThrows;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Rankup.getRankup().getRankPlayerManager().handleRankupPlayerCreation(event.getPlayer().getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Rankup.getRankup().getRankPlayerManager().getDao().saveEntityAsync(Rankup.getRankup().getRankPlayerManager().getRankupPlayer(event.getPlayer().getName()));
    }

    @SneakyThrows
    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerQuitAsyncEvent(PlayerPreQuitEvent e) {
        if (e.isCancelled()) return;
        RankPlayerDAO dao = (RankPlayerDAO) Rankup.getRankup().getRankPlayerManager().getDao();
        try {
            String name = e.getUser().getPlayerName();
            RankPlayer rankPlayer = Rankup.getRankup().getRankPlayerManager().getRankupPlayer(name);
            e.addForSave(dao, new ObjectForSave(Filters.eq(dao.getData().getKey(rankPlayer)), dao.convertToJson(rankPlayer)));
            dao.removeEntityforUpdates(rankPlayer);
        } catch (JsonSerializeException ex) {
            ex.printStackTrace();
        }
    }
}
