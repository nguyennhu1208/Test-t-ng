package GiaoDien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class XoaLopHoc extends JInternalFrame {

    private JTextField txtUrl;
    private JTextField txtExcel;
    private JTable table;
    private DefaultTableModel model;
    private JProgressBar progressBar;
    private JButton btnRun;

    public XoaLopHoc() {

        setTitle("TEST TỰ ĐỘNG - XÓA LỚP HỌC");
        setClosable(true);
        setResizable(true);
        setMaximizable(true);
        setSize(900, 500);
        setLayout(new BorderLayout(10, 10));

        // ===== TOP =====
        JPanel top = new JPanel(new GridLayout(3, 1, 5, 5));

        JPanel pUrl = new JPanel(new BorderLayout(5, 5));
        pUrl.add(new JLabel("Link web:"), BorderLayout.WEST);
        txtUrl = new JTextField("https://test.psi.plt.pro.vn/login");
        pUrl.add(txtUrl, BorderLayout.CENTER);

        JPanel pExcel = new JPanel(new BorderLayout(5, 5));
        pExcel.add(new JLabel("File Excel:"), BorderLayout.WEST);
        txtExcel = new JTextField();
        txtExcel.setEditable(false);
        JButton btnBrowse = new JButton("Chọn file");
        pExcel.add(txtExcel, BorderLayout.CENTER);
        pExcel.add(btnBrowse, BorderLayout.EAST);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        top.add(pUrl);
        top.add(pExcel);
        top.add(progressBar);

        add(top, BorderLayout.NORTH);

        // ===== TABLE =====
        model = new DefaultTableModel(
                new Object[]{"STT", "Mã lớp", "Tên lớp", "Kết quả"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== BUTTON =====
        JPanel bottom = new JPanel();
        btnRun = new JButton("Run Test");
        bottom.add(btnRun);
        add(bottom, BorderLayout.SOUTH);

        // ===== ACTION =====
        btnBrowse.addActionListener(e -> chooseExcel());
        btnRun.addActionListener(e -> runTest());
    }

    // ===== CHỌN FILE EXCEL =====
    private void chooseExcel() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel Files (*.xls, *.xlsx)", "xls", "xlsx"
        ));
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            txtExcel.setText(file.getAbsolutePath());
        }
    }

    // ===== RUN TEST (GIỮ NGUYÊN LOGIC) =====
    private void runTest() {

        String url = txtUrl.getText().trim();
        String excel = txtExcel.getText().trim();

        if (url.isEmpty() || excel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin");
            return;
        }

        model.setRowCount(0);
        progressBar.setValue(0);
        btnRun.setEnabled(false);

        SwingWorker<Void, Object[]> worker = new SwingWorker<Void, Object[]>() {

            @Override
            protected Void doInBackground() throws Exception {

                XoaLopHocService.runTest(url, excel,
                        new XoaLopHocService.LopHocCallback() {

                            @Override
                            public void onRowResult(int stt, String maLop,
                                                    String tenLop, String result) {
                                publish(new Object[]{stt, maLop, tenLop, result});
                            }

                            @Override
                            public void onProgress(int percent) {
                                setProgress(percent);
                            }
                        });
                return null;
            }

            @Override
            protected void process(List<Object[]> chunks) {
                for (Object[] row : chunks) {
                    model.addRow(row);
                }
            }

            @Override
            protected void done() {
                btnRun.setEnabled(true);
                progressBar.setValue(100);
                JOptionPane.showMessageDialog(XoaLopHoc.this,
                        "Test xóa lớp học hoàn thành!");
            }
        };

        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((Integer) evt.getNewValue());
            }
        });

        worker.execute();
    }
}
