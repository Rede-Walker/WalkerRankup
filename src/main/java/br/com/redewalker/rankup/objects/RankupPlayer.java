package br.com.redewalker.rankup.objects;

import br.com.redewalker.common.database.models.annotations.Key;
import br.com.redewalker.common.database.models.annotations.Storable;
import br.com.redewalker.rankup.Rankup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Storable("rankup_player")
@AllArgsConstructor
@Builder
@Getter
public class RankupPlayer {

    @Key private String name;
    @Builder.Default private double coins = 0;
    @Setter private transient CoinsRanking ranking;

    public RankupPlayer() {}

    public void setCoins(double x){
        if(x < 0) x = 0;
        this.coins = x; save();
    }

    public void addCoins(double x){
        this.coins += x; save();
    }

    public void removeCoins(double x){
        if((this.coins - x) < 0) this.coins = 0;
        else this.coins -= x;
        save();
    }

    public void save(){
        Rankup.getRankup().getRankupPlayerManager().getDao().saveEntity(this);
    }

}
