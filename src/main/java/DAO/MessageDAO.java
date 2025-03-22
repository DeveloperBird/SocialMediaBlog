package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.*;

public class MessageDAO {

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM message";

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"), rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));

                messages.add(message);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public List<Message> getAllMessagesFromAccount(int id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch")));
            }

            return messages;
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if(rs.next()){
                int genMsgId = rs.getInt(1);

                return new Message(genMsgId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message updateMessage(int id, String message_text){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, message_text);
            ps.setInt(2, id);
            
            int rowsAffected = ps.executeUpdate();

            if(rowsAffected > 0){
                return getMessageById(id);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new Message(id, rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message deleteMessage(int id){
        Message messageToDelete = getMessageById(id);
        if (messageToDelete == null) {
            return null;
        }

        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                return messageToDelete;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


}
