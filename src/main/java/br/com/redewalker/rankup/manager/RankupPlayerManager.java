package br.com.redewalker.rankup.manager;

import br.com.redewalker.common.Manager;
import br.com.redewalker.common.database.daos.MongoDBDao;
import br.com.redewalker.common.database.exceptions.ValueNotFoundException;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.dao.RankupPlayerDAO;
import br.com.redewalker.rankup.objects.RankupPlayer;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RankupPlayerManager extends Manager<RankupPlayer, Rankup> {

    private final RankupPlayerDAO dao;
    @Getter private final HashMap<String, RankupPlayer> rankupPlayerHashMap;

    public RankupPlayerManager(Rankup plugin) {
        super(plugin);
        this.dao = new RankupPlayerDAO();
        this.rankupPlayerHashMap = new HashMap<>();
    }

    public List<RankupPlayer> getRankupPlayers(){
        return new ArrayList<>(rankupPlayerHashMap.values());
    }

    public void handleRankupPlayerCreation(String nickname){
        if(!rankupPlayerHashMap.containsKey(nickname.toLowerCase())){
            RankupPlayer rankupPlayer = createRankupPlayer(nickname);
            this.rankupPlayerHashMap.put(nickname.toLowerCase(), rankupPlayer);
        }
    }

    public RankupPlayer getRankupPlayer(String nickname){
        return rankupPlayerHashMap.get(nickname.toLowerCase());
    }

    private RankupPlayer createRankupPlayer(String nickname) {
        RankupPlayer rankupPlayer;
        try {
            rankupPlayer = this.dao.find(this.dao.getData().getKey(RankupPlayer.builder().name(nickname).build()));
        } catch (ValueNotFoundException e) {
            rankupPlayer = RankupPlayer.builder()
                    .name(nickname)
                    .build();
        }

        return rankupPlayer;
    }

    @Override
    public void load() {
        this.dao.findAll().forEach(r->{
            String nickName = r.getName().toLowerCase();
            this.rankupPlayerHashMap.put(nickName, r);

        });
    }

    @SneakyThrows
    @Override
    public void save() {
        this.dao.runUpdatesSync();
    }

    @SneakyThrows
    @Override
    public void run() {
        this.dao.runUpdatesSync();
    }

    @Override
    public String getManagerName() {
        return this.dao.getData().getName();
    }

    @Override
    public RankupPlayer getObjectUpdated(String s) {
        return null;
    }

    @Override
    public MongoDBDao<RankupPlayer> getDao() {
        return this.dao;
    }
}
