package br.com.redewalker.rankup.systems.rankplayer.coins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CoinsRanking {

    private double coins;
    private String name;
    private int position;
}
