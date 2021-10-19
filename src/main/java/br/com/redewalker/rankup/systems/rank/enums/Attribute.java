package br.com.redewalker.rankup.systems.rank.enums;

import lombok.Getter;

public enum Attribute {

    STRENGTH("Ataque", "Aumento do poder de ataque.", 10),
    DEFENSE("Defesa", "Aumento de sua defesa.", 10),
    INTELIGENCY("Inteligência", "Melhore seus encantamentos.", 5),
    DEXTERITY("Destreza", "Melhore sua agilidade.", 5),
    COMMERCE("Comércio", "Ganhe mais coins em farms e minas.", 5);



    @Getter private final String name;
    @Getter private final String description;
    @Getter private final int maxLevel;

    Attribute(String name, String description, int maxLevel){
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
    }
}
