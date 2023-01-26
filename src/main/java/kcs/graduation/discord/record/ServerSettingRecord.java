package kcs.graduation.discord.record;

import lombok.Data;

@Data
public class ServerSettingRecord {
    private long server_id;
    private long mention_channel_id;
    private long first_channel_id;
    private long static_category_id;
    private long event_category_id;
    private long created_under_category_id;
    private long temp_voice_category_id;
    private long temp_text_category_id;
    private boolean temp_created_by;
    private boolean temp_text_created_by;
    private boolean new_under_category_by;
    private int default_size;
    private String default_name;
    private String stereo_typed;
}
