package br.com.redewalker.rankup.objects;

import br.com.redewalker.common.database.models.annotations.Key;
import br.com.redewalker.common.database.models.annotations.Storable;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.objects.enums.Attribute;
import br.com.redewalker.rankup.objects.enums.Preference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Storable("rankup_player")
@AllArgsConstructor
@Builder
@Getter
public class RankPlayer {

    @Key private String name;
    @Builder.Default private double coins = 0;
    @Builder.Default private double rankpoints = 0;
    private HashMap<Preference, Boolean> preferences;
    private HashMap<Attribute, Integer> attributes;
    @Setter private transient CoinsRanking ranking;

    public RankPlayer() {}

    public void setRankpoints(double x){
        if(x < 0) x = 0;
        this.rankpoints = x; save();
    }

    public void addRankpoints(double x){
        this.rankpoints += x; save();
    }

    public void removeRankpoints(double x){
        if((this.rankpoints - x) < 0) this.rankpoints = 0;
        else this.rankpoints -= x;
        save();
    }

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

    public void setPreference(Preference preference, boolean value){
        preferences.put(preference, value);
        save();
    }

    public void setAttribute(Attribute attribute, int level){
        attributes.put(attribute, level);
        save();
    }

    public void save(){
        Rankup.getRankup().getRankPlayerManager().getDao().saveEntity(this);
    }

}
