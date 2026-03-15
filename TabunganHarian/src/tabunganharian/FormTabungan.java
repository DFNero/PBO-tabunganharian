package tabunganharian;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.table.DefaultTableCellRenderer;

public class FormTabungan extends JFrame {

    private JTextField txtId      = new JTextField(10);
    private JTextField txtTanggal = new JTextField(10);
    private JTextField txtSumber  = new JTextField(10);
    private JTextField txtNominal = new JTextField(10);

    private DefaultTableModel tableModel;
    private JTable tblTabungan;

    private TabunganController controller = new TabunganController();

    public FormTabungan() {
        setTitle("Tabungan Harian");
        setSize(750, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 2));
        form.add(new JLabel("ID (otomatis):")); form.add(txtId);
        form.add(new JLabel("Tanggal (dd-MM-yyyy):")); form.add(txtTanggal);
        form.add(new JLabel("Sumber:")); form.add(txtSumber);
        form.add(new JLabel("Nominal:")); form.add(txtNominal);

        txtId.setEditable(false);

        // tombol 
        JButton btnSimpan = new JButton("Simpan");
        JButton btnUbah   = new JButton("Ubah");
        JButton btnHapus  = new JButton("Hapus");
        JButton btnReset  = new JButton("Reset");

        JPanel tombol = new JPanel(); 
        tombol.add(btnReset);
        tombol.add(btnUbah);
        tombol.add(btnHapus);
        tombol.add(btnSimpan);
        form.add(tombol);

        // --- Tabel ---
        tableModel = new DefaultTableModel(new Object[]{"ID", "Tanggal", "Sumber", "Nominal"}, 0);
        tblTabungan = new JTable(tableModel);

        // format kolom Nominal (kolom index 3) jadi "100.000"
        tblTabungan.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id", "ID"));
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Integer) {
                    value = nf.format(value); // angka diformat jadi "100.000"
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        // --- Susun ke Frame ---
        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tblTabungan), BorderLayout.CENTER);

        // --- Event tombol ---
        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnReset.addActionListener(e -> reset());

        // Klik baris tabel -> isi form
        tblTabungan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) isiFormDariTabel();
        });

        loadData();
    }

    // CREATE
    private void simpan() {
        try {
            controller.insert(txtTanggal.getText(), txtSumber.getText(), Integer.parseInt(txtNominal.getText()));
            JOptionPane.showMessageDialog(this, "Data disimpan.");
            loadData(); reset();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // UPDATE
    private void ubah() {
        try {
            controller.update(Integer.parseInt(txtId.getText()), txtTanggal.getText(), txtSumber.getText(), Integer.parseInt(txtNominal.getText()));
            JOptionPane.showMessageDialog(this, "Data diubah.");
            loadData(); reset();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // DELETE
    private void hapus() {
        try {
            controller.delete(Integer.parseInt(txtId.getText()));
            JOptionPane.showMessageDialog(this, "Data dihapus.");
            loadData(); reset();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // RESET form
    private void reset() {
        txtId.setText(""); txtTanggal.setText(""); txtSumber.setText(""); txtNominal.setText("");
        tblTabungan.clearSelection();
    }

    // READ - load semua data ke tabel
    private void loadData() {
        try {
            controller.loadAll(tableModel);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // Klik baris -> isi field form
    private void isiFormDariTabel() {
        int row = tblTabungan.getSelectedRow();
        if (row == -1) return;
        txtId.setText(tableModel.getValueAt(row, 0).toString());
        txtTanggal.setText(tableModel.getValueAt(row, 1).toString());
        txtSumber.setText(tableModel.getValueAt(row, 2).toString());
        txtNominal.setText(tableModel.getValueAt(row, 3).toString());
    }
}