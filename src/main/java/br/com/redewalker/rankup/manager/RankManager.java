package br.com.redewalker.rankup.manager;

import br.com.redewalker.common.Manager;
import br.com.redewalker.common.database.daos.MongoDBDao;
import br.com.redewalker.common.database.exceptions.ValueNotFoundException;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.dao.RankDAO;
import br.com.redewalker.rankup.objects.Rank;
import br.com.redewalker.rankup.objects.enums.Ranks;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.HashMap;

public class RankManager extends Manager<Rank, Rankup> {

    @Getter private final RankDAO rankDAO;
    @Getter private final HashMap<Integer, Rank> ranks;

    public RankManager(Rankup plugin) {
        super(plugin);
        this.rankDAO = new RankDAO();
        this.ranks = new HashMap<>();
        checkData();
    }

    public void checkData(){
        if(ranks.isEmpty()){
            for(Ranks ranks : Ranks.values()){
                handleRankCreation(ranks.getRankPosition(), ranks.getName(), ranks.getTag(), ranks.isFirst(), ranks.isLast());
            }
        }
    }

    public boolean isLastRank(Rank rank){
        int nextRank = rank.getPosition() + 1;
        return !ranks.containsKey(nextRank);
    }


    public void handleRankCreation(int rankPosition, String name, String tag, boolean first, boolean last){
        Rank rank = Rank.builder().position(rankPosition).name(name).tag(tag).first(first).last(last).build();
        if(!ranks.containsKey(rankPosition)){
            ranks.put(rankPosition, rank);
        }
    }


    public Rank getRank(int rankPosition){
        Rank rank;
        try{
          rank = this.rankDAO.find(this.rankDAO.getData().getKey(Rank.builder().position(rankPosition).build()));
        }catch (ValueNotFoundException e){
            rank = Rank.builder()
                    .position(rankPosition)
                    .name("nome")
                    .tag("tag")
                    .first(false)
                    .last(false)
                    .build();
            this.rankDAO.createObject(rank);
        }

        return rank;
    }

    @Override
    public void load() {
        this.rankDAO.findAll().forEach(r->
            this.ranks.put(r.getPosition(), r) );
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
    public Rank getObjectUpdated(String s) {
        return null;
    }

    @Override
    public Rank getObjectUpdated(Object o) {
        return null;
    }

    @Override
    public MongoDBDao<Rank> getDao() {
        return rankDAO;
    }
}
