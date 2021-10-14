package br.com.redewalker.rankup;

import br.com.redewalker.api.API;
import br.com.redewalker.rankup.commands.CoinsCommand;
import br.com.redewalker.rankup.listeners.JoinEvents;
import br.com.redewalker.rankup.manager.CoinsRankingManager;
import br.com.redewalker.rankup.manager.RankManager;
import br.com.redewalker.rankup.manager.RankPlayerManager;
import br.com.redewalker.rankup.objects.CoinsRanking;
import br.com.redewalker.rankup.objects.RankPlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class Rankup extends JavaPlugin {

    @Getter public static Rankup rankup;
    @Getter private RankPlayerManager rankPlayerManager;
    @Getter private CoinsRankingManager coinsRankingManager;
    @Getter private RankManager rankManager;

    @Override
    public void onEnable() {
        rankup = this;
        this.rankManager = new RankManager(this);
        this.rankManager.load();
        this.rankPlayerManager = new RankPlayerManager(this);
        this.rankPlayerManager.load();
        this.coinsRankingManager = new CoinsRankingManager();
        this.rankManager = new RankManager(this);
        API.getInstance().getCommons().registerManager(this.rankManager);
        API.getInstance().getCommons().registerManager(this.rankPlayerManager);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, this::updateCoinsRanking, 20L, 600 * 20L);


        new CoinsCommand(this);
        Bukkit.getPluginManager().registerEvents(new JoinEvents(), this);
    }

    @Override
    public void onDisable() {
        API.getInstance().getCommons().unregisterManager(this.rankPlayerManager);
        API.getInstance().getCommons().unregisterManager(this.rankManager);
    }



    public void updateCoinsRanking(){
        coinsRankingManager.getRanking().clear();

        final List<RankPlayer> rankPlayers = rankPlayerManager.getRankupPlayers()
                .stream()
                .sorted(Comparator.comparingDouble(RankPlayer::getCoins).reversed())
                .collect(Collectors.toList());

        for (int position = 0; position < rankPlayers.size(); position++) {
            RankPlayer rankPlayer = rankPlayers.get(position);

            final CoinsRanking ranking = CoinsRanking.builder()
                    .name(rankPlayer.getName())
                    .position((position + 1))
                    .coins(rankPlayer.getCoins())
                    .build();

            rankPlayer.setRanking(ranking);

            coinsRankingManager.getRanking().add(ranking);
        }
    }
}
