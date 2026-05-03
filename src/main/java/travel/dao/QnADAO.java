package travel.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import travel.DBConnection;
import travel.model.QnA;

public class QnADAO {

    private QnA mapRow(ResultSet rs) throws SQLException {
        QnA q = new QnA();
        q.setQuestionID(rs.getInt("questionID"));
        q.setCustomerID(rs.getInt("customerID"));
        int empID = rs.getInt("employeeID");
        q.setEmployeeID(rs.wasNull() ? -1 : empID);
        q.setQuestion(rs.getString("question"));
        q.setAnswer(rs.getString("answer"));
        return q;
    }

    public List<QnA> findAll() {
        String sql = "SELECT * FROM QnA";
        List<QnA> results = new ArrayList<>();
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public List<QnA> findByCustomer(int customerID) {
        String sql = "SELECT * FROM QnA WHERE customerID = ?";
        List<QnA> results = new ArrayList<>();
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public List<QnA> searchByKeyword(String keyword) {
        String sql = "SELECT * FROM QnA WHERE question LIKE ? OR answer LIKE ?";
        List<QnA> results = new ArrayList<>();
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public List<QnA> findUnanswered() {
        String sql = "SELECT * FROM QnA WHERE answer IS NULL";
        List<QnA> results = new ArrayList<>();
        try (Connection conn = DBConnection.get();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) 
                results.add(mapRow(rs));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        return results;
    }

    public void insert(QnA q) {
        String sql = "INSERT INTO QnA (customerID, question) VALUES (?, ?)";
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, q.getCustomerID());
            ps.setString(2, q.getQuestion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void answer(int questionID, int employeeID, String answer) {
        String sql = "UPDATE QnA SET answer = ?, employeeID = ? WHERE questionID = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, answer);
            ps.setInt(2, employeeID);
            ps.setInt(3, questionID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
