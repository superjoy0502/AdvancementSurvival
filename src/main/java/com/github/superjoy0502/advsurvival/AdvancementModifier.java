package com.github.superjoy0502.advsurvival;

import org.bukkit.advancement.Advancement;

public class AdvancementModifier {

    Advancement advancement;
    String type;
    AdvancementModifierCondition condition;
    AdvancementModifierReward reward;

    public AdvancementModifier(Advancement advancement, String type, AdvancementModifierCondition condition, AdvancementModifierReward reward) {
        this.advancement = advancement;
        this.type = type;
        this.condition = condition;
        this.reward = reward;
    }

}
