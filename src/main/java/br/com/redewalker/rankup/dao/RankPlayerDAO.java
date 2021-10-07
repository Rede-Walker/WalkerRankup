package br.com.redewalker.rankup.dao;

import br.com.redewalker.api.API;
import br.com.redewalker.common.database.daos.MongoDBDao;
import br.com.redewalker.rankup.objects.RankPlayer;

public class RankPlayerDAO extends MongoDBDao<RankPlayer> {

    public RankPlayerDAO() {
        super(RankPlayer.class, API.getInstance().getMongoDB(), "testes", API.getInstance().getServerName());
    }

    @Override
    public void onUpdatedInDatabase(Object o, String s, String s1, String s2) {

    }

    @Override
    public void onCreatedObject(Object o, String s, String s1) {

    }

}
