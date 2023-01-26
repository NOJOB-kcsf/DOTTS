package kcs.graduation.discord.dao;

import kcs.graduation.discord.dao.core.BaseDAO;
import kcs.graduation.discord.parameter.DAOParameters;
import kcs.graduation.discord.parameter.EventDataParameters;
import kcs.graduation.discord.parameter.NamePresetParameters;
import kcs.graduation.discord.record.EventDataRecord;
import kcs.graduation.discord.record.NamePresetRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeleteSelectSystemDAO extends BaseDAO {
    public void deleteNamePreset(ArrayList<NamePresetRecord> namePresetRecords) {
        this.open();
        PreparedStatement preStatement = null;
        long serverId = namePresetRecords.get(0).getServer_id();
        String nameQ = "?" + ",?".repeat(Math.max(0, namePresetRecords.size() - 1));
        try {
            String sql = "DELETE FROM " + DAOParameters.TABLE_NAME_PRESET.getParam() + " WHERE " + NamePresetParameters.S_ID.getParam() + " = ? AND " + NamePresetParameters.NAME.getParam() + " IN (" + nameQ + ")";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            for (int i = 0; i < nameQ.length(); i++) {
                preStatement.setString(i + 2, namePresetRecords.get(i).getName());
            }
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public ArrayList<NamePresetRecord> getPresetName(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<NamePresetRecord> namePresetRecords = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + DAOParameters.TABLE_NAME_PRESET.getParam() + " WHERE " + NamePresetParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                NamePresetRecord record = new NamePresetRecord();
                record.setServer_id(rs.getLong(NamePresetParameters.S_ID.getParam()));
                record.setName(rs.getString(NamePresetParameters.NAME.getParam()));
                namePresetRecords.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return namePresetRecords;
    }

    public ArrayList<EventDataRecord> getEventData(long serverId, long start, int limit) {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<EventDataRecord> eventDataRecords = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " WHERE " + EventDataParameters.S_ID.getParam() + " = ? ORDER BY " + EventDataParameters.ID.getParam() + " LIMIT ?,?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            preStatement.setLong(2, start);
            preStatement.setInt(3, limit);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                EventDataRecord record = new EventDataRecord();
                record.setServerId(serverId);
                record.setId(rs.getInt(EventDataParameters.ID.getParam()));
                eventDataRecords.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return eventDataRecords;
    }

    public ArrayList<EventDataRecord> getEventData(long serverId, int limit) {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<EventDataRecord> eventDataRecords = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " WHERE " + EventDataParameters.S_ID.getParam() + " = ? ORDER BY " + EventDataParameters.ID.getParam() + " LIMIT ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            preStatement.setInt(2, limit);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                EventDataRecord record = new EventDataRecord();
                record.setServerId(serverId);
                record.setId(rs.getInt(EventDataParameters.ID.getParam()));
                eventDataRecords.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return eventDataRecords;
    }

    public long getEventAllDataCount(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        long count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM " + DAOParameters.TABLE_EVENT_SETTING.getParam() + " WHERE " + EventDataParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return count;
    }
}
