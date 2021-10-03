package br.com.redewalker.rankup.dao;

import br.com.redewalker.api.API;
import br.com.redewalker.common.database.daos.MongoDBDao;
import br.com.redewalker.rankup.objects.RankupPlayer;
import com.mongodb.BasicDBObject;

public class RankupPlayerDAO extends MongoDBDao<RankupPlayer> {

    public RankupPlayerDAO() {
        super(RankupPlayer.class, API.getInstance().getMongoDB(), "testes", API.getInstance().getServerName());
    }

    @Override
    public void onUpdatedInDatabase(Object o, String s, String s1, String s2) {

    }

    @Override
    public void onCreatedObject(Object o, String s, String s1) {

    }

}
