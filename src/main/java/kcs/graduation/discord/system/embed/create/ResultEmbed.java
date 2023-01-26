package kcs.graduation.discord.system.embed.create;

import kcs.graduation.discord.system.embed.Constant;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;

public class ResultEmbed {
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
        embedBuilder.setTitle("完了");
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

    private ArrayList<String> getContent(int idx) {
        ArrayList<String> content = new ArrayList<>();
        content.add("結果");
        switch (idx) {
            case Constant.COMPLETION -> content.add("完了。");
            case Constant.UNFINISHED -> content.add("実行不可。");
            case Constant.UNKNOWN -> content.add("未知のエラーです。BOT管理者に問い合わせてください。");
            default -> content.add("BOT管理者に問い合わせてください。");
        }
        return content;
    }
}
