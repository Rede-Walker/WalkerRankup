package br.com.redewalker.rankup.systems.rank;

import br.com.redewalker.api.basics.Manager;
import br.com.redewalker.api.database.daos.MongoDBDao;
import br.com.redewalker.api.database.exceptions.ValueNotFoundException;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.storage.RankDAO;
import br.com.redewalker.rankup.systems.rank.enums.Ranks;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RankManager extends Manager<Rank, Rankup> {

    @Getter private final RankDAO rankDAO;
    @Getter private final List<Rank> ranks;

    public RankManager(Rankup plugin) {
        super(plugin);
        this.rankDAO = new RankDAO();
        this.ranks = new ArrayList<>();
        load();
        checkData();
    }

    public void debug(){
        ranks.stream().forEach(r-> System.out.println(r.getPosition() + " - " + r.getName()));
    }

    public void checkData(){
        for(Ranks rank : Ranks.values()){
            handleRankCreation(rank.getRankPosition(), rank.getName(), rank.getTag(), rank.isFirst(), rank.isLast(), false);
        }

    }

    public boolean isLastRank(Rank rank){
        int nextRank = rank.getPosition() + 1;
        if(nextRank == ranks.size()) return true;
        Rank rankNext = ranks.get(nextRank);
        return rankNext == null;
    }

    public Rank getNextRank(Rank rank){
        int nextRank = rank.getPosition() + 1;
        return ranks.get(nextRank);
    }

    public Rank getDefaultRank(){
        return ranks.stream().filter(rank -> rank.isFirst()).findFirst().orElse(null);
    }


    public void handleRankCreation(int rankPosition, String name, String tag, boolean first, boolean last, boolean debug) {
        if (ranks.isEmpty() || ranks.get(rankPosition) == null) {
            Rank rank;
            try {
                rank = this.rankDAO.find(this.rankDAO.getData().getKey(Rank.builder().position(rankPosition).build()));
            } catch (ValueNotFoundException e) {
                rank = Rank.builder()
                        .position(rankPosition)
                        .name(name)
                        .tag(tag)
                        .first(first)
                        .last(last)
                        .build();
                this.rankDAO.createObject(rank);
            }
            this.ranks.add(rankPosition, rank);
        } else {
            if(debug) System.out.println("Já existe um rank com essa posição! #" + rankPosition);
        }
    }

    public Rank getRank(int position){
        return this.ranks.get(position);
    }

    @Override
    public void load() {
        this.rankDAO.findAll().stream().forEach(rank -> {
            if(ranks.isEmpty() || !ranks.contains(rank)) ranks.add(rank.getPosition(), rank);
        });
    }

    @SneakyThrows
    @Override
    public void save() {
        this.rankDAO.runUpdatesSync();
    }

    @SneakyThrows
    @Override
    public void run() {
        this.rankDAO.runUpdatesSync();
    }

    @Override
    public String getManagerName() {
        return this.rankDAO.getData().getName();
    }

    @Override
    public CompletableFuture<Rank> getObjectUpdated(String s) {
        return null;
    }

    @Override
    public CompletableFuture<Rank> getObjectUpdated(Object o) {
        return null;
    }

    @Override
    public MongoDBDao<Rank> getDao() {
        return rankDAO;
    }
}
