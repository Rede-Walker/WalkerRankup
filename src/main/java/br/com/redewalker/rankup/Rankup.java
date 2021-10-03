package br.com.redewalker.rankup;

import br.com.redewalker.api.API;
import br.com.redewalker.rankup.commands.CoinsCommand;
import br.com.redewalker.rankup.listeners.JoinEvents;
import br.com.redewalker.rankup.manager.CoinsRankingManager;
import br.com.redewalker.rankup.manager.RankupPlayerManager;
import br.com.redewalker.rankup.objects.CoinsRanking;
import br.com.redewalker.rankup.objects.RankupPlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public final class Rankup extends JavaPlugin {

    @Getter public static Rankup rankup;
    @Getter private RankupPlayerManager rankupPlayerManager;
    @Getter private CoinsRankingManager coinsRankingManager;

    @Override
    public void onEnable() {
        rankup = this;
        this.rankupPlayerManager = new RankupPlayerManager(this);
        this.rankupPlayerManager.load();
        this.coinsRankingManager = new CoinsRankingManager();
        API.getInstance().getCommons().registerManager(this.rankupPlayerManager);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, this::updateCoinsRanking, 20L, 600 * 20L);


        new CoinsCommand(this);
        Bukkit.getPluginManager().registerEvents(new JoinEvents(), this);
    }

    @Override
    public void onDisable() {
        API.getInstance().getCommons().unregisterManager(this.rankupPlayerManager);
    }



    public void updateCoinsRanking(){
        coinsRankingManager.getRanking().clear();

        final List<RankupPlayer> rankupPlayers = rankupPlayerManager.getRankupPlayers()
                .stream()
                .sorted(Comparator.comparingDouble(RankupPlayer::getCoins).reversed())
                .collect(Collectors.toList());

        for (int position = 0; position < rankupPlayers.size(); position++) {
            RankupPlayer rankupPlayer = rankupPlayers.get(position);

            final CoinsRanking ranking = CoinsRanking.builder()
                    .name(rankupPlayer.getName())
                    .position((position + 1))
                    .coins(rankupPlayer.getCoins())
                    .build();

            rankupPlayer.setRanking(ranking);

            coinsRankingManager.getRanking().add(ranking);
        }
    }
}
