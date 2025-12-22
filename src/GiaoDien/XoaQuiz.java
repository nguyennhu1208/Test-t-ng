package GiaoDien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class XoaQuiz extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    
    JTextField txtExcel = new JTextField();
    JTextField txtUrl = new JTextField("https://test.psi.plt.pro.vn/admin/quiz-management");
    JTextArea log = new JTextArea();

    DefaultTableModel model = new DefaultTableModel(
        new String[]{"STT", "Tên bộ câu hỏi", "Trạng thái"}, 0
    );

    JTable table = new JTable(model);

    public XoaQuiz() {
        setTitle("Tool Auto Xóa Bộ Câu Hỏi");
        setSize(850, 600);
      
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel top = new JPanel(new GridLayout(3, 1, 5, 5));

        top.add(row("File Excel:", txtExcel, btnExcel()));
        top.add(row("Link web:", txtUrl, null));

        JButton btnRun = new JButton("RUN KIỂM TRA & XÓA");
        btnRun.addActionListener(e -> runTool());
        top.add(btnRun);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        log.setEditable(false);
        log.setRows(6);
        add(new JScrollPane(log), BorderLayout.SOUTH);
    }

    JPanel row(String label, JTextField txt, JButton btn) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.add(new JLabel(label), BorderLayout.WEST);
        p.add(txt, BorderLayout.CENTER);
        if (btn != null) p.add(btn, BorderLayout.EAST);
        return p;
    }

    JButton btnExcel() {
        JButton b = new JButton("Chọn");
        b.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel (*.xls, *.xlsx)", "xls", "xlsx"
            ));
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                txtExcel.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });
        return b;
    }
    
    void runTool() {
        try {
            // Kiểm tra link web
            String url = txtUrl.getText().trim();
            if (url.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập link web trước khi chạy!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra file Excel
            String excelPath = txtExcel.getText().trim();
            if (excelPath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn file Excel trước khi chạy!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Đọc danh sách quiz từ Excel
            List<String> quizzes = ExcelHelperXoaQuiz.readQuizNames(excelPath);

            if (quizzes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "File Excel không có quiz nào!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Reset bảng
            model.setRowCount(0);
            int stt = 1;
            for (String q : quizzes) {
                model.addRow(new Object[]{stt++, q, "Chưa kiểm tra"});
            }

            // Chạy Selenium
            new SeleniumXoaQuiz(model, log).run(quizzes, url);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }



    public static void main(String[] args) {
        new XoaQuiz().setVisible(true);
    }
}
