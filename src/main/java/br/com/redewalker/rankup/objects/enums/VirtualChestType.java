package br.com.redewalker.rankup.objects.enums;

import lombok.Getter;

public enum VirtualChestType {

    SIMPLE("Simples", 0, 27),
    COMPLETE("Completo", 1000, 54);


    @Getter private long cost;
    @Getter private String name;
    @Getter private int slots;

    VirtualChestType(String name, long cost, int slots){
        this.cost = cost;
        this.name = name;
        this.slots = slots;
    }

    public static VirtualChestType getBySlots(int slots){
        if(slots == 27) return SIMPLE;
        return COMPLETE;
    }
}
