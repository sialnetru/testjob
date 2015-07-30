package test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 * Created by admin on 28.07.2015.
 */
public class GroupDAO {
    private String table = "sgroup";
    private String path;
    private Connection c=null;

    public GroupDAO(String path) {
        this.path=path;
    }

    public void getCon() throws Exception {
            Class.forName("org.hsqldb.jdbcDriver");
            c=DriverManager.getConnection("jdbc:hsqldb:file:"+path, "SA", "");
    }
    public void closeCon() throws SQLException {
        c.close();
        c=null;
    }
    public PreparedStatement getPS(String sql) throws Exception {
        getCon();
        return c.prepareStatement(sql);
    }
    public Statement getS() throws Exception {
        getCon();
        return c.createStatement();
    }
    public JTable getTable(){
        JTable t1 = new JTable();
        DefaultTableModel dm = new DefaultTableModel();
        try {
            Statement st = getS();
            ResultSet rs = st.executeQuery("select * from " + table);
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            String c[] = new String[cols];
            for (int i = 0; i < cols; i++) {
                c[i] = rsmd.getColumnName(i + 1);
                dm.addColumn(c[i]);
            }
            Object row[] = new Object[cols];
            while (rs.next()) {
                for (int i = 0; i < cols; i++) {
                    row[i] = rs.getString(i + 1);
                }
                dm.addRow(row);
            }
            st.close(); closeCon();
        } catch (Exception e) {
            e.printStackTrace();
        }
        t1.setModel(dm);
        return t1;
    }

    public boolean deleteGroup(long id)  {
        try {
            Statement st = getS();
            st.executeUpdate("DELETE FROM sgroup WHERE g_number=" + id);
            st.close(); closeCon();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertGroup(long id, String faculty){
        try {
            String sql = "INSERT INTO sgroup (g_number,g_faculty) VALUES (?,?)";
            PreparedStatement preparedStatement = getPS(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, faculty);
            preparedStatement.executeUpdate();
            preparedStatement.close(); closeCon();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateGroup(long id, long new_id, String new_faculty) {
        try {
        String sql = "UPDATE sgroup SET g_number=?,g_faculty=? WHERE g_number=?";
        PreparedStatement preparedStatement = getPS(sql);
        preparedStatement.setLong(1, new_id);
        preparedStatement.setString(2, new_faculty);
        preparedStatement.setLong(3, id);
        preparedStatement.executeUpdate();
        preparedStatement.close(); closeCon();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
