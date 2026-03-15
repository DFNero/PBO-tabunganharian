package tabunganharian;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TabunganController {
    
    
    // Simpan satu record baru
    public boolean insert(String tanggal, String sumber, int nominal) throws SQLException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date parsed = sdf.parse(tanggal);
        
        String sql = "INSERT INTO tabungan_harian (tanggal, sumber, nominal) VALUES (?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new Date(parsed.getTime()));
            ps.setString(2, sumber);
            ps.setInt(3, nominal);
            return ps.executeUpdate() > 0;
        }
    }

    // Ubah record berdasarkan id
    public boolean update(int id, String tanggal, String sumber, int nominal) throws SQLException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date parsed = sdf.parse(tanggal);
        
        String sql = "UPDATE tabungan_harian SET tanggal=?, sumber=?, nominal=? WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new Date(parsed.getTime()));
            ps.setString(2, sumber);
            ps.setInt(3, nominal);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Hapus record berdasarkan id
    public boolean delete(int id) throws SQLException {
        String sql = "delete from tabungan_harian WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Load semua data ke DefaultTableModel
    public void loadAll(DefaultTableModel model) throws SQLException {
        model.setRowCount(0);
        String sql = "select *from tabungan_harian order by id";
        try (Connection conn = Koneksi.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getDate("tanggal") != null ? new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("tanggal")) : "",  // <-- ini
                    rs.getString("sumber"),
                    rs.getInt("nominal")
                });
            }
        }
    }
}