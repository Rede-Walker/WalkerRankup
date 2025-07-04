package br.com.redewalker.rankup.systems.rankplayer;

import br.com.redewalker.api.database.models.annotations.Key;
import br.com.redewalker.api.database.models.annotations.Storable;
import br.com.redewalker.rankup.Rankup;
import br.com.redewalker.rankup.systems.rank.Rank;
import br.com.redewalker.rankup.systems.rankplayer.coins.CoinsRanking;
import br.com.redewalker.rankup.systems.rank.enums.Attribute;
import br.com.redewalker.rankup.systems.rankplayer.enums.Preference;
import br.com.redewalker.rankup.systems.rankplayer.exceptions.RankupException;
import br.com.redewalker.rankup.systems.virtualchests.VirtualChest;
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
    private int rank;
    private HashMap<Preference, Boolean> preferences;
    private HashMap<Attribute, Integer> attributes;
    @Setter private transient CoinsRanking ranking;

    public RankPlayer() {}

    public void addVirtualChest(VirtualChest chest){
        this.virtualChests.add(chest); save();
    }

    public void addVirtualChest(){
        VirtualChest virtualChest = new VirtualChest(name, getNextVirtualChestId());
        virtualChests.add(virtualChest); save();
    }

    private int getNextVirtualChestId(){
        return virtualChests.size() + 1;
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

    public Rank getRank() {
        return Rankup.getRankup().getRankManager().getRank(this.rank);
    }

    public void rankup() throws RankupException {
        if(Rankup.getRankup().getRankManager().isLastRank(getRank())) throw new RankupException("Você já chegou ao último rank.");
        if(Rankup.getRankup().getRankManager().getNextRank(getRank()).getCost() > this.rankpoints) throw new RankupException("Você não possui pontos de rank necessários para upar.");

        Rank nextRank = Rankup.getRankup().getRankManager().getNextRank(getRank());
        this.rank = nextRank.getPosition();
        save();

    }

    public void save(){
        Rankup.getRankup().getRankPlayerManager().getDao().saveEntity(this);
    }

}
