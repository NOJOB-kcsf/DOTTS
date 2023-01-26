package kcs.graduation.discord.dao;

import kcs.graduation.discord.dao.core.BaseDAO;
import kcs.graduation.discord.parameter.DAOParameters;
import kcs.graduation.discord.parameter.StaticChannelParameters;
import kcs.graduation.discord.record.StaticChannelRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaticChannelDAO extends BaseDAO {

    public void addStaticChannelLink(StaticChannelRecord record) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_STATIC_CHANNEL.getParam() + " VALUES (?,?,?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, record.getServer_id());
            preStatement.setLong(2, record.getVoice_channel_id());
            preStatement.setLong(3, record.getText_channel_id());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public boolean checkStaticData(long channelId) {
        this.open();
        PreparedStatement preStatement = null;
        boolean check = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + StaticChannelParameters.S_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_STATIC_CHANNEL.getParam() + " WHERE "
                    + StaticChannelParameters.VC_ID.getParam() + " = ?) AS STATIC_CHECK";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, channelId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                check = rs.getBoolean("STATIC_CHECK");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return check;
    }

    public long getStaticChannelData(long voice_channel_id) {
        this.open();
        PreparedStatement preStatement = null;
        long text_chanel_id = 0;
        try {
            String sql = "SELECT " + StaticChannelParameters.TC_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_STATIC_CHANNEL.getParam()
                    + " WHERE " + StaticChannelParameters.VC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, voice_channel_id);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                text_chanel_id = rs.getLong(StaticChannelParameters.TC_ID.getParam());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return text_chanel_id;
    }

    public void updateStaticTextChannel(long id, long serverTextChannelId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_STATIC_CHANNEL.getParam() + " SET "
                    + StaticChannelParameters.TC_ID.getParam() + " = ?"
                    + " WHERE " + StaticChannelParameters.TC_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverTextChannelId);
            preStatement.setLong(2, id);
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }
}
