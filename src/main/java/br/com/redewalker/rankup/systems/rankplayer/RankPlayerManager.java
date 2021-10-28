package br.com.redewalker.rankup.systems.rankplayer;

import br.com.redewalker.api.basics.Manager;
import br.com.redewalker.api.database.daos.MongoDBDao;
import br.com.redewalker.api.database.exceptions.ValueNotFoundException;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.storage.RankPlayerDAO;
import br.com.redewalker.rankup.systems.rank.enums.Attribute;
import br.com.redewalker.rankup.systems.rankplayer.enums.Preference;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RankPlayerManager extends Manager<RankPlayer, Rankup> {

    private final RankPlayerDAO dao;
    @Getter private final HashMap<String, RankPlayer> rankupPlayerHashMap;

    public RankPlayerManager(Rankup plugin) {
        super(plugin);
        this.dao = new RankPlayerDAO();
        this.rankupPlayerHashMap = new HashMap<>();
    }

    public List<RankPlayer> getRankupPlayers(){
        return new ArrayList<>(rankupPlayerHashMap.values());
    }

    public void handleRankupPlayerCreation(String nickname){
        if(!rankupPlayerHashMap.containsKey(nickname.toLowerCase())){
            RankPlayer rankPlayer = createRankupPlayer(nickname);
            this.rankupPlayerHashMap.put(nickname.toLowerCase(), rankPlayer);
        }
    }

    public RankPlayer getRankupPlayer(String nickname){
        return rankupPlayerHashMap.get(nickname.toLowerCase());
    }

    private RankPlayer createRankupPlayer(String nickname) {
        RankPlayer rankPlayer;
        try {
            rankPlayer = this.dao.find(this.dao.getData().getKey(RankPlayer.builder().name(nickname).build()));
        } catch (ValueNotFoundException e) {
            HashMap<Preference, Boolean> hashmap = new HashMap<>();
            hashmap.put(Preference.RECEIVE_COINS, true);
            rankPlayer = RankPlayer.builder()
                    .name(nickname)
                    .preferences(hashmap)
                    .rank(0)
                    .attributes(getNewAttributeMap())
                    .build();
            this.dao.createObject(rankPlayer);
        }

        return rankPlayer;
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
    public CompletableFuture<RankPlayer> getObjectUpdated(String s) {
        return null;
    }

    @Override
    public CompletableFuture<RankPlayer> getObjectUpdated(Object o) {
        return null;
    }

    @Override
    public MongoDBDao<RankPlayer> getDao() {
        return this.dao;
    }

    private HashMap<Attribute, Integer> getNewAttributeMap(){
        HashMap<Attribute, Integer> map = new HashMap<>();
        Arrays.stream(Attribute.values()).forEach(r-> map.put(r, 1));
        return map;
    }
}
