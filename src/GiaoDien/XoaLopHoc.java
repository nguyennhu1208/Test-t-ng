package GiaoDien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class XoaLopHoc extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtUrl;
    private JTextField txtExcel;
    private JButton btnBrowse;
    private JButton btnRun;
    private DefaultTableModel tableModel;
    private JProgressBar progressBar;
    private JScrollPane scrollPane;

    public XoaLopHoc() {
        setTitle("TEST TỰ ĐỘNG - XÓA LỚP HỌC");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setBounds(50, 50, 900, 500);

        initUI();
    }

    private void initUI() {
        getContentPane().setLayout(new BorderLayout(10, 10));

        // ===== PANEL INPUT =====
        JPanel pnlTop = new JPanel(new GridLayout(3, 1, 5, 5));

        txtUrl = new JTextField("https://test.psi.plt.pro.vn/login");

        txtExcel = new JTextField();
        txtExcel.setEditable(false);

        btnBrowse = new JButton("Chọn file Excel");

        JPanel p1 = new JPanel(new BorderLayout(5, 5));
        p1.add(new JLabel("URL hệ thống:"), BorderLayout.WEST);
        p1.add(txtUrl, BorderLayout.CENTER);

        JPanel p2 = new JPanel(new BorderLayout(5, 5));
        p2.add(new JLabel("File Excel:"), BorderLayout.WEST);
        p2.add(txtExcel, BorderLayout.CENTER);
        p2.add(btnBrowse, BorderLayout.EAST);

        JPanel p3 = new JPanel();
        
                // ===== PROGRESS BAR =====
                progressBar = new JProgressBar(0, 100);
                p3.add(progressBar);
                progressBar.setStringPainted(true);

        pnlTop.add(p1);
        pnlTop.add(p2);
        pnlTop.add(p3);

        getContentPane().add(pnlTop, BorderLayout.NORTH);
        btnRun = new JButton("RUN TEST");
        getContentPane().add(btnRun, BorderLayout.SOUTH);
        
        scrollPane = new JScrollPane((Component) null);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        btnRun.addActionListener(e -> runTest());

        // ===== TABLE RESULT =====
        tableModel = new DefaultTableModel(
                new String[]{"STT", "Mã lớp", "Tên lớp", "Kết quả"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // ===== EVENTS =====
        btnBrowse.addActionListener(e -> chooseExcel());
    }

    // ===== CHỌN FILE EXCEL =====
    private void chooseExcel() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            txtExcel.setText(file.getAbsolutePath());
        }
    }

    // ===== RUN SELENIUM TEST =====
    private void runTest() {
        String url = txtUrl.getText().trim();
        String excelPath = txtExcel.getText().trim();

        if (url.isEmpty() || excelPath.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập URL và chọn file Excel!",
                    "Thiếu dữ liệu",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);
        progressBar.setValue(0);
        btnRun.setEnabled(false);

        new Thread(() -> {
            try {
                XoaLopHocService.runTest(
                        url,
                        excelPath,
                        new XoaLopHocService.LopHocCallback() {

                            @Override
                            public void onRowResult(int stt, String maLop,
                                                    String tenLop, String result) {
                                SwingUtilities.invokeLater(() -> {
                                    tableModel.addRow(new Object[]{
                                            stt, maLop, tenLop, result
                                    });
                                });
                            }

                            @Override
                            public void onProgress(int percent) {
                                SwingUtilities.invokeLater(() ->
                                        progressBar.setValue(percent));
                            }
                        }
                );

                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(XoaLopHoc.this,
                                "Hoàn thành test xóa lớp học!",
                                "DONE",
                                JOptionPane.INFORMATION_MESSAGE));

            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(XoaLopHoc.this,
                                "Lỗi khi chạy test:\n" + ex.getMessage(),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE));
            } finally {
                SwingUtilities.invokeLater(() -> btnRun.setEnabled(true));
            }
        }).start();
    }
}
