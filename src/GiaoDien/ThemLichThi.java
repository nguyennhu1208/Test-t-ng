package GiaoDien;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ThemLichThi extends JInternalFrame {

    private JTextField txtExcel;

    private JTextField txtTenDe;
    private JTextField txtMaDe;
    private JTextField txtLop;
    private JTextField txtBatDau;
    private JTextField txtKetThuc;
    private JTextField txtSoLan;

    private JCheckBox chkXaoCau;
    private JCheckBox chkXaoDapAn;

    private JProgressBar progressBar;
    private JButton btnRun;

    public ThemLichThi() {

        setTitle("TEST TỰ ĐỘNG - TẠO LỊCH THI");
        setBounds(100, 100, 650, 480);
        setClosable(true);
        setLayout(new BorderLayout(8, 8));

        /* ================= FILE EXCEL ================= */
        JPanel filePanel = new JPanel(new BorderLayout(5, 5));
        filePanel.setBorder(BorderFactory.createTitledBorder("File Excel"));

        txtExcel = new JTextField();
        JButton btnChoose = new JButton("Chọn file");

        filePanel.add(txtExcel, BorderLayout.CENTER);
        filePanel.add(btnChoose, BorderLayout.EAST);

        add(filePanel, BorderLayout.NORTH);

        /* ================= FORM ================= */
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin lịch thi"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtTenDe = new JTextField();
        txtMaDe = new JTextField();
        txtLop = new JTextField();
        txtBatDau = new JTextField();
        txtKetThuc = new JTextField();
        txtSoLan = new JTextField();

        chkXaoCau = new JCheckBox("Xáo trộn câu hỏi");
        chkXaoDapAn = new JCheckBox("Xáo trộn đáp án");

        int y = 0;
        addRow(panel, gbc, y++, "Tên đề thi:", txtTenDe);
        addRow(panel, gbc, y++, "Mã đề thi:", txtMaDe);
        addRow(panel, gbc, y++, "Lớp thi:", txtLop);
        addRow(panel, gbc, y++, "Bắt đầu:", txtBatDau);
        addRow(panel, gbc, y++, "Kết thúc:", txtKetThuc);
        addRow(panel, gbc, y++, "Số lần làm:", txtSoLan);

        gbc.gridx = 1;
        gbc.gridy = y++;
        panel.add(chkXaoCau, gbc);

        gbc.gridy = y++;
        panel.add(chkXaoDapAn, gbc);

        add(panel, BorderLayout.CENTER);

        /* ================= BOTTOM ================= */
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        btnRun = new JButton("Run Test");

        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.add(progressBar, BorderLayout.CENTER);
        south.add(btnRun, BorderLayout.EAST);

        add(south, BorderLayout.SOUTH);

        /* ================= EVENTS ================= */
        btnChoose.addActionListener(e -> chooseExcel());
        btnRun.addActionListener(e -> runTest());
    }

    /* ================= HELPER ================= */
    private void addRow(JPanel panel, GridBagConstraints gbc, int y,
                        String label, JComponent field) {

        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    /* ================= CHOOSE FILE ================= */
    private void chooseExcel() {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel Files (*.xls, *.xlsx)", "xls", "xlsx"
        ));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            txtExcel.setText(file.getAbsolutePath());
        }
    }

    /* ================= RUN ================= */
    private void runTest() {

        if (txtExcel.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn file Excel!",
                    "Thiếu file", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Map<String, String> info = new HashMap<>();
        info.put("EXCEL", txtExcel.getText().trim());
        info.put("TEN_DE_THI", txtTenDe.getText().trim());
        info.put("MA_DE", txtMaDe.getText().trim());
        info.put("LOP_THI", txtLop.getText().trim());
        info.put("BAT_DAU", txtBatDau.getText().trim());
        info.put("KET_THUC", txtKetThuc.getText().trim());
        info.put("SO_LAN", txtSoLan.getText().trim());
        info.put("XAO_CAU", String.valueOf(chkXaoCau.isSelected()));
        info.put("XAO_DAP_AN", String.valueOf(chkXaoDapAn.isSelected()));

        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        btnRun.setEnabled(false);

        new Thread(() -> {
            try {
                ThemLichThiService.run(info);
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this,
                                "Tạo lịch thi thành công!",
                                "Hoàn tất", JOptionPane.INFORMATION_MESSAGE));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this,
                                ex.getMessage(),
                                "Lỗi", JOptionPane.ERROR_MESSAGE));
            } finally {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setIndeterminate(false);
                    progressBar.setVisible(false);
                    btnRun.setEnabled(true);
                });
            }
        }).start();
    }
}
