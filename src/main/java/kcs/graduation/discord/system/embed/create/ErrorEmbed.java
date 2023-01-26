package kcs.graduation.discord.system.embed.create;

import kcs.graduation.discord.system.embed.ErrorNumber;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;

public class ErrorEmbed {
    public EmbedBuilder create(int errorNum, String contentText) {
        ArrayList<String> content = getContent(errorNum);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle(content.get(0));
        embedBuilder.setDescription(contentText);
        return embedBuilder;
    }

    public EmbedBuilder create(String contentText) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle("エラー");
        embedBuilder.setDescription(contentText);
        return embedBuilder;
    }

    public EmbedBuilder create(int errorNum) {
        ArrayList<String> content = getContent(errorNum);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle(content.get(0));
        embedBuilder.setDescription(content.get(1));
        return embedBuilder;
    }

    private ArrayList<String> getContent(int errorNum) {
        ArrayList<String> content = new ArrayList<>();
        content.add("Error");
        switch (errorNum) {
            case ErrorNumber.NOT_ADMIN -> content.add("貴方はサーバー管理者ではありません。");
            case ErrorNumber.NOT_MANAGE -> content.add("貴方は通話管理者ではありません。");
            case ErrorNumber.NOT_TEMP_CHANNEL -> content.add("一時通話チャンネル内でのみ使用可能です。");
            case ErrorNumber.UNKNOWN -> content.add("未知のエラーです。BOT管理者に問い合わせてください。");
            default -> content.add("BOT管理者に問い合わせてください。");
        }
        return content;
    }
}
