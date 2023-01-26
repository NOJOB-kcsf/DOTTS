package kcs.graduation.discord.dao;

import kcs.graduation.discord.dao.core.BaseDAO;
import kcs.graduation.discord.parameter.*;
import kcs.graduation.discord.record.MessageRecord;
import kcs.graduation.discord.record.ServerSettingRecord;
import kcs.graduation.discord.record.TempChannelRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InfoMessageDAO extends BaseDAO {
    public TempChannelRecord getDataToHideAndLock(long id) {
        this.open();
        PreparedStatement preStatement = null;
        TempChannelRecord record = null;
        try {
            String sql = "SELECT "
                    + TempChannelParameters.VC_ID.getParam()
                    + ", " + TempChannelParameters.OWNER_U_ID.getParam()
                    + ", " + TempChannelParameters.HIDE_BY.getParam()
                    + ", " + TempChannelParameters.LOCK_BY.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " WHERE " + TempChannelParameters.TC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            ResultSet rs = preStatement.executeQuery();
            record = new TempChannelRecord();
            while (rs.next()) {
                record.setVoice_channel_id(rs.getLong(TempChannelParameters.VC_ID.getParam()));
                record.setOwner_user_id(rs.getLong(TempChannelParameters.OWNER_U_ID.getParam()));
                record.setHide_by(rs.getBoolean(TempChannelParameters.HIDE_BY.getParam()));
                record.setLock_by(rs.getBoolean(TempChannelParameters.LOCK_BY.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
    }

    public boolean tempTextChannelCheck(long id) {
        this.open();
        PreparedStatement preStatement = null;
        boolean check = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + TempChannelParameters.TC_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " WHERE "
                    + TempChannelParameters.TC_ID.getParam() + " = ?) AS text_channel";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                check = rs.getBoolean("text_channel");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return check;
    }

    public void updateTempHide(TempChannelRecord tempChannelRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " SET " + TempChannelParameters.HIDE_BY.getParam() + " = ?"
                    + " WHERE " + TempChannelParameters.VC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setBoolean(1, tempChannelRecord.isHide_by());
            preStatement.setLong(2, tempChannelRecord.getVoice_channel_id());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateTempLock(TempChannelRecord tempChannelRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " SET " + TempChannelParameters.LOCK_BY.getParam() + " = ?"
                    + " WHERE " + TempChannelParameters.VC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setBoolean(1, tempChannelRecord.isLock_by());
            preStatement.setLong(2, tempChannelRecord.getVoice_channel_id());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public TempChannelRecord getOwnerUserId(long id) {
        this.open();
        PreparedStatement preStatement = null;
        TempChannelRecord record = null;
        try {
            String sql = "SELECT "
                    + TempChannelParameters.OWNER_U_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " WHERE " + TempChannelParameters.TC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            ResultSet rs = preStatement.executeQuery();
            record = new TempChannelRecord();
            while (rs.next()) {
                record.setOwner_user_id(rs.getLong(TempChannelParameters.OWNER_U_ID.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
    }

    public ArrayList<String> getNamePreset(long id) {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<String> namePreset = new ArrayList<>();
        try {
            String sql = "SELECT "
                    + NamePresetParameters.NAME.getParam()
                    + " FROM " + DAOParameters.TABLE_NAME_PRESET.getParam()
                    + " WHERE " + NamePresetParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                namePreset.add(rs.getString(NamePresetParameters.NAME.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return namePreset;
    }

    public ServerSettingRecord getMentionData(long id) {
        this.open();
        PreparedStatement preStatement = null;
        ServerSettingRecord record = null;
        try {
            String sql = "SELECT "
                    + ServerSettingParameters.M_C.getParam()
                    + " FROM " + DAOParameters.TABLE_SERVER_SETTING.getParam()
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
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

    public void addMentionMessage(MessageRecord messageRecord) {
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

    public ArrayList<MessageRecord> getMentionMessage(long id) {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<MessageRecord> messageRecords = new ArrayList<>();
        try {
            String sql = "SELECT "
                    + MentionMessageParameters.M_LINK.getParam()
                    + " FROM " + DAOParameters.TABLE_MENTION_MESSAGE.getParam()
                    + " WHERE " + MentionMessageParameters.TC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                MessageRecord messageRecord = new MessageRecord();
                messageRecord.setLink(rs.getString(MentionMessageParameters.M_LINK.getParam()));
                messageRecords.add(messageRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return messageRecords;
    }

    public TempChannelRecord addTempChannelData(long id, TempChannelRecord mentionRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "SELECT "
                    + TempChannelParameters.VC_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " WHERE " + TempChannelParameters.TC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                mentionRecord.setVoice_channel_id(rs.getLong(TempChannelParameters.VC_ID.getParam()));
                mentionRecord.setText_channel_id(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return mentionRecord;
    }

    public void deleteMentionMessage(long text_channel_id) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "DELETE FROM " + DAOParameters.TABLE_MENTION_MESSAGE.getParam()
                    + " WHERE " + MentionMessageParameters.TC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, text_channel_id);
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateTempChannelOwner(long id, long voice_channel_id) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " SET " + TempChannelParameters.OWNER_U_ID.getParam() + " = ?"
                    + " WHERE " + TempChannelParameters.VC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            preStatement.setLong(2, voice_channel_id);
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public long getVoiceChannelId(long id) {
        this.open();
        PreparedStatement preStatement = null;
        long voice_channel_id = 0;
        try {
            String sql = "SELECT "
                    + TempChannelParameters.VC_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " WHERE " + TempChannelParameters.TC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                voice_channel_id = rs.getLong(TempChannelParameters.VC_ID.getParam());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return voice_channel_id;
    }
}

