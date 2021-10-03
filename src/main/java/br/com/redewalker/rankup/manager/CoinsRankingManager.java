package br.com.redewalker.rankup.manager;

import br.com.redewalker.rankup.objects.CoinsRanking;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CoinsRankingManager {

    @Getter private final List<CoinsRanking> ranking;

    public CoinsRankingManager(){
        this.ranking = new ArrayList<>();
    }

    public CoinsRanking getPosition(int position){
        return ranking.stream().filter(rank -> rank.getPosition() == position).findAny().orElse(null);
    }

    public List<CoinsRanking> getTop(int size) {
        final List<CoinsRanking> rankings = new ArrayList<>();
        for (int position = 0; position < size; position++) {
            final CoinsRanking ranking = getPosition(position);

            if (ranking == null)
                continue;

            rankings.add(ranking);
        }
        return rankings;
    }
}
