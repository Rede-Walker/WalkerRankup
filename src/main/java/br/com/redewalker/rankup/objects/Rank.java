package br.com.redewalker.rankup.objects;

import br.com.redewalker.api.database.models.annotations.Key;
import br.com.redewalker.api.database.models.annotations.Storable;
import br.com.redewalker.rankup.Rankup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Storable("ranks")
@AllArgsConstructor
@Builder
@Getter
public class Rank {

    @Key
    private int position;
    private String name;
    private String tag;
    private double cost;
    private boolean last;
    private boolean first;

    public Rank(){ }

    public void setName(String x){
        this.name = x; save();
    }

    public void setTag(String x){
        this.tag = x; save();
    }

    public void setCost(double x){
        this.cost = x; save();
    }

    public void setLast(boolean x){
        this.last = x; save();
    }

    public void setFirst(boolean x){
        this.first = x; save();
    }

    public void save() {
        Rankup.getRankup().getRankManager().getDao().saveEntity(this);
    }
}
