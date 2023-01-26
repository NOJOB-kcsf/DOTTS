package kcs.graduation.discord.system;

import kcs.graduation.discord.dao.SlashCommandDAO;
import kcs.graduation.discord.record.MessageRecord;
import kcs.graduation.discord.record.ServerSettingRecord;
import kcs.graduation.discord.record.TempChannelRecord;
import kcs.graduation.discord.system.core.Processing;
import kcs.graduation.discord.system.core.SystemMaster;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.ServerVoiceChannelUpdater;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;

import java.io.IOException;
import java.net.InetAddress;

public class SlashCommand extends SystemMaster {
    DiscordApi api = getAPI();

    // madeW
    public SlashCommand() {
        SlashCommandDAO dao = new SlashCommandDAO();
        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            String cmd = interaction.getCommandName();
            User sendUser = interaction.getUser();
            // サーバー内
            if (interaction.getServer().isPresent() && interaction.getChannel().isPresent()) {
                Server server = interaction.getServer().get();
                long serverId = server.getId();
                switch (cmd) {
                    case "n", "name", "s", "size", "m", "men", "user", "claim" -> {
                        if (sendUser.getConnectedVoiceChannel(server).isEmpty()) {
                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("通話に居る時しか使えません。").respond();
                            break;
                        } else if (!dao.CheckIfChannel(sendUser.getConnectedVoiceChannel(server).get().getId())) {
                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("一時通話に居る時しか使えません。").respond();
                            break;
                        }
                        boolean isManage = dao.CheckIfManage(sendUser.getId(), sendUser.getConnectedVoiceChannel(server).get().getId());
                        TempChannelRecord tempChannelRecord = dao.getTempChannelData(sendUser.getConnectedVoiceChannel(server).get().getId());
                        if (isManage) {
                            if (cmd.equals("n") || cmd.equals("name") && interaction.getOptionStringValueByName("name").isPresent()) {
                                sendUser.getConnectedVoiceChannel(server).get().createUpdater().setName(interaction.getOptionStringValueByName("name").get()).update();
                                if (api.getServerTextChannelById(tempChannelRecord.getText_channel_id()).isPresent()) {
                                    api.getServerTextChannelById(tempChannelRecord.getText_channel_id()).get().createUpdater().setName(interaction.getOptionStringValueByName("name").get()).update();
                                }
                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("通話名を変更しました。").respond();
                            } else if (cmd.equals("s") || cmd.equals("size") && interaction.getOptionLongValueByName("size").isPresent()) {
                                if (interaction.getOptionLongValueByName("size").get() > 99 && 0 > interaction.getOptionLongValueByName("size").get()) {
                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("0 ~ 99までの数字を入力して下さい。").respond();
                                } else {
                                    sendUser.getConnectedVoiceChannel(server).get().createUpdater().setUserLimit(interaction.getOptionLongValueByName("size").get().intValue()).update();
                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("通話上限人数を変更しました。").respond();
                                }
                            } else if (cmd.equals("m") || cmd.equals("men")) {
                                ServerSettingRecord serverDataRecord = dao.getMentionData(serverId);
                                if (api.getServerTextChannelById(serverDataRecord.getMention_channel_id()).isEmpty()) {
                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("メンション送信先が設定されていません。\nサーバー管理者に報告してください。").respond();
                                }
                                ServerTextChannel textChannel = api.getServerTextChannelById(serverDataRecord.getMention_channel_id()).get();
                                EmbedBuilder embed = new EmbedBuilder()
                                        .setAuthor(interaction.getUser())
                                        .setTitle("募集");
                                if (interaction.getOptionStringValueByName("text").isPresent()) {
                                    embed.addField("募集内容", interaction.getOptionStringValueByName("text").get());
                                }
                                embed.addField("チャンネル", "<#" + sendUser.getConnectedVoiceChannel(server).get().getId() + ">");
                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("募集送信完了。").respond();
                                Message message = new MessageBuilder().setEmbed(embed).send(textChannel).join();
                                MessageRecord messageRecord = new MessageRecord();
                                messageRecord.setVoiceChannelId(sendUser.getConnectedVoiceChannel(server).get().getId());
                                messageRecord.setTextChannelId(tempChannelRecord.getText_channel_id());
                                messageRecord.setServerId(interaction.getServer().get().getId());
                                messageRecord.setMessageId(message.getId());
                                messageRecord.setLink(message.getLink().toString());
                                dao.setMentionMessage(messageRecord);
                            } else if (cmd.equals("user") && interaction.getOptionByIndex(0).isPresent()) {
                                String userCmd = interaction.getOptionByIndex(0).get().getName();
                                SlashCommandInteractionOption option = interaction.getOptionByIndex(0).get();
                                if (userCmd.equals("add")) {
                                    if (option.getOptionUserValueByName("selectUser").isPresent()) {
                                        User user = option.getOptionUserValueByName("selectUser").get();
                                        ServerVoiceChannel voiceChannel = sendUser.getConnectedVoiceChannel(server).get();
                                        ServerVoiceChannelUpdater updater = voiceChannel.createUpdater();
                                        updater.addPermissionOverwrite(user, new Processing().getUserPermission());
                                        updater.update();
                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(user.getName() + "を追加しました。").respond();
                                    } else {
                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("ユーザーを選択してください。").respond();
                                    }
                                } else if (userCmd.equals("delete")) {
                                    if (option.getOptionUserValueByName("selectUser").isPresent()) {
                                        User user = option.getOptionUserValueByName("selectUser").get();
                                        ServerVoiceChannel voiceChannel = sendUser.getConnectedVoiceChannel(server).get();
                                        ServerVoiceChannelUpdater updater = voiceChannel.createUpdater();
                                        updater.addPermissionOverwrite(user, new PermissionsBuilder().setDenied(PermissionType.CONNECT, PermissionType.VIEW_CHANNEL).build());
                                        updater.update();
                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(user.getName() + "が通話に入れないようにしました。").respond();
                                    } else {
                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("ユーザーを選択してください").respond();
                                    }
                                }
                            } else if (cmd.equals("claim")) {
                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("貴方は既に通話管理者です").respond();
                            }
                        } else {
                            if (cmd.equals("claim")) {
                                boolean claimSw = sendUser.getConnectedVoiceChannel(server).get().getConnectedUserIds().contains(tempChannelRecord.getOwner_user_id());
                                if (!claimSw) {
                                    sendUser.getConnectedVoiceChannel(server).get().createUpdater().addPermissionOverwrite(sendUser, new Processing().getChannelManagePermission()).update();
                                    if (api.getServerTextChannelById(tempChannelRecord.getText_channel_id()).isPresent()) {
                                        api.getServerTextChannelById(tempChannelRecord.getText_channel_id()).get().createUpdater().addPermissionOverwrite(sendUser, new Processing().getChannelManagePermission()).update();
                                    }
                                    dao.updateOwnerUser(tempChannelRecord.getText_channel_id(), sendUser.getId());
                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(sendUser.getName() + "が新しく通話管理者になりました").respond();
                                } else {
                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("通話管理者が通話にいらっしゃいます").respond();
                                }
                            } else {
                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("貴方は通話管理者ではありません").respond();
                            }
                        }
                    }
                }
            }
            if ("ping".equals(cmd)) {
                long ping = 0L;
                try {
                    InetAddress address = InetAddress.getByName("8.8.8.8");
                    for (int n = 0; n < 5; n++) {
                        long start = System.currentTimeMillis();
                        boolean ena = address.isReachable(100);
                        long end = System.currentTimeMillis();
                        if (ena) ping += (end - start);
                    }
                    ping /= 5L;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(ping + "ms").respond();
            }
        });
    }
}
