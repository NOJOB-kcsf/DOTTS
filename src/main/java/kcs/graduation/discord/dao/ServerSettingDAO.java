package kcs.graduation.discord.dao;

import kcs.graduation.discord.dao.core.BaseDAO;
import kcs.graduation.discord.parameter.DAOParameters;
import kcs.graduation.discord.parameter.ServerSettingParameters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServerSettingDAO extends BaseDAO {

    public void updateMentionChannel(long serverId, long mentionChannelId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " SET "
                    + ServerSettingParameters.M_C.getParam() + " = ?"
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, mentionChannelId);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateUnderCategory(long serverId, long id) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " SET "
                    + ServerSettingParameters.UNDER_C_ID.getParam() + " = ?"
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateFirstChannel(long serverId, long id) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " SET "
                    + ServerSettingParameters.FC_ID.getParam() + " = ?"
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            System.out.println(sql);
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateTempVoiceCategory(long serverId, long id) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " SET "
                    + ServerSettingParameters.T_VC_ID.getParam() + " = ?"
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateTempVoiceCreateBy(long serverId, boolean by) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " SET "
                    + ServerSettingParameters.T_BY.getParam() + " = ?"
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setBoolean(1, by);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateTempTextCategory(long serverId, long id) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " SET "
                    + ServerSettingParameters.T_TC_ID.getParam() + " = ?"
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateTempTextCreateBy(long serverId, boolean by) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " SET "
                    + ServerSettingParameters.T_TC_BY.getParam() + " = ?"
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setBoolean(1, by);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateNewCategoryCreateBy(long serverId, boolean by) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " SET "
                    + ServerSettingParameters.UNDER_C_BY.getParam() + " = ?"
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setBoolean(1, by);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateDefaultSize(long serverId, int size) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " SET "
                    + ServerSettingParameters.D_SIZE.getParam() + " = ?"
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setInt(1, size);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateDefaultName(long serverId, String name) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " SET "
                    + ServerSettingParameters.D_NAME.getParam() + " = ?"
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, name);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateTemplate(long serverId, String template) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_SETTING.getParam() + " SET "
                    + ServerSettingParameters.S_TYPE.getParam() + " = ?"
                    + " WHERE " + ServerSettingParameters.S_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, template);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }
}
