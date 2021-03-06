/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanager;

import com.mysql.cj.jdbc.Blob;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import static java.sql.JDBCType.BLOB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author shubh
 */
public class DatabaseManager {
    
    private static DatabaseManager instance = null; 
  
    Connection conn;
    PreparedStatement pstmt;
  
    private DatabaseManager() 
    { 
        initDBConnection();
    } 
  
    // static method to create instance of Singleton class 
    public static DatabaseManager getInstance() 
    { 
        if (instance == null) 
            instance = new DatabaseManager(); 
  
        return instance; 
    }
    
    private void initDBConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/inventory_manager", "root", "root");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    boolean RegisterUser(String fname, String lname, String email, String pass, String mobile, int role, int status, String gender, String address, String salutation) throws SQLException {
        pstmt = conn.prepareStatement("INSERT INTO user(fname, lname, email, password, mobile, role, status, gender, address, salutation) VALUES(?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, fname);
            pstmt.setString(2, lname);
            pstmt.setString(3, email);
            pstmt.setString(4, pass);
            pstmt.setString(5, mobile);
            pstmt.setInt(6, role);
            pstmt.setInt(7, status);
            pstmt.setString(8, gender);
            pstmt.setString(9, address);
            pstmt.setString(10, salutation);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
    }
    
    boolean UpdateUserData(String fname, String lname, String email, String mobile, String gender, String address, String salutation) throws SQLException {
        pstmt = conn.prepareStatement("UPDATE user set fname = ?, lname = ?, email = ?, mobile = ?, gender = ?, address = ?, salutation = ? WHERE iduser = ?");
            pstmt.setString(1, fname);
            pstmt.setString(2, lname);
            pstmt.setString(3, email);
            pstmt.setString(4, mobile);
            pstmt.setString(5, gender);
            pstmt.setString(6, address);
            pstmt.setString(7, salutation);
            pstmt.setString(8, Profile.getId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
    }

    boolean emailExists(String email) throws SQLException {
        pstmt = conn.prepareStatement("SELECT iduser FROM user WHERE email = ?");
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }

    boolean LogniUser(String email, String pass) throws SQLException {
        pstmt = conn.prepareStatement("SELECT iduser FROM user WHERE email = ? AND password = ?");
        pstmt.setString(1, email);
        pstmt.setString(2, pass);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            Profile.setId(rs.getString("iduser"));
            return true;
        }
        return false;
    }

    boolean loadProfile() throws SQLException {
        pstmt = conn.prepareStatement("SELECT * FROM user WHERE iduser = ?");
        pstmt.setString(1, Profile.getId());
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            Profile.setAll(rs.getString("iduser"), rs.getString("salutation"), rs.getString("fname"), rs.getString("lname"), rs.getString("email"), rs.getString("mobile"), rs.getString("gender"), rs.getInt("role"), rs.getString("address"));
            return true;
        }else{
            throw new SQLException();
        }
    }

    boolean CheckPass(String currentPass) throws SQLException {
        pstmt = conn.prepareStatement("SELECT password FROM user WHERE iduser = ?");
        pstmt.setString(1, Profile.getId());
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getString("password").equals(currentPass);
    }

    boolean UpdateUserPassword(String newPass) throws SQLException {
        pstmt = conn.prepareStatement("UPDATE user set password = ? WHERE iduser = ?");
            pstmt.setString(1, newPass);
            pstmt.setString(2, Profile.getId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
    }

    boolean AddItem(String name, String category, String unit, InputStream image, int quantity, int unitprice, String status) throws SQLException {
        String query;
        image = null;
        if(image == null){
            query = "INSERT INTO `item` (uid, name, category_id, unit_id, quantity, unit_price, status) VALUES (?,?,?,?,?,?,?)";
        }else
            query = "INSERT INTO `item` (uid, name, category_id, unit_id, image, quantity, unit_price, status) VALUES (?,?,?,?,?,?,?,?)";
        pstmt = conn.prepareStatement(query);
        pstmt.setString(1, Profile.getId());
        pstmt.setString(2, name);
        pstmt.setString(3, category);
        pstmt.setString(4, unit);
        if(image == null){
            pstmt.setInt(5, quantity);
            pstmt.setInt(6, unitprice);
            pstmt.setString(7, status);
        }else{
            pstmt.setBlob(5, image);
            pstmt.setInt(6, quantity);
            pstmt.setInt(7, unitprice);
            pstmt.setString(8, status);
        }
        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
    }

    Vector GetMyItems() throws SQLException {
        pstmt = conn.prepareStatement("SELECT * FROM item WHERE uid = ?");
        pstmt.setString(1, Profile.getId());
        ResultSet rs = pstmt.executeQuery();
        Vector<Vector<String>> vs = new Vector<>();
        
        while(rs.next()){
            Vector<String> items = new Vector<>();
            items.add(rs.getString("iditem"));
            items.add(rs.getString("name"));
            items.add(rs.getString("category_id"));
            items.add(""+rs.getInt("quantity"));
            items.add(rs.getString("unit_id"));
            items.add(rs.getString("unit_price"));
            items.add(rs.getString("status"));
            items.add("image");
            
            vs.add(items);
        }
        return vs;
    }

    boolean UpdateItem(int selectedItemId, String name, String category, String unit, InputStream image, int quantity, int unitprice, String status) throws SQLException {
        String query;
        image = null;
        if(image == null){
            query = "UPDATE `item` SET uid=?, `name` = ?, `category_id` = ?, `unit_id` = ?, `quantity` = ?, `unit_price` = ?, `status` = ? WHERE (`iditem` = ?)";
            //query = "UPADTE `item` SET uid=?, name=?, category_id=?, unit_id=?, quantity=?, unit_price=?, status=? where iditem=?";
            System.out.print(query);
        }else
            query = "UPADTE `item` set uid=?, name=?, category_id=?, unit_id=?, image=?, quantity=?, unit_price=?, status=? where iditem=?";
        System.out.print("q2: "+query);
        pstmt = conn.prepareStatement(query);
        pstmt.setString(1, Profile.getId());
        pstmt.setString(2, name);
        pstmt.setString(3, category);
        pstmt.setString(4, unit);
        if(image == null){
            pstmt.setInt(5, quantity);
            pstmt.setInt(6, unitprice);
            pstmt.setString(7, status);
            pstmt.setInt(8, selectedItemId);
        }else{
            pstmt.setBlob(5, image);
            pstmt.setInt(6, quantity);
            pstmt.setInt(7, unitprice);
            pstmt.setString(8, status);
            pstmt.setInt(9, selectedItemId);
        }
        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
    }
    
}
