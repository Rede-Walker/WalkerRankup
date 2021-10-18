package br.com.redewalker.rankup.objects;

import br.com.redewalker.api.database.models.annotations.Key;
import br.com.redewalker.api.database.models.annotations.Storable;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.objects.enums.Attribute;
import br.com.redewalker.rankup.objects.enums.Preference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Storable("rankup_player")
@AllArgsConstructor
@Builder
@Getter
public class RankPlayer {

    @Key
    private String name;
    @Builder.Default private double coins = 0;
    @Builder.Default private double rankpoints = 0;
    @Builder.Default private List<VirtualChest> virtualChests = new ArrayList<>();
    private Rank rank;
    private HashMap<Preference, Boolean> preferences;
    private HashMap<Attribute, Integer> attributes;
    @Setter private transient CoinsRanking ranking;

    public RankPlayer() {}

    public void addVirtualChest(VirtualChest chest){
        this.virtualChests.add(chest); save();
    }

    public void removeVirtualChest(VirtualChest chest){
        this.virtualChests.remove(chest); save();
    }

    public void setVirtualChests(List<VirtualChest> virtualChests){
        this.virtualChests = virtualChests; save();
    }

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

    public boolean rankup(){
        if(Rankup.getRankup().getRankManager().isLastRank(this.rank)) return false;
        if(Rankup.getRankup().getRankManager().getNextRank(this.rank).getCost() > this.rankpoints) return false;
        Rank nextRank = Rankup.getRankup().getRankManager().getNextRank(this.rank);
        this.rank = nextRank;
        save();

        return true;
    }

    public void save(){
        Rankup.getRankup().getRankPlayerManager().getDao().saveEntity(this);
    }

}
