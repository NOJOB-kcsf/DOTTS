package kcs.graduation.discord.dao;

import kcs.graduation.discord.dao.core.BaseDAO;
import kcs.graduation.discord.parameter.*;
import kcs.graduation.discord.record.MessageRecord;
import kcs.graduation.discord.record.NamePresetRecord;
import kcs.graduation.discord.record.ServerSettingRecord;
import kcs.graduation.discord.record.TempChannelRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TempChannelDAO extends BaseDAO {

    public boolean firstCheckFit(long id) {
        this.open();
        PreparedStatement preStatement = null;
        boolean check = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + ServerSettingParameters.FC_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " WHERE "
                    + ServerSettingParameters.FC_ID.getParam() + " = ?) AS first_channel";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                check = rs.getBoolean("first_channel");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return check;
    }

    public ServerSettingRecord getTempSetting(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        ServerSettingRecord record = null;
        try {
            String sql = "SELECT "
                    + ServerSettingParameters.T_VC_ID.getParam()
                    + ", " + ServerSettingParameters.T_TC_ID.getParam()
                    + ", " + ServerSettingParameters.T_BY.getParam()
                    + ", " + ServerSettingParameters.T_TC_BY.getParam()
                    + ", " + ServerSettingParameters.UNDER_C_ID.getParam()
                    + ", " + ServerSettingParameters.UNDER_C_BY.getParam()
                    + ", " + ServerSettingParameters.D_SIZE.getParam()
                    + ", " + ServerSettingParameters.D_NAME.getParam()
                    + ", " + ServerSettingParameters.M_C.getParam()
                    + " FROM " + DAOParameters.TABLE_SERVER_SETTING.getParam()
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            record = new ServerSettingRecord();
            while (rs.next()) {
                record.setTemp_voice_category_id(rs.getLong(ServerSettingParameters.T_VC_ID.getParam()));
                record.setTemp_text_category_id(rs.getLong(ServerSettingParameters.T_TC_ID.getParam()));
                record.setTemp_created_by(rs.getBoolean(ServerSettingParameters.T_BY.getParam()));
                record.setTemp_text_created_by(rs.getBoolean(ServerSettingParameters.T_TC_BY.getParam()));
                record.setCreated_under_category_id(rs.getLong(ServerSettingParameters.UNDER_C_ID.getParam()));
                record.setNew_under_category_by(rs.getBoolean(ServerSettingParameters.UNDER_C_BY.getParam()));
                record.setDefault_size(rs.getInt(ServerSettingParameters.D_SIZE.getParam()));
                record.setDefault_name(rs.getString(ServerSettingParameters.D_NAME.getParam()));
                record.setMention_channel_id(rs.getLong(ServerSettingParameters.M_C.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
    }

    public void addTempChannel(TempChannelRecord tempChannelRecord) {
        this.open();
        PreparedStatement preStatement = null;
        int i = 1;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " VALUES (?,?,?,?,?,?,?,?,?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, tempChannelRecord.getServer_id());
            preStatement.setLong(2, tempChannelRecord.getCategory_id());
            preStatement.setLong(3, tempChannelRecord.getVoice_channel_id());
            preStatement.setLong(4, tempChannelRecord.getText_channel_id());
            preStatement.setLong(5, tempChannelRecord.getInfo_message_id());
            preStatement.setLong(6, tempChannelRecord.getOwner_user_id());
            preStatement.setBoolean(7, false);
            preStatement.setBoolean(8, false);
            preStatement.setBoolean(9, tempChannelRecord.isNewCategoryBy());

            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public boolean tempVoiceChannelCheck(long id) {
        this.open();
        PreparedStatement preStatement = null;
        boolean check = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + TempChannelParameters.VC_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " WHERE "
                    + TempChannelParameters.VC_ID.getParam() + " = ?) AS voice_channel";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                check = rs.getBoolean("voice_channel");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return check;
    }

    public TempChannelRecord geTempJoinVoiceChannel(long id) {
        this.open();
        PreparedStatement preStatement = null;
        TempChannelRecord record = null;
        try {
            String sql = "SELECT "
                    + TempChannelParameters.TC_ID.getParam()
                    + ", " + TempChannelParameters.OWNER_U_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " WHERE " + TempChannelParameters.VC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            ResultSet rs = preStatement.executeQuery();
            record = new TempChannelRecord();
            while (rs.next()) {
                record.setText_channel_id(rs.getLong(TempChannelParameters.TC_ID.getParam()));
                record.setOwner_user_id(rs.getLong(TempChannelParameters.OWNER_U_ID.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
    }

    public TempChannelRecord geTempLeaveVoiceChannel(long id) {
        this.open();
        PreparedStatement preStatement = null;
        TempChannelRecord record = null;
        try {
            String sql = "SELECT "
                    + TempChannelParameters.TC_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " WHERE " + TempChannelParameters.VC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            ResultSet rs = preStatement.executeQuery();
            record = new TempChannelRecord();
            while (rs.next()) {
                record.setText_channel_id(rs.getLong(TempChannelParameters.TC_ID.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
    }

    public TempChannelRecord geTempVoiceChannelZeroUsers(long id) {
        this.open();
        PreparedStatement preStatement = null;
        TempChannelRecord record = null;
        try {
            String sql = "SELECT "
                    + TempChannelParameters.TC_ID.getParam()
                    + ", " + TempChannelParameters.N_C_BY.getParam()
                    + ", " + TempChannelParameters.C_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " WHERE " + TempChannelParameters.VC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            ResultSet rs = preStatement.executeQuery();
            record = new TempChannelRecord();
            while (rs.next()) {
                record.setVoice_channel_id(id);
                record.setText_channel_id(rs.getLong(TempChannelParameters.TC_ID.getParam()));
                record.setNewCategoryBy(rs.getBoolean(TempChannelParameters.N_C_BY.getParam()));
                record.setCategory_id(rs.getLong(TempChannelParameters.C_ID.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
    }

    public void deleteTempChannel(TempChannelRecord tempChannelRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "DELETE "
                    + "mention_message, "
                    + "temp_channel "
                    + "FROM "
                    + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " AS temp_channel"
                    + " LEFT JOIN " + DAOParameters.TABLE_MENTION_MESSAGE.getParam() + " AS mention_message ON temp_channel." + TempChannelParameters.VC_ID.getParam() + " = mention_message." + MentionMessageParameters.VC_ID.getParam()
                    + " WHERE temp_channel." + TempChannelParameters.VC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, tempChannelRecord.getVoice_channel_id());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public ArrayList<MessageRecord> getMentionMessage(long voiceChanelId) {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<MessageRecord> arrayList = new ArrayList<>();
        try {
            String sql = "SELECT " + MentionMessageParameters.M_LINK.getParam()
                    + " FROM " + DAOParameters.TABLE_MENTION_MESSAGE.getParam()
                    + " WHERE " + MentionMessageParameters.VC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, voiceChanelId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                MessageRecord record = new MessageRecord();
                record.setLink(rs.getString(MentionMessageParameters.M_LINK.getParam()));
                arrayList.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return arrayList;
    }

    public int getNamePresetSize(long id) {
        this.open();
        PreparedStatement preStatement = null;
        int size = 0;
        try {
            String sql = "SELECT COUNT(*) AS size FROM " + DAOParameters.TABLE_NAME_PRESET.getParam()
                    + " WHERE " + NamePresetParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                size = rs.getInt("size");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return size;
    }

    public void addNamePreset(NamePresetRecord record) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_NAME_PRESET.getParam()
                    + " (" + NamePresetParameters.S_ID.getParam()
                    + ", " + NamePresetParameters.NAME.getParam()
                    + ") VALUES (?, ?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, record.getServer_id());
            preStatement.setString(2, record.getName());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }
}
