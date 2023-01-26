package kcs.graduation.discord.system.embed.create;

import kcs.graduation.discord.record.EventDataRecord;
import kcs.graduation.discord.system.core.SystemMaster;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.util.Date;

public class CompleteEmbed extends SystemMaster {

    public EmbedBuilder createEmbed(ServerVoiceChannel serverVoiceChannel, int result_code) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Processing.getTitle(result_code));
        embedBuilder.setDescription(Processing.getDescription(result_code, serverVoiceChannel));
        return embedBuilder;
    }

    public EmbedBuilder createEmbed(ChannelCategory channelCategory, int result_code) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Processing.getTitle(result_code));
        embedBuilder.setDescription(Processing.getDescription(result_code, channelCategory));
        return embedBuilder;
    }

    public EmbedBuilder createEmbed(EventDataRecord eventDataRecord, int result_code) {
        kcs.graduation.discord.system.core.Processing processing = new kcs.graduation.discord.system.core.Processing();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Processing.getTitle(result_code));
        embedBuilder.addField("イベントネーム", eventDataRecord.getEventName(), true);
        if (eventDataRecord.getEventDescription() != null && !eventDataRecord.getEventDescription().isEmpty())
            embedBuilder.addInlineField("イベント説明", eventDataRecord.getEventDescription());
        embedBuilder.addField("チャンネルネーム", eventDataRecord.getChannelName(), true);
        embedBuilder.addInlineField("初期通話人数制限", eventDataRecord.getDefaultSize() == 0 ? "無制限" : eventDataRecord.getDefaultSize() + "人");
        embedBuilder.addField("最初の時間", "<t:" + eventDataRecord.getNextStartTime().toInstant().getEpochSecond() + ":F>", true);
        embedBuilder.addInlineField("周期", eventDataRecord.getDelayTime() + processing.timeUnitToTimeString(eventDataRecord.getDelayUnit()));
        embedBuilder.addField("終了時間", eventDataRecord.getEndTime() + processing.timeUnitToTimeString(eventDataRecord.getEndUnit()), true);
        if (eventDataRecord.getRoleId() != -1)
            embedBuilder.addField("メンション先ロール", "<@&" + eventDataRecord.getRoleId() + ">");
        return embedBuilder;
    }

    public EmbedBuilder createEmbedOneHourAgo(EventDataRecord record) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("募集予告！");
        if (!record.getEventDescription().isEmpty()) {
            embedBuilder.setDescription(record.getEventDescription());
        }
        embedBuilder.addField("開始時刻", "<t:" + record.getNextStartTime().toInstant().getEpochSecond() + ":R>", true);
        if (record.getRoleId() != -1) {
            embedBuilder.addInlineField("募集ロール", "<@&" + record.getRoleId() + ">");
        }
        return embedBuilder;
    }

    public EmbedBuilder createEmbedExecute(EventDataRecord record) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("イベント開始" + (!record.getEventName().isEmpty() ? "-" + record.getEventName() : ""));
        StringBuilder stringBuilder = new StringBuilder();
        if (!record.getEventDescription().isEmpty()) {
            stringBuilder.append(record.getEventDescription()).append("-");
        }
        if (record.getDefaultSize() != -1L) {
            stringBuilder.append("最大 人数").append(record.getDefaultSize()).append("人");
        }
        embedBuilder.setDescription(stringBuilder.toString());
        embedBuilder.addField("開始時刻", "<t:" + record.getNextStartTime().toInstant().getEpochSecond() + ":R>から開催中", true);
        if (record.getRoleId() != -1) {
            embedBuilder.addInlineField("募集ロール", "<@&" + record.getRoleId() + ">");
        }
        embedBuilder.addInlineField("ボイスチャンネル", "<#" + record.getVoiceChannelId() + ">");

        return embedBuilder;
    }

    public EmbedBuilder createEmbedBeforeEnd(Date time) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("イベント終了予告");
        embedBuilder.addField("開始時刻", "<t:" + time.toInstant().getEpochSecond() + ":R>後終了", true);
        return embedBuilder;
    }

    public EmbedBuilder createEmbed(ServerTextChannel mentionChannel, int result_code) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Processing.getTitle(result_code));
        embedBuilder.setDescription(Processing.getDescription(result_code, mentionChannel));
        return embedBuilder;
    }

    public EmbedBuilder createEmbed(boolean by, int result_code) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Processing.getTitle(result_code));
        embedBuilder.setDescription(Processing.getDescription(result_code, by));
        return embedBuilder;
    }

    public EmbedBuilder createEmbed(String str, int result_code) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Processing.getTitle(result_code));
        embedBuilder.setDescription(Processing.getDescription(result_code, str));
        return embedBuilder;
    }

    public EmbedBuilder createEmbed(int size, int result_code) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Processing.getTitle(result_code));
        embedBuilder.setDescription(Processing.getDescription(result_code, size));
        return embedBuilder;
    }

    public EmbedBuilder createEmbed(long id, int tempChangeOwner) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Processing.getTitle(tempChangeOwner));
        embedBuilder.setDescription(Processing.getDescription(tempChangeOwner, id));
        return embedBuilder;
    }
}
