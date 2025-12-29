package GiaoDien;

import java.awt.*;
import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;

public class ThemQuiz extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtExcel;

    // ===== THÔNG TIN BỘ QUIZ =====
    private JTextField txtTenBo;
    private JTextArea txtMoTa;
    private JTextField txtThoiGian;
    private JTextField txtSoCau;

    private JTable table;
    private DefaultTableModel model;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ThemQuiz frame = new ThemQuiz();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ThemQuiz() {
        setTitle("TEST TỰ ĐỘNG - THÊM BỘ CÂU HỎI");
        setBounds(100, 100, 900, 560);
        setClosable(true);
        setLayout(new BorderLayout(8, 8));

        /* ================= FILE EXCEL ================= */
        JPanel filePanel = new JPanel(new BorderLayout(5, 5));
        filePanel.setBorder(BorderFactory.createTitledBorder("File Excel"));

        txtExcel = new JTextField();
        JButton btnChoose = new JButton("Chọn file");

        filePanel.add(txtExcel, BorderLayout.CENTER);
        filePanel.add(btnChoose, BorderLayout.EAST);

        /* ================= THÔNG TIN QUIZ ================= */
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin bộ câu hỏi"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTenBo = new JTextField();
        txtMoTa = new JTextArea(3, 20);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtThoiGian = new JTextField();
        txtSoCau = new JTextField();

        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Tên bộ câu hỏi:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(txtTenBo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JScrollPane(txtMoTa), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Thời gian (phút):"), gbc);
        gbc.gridx = 1;
        infoPanel.add(txtThoiGian, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Số lượng câu hỏi:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(txtSoCau, gbc);

        /* ================= GOM PHẦN TRÊN ================= */
        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        northPanel.add(filePanel, BorderLayout.NORTH);
        northPanel.add(infoPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);

        /* ================= TABLE ================= */
        model = new DefaultTableModel(
                new String[]{"STT", "Nội dung", "A", "B", "C", "D", "Đáp án"}, 0);

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        /* ================= RUN ================= */
        JButton btnRun = new JButton("Run Test");
        JPanel south = new JPanel();
        south.add(btnRun);
        add(south, BorderLayout.SOUTH);

        /* ================= EVENTS ================= */
        btnChoose.addActionListener(e -> chooseExcel());
        btnRun.addActionListener(e -> runTest());
    }

    /* ================= EVENTS ================= */

    private void chooseExcel() {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel Files (*.xls, *.xlsx)", "xls", "xlsx"
        ));
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            String path = chooser.getSelectedFile().getAbsolutePath();
            txtExcel.setText(path);

            ExcelQuizReader.read(path, model);

            // 👉 ĐỔ QUIZ INFO TỪ EXCEL LÊN UI (NẾU CÓ)
            txtTenBo.setText(ExcelQuizReader.quizInfo.getOrDefault("TEN_BO", ""));
            txtMoTa.setText(ExcelQuizReader.quizInfo.getOrDefault("MO_TA", ""));
            txtThoiGian.setText(ExcelQuizReader.quizInfo.getOrDefault("THOI_GIAN", ""));
            txtSoCau.setText(ExcelQuizReader.quizInfo.getOrDefault("SO_CAU", ""));
        }
    }

    private void runTest() {

        // Validate file
        if (txtExcel.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn file Excel!",
                    "Thiếu file", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ExcelQuizReader.questions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Danh sách câu hỏi trống!",
                    "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate info
        if (txtTenBo.getText().trim().isEmpty()
                || txtThoiGian.getText().trim().isEmpty()
                || txtSoCau.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ thông tin bộ câu hỏi!",
                    "Thiếu thông tin", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 👉 UI LÀ NGUỒN CUỐI
        ExcelQuizReader.quizInfo.put("TEN_BO", txtTenBo.getText().trim());
        ExcelQuizReader.quizInfo.put("MO_TA", txtMoTa.getText().trim());
        ExcelQuizReader.quizInfo.put("THOI_GIAN", txtThoiGian.getText().trim());
        ExcelQuizReader.quizInfo.put("SO_CAU", txtSoCau.getText().trim());

        // RUN
    


        SeleniumCreateQuiz.run(
            ExcelQuizReader.quizInfo,
            ExcelQuizReader.questions
        );

    }
}
