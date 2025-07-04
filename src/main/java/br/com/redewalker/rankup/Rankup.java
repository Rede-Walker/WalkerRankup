package br.com.redewalker.rankup;

import br.com.redewalker.api.API;
import br.com.redewalker.rankup.commands.ChestCommand;
import br.com.redewalker.rankup.commands.CoinsCommand;
import br.com.redewalker.rankup.commands.RankupCommand;
import br.com.redewalker.rankup.listeners.JoinEvents;
import br.com.redewalker.rankup.listeners.RankupListener;
import br.com.redewalker.rankup.listeners.VirtualChestListeners;
import br.com.redewalker.rankup.systems.rankplayer.coins.CoinsRankingManager;
import br.com.redewalker.rankup.systems.rank.RankManager;
import br.com.redewalker.rankup.systems.rankplayer.RankPlayerManager;
import br.com.redewalker.rankup.systems.virtualchests.VirtualChestManager;
import br.com.redewalker.rankup.systems.rankplayer.coins.CoinsRanking;
import br.com.redewalker.rankup.systems.rankplayer.RankPlayer;
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
    @Getter private VirtualChestManager virtualChestManager;

    @Override
    public void onEnable() {
        rankup = this;
        this.rankManager = new RankManager(this);
        this.rankPlayerManager = new RankPlayerManager(this);
        this.rankPlayerManager.load();
        this.coinsRankingManager = new CoinsRankingManager();
        this.rankManager = new RankManager(this);
        this.virtualChestManager = new VirtualChestManager(this);
        API.getInstance().getCommons().registerManager(this.rankManager);
        API.getInstance().getCommons().registerManager(this.rankPlayerManager);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, this::updateCoinsRanking, 20L, 600 * 20L);


        new ChestCommand();
        new CoinsCommand(this);
        new RankupCommand();
        Bukkit.getPluginManager().registerEvents(new RankupListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinEvents(), this);
        Bukkit.getPluginManager().registerEvents(new VirtualChestListeners(), this);
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
