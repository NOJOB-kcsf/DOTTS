package kcs.graduation.discord.dao;

import kcs.graduation.discord.dao.core.BaseDAO;
import kcs.graduation.discord.parameter.DAOParameters;
import kcs.graduation.discord.parameter.EventChannelParameters;
import kcs.graduation.discord.parameter.EventDataParameters;
import kcs.graduation.discord.parameter.ServerSettingParameters;
import kcs.graduation.discord.record.EventChannelRecord;
import kcs.graduation.discord.record.EventDataRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventChannelDAO extends BaseDAO {

    public void setEventChannelCategory(long serverId, long categoryId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " SET "
                    + ServerSettingParameters.EC_ID.getParam() + " = ?"
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, categoryId);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public boolean checkEventCategoryId(long serverId) {
        return getEventChannelCategoryId(serverId) == 0;
    }

    public long getEventChannelCategoryId(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        long eventCategoryId = -1L;
        try {
            String sql = "SELECT " + ServerSettingParameters.EC_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " WHERE "
                    + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                eventCategoryId = rs.getLong(ServerSettingParameters.EC_ID.getParam());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return eventCategoryId;
    }

    public int getEventDataCount(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        int count = -1;
        try {
            String sql = "SELECT COUNT(" + EventDataParameters.E_N.getParam() + ") AS COUNT " +
                    "FROM " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " WHERE "
                    + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                count = rs.getInt("COUNT");
            }
        } catch (SQLSyntaxErrorException e) {
            count = 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return count;
    }

    public void setEventData(EventDataRecord eventDataRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_EVENT_SETTING.getParam()
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            preStatement = con.prepareStatement(sql);
            int index = 0;
            preStatement.setLong(++index, eventDataRecord.getServerId());
            preStatement.setString(++index, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(eventDataRecord.getNextStartTime()));
            preStatement.setLong(++index, eventDataRecord.getDelayTime());
            preStatement.setString(++index, eventDataRecord.getDelayUnit());
            preStatement.setLong(++index, eventDataRecord.getEndTime());
            preStatement.setString(++index, eventDataRecord.getEndUnit());
            preStatement.setString(++index, eventDataRecord.getEventName());
            preStatement.setString(++index, eventDataRecord.getChannelName());
            preStatement.setString(++index, eventDataRecord.getEventDescription() == null ? "" : eventDataRecord.getEventDescription());
            preStatement.setLong(++index, eventDataRecord.getRoleId());
            preStatement.setInt(++index, eventDataRecord.getDefaultSize() == -1L ? 0 : (int) eventDataRecord.getDefaultSize());
            preStatement.setInt(++index, getEventDataCount(eventDataRecord.getServerId()) + 1);
            preStatement.setLong(++index, -1L);
            preStatement.setBoolean(++index, false);
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public boolean checkEventNameIsCovering(String event_name) {
        this.open();
        PreparedStatement preStatement = null;
        boolean ifCheck = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + EventDataParameters.E_N.getParam()
                    + " FROM " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " WHERE "
                    + EventDataParameters.E_N.getParam() + " = ?) AS event_name_covering";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, event_name);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                ifCheck = rs.getBoolean("event_name_covering");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return ifCheck;
    }

    public ArrayList<EventDataRecord> getEventOneHourAgoData(Date time) {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<EventDataRecord> eventDataRecords = new ArrayList<>();
        try {
            String sql = "SELECT EVENT_SETING." + EventDataParameters.N_ST.getParam()
                    + ",EVENT_SETING." + EventDataParameters.ID.getParam()
                    + ",EVENT_SETING." + EventDataParameters.S_ID.getParam()
                    + ",EVENT_SETING." + EventDataParameters.EM_RI.getParam()
                    + ",EVENT_SETING." + EventDataParameters.E_D.getParam()
                    + ",SERVER_SETTING." + ServerSettingParameters.M_C.getParam()
                    + " FROM " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " AS EVENT_SETING " +
                    "LEFT JOIN " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " AS SERVER_SETTING ON EVENT_SETING." + EventDataParameters.S_ID.getParam() + " = SERVER_SETTING." + ServerSettingParameters.S_ID.getParam()
                    + " WHERE EVENT_SETING."
                    + EventDataParameters.N_ST.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                EventDataRecord record = new EventDataRecord();
                record.setServerId(rs.getLong("EVENT_SETING." + EventDataParameters.S_ID.getParam()));
                record.setNextStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("EVENT_SETING." + EventDataParameters.N_ST.getParam())));
                record.setRoleId(rs.getLong("EVENT_SETING." + EventDataParameters.EM_RI.getParam()));
                record.setEventDescription(rs.getString("EVENT_SETING." + EventDataParameters.E_D.getParam()));
                record.setId(rs.getInt("EVENT_SETING." + EventDataParameters.ID.getParam()));
                record.setMentionChannelId(rs.getLong("SERVER_SETTING." + ServerSettingParameters.M_C.getParam()));
                eventDataRecords.add(record);
            }
        } catch (SQLSyntaxErrorException e) {
            return eventDataRecords;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            this.close(preStatement);
        }
        return eventDataRecords;
    }

    public void setEventMentionMessage(ArrayList<EventDataRecord> eventDataRecords) {
        eventDataRecords.forEach(eventDataRecord -> {
            this.open();
            PreparedStatement preStatement = null;
            try {
                String sql = "UPDATE " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " SET "
                        + EventDataParameters.M_M_L.getParam() + " = ?"
                        + " WHERE " + EventDataParameters.S_ID.getParam() + " = ? AND "
                        + EventDataParameters.ID.getParam() + " = ?";
                preStatement = con.prepareStatement(sql);
                preStatement.setString(1, eventDataRecord.getMentionMessageLink());
                preStatement.setLong(2, eventDataRecord.getServerId());
                preStatement.setLong(3, eventDataRecord.getId());
                preStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                this.close(preStatement);
            }
        });
    }

    public ArrayList<EventDataRecord> getEventExecute(Date date) {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<EventDataRecord> eventDataRecords = new ArrayList<>();
        try {
            String sql = "SELECT EVENT_SETING." + EventDataParameters.N_ST.getParam()
                    + ",EVENT_SETING." + EventDataParameters.S_ID.getParam()
                    + ",EVENT_SETING." + EventDataParameters.E_N.getParam()
                    + ",EVENT_SETING." + EventDataParameters.ID.getParam()
                    + ",EVENT_SETING." + EventDataParameters.EM_RI.getParam()
                    + ",EVENT_SETING." + EventDataParameters.M_M_L.getParam()
                    + ",EVENT_SETING." + EventDataParameters.C_S.getParam()
                    + ",EVENT_SETING." + EventDataParameters.E_D.getParam()
                    + ",EVENT_SETING." + EventDataParameters.C_N.getParam()
                    + ",SERVER_SETTING." + ServerSettingParameters.M_C.getParam()
                    + ",SERVER_SETTING." + ServerSettingParameters.EC_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " AS EVENT_SETING " +
                    "LEFT JOIN " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " AS SERVER_SETTING ON EVENT_SETING." + EventDataParameters.S_ID.getParam() + " = SERVER_SETTING." + ServerSettingParameters.S_ID.getParam()
                    + " WHERE EVENT_SETING."
                    + EventDataParameters.N_ST.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                EventDataRecord record = new EventDataRecord();
                record.setNextStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("EVENT_SETING." + EventDataParameters.N_ST.getParam())));
                record.setEventName(rs.getString("EVENT_SETING." + EventDataParameters.E_N.getParam()));
                record.setServerId(rs.getLong("EVENT_SETING." + EventDataParameters.S_ID.getParam()));
                record.setId(rs.getInt("EVENT_SETING." + EventDataParameters.ID.getParam()));
                record.setRoleId(rs.getLong("EVENT_SETING." + EventDataParameters.EM_RI.getParam()));
                record.setMentionMessageLink(rs.getString("EVENT_SETING." + EventDataParameters.M_M_L.getParam()));
                record.setDefaultSize(rs.getLong("EVENT_SETING." + EventDataParameters.C_S.getParam()));
                record.setEventDescription(rs.getString("EVENT_SETING." + EventDataParameters.E_D.getParam()));
                record.setChannelName(rs.getString("EVENT_SETING." + EventDataParameters.C_N.getParam()));
                record.setMentionChannelId(rs.getLong("SERVER_SETTING." + ServerSettingParameters.M_C.getParam()));
                record.setEventCategoryId(rs.getLong("SERVER_SETTING." + ServerSettingParameters.EC_ID.getParam()));
                eventDataRecords.add(record);
            }
        } catch (SQLSyntaxErrorException e) {
            return eventDataRecords;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            this.close(preStatement);
        }
        return eventDataRecords;
    }

    public void addEventChannel(EventChannelRecord eventChannelRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_EVENT_CHANNEL.getParam()
                    + " VALUES (?,?,?,?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, eventChannelRecord.getServer_id());
            preStatement.setInt(2, (int) eventChannelRecord.getId());
            preStatement.setLong(3, eventChannelRecord.getVoice_channel_id());
            preStatement.setLong(4, eventChannelRecord.getText_channel_id());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void setEventExecute(EventDataRecord eventDataRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " SET "
                    + EventDataParameters.E_E.getParam() + " = ?"
                    + " WHERE " + EventDataParameters.S_ID.getParam() + " = ? AND "
                    + EventDataParameters.ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setBoolean(1, true);
            preStatement.setLong(2, eventDataRecord.getServerId());
            preStatement.setLong(3, eventDataRecord.getId());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public ArrayList<EventDataRecord> getEventBeforeEnd() {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<EventDataRecord> eventDataRecords = new ArrayList<>();
        try {
            String sql = "SELECT EVENT_CHANNEL." + EventChannelParameters.TC_ID.getParam()
                    // 時間が取得できてない
                    + ",EVENT_SETING." + EventDataParameters.N_ST.getParam()
                    + ",EVENT_SETING." + EventDataParameters.E_T.getParam()
                    + ",EVENT_SETING." + EventDataParameters.E_U.getParam()
                    + " FROM " + DAOParameters.TABLE_EVENT_CHANNEL.getParam() + " AS EVENT_CHANNEL " +
                    "LEFT JOIN " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " AS EVENT_SETING ON EVENT_CHANNEL." + EventChannelParameters.S_ID.getParam() + " = EVENT_SETING." + EventDataParameters.S_ID.getParam()
                    + " WHERE EVENT_SETING." + EventDataParameters.E_E.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setBoolean(1, true);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                EventDataRecord eventDataRecord = new EventDataRecord();
                eventDataRecord.setNextStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString(EventDataParameters.N_ST.getParam())));
                eventDataRecord.setTextChannelId(rs.getLong(EventChannelParameters.TC_ID.getParam()));
                eventDataRecord.setEndTime(rs.getLong(EventDataParameters.E_T.getParam()));
                eventDataRecord.setEndUnit(rs.getString(EventDataParameters.E_U.getParam()));
                eventDataRecords.add(eventDataRecord);
            }
        } catch (SQLSyntaxErrorException e) {
            return eventDataRecords;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            this.close(preStatement);
        }
        return eventDataRecords;
    }

    public ArrayList<EventDataRecord> getEventEnd() {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<EventDataRecord> eventDataRecords = new ArrayList<>();
        try {
            String sql = "SELECT EVENT_CHANNEL." + EventChannelParameters.TC_ID.getParam()
                    + ",EVENT_CHANNEL." + EventChannelParameters.VC_ID.getParam()
                    + ",EVENT_CHANNEL." + EventChannelParameters.S_ID.getParam()
                    + ",EVENT_SETING." + EventChannelParameters.ID.getParam()
                    + ",EVENT_SETING." + EventDataParameters.N_ST.getParam()
                    + ",EVENT_SETING." + EventDataParameters.E_T.getParam()
                    + ",EVENT_SETING." + EventDataParameters.E_U.getParam()
                    + ",EVENT_SETING." + EventDataParameters.D_T.getParam()
                    + ",EVENT_SETING." + EventDataParameters.D_U.getParam()
                    + ",EVENT_SETING." + EventDataParameters.M_M_L.getParam()
                    + ",SERVER_SETING." + ServerSettingParameters.M_C.getParam()
                    + " FROM " + DAOParameters.TABLE_EVENT_CHANNEL.getParam() + " AS EVENT_CHANNEL"
                    + " LEFT JOIN " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " AS EVENT_SETING ON EVENT_CHANNEL." + EventChannelParameters.S_ID.getParam() + " = EVENT_SETING." + EventDataParameters.S_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " AS SERVER_SETING ON EVENT_CHANNEL." + EventChannelParameters.S_ID.getParam() + " = SERVER_SETING." + ServerSettingParameters.S_ID.getParam()
                    + " WHERE EVENT_SETING." + EventDataParameters.E_E.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setBoolean(1, true);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                EventDataRecord eventDataRecord = new EventDataRecord();
                eventDataRecord.setServerId(rs.getLong(EventChannelParameters.S_ID.getParam()));
                eventDataRecord.setId(rs.getInt(EventDataParameters.ID.getParam()));
                eventDataRecord.setNextStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString(EventDataParameters.N_ST.getParam())));
                eventDataRecord.setTextChannelId(rs.getLong(EventChannelParameters.TC_ID.getParam()));
                eventDataRecord.setVoiceChannelId(rs.getLong(EventChannelParameters.VC_ID.getParam()));
                eventDataRecord.setEndTime(rs.getLong(EventDataParameters.E_T.getParam()));
                eventDataRecord.setEndUnit(rs.getString(EventDataParameters.E_U.getParam()));
                eventDataRecord.setDelayTime(rs.getLong(EventDataParameters.D_T.getParam()));
                eventDataRecord.setDelayUnit(rs.getString(EventDataParameters.D_U.getParam()));
                eventDataRecord.setMentionMessageLink(rs.getString(EventDataParameters.M_M_L.getParam()));
                eventDataRecord.setMentionChannelId(rs.getLong(ServerSettingParameters.M_C.getParam()));
                eventDataRecords.add(eventDataRecord);
            }
        } catch (SQLSyntaxErrorException e) {
            return eventDataRecords;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            this.close(preStatement);
        }
        return eventDataRecords;
    }

    public void setEventEnd(EventDataRecord eventDataRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " SET "
                    + EventDataParameters.E_E.getParam() + " = ?"
                    + " WHERE " + EventDataParameters.S_ID.getParam() + " = ? AND "
                    + EventDataParameters.ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setBoolean(1, false);
            preStatement.setLong(2, eventDataRecord.getServerId());
            preStatement.setLong(3, eventDataRecord.getId());
            preStatement.executeUpdate();
            setNextEventTime(eventDataRecord);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void setNextEventTime(EventDataRecord eventDataRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " SET "
                    + EventDataParameters.N_ST.getParam() + " = ?"
                    + " WHERE " + EventDataParameters.S_ID.getParam() + " = ? AND "
                    + EventDataParameters.E_E.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(eventDataRecord.getNextStartTime()));
            preStatement.setLong(2, eventDataRecord.getServerId());
            preStatement.setLong(3, eventDataRecord.getId());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public boolean checkEventData(long channelId) {
        this.open();
        PreparedStatement preStatement = null;
        boolean check = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + EventChannelParameters.S_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_EVENT_CHANNEL.getParam() + " WHERE "
                    + EventChannelParameters.VC_ID.getParam() + " = ?) AS EVENT_CHECK";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, channelId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                check = rs.getBoolean("EVENT_CHECK");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return check;
    }

    public long getEventTextChannelData(long channelId) {
        this.open();
        PreparedStatement preStatement = null;
        long text_chanel_id = 0;
        try {
            String sql = "SELECT " + EventChannelParameters.TC_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_EVENT_CHANNEL.getParam()
                    + " WHERE " + EventChannelParameters.VC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, channelId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                text_chanel_id = rs.getLong(EventChannelParameters.TC_ID.getParam());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return text_chanel_id;
    }

    public void setEventMentionMessage(EventDataRecord eventDataRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " SET "
                    + EventDataParameters.M_M_L.getParam() + " = ?"
                    + " WHERE " + EventDataParameters.S_ID.getParam() + " = ? AND "
                    + EventDataParameters.E_E.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, eventDataRecord.getMentionMessageLink());
            preStatement.setLong(2, eventDataRecord.getServerId());
            preStatement.setLong(3, eventDataRecord.getId());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void deleteEventChannel(EventDataRecord eventDataRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "DELETE FROM " + DAOParameters.TABLE_EVENT_CHANNEL.getParam()
                    + " WHERE " + EventChannelParameters.VC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, eventDataRecord.getVoiceChannelId());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateEventNextStartTime(EventDataRecord eventDataRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " SET "
                    + EventDataParameters.N_ST.getParam() + " = ?"
                    + " WHERE " + EventDataParameters.S_ID.getParam() + " = ? AND "
                    + EventDataParameters.ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(eventDataRecord.getNextStartTime()));
            preStatement.setLong(2, eventDataRecord.getServerId());
            preStatement.setLong(3, eventDataRecord.getId());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }
}
