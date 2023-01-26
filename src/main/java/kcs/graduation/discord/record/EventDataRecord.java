package kcs.graduation.discord.record;

import lombok.Data;

import java.util.Date;

@Data
public class EventDataRecord {

    private long serverId = -1; //
    private String eventName; //
    private String channelName; //
    private Date nextStartTime; //
    private long delayTime = -1; //
    private String delayUnit; //
    private long endTime = -1; //
    private String endUnit; //
    private int id;
    // ↑NOT NULL  ↓NULL
    private String eventDescription; //
    private long roleId = -1;
    private long defaultSize = -1; //
    private String mentionMessageLink = "";

    // 非常用
    private long mentionChannelId;
    private long eventCategoryId;
    private boolean execute;
    private long textChannelId;
    private long voiceChannelId;
}
