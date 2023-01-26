package kcs.graduation.discord.dao;

import kcs.graduation.discord.dao.core.BaseDAO;
import kcs.graduation.discord.parameter.DAOParameters;
import kcs.graduation.discord.parameter.ServerSettingParameters;
import kcs.graduation.discord.parameter.TempChannelParameters;
import kcs.graduation.discord.record.MessageRecord;
import kcs.graduation.discord.record.ServerSettingRecord;
import kcs.graduation.discord.record.TempChannelRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SlashCommandDAO extends BaseDAO {
    public boolean CheckIfManage(long userId, long channel) {
        this.open();
        PreparedStatement preStatement = null;
        boolean isCheck = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + TempChannelParameters.OWNER_U_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " WHERE "
                    + TempChannelParameters.OWNER_U_ID.getParam() + " = ? AND " + TempChannelParameters.VC_ID.getParam() + " = ?) AS manage_check";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, userId);
            preStatement.setLong(2, channel);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                isCheck = rs.getBoolean("manage_check");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return isCheck;
    }

    public boolean CheckIfChannel(long channel) {
        this.open();
        PreparedStatement preStatement = null;
        boolean isCheck = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + TempChannelParameters.OWNER_U_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " WHERE "
                    + TempChannelParameters.VC_ID.getParam() + " = ?) AS channel_check";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, channel);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                isCheck = rs.getBoolean("channel_check");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return isCheck;
    }

    public TempChannelRecord getTempChannelData(long channelId) {
        this.open();
        PreparedStatement preStatement = null;
        TempChannelRecord record = null;
        try {
            String sql = "SELECT " + TempChannelParameters.TC_ID.getParam()
                    + "," + TempChannelParameters.OWNER_U_ID.getParam()
                    + " FROM "
                    + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " WHERE " + TempChannelParameters.VC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, channelId);
            ResultSet rs = preStatement.executeQuery();
            record = new TempChannelRecord();
            while (rs.next()) {
                record.setOwner_user_id(rs.getLong(TempChannelParameters.OWNER_U_ID.getParam()));
                record.setText_channel_id(rs.getLong(TempChannelParameters.TC_ID.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
    }

    public ServerSettingRecord getMentionData(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        ServerSettingRecord record = null;
        try {
            String sql = "SELECT "
                    + ServerSettingParameters.M_C.getParam()
                    + " FROM " + DAOParameters.TABLE_SERVER_SETTING.getParam()
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            record = new ServerSettingRecord();
            while (rs.next()) {
                record.setMention_channel_id(rs.getLong(ServerSettingParameters.M_C.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
    }

    public void setMentionMessage(MessageRecord messageRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_MENTION_MESSAGE.getParam() + " VALUES (?, ?, ?, ?, ?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, messageRecord.getServerId());
            preStatement.setLong(2, messageRecord.getMessageId());
            preStatement.setLong(3, messageRecord.getTextChannelId());
            preStatement.setLong(4, messageRecord.getVoiceChannelId());
            preStatement.setString(5, messageRecord.getLink());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateOwnerUser(long text_channel_id, long id) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " SET "
                    + TempChannelParameters.OWNER_U_ID.getParam() + " = ?"
                    + " WHERE " + TempChannelParameters.TC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            preStatement.setLong(2, text_channel_id);
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }
}
