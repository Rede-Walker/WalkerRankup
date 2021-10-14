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
        for(Ranks rank : Ranks.values()){
            handleRankCreation(rank.getRankPosition(), rank.getName(), rank.getTag(), rank.isFirst(), rank.isLast());
        }

    }

    public boolean isLastRank(Rank rank){
        int nextRank = rank.getPosition() + 1;
        return !ranks.containsKey(nextRank);
    }

    public Rank getNextRank(Rank rank){
        int nextRank = rank.getPosition() + 1;
        return ranks.get(nextRank);
    }

    public Rank getDefaultRank(){
        return ranks.values().stream().filter(rank -> rank.isFirst()).findFirst().orElse(null);
    }


    public void handleRankCreation(int rankPosition, String name, String tag, boolean first, boolean last){
        if(!ranks.containsKey(rankPosition)){
            Rank rank;
            try {
                rank = getRank(rankPosition);
            }catch(ValueNotFoundException e){
                rank = Rank.builder()
                        .position(rankPosition)
                        .name(name)
                        .tag(tag)
                        .first(first)
                        .last(last)
                        .build();
                this.rankDAO.createObject(rank);
            }
            this.ranks.put(rankPosition, rank);
        }else{
            System.out.println("Já existe um rank com essa posição! #" + rankPosition);
        }
    }


    public Rank getRank(int rankPosition) throws ValueNotFoundException{
        return this.rankDAO.find(this.rankDAO.getData().getKey(Rank.builder().position(rankPosition).build()));
    }

    @Override
    public void load() {
        this.rankDAO.findAll().forEach(r->
            this.ranks.put(r.getPosition(), r) );
        System.out.println(ranks);
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
