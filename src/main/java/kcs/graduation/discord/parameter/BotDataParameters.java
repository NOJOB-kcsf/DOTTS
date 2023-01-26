package kcs.graduation.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BotDataParameters {

    B_NAME("BOT_NAME"),
    B_TOKEN("BOT_TOKEN"),
    D_TOKEN("DEEPL_TOKEN");

    private final String param;
}
