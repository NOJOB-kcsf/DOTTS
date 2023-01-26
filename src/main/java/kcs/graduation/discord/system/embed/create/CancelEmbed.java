package kcs.graduation.discord.system.embed.create;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class CancelEmbed {

    public EmbedBuilder createEmbed(ServerVoiceChannel serverVoiceChannel, int result_code) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Processing.getTitle(result_code));
        embedBuilder.setDescription(Processing.getDescription(result_code, serverVoiceChannel));
        return embedBuilder;
    }

    public EmbedBuilder createEmbed(int result_code) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Processing.getTitle(result_code));
        embedBuilder.setDescription(Processing.getDescription(result_code));
        return embedBuilder;
    }
}
