package kcs.graduation.discord.system;

import kcs.graduation.discord.dao.InfoMessageDAO;
import kcs.graduation.discord.record.MessageRecord;
import kcs.graduation.discord.record.ServerSettingRecord;
import kcs.graduation.discord.record.TempChannelRecord;
import kcs.graduation.discord.system.core.Processing;
import kcs.graduation.discord.system.core.SystemMaster;
import kcs.graduation.discord.system.embed.Constant;
import kcs.graduation.discord.system.embed.create.CancelEmbed;
import kcs.graduation.discord.system.embed.create.CompleteEmbed;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.*;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.ButtonInteraction;
import org.javacord.api.interaction.SelectMenuInteraction;

import java.util.ArrayList;

public class InfoMessage extends SystemMaster {
    DiscordApi api = getAPI();

    // madeW
    public InfoMessage() {
        api.addButtonClickListener(event -> {
            ButtonInteraction interaction = event.getButtonInteraction();
            String customId = interaction.getCustomId();
            InfoMessageDAO dao = new InfoMessageDAO();
            if (interaction.getServer().isPresent() && interaction.getChannel().isPresent() && dao.tempTextChannelCheck(interaction.getChannel().get().getId())) {
                switch (customId) {
                    case "hide" -> {
                        TempChannelRecord tempChannelRecord = dao.getDataToHideAndLock(interaction.getChannel().get().getId());
                        Processing processing = new Processing();
                        User user = interaction.getUser();
                        if (user.getId() != tempChannelRecord.getOwner_user_id()) {
                            CancelEmbed cancelEmbed = new CancelEmbed();
                            interaction.createImmediateResponder().addEmbed(cancelEmbed.createEmbed(Constant.TEMP_USER_IS_NO_OWNER)).setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        tempChannelRecord.setHide_by(!tempChannelRecord.isHide_by());
                        Server server = interaction.getServer().get();
                        if (server.getVoiceChannelById(tempChannelRecord.getVoice_channel_id()).isPresent()) {
                            PermissionsBuilder permissionsBuilder = new PermissionsBuilder();
                            if (tempChannelRecord.isLock_by()) permissionsBuilder.setDenied(PermissionType.CONNECT);
                            else permissionsBuilder.setUnset(PermissionType.CONNECT);
                            ServerVoiceChannel serverVoiceChannel = server.getVoiceChannelById(tempChannelRecord.getVoice_channel_id()).get();
                            serverVoiceChannel.createUpdater().addPermissionOverwrite(server.getEveryoneRole(), (tempChannelRecord.isHide_by() ? permissionsBuilder.setDenied(PermissionType.VIEW_CHANNEL) : permissionsBuilder.setUnset(PermissionType.VIEW_CHANNEL)).build()).update();
                            CompleteEmbed completeEmbed = new CompleteEmbed();
                            interaction.createImmediateResponder().addEmbed(completeEmbed.createEmbed(serverVoiceChannel, (tempChannelRecord.isHide_by() ? Constant.TEMP_IS_HIDE : Constant.TEMP_IS_NOT_HIDE))).setFlags(MessageFlag.EPHEMERAL).respond();
                        }
                        dao.updateTempHide(tempChannelRecord);
                        interaction.getMessage().createUpdater().setEmbed(processing.rePressInfoMessage(user, tempChannelRecord.isHide_by(), tempChannelRecord.isLock_by())).applyChanges().join();
                    }
                    case "lock" -> {
                        TempChannelRecord tempChannelRecord = dao.getDataToHideAndLock(interaction.getChannel().get().getId());
                        Processing processing = new Processing();
                        User user = interaction.getUser();
                        if (user.getId() != tempChannelRecord.getOwner_user_id()) {
                            CancelEmbed cancelEmbed = new CancelEmbed();
                            interaction.createImmediateResponder().addEmbed(cancelEmbed.createEmbed(Constant.TEMP_USER_IS_NO_OWNER)).setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        tempChannelRecord.setLock_by(!tempChannelRecord.isLock_by());
                        Server server = interaction.getServer().get();
                        if (server.getVoiceChannelById(tempChannelRecord.getVoice_channel_id()).isPresent()) {
                            PermissionsBuilder permissionsBuilder = new PermissionsBuilder();
                            if (tempChannelRecord.isHide_by())
                                permissionsBuilder.setDenied(PermissionType.VIEW_CHANNEL);
                            else permissionsBuilder.setUnset(PermissionType.VIEW_CHANNEL);
                            ServerVoiceChannel serverVoiceChannel = server.getVoiceChannelById(tempChannelRecord.getVoice_channel_id()).get();
                            serverVoiceChannel.createUpdater().addPermissionOverwrite(server.getEveryoneRole(), (tempChannelRecord.isLock_by() ? permissionsBuilder.setDenied(PermissionType.CONNECT) : permissionsBuilder.setUnset(PermissionType.CONNECT)).build()).update();
                            CompleteEmbed completeEmbed = new CompleteEmbed();
                            interaction.createImmediateResponder().addEmbed(completeEmbed.createEmbed(serverVoiceChannel, (tempChannelRecord.isLock_by() ? Constant.TEMP_IS_LOCK : Constant.TEMP_IS_NOT_LOCK))).setFlags(MessageFlag.EPHEMERAL).respond();
                        }
                        dao.updateTempLock(tempChannelRecord);
                        interaction.getMessage().createUpdater().setEmbed(processing.rePressInfoMessage(user, tempChannelRecord.isLock_by(), tempChannelRecord.isLock_by())).applyChanges().join();
                    }
                    case "name" -> {
                        TempChannelRecord tempChannelRecord = dao.getOwnerUserId(interaction.getChannel().get().getId());
                        User user = interaction.getUser();
                        if (user.getId() != tempChannelRecord.getOwner_user_id()) {
                            CancelEmbed cancelEmbed = new CancelEmbed();
                            interaction.createImmediateResponder().addEmbed(cancelEmbed.createEmbed(Constant.TEMP_USER_IS_NO_OWNER)).setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        ArrayList<String> names = dao.getNamePreset(interaction.getServer().get().getId());
                        if (names.size() > 0) {
                            SelectMenuBuilder selectMenuBuilder = new SelectMenuBuilder().setCustomId("name").setPlaceholder("変更したい名前を設定してください").setMaximumValues(1).setMinimumValues(1);
                            for (String name : names) {
                                selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(name).setValue(name).build());
                            }
                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("通話名前変更")
                                    .addComponents(ActionRow.of(selectMenuBuilder.build())).respond();
                        } else {
                            CancelEmbed cancelEmbed = new CancelEmbed();
                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).addEmbed(cancelEmbed.createEmbed(Constant.NAME_PRESET_NO_ELEMENT)).respond();
                        }
                    }
                    case "next" -> {
                        interaction.getMessage().createUpdater().removeAllComponents().addComponents(
                                ActionRow.of(Button.danger("back", "前の項目"),
                                        Button.success("remove-recruiting", "募集文削除"),
                                        Button.success("hide", "非表示切替"),
                                        Button.success("lock", "参加許可切替"))).applyChanges();
                        interaction.createImmediateResponder().respond();
                    }
                    case "back" -> {
                        interaction.getMessage().createUpdater().removeAllComponents().addComponents(
                                ActionRow.of(Button.success("name", "通話名前変更"),
                                        Button.success("size", "通話人数変更"),
                                        Button.success("send-recruiting", "募集送信"),
                                        Button.success("claim", "通話権限獲得"),
                                        Button.danger("next", "次の項目"))).applyChanges();
                        interaction.createImmediateResponder().respond();
                    }
                    case "size" -> {
                        TempChannelRecord tempChannelRecord = dao.getOwnerUserId(interaction.getChannel().get().getId());
                        if (interaction.getUser().getId() != tempChannelRecord.getOwner_user_id()) {
                            CancelEmbed cancelEmbed = new CancelEmbed();
                            interaction.createImmediateResponder().addEmbed(cancelEmbed.createEmbed(Constant.TEMP_USER_IS_NO_OWNER)).setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        SelectMenuBuilder selectMenuBuilder = new SelectMenuBuilder().setCustomId("size").setPlaceholder("変更したい人数を選択してください").setMaximumValues(1).setMinimumValues(1);
                        selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(Integer.toString(0)).setValue(Integer.toString(0)).build());
                        for (int n = 2; n < 7; n++) {
                            selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(Integer.toString(n)).setValue(Integer.toString(n)).build());
                        }
                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("通話人数変更")
                                .addComponents(ActionRow.of(selectMenuBuilder.build())).respond();
                    }

                    case "send-recruiting" -> {
                        TempChannelRecord tempChannelRecord = dao.getOwnerUserId(interaction.getChannel().get().getId());
                        if (interaction.getUser().getId() != tempChannelRecord.getOwner_user_id()) {
                            CancelEmbed cancelEmbed = new CancelEmbed();
                            interaction.createImmediateResponder().addEmbed(cancelEmbed.createEmbed(Constant.TEMP_USER_IS_NO_OWNER)).setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        ServerSettingRecord mentionRecord = dao.getMentionData(interaction.getServer().get().getId());
                        if (tempChannelRecord.getOwner_user_id() == interaction.getUser().getId() && interaction.getServer().get().getTextChannelById(mentionRecord.getMention_channel_id()).isPresent()) {
                            ServerTextChannel textChannel = interaction.getServer().get().getTextChannelById(mentionRecord.getMention_channel_id()).get();
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setAuthor(interaction.getUser())
                                    .setTitle("募集");
                            tempChannelRecord = dao.addTempChannelData(interaction.getChannel().get().getId(), tempChannelRecord);
                            if (api.getServerVoiceChannelById(tempChannelRecord.getVoice_channel_id()).isPresent()) {
                                embed.addField("チャンネル", "<#" + api.getServerVoiceChannelById(tempChannelRecord.getVoice_channel_id()).get().getIdAsString() + ">");
                            }
                            Message message = new MessageBuilder().setEmbed(embed).send(textChannel).join();
                            MessageRecord messageRecord = new MessageRecord();
                            messageRecord.setVoiceChannelId(tempChannelRecord.getVoice_channel_id());
                            messageRecord.setTextChannelId(tempChannelRecord.getText_channel_id());
                            messageRecord.setServerId(interaction.getServer().get().getId());
                            messageRecord.setMessageId(message.getId());
                            messageRecord.setLink(message.getLink().toString());
                            dao.addMentionMessage(messageRecord);
                            interaction.createImmediateResponder().respond();
                        }
                    }
                    case "remove-recruiting" -> {
                        TempChannelRecord tempChannelRecord = dao.getOwnerUserId(interaction.getChannel().get().getId());
                        if (interaction.getUser().getId() != tempChannelRecord.getOwner_user_id()) {
                            CancelEmbed cancelEmbed = new CancelEmbed();
                            interaction.createImmediateResponder().addEmbed(cancelEmbed.createEmbed(Constant.TEMP_USER_IS_NO_OWNER)).setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        ServerSettingRecord mentionRecord = dao.getMentionData(interaction.getServer().get().getId());
                        if (tempChannelRecord.getOwner_user_id() == interaction.getUser().getId() && interaction.getServer().get().getTextChannelById(mentionRecord.getMention_channel_id()).isPresent()) {
                            ArrayList<MessageRecord> mentionList = dao.getMentionMessage(interaction.getChannel().get().getId());
                            for (MessageRecord messageRecord : mentionList) {
                                if (api.getMessageByLink(messageRecord.getLink()).isPresent()) {
                                    api.getMessageByLink(messageRecord.getLink()).get().join().delete();
                                }
                            }
                            dao.deleteMentionMessage(tempChannelRecord.getText_channel_id());
                            CompleteEmbed completeEmbed = new CompleteEmbed();
                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).addEmbed(completeEmbed.createEmbed(interaction.getChannel().get().getIdAsString(), Constant.MENTION_MESSAGE_DELETE)).respond();
                        }
                    }
                    case "claim" -> {
                        TempChannelRecord tempChannelRecord = dao.getOwnerUserId(interaction.getChannel().get().getId());
                        tempChannelRecord = dao.addTempChannelData(interaction.getChannel().get().getId(), tempChannelRecord);
                        if (interaction.getUser().getId() == tempChannelRecord.getOwner_user_id()) {
                            CancelEmbed cancelEmbed = new CancelEmbed();
                            interaction.createImmediateResponder().addEmbed(cancelEmbed.createEmbed(Constant.TEMP_USER_IS_OWNER)).setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        Server server = interaction.getServer().get();
                        if (server.getVoiceChannelById(tempChannelRecord.getVoice_channel_id()).isPresent() && server.getVoiceChannelById(tempChannelRecord.getVoice_channel_id()).get().getConnectedUserIds().contains(tempChannelRecord.getOwner_user_id())) {
                            CancelEmbed cancelEmbed = new CancelEmbed();
                            interaction.createImmediateResponder().addEmbed(cancelEmbed.createEmbed(Constant.TEMP_ALREADY_OWNER)).setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        dao.updateTempChannelOwner(interaction.getUser().getId(), tempChannelRecord.getVoice_channel_id());
                        Processing processing = new Processing();
                        interaction.getMessage().createUpdater().setEmbed(processing.rePressInfoMessage(interaction.getUser(), tempChannelRecord.isHide_by(), tempChannelRecord.isLock_by())).applyChanges().join();
                        if (server.getVoiceChannelById(tempChannelRecord.getVoice_channel_id()).isPresent()) {
                            server.getVoiceChannelById(tempChannelRecord.getVoice_channel_id()).get().createUpdater().addPermissionOverwrite(interaction.getUser(), processing.getChannelManagePermission()).update();
                        }
                        if (server.getTextChannelById(tempChannelRecord.getText_channel_id()).isPresent()) {
                            server.getTextChannelById(tempChannelRecord.getText_channel_id()).get().createUpdater().addPermissionOverwrite(interaction.getUser(), processing.getChannelManagePermission()).update();
                        }
                        CompleteEmbed completeEmbed = new CompleteEmbed();
                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).addEmbed(completeEmbed.createEmbed(interaction.getUser().getId(), Constant.TEMP_CHANGE_OWNER)).respond();
                    }
                }

            }
        });

        api.addSelectMenuChooseListener(selectMenuChooseEvent -> {
            SelectMenuInteraction interaction = selectMenuChooseEvent.getSelectMenuInteraction();
            String customId = interaction.getCustomId();
            if (interaction.getChannel().isPresent()) {
                InfoMessageDAO dao = new InfoMessageDAO();
                TempChannelRecord tempChannelRecord = dao.getOwnerUserId(interaction.getChannel().get().getId());
                if (tempChannelRecord.getOwner_user_id() == interaction.getUser().getId()) {
                    if ("size".equals(customId)) {
                        int size = Integer.parseInt(interaction.getChosenOptions().get(0).getValue());
                        long tempVoiceChannelId = dao.getVoiceChannelId(interaction.getChannel().get().getId());
                        if (api.getServerVoiceChannelById(tempVoiceChannelId).isPresent()) {
                            api.getServerVoiceChannelById(tempVoiceChannelId).get().createUpdater().setUserLimit(size).update();
                            CompleteEmbed completeEmbed = new CompleteEmbed();
                            interaction.createImmediateResponder().addEmbed(completeEmbed.createEmbed(size, Constant.TEMP_SIZE_SET)).setFlags(MessageFlag.EPHEMERAL).respond();
                        }
                    } else if ("name".equals(customId)) {
                        String name = interaction.getChosenOptions().get(0).getValue();
                        long tempVoiceChannelId = dao.getVoiceChannelId(interaction.getChannel().get().getId());
                        if (api.getServerVoiceChannelById(tempVoiceChannelId).isPresent()) {
                            api.getServerVoiceChannelById(tempVoiceChannelId).get().createUpdater().setName(name).update();
                            if (interaction.getChannel().get().asServerTextChannel().isPresent())
                                interaction.getChannel().get().asServerTextChannel().get().createUpdater().setName(name).update();
                            CompleteEmbed completeEmbed = new CompleteEmbed();
                            interaction.createImmediateResponder().addEmbed(completeEmbed.createEmbed(name, Constant.TEMP_CHANGE_NAME)).setFlags(MessageFlag.EPHEMERAL).respond();
                        }
                    }
                } else {
                    CancelEmbed cancelEmbed = new CancelEmbed();
                    interaction.createImmediateResponder().addEmbed(cancelEmbed.createEmbed(Constant.TEMP_USER_IS_NO_OWNER)).setFlags(MessageFlag.EPHEMERAL).respond();
                }
            }
        });
    }
}
