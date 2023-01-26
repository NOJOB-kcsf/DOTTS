package kcs.graduation.discord.record;

import lombok.Data;

@Data
public class TempChannelRecord {
    private long server_id;
    private long category_id;
    private long voice_channel_id;
    private long text_channel_id;
    private long info_message_id;
    private long owner_user_id;
    private boolean hide_by;
    private boolean lock_by;
    private boolean newCategoryBy;
}
