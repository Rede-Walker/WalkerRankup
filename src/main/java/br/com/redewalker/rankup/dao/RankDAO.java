package br.com.redewalker.rankup.dao;

import br.com.redewalker.api.API;
import br.com.redewalker.common.database.daos.MongoDBDao;
import br.com.redewalker.rankup.objects.Rank;

public class RankDAO extends MongoDBDao<Rank> {

    public RankDAO() {
        super(Rank.class, API.getInstance().getMongoDB(), "testes", API.getInstance().getServerName());
    }

    @Override
    public void onUpdatedInDatabase(Object o, String s, String s1, String s2) {

    }

    @Override
    public void onCreatedObject(Object o, String s, String s1) {

    }
}
