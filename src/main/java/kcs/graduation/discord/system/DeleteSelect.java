package kcs.graduation.discord.system;

import kcs.graduation.discord.dao.DeleteSelectSystemDAO;
import kcs.graduation.discord.record.EventDataRecord;
import kcs.graduation.discord.record.NamePresetRecord;
import kcs.graduation.discord.system.core.SystemMaster;
import kcs.graduation.discord.system.embed.Constant;
import kcs.graduation.discord.system.embed.create.CancelEmbed;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.*;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.ButtonInteraction;
import org.javacord.api.interaction.SelectMenuInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

import java.util.ArrayList;

public class DeleteSelect extends SystemMaster {
    DiscordApi api = getAPI();
    // madeW
    DeleteSelectSystemDAO dao = new DeleteSelectSystemDAO();

    public DeleteSelect() {
        api.addSelectMenuChooseListener(event -> {
            SelectMenuInteraction interaction = event.getSelectMenuInteraction();
            if (interaction.getServer().isPresent()) {
                Server server = interaction.getServer().get();
                boolean isAdmin = server.isAdmin(interaction.getUser());
                if ("removeName".equals(interaction.getCustomId())) {
                    if (isAdmin) {
                        ArrayList<NamePresetRecord> namePresetRecords = new ArrayList<>();
                        for (SelectMenuOption selectMenuOption : interaction.getChosenOptions()) {
                            NamePresetRecord record = new NamePresetRecord();
                            record.setServer_id(server.getId());
                            record.setName(selectMenuOption.getValue());
                            namePresetRecords.add(record);
                        }
                        dao.deleteNamePreset(namePresetRecords); // 削除実行
                        StringBuilder content = new StringBuilder("'").append(namePresetRecords.get(0).getName()).append("`");
                        for (NamePresetRecord record : namePresetRecords) {
                            if (namePresetRecords.indexOf(record) == 0) continue;
                            content.append("\n`").append(record.getName()).append("`");
                        }
                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("変更候補 " + content + "を削除しました。").respond();
                    } else {
                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).addEmbed(new CancelEmbed().createEmbed(Constant.NOT_ADMIN)).respond();
                    }
                } else if ("removeEventData".equals(interaction.getCustomId())) {
                    if (isAdmin) {
                        ArrayList<NamePresetRecord> namePresetRecords = new ArrayList<>();
                        for (SelectMenuOption selectMenuOption : interaction.getChosenOptions()) {
                            NamePresetRecord record = new NamePresetRecord();
                            record.setServer_id(server.getId());
                            record.setName(selectMenuOption.getValue());
                            namePresetRecords.add(record);
                        }
                        dao.deleteNamePreset(namePresetRecords); // 削除実行
                        StringBuilder content = new StringBuilder("'").append(namePresetRecords.get(0).getName()).append("`");
                        for (NamePresetRecord record : namePresetRecords) {
                            if (namePresetRecords.indexOf(record) == 0) continue;
                            content.append("\n`").append(record.getName()).append("`");
                        }
                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("変更候補 " + content + "を削除しました。").respond();
                    } else {
                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).addEmbed(new CancelEmbed().createEmbed(Constant.NOT_ADMIN)).respond();
                    }
                }
            }
        });

        api.addSlashCommandCreateListener(slashCommandCreateEvent -> {
            SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
            String cmd = interaction.getCommandName();
            User sendUser = interaction.getUser();
            if (interaction.getServer().isPresent() && interaction.getChannel().isPresent() && interaction.getOptionByIndex(0).isPresent()) {
                InteractionImmediateResponseBuilder responseBuilder = interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL);
                Server server = interaction.getServer().get();
                long serverId = server.getId();
                if ("remove".equals(cmd)) {
                    if (server.isAdmin(sendUser)) {
                        SlashCommandInteractionOption subcommand = interaction.getOptionByIndex(0).get();
                        switch (subcommand.getName()) {
                            case "name_preset" -> {
                                DeleteSelectSystemDAO dao = new DeleteSelectSystemDAO();
                                ArrayList<NamePresetRecord> names = dao.getPresetName(serverId);
                                if (names.size() > 0) {
                                    SelectMenuBuilder selectMenuBuilder = new SelectMenuBuilder().setCustomId("removeName").setPlaceholder("削除したい名前候補を選択してください").setMaximumValues(1).setMinimumValues(names.size());
                                    selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(Integer.toString(0)).setValue(Integer.toString(0)).build());
                                    for (NamePresetRecord name : names) {
                                        selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(name.getName()).setValue(name.getName()).build());
                                    }
                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("名前変更候補削除")
                                            .addComponents(ActionRow.of(selectMenuBuilder.build())).respond();
                                } else {
                                    responseBuilder.addEmbed(new CancelEmbed().createEmbed(Constant.NAME_PRESET_NO_ELEMENT)).respond();
                                }
                            }
                            case "event_data" -> {
                                DeleteSelectSystemDAO dao = new DeleteSelectSystemDAO();
                                ArrayList<EventDataRecord> records = dao.getEventData(serverId, 25);
                                if (records.size() > 0) {
                                    SelectMenuBuilder selectMenuBuilder = new SelectMenuBuilder().setCustomId("removeEventData").setPlaceholder("削除したいイベント削除を選択してください").setMaximumValues(1).setMinimumValues(records.size());
                                    selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(Integer.toString(0)).setValue(Integer.toString(0)).build());
                                    for (EventDataRecord record : records) {
                                        selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(record.getEventName()).setValue(record.getServerId() + ":" + record.getId()).build());
                                    }
                                    responseBuilder.setFlags(MessageFlag.EPHEMERAL).setContent("イベント削除")
                                            .addComponents(ActionRow.of(selectMenuBuilder.build()));
                                    if (dao.getEventAllDataCount(serverId) > 25) {
                                        responseBuilder.addComponents(ActionRow.of(Button.success("next-event:25", "次の項目"))).respond();
                                    }
                                } else {
                                    responseBuilder.addEmbed(new CancelEmbed().createEmbed(Constant.EVENT_DATA_NO_ELEMENT)).respond();
                                }
                            }
                        }
                    }
                }
            }
        });

        api.addButtonClickListener(buttonClickEvent -> {
            ButtonInteraction interaction = buttonClickEvent.getButtonInteraction();
            String[] cmd = interaction.getCustomId().split(":");
            String command = cmd[0];
            long startDataIndex = Long.parseLong(cmd[1]);
            if (("next-event".equals(command) || "prev-event".equals(command)) && interaction.getServer().isPresent() && interaction.getServer().get().isAdmin(interaction.getUser())) {
                InteractionImmediateResponseBuilder responseBuilder = interaction.createImmediateResponder();
                Server server = interaction.getServer().get();
                DeleteSelectSystemDAO dao = new DeleteSelectSystemDAO();
                ArrayList<EventDataRecord> records = dao.getEventData(server.getId(), startDataIndex, 25);
                if (records.size() > 0) {
                    SelectMenuBuilder selectMenuBuilder = new SelectMenuBuilder().setCustomId("removeEventData").setPlaceholder("削除したいイベント削除を選択してください").setMaximumValues(1).setMinimumValues(records.size());
                    selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(Integer.toString(0)).setValue(Integer.toString(0)).build());
                    for (EventDataRecord record : records) {
                        selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(record.getEventName()).setValue(record.getServerId() + ":" + record.getId()).build());
                    }
                    responseBuilder.setFlags(MessageFlag.EPHEMERAL).setContent("イベント削除")
                            .addComponents(ActionRow.of(selectMenuBuilder.build()));
                    long dataCount = dao.getEventAllDataCount(server.getId());
                    if (startDataIndex - 25 > 0) {
                        responseBuilder.addComponents(ActionRow.of(Button.danger("prev-event:" + (startDataIndex - 25), "前の項目")));
                    }
                    if (dataCount > startDataIndex + 25) {
                        responseBuilder.addComponents(ActionRow.of(Button.success("next-event:" + (startDataIndex + 25), "次の項目"))).respond();
                    }

                } else {
                    responseBuilder.addEmbed(new CancelEmbed().createEmbed(Constant.EVENT_DATA_NO_ELEMENT)).respond();
                }
            }
        });
    }
}
