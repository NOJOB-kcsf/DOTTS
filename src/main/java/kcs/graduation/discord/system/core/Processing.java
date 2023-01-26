package kcs.graduation.discord.system.core;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerTextChannelBuilder;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletionException;

public class Processing extends SystemMaster {
    DiscordApi api = getAPI();

    public Permissions getUserPermission() {
        return
                new PermissionsBuilder()
                        .setAllowed(
                                PermissionType.VIEW_CHANNEL,
                                PermissionType.SEND_MESSAGES,
                                PermissionType.ADD_REACTIONS,
                                PermissionType.ATTACH_FILE,
                                PermissionType.USE_VOICE_ACTIVITY,
                                PermissionType.STREAM,
                                PermissionType.SPEAK,
                                PermissionType.READ_MESSAGE_HISTORY,
                                PermissionType.CONNECT,
                                PermissionType.CHANGE_NICKNAME
                        )
                        .build();
    }

    public Permissions getChannelManagePermission() {
        return
                new PermissionsBuilder()
                        .setAllowed(
                                PermissionType.VIEW_CHANNEL,
                                PermissionType.SEND_MESSAGES,
                                PermissionType.ADD_REACTIONS,
                                PermissionType.ATTACH_FILE,
                                PermissionType.USE_VOICE_ACTIVITY,
                                PermissionType.STREAM,
                                PermissionType.SPEAK,
                                PermissionType.READ_MESSAGE_HISTORY,
                                PermissionType.CONNECT,
                                PermissionType.CHANGE_NICKNAME,
                                PermissionType.MANAGE_CHANNELS,
                                PermissionType.MANAGE_MESSAGES,
                                PermissionType.USE_APPLICATION_COMMANDS
                        )
                        .build();
    }

    public EmbedBuilder rePressInfoMessage(User user, boolean hideBy, boolean lockBy) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setTitle("通話設定")
                .addInlineField("通話管理者", user.getDiscriminatedName())
                .addInlineField("表示設定", hideBy ? "非表示中" : "表示中")
                .addInlineField("ロック設定", lockBy ? "ロック中" : "アンロック中")
                .setThumbnail(user.getAvatar());
        return embedBuilder;
    }

    public boolean checkServerTextChannelAllowedPermission(ServerTextChannel serverTextChannel, PermissionType type, boolean by) {
        User bot = api.getYourself();
        return serverTextChannel.hasPermission(bot, type) == by
                || serverTextChannel.hasPermission(bot, PermissionType.ADMINISTRATOR) == by
                || (serverTextChannel.getCategory().isPresent() && serverTextChannel.getCategory().get().hasPermission(bot, type)) == by
                || (serverTextChannel.getCategory().isPresent() && serverTextChannel.getCategory().get().hasPermission(bot, PermissionType.ADMINISTRATOR)) == by
                || serverTextChannel.getServer().hasPermissions(bot, type) == by
                || serverTextChannel.getServer().hasPermissions(bot, PermissionType.ADMINISTRATOR) == by;
    }

    public boolean checkServerVoiceChannelAllowedPermission(ServerVoiceChannel serverChannel, PermissionType type, boolean by) {
        User bot = api.getYourself();
        return serverChannel.hasPermission(bot, type) == by
                || serverChannel.hasPermission(bot, PermissionType.ADMINISTRATOR) == by
                || (serverChannel.getCategory().isPresent() && serverChannel.getCategory().get().hasPermission(bot, type)) == by
                || (serverChannel.getCategory().isPresent() && serverChannel.getCategory().get().hasPermission(bot, PermissionType.ADMINISTRATOR)) == by
                || serverChannel.getServer().hasPermissions(bot, type) == by
                || serverChannel.getServer().hasPermissions(bot, PermissionType.ADMINISTRATOR) == by;
    }

    public ServerTextChannelBuilder CloneServerTextChannel(ServerTextChannel serverTextChannel, ServerTextChannelBuilder serverTextChannelBuilder) {

        if (serverTextChannel.getCategory().isPresent())
            serverTextChannelBuilder.setCategory(serverTextChannel.getCategory().get());
        serverTextChannelBuilder.setTopic(serverTextChannel.getTopic());
        serverTextChannelBuilder.setName(serverTextChannel.getName());
        serverTextChannelBuilder.setSlowmodeDelayInSeconds(serverTextChannel.getSlowmodeDelayInSeconds());
        serverTextChannelBuilder.setRawPosition(serverTextChannel.getRawPosition());
        serverTextChannel.getOverwrittenRolePermissions().forEach((aLong, permissions) -> {
            try {
                serverTextChannelBuilder.addPermissionOverwrite(api.getRoleById(aLong).orElseThrow(), permissions);
            } catch (NoSuchElementException ignored) {
            }
        });
        serverTextChannel.getOverwrittenUserPermissions().forEach((aLong, permissions) -> {
            try {
                serverTextChannelBuilder.addPermissionOverwrite(api.getUserById(aLong).join(), permissions.toBuilder().setUnset(PermissionType.VIEW_CHANNEL).build());
            } catch (CompletionException ignored) {
            }
        });

        return serverTextChannelBuilder;
    }

    public String timeUnitToTimeString(String unit) {
        return switch (unit) {
            case "S" -> "秒";
            case "M" -> "分";
            case "H" -> "時間";
            case "D" -> "日";
            case "Y" -> "年";
            default -> "該当なしエラー";
        };
    }

    public boolean checkTimeAndUnit(Long delay_time, String delay_unit, Long end_time, String end_unit) {
        ArrayList<String> units = new ArrayList<>(Arrays.asList("S", "M", "H", "D", "Y"));
        int delayIdx = units.indexOf(delay_unit);
        int endIdx = units.indexOf(end_unit);
        boolean checkTime = true;
        if (delayIdx < endIdx) {
            checkTime = false;
        } else if (delayIdx == endIdx && delay_time < end_time) {
            checkTime = false;
        }
        return checkTime;
    }

    public String channelNameRePress(String default_name, String name) {
        return default_name.replaceAll("&name&", name);
    }
}
