/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Models.Feedback;
import Utils.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author admin
 */
public class FeedbackReplyDAO extends DBContext {

    public boolean updateFeedbackReply(int replyID, String replyText) {
        String sql = "UPDATE FeedbackReplies SET ReplyText=? WHERE ReplyID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, replyText);
            ps.setInt(2, replyID);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFeedbackReply(int feedbackID) {
        String deleteReplies = "DELETE FROM FeedbackReplies WHERE ReplyID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps1 = conn.prepareStatement(deleteReplies);) {

            ps1.setInt(1, feedbackID);
            return ps1.executeUpdate() > 0; // Xóa phản hồi chính
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
//    public static void main(String[] args) {
//        FeedbackReplyDAO dao = new FeedbackReplyDAO();
//        boolean isDeleted = dao.deleteFeedbackReply(10);
//        System.out.println(isDeleted);
//        
//    }  
      
}
