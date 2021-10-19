package br.com.redewalker.rankup.systems.rank.enums;

import lombok.Getter;

public enum Ranks {

    PRIMEIRO_RANK(0, "Primeiro Rank", "[I]", 0, true, false),
    SEGUNDO_RANK(1, "Segundo Rank", "[II]", 1),
    TERCEIRO_RANK(2, "Terceiro Rank", "[III]", 2),
    QUARTO_RANK(3, "Quarto Rank", "[IV]", 3),
    QUINTO_RANK(4, "Quinto Rank", "[V]", 4, true);

    @Getter private final int rankPosition;
    @Getter private final String name;
    @Getter private final String tag;
    @Getter private final double cost;
    @Getter private final boolean last;
    @Getter private final boolean first;

    Ranks(int position, String name, String tag, double cost){
        this.rankPosition = position;
        this.name = name;
        this.tag = tag;
        this.cost = cost;
        this.last = false;
        this.first = false;
    }

    Ranks(int position, String name, String tag, double cost, boolean first, boolean last){
        this.rankPosition = position;
        this.name = name;
        this.tag = tag;
        this.cost = cost;
        this.last = last;
        this.first = first;
    }

    Ranks(int position, String name, String tag, double cost, boolean last){
        this.rankPosition = position;
        this.name = name;
        this.tag = tag;
        this.cost = cost;
        this.last = last;
        this.first = false;
    }
}
