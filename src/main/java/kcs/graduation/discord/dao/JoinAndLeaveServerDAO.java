package kcs.graduation.discord.dao;

import kcs.graduation.discord.dao.core.BaseDAO;
import kcs.graduation.discord.parameter.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JoinAndLeaveServerDAO extends BaseDAO {
    // madeW
    public void addServerData(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_SERVER_SETTING.getParam() + "(" + ServerSettingParameters.S_ID.getParam() + ") VALUES (?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void deleteServerData(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "DELETE "
                    + "setting, "
                    + "delete_message, "
                    + "delete_time, "
                    + "event_channel, "
                    + "event_data, "
                    + "mention_message, "
                    + "nmame_preset, "
                    + "react_emoji_and_role, "
                    + "react_message, "
                    + "temp_channel, "
                    + "static_channel "
                    + "FROM "
                    + DAOParameters.TABLE_SERVER_SETTING.getParam() + " AS setting"
                    + " LEFT JOIN " + DAOParameters.TABLE_DELETE_MESSAGE.getParam() + " AS delete_message ON setting." + ServerSettingParameters.S_ID.getParam() + " = delete_message." + DeleteMessageParameters.S_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_DELETE_TIME.getParam() + " AS delete_time ON setting." + ServerSettingParameters.S_ID.getParam() + " = delete_time." + DeleteTimeParameters.S_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_EVENT_CHANNEL.getParam() + " AS event_channel ON setting." + ServerSettingParameters.S_ID.getParam() + " = event_channel." + EventChannelParameters.S_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " AS event_data ON setting." + ServerSettingParameters.S_ID.getParam() + " = event_data." + NamePresetParameters.S_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_MENTION_MESSAGE.getParam() + " AS mention_message ON setting." + ServerSettingParameters.S_ID.getParam() + " = mention_message." + MentionMessageParameters.S_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_NAME_PRESET.getParam() + " AS nmame_preset ON setting." + ServerSettingParameters.S_ID.getParam() + " = nmame_preset." + NamePresetParameters.S_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_REACT_EMOJI_AND_ROLE.getParam() + " AS react_emoji_and_role ON setting." + ServerSettingParameters.S_ID.getParam() + " = react_emoji_and_role." + ReactEmojiAndRoleParameters.S_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_REACT_MESSAGE.getParam() + " AS react_message ON setting." + ServerSettingParameters.S_ID.getParam() + " = react_message." + ReactMessageParameters.S_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " AS temp_channel ON setting." + ServerSettingParameters.S_ID.getParam() + " = temp_channel." + TempChannelParameters.S_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_STATIC_CHANNEL.getParam() + " AS static_channel ON setting." + ServerSettingParameters.S_ID.getParam() + " = static_channel." + StaticChannelParameters.S_ID.getParam()
                    + " WHERE setting." + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }
}
