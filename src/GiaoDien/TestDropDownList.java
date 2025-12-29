package GiaoDien;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TestDropDownList extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private JTable table;
    private JTextField txtUrl;
    private JTextArea txtExpected;
    private JProgressBar progressBar;
    private JButton btnRun;

    // Text mặc định (placeholder)
    private static final String DEFAULT_URL_TEXT = "Nhập link web cần kiểm tra dropdown";
    private static final String DEFAULT_EXPECTED_TEXT = "Mỗi dòng là 1 giá trị expected";

    public TestDropDownList() {

        setTitle("Test Dropdown List");
        setClosable(true);
        setResizable(true);
        setMaximizable(true);
        setIconifiable(false);
        setSize(800, 550);
        setLocation(50, 50);
        setVisible(true);

        getContentPane().setLayout(null);

        // ===== URL =====
        JLabel lblUrl = new JLabel("URL:");
        lblUrl.setBounds(10, 10, 50, 25);
        getContentPane().add(lblUrl);

        txtUrl = new JTextField();
        txtUrl.setBounds(60, 10, 700, 25);
        addPlaceholder(txtUrl, DEFAULT_URL_TEXT);
        getContentPane().add(txtUrl);

        // ===== EXPECTED DATA =====
        JLabel lblExpected = new JLabel("Expected Data (mỗi dòng 1 giá trị):");
        lblExpected.setBounds(10, 45, 300, 25);
        getContentPane().add(lblExpected);

        txtExpected = new JTextArea();
        JScrollPane expectedScroll = new JScrollPane(txtExpected);
        expectedScroll.setBounds(10, 75, 350, 150);
        addPlaceholder(txtExpected, DEFAULT_EXPECTED_TEXT);
        getContentPane().add(expectedScroll);

        // ===== RUN BUTTON =====
        btnRun = new JButton("Run Test");
        btnRun.setBounds(380, 75, 120, 30);
        getContentPane().add(btnRun);

        // ===== PROGRESS BAR =====
        progressBar = new JProgressBar();
        progressBar.setBounds(520, 75, 250, 30);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        getContentPane().add(progressBar);

        // ===== TABLE =====
        JScrollPane tableScroll = new JScrollPane();
        tableScroll.setBounds(10, 240, 760, 260);
        table = new JTable();
        tableScroll.setViewportView(table);
        getContentPane().add(tableScroll);

        // ===== ACTION =====
        btnRun.addActionListener(e -> startTest());
    }

    // ================= PLACEHOLDER CHO TEXTFIELD =================
    private void addPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    // ================= PLACEHOLDER CHO TEXTAREA =================
    private void addPlaceholder(JTextArea area, String placeholder) {
        area.setText(placeholder);
        area.setForeground(Color.GRAY);

        area.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (area.getText().equals(placeholder)) {
                    area.setText("");
                    area.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (area.getText().trim().isEmpty()) {
                    area.setText(placeholder);
                    area.setForeground(Color.GRAY);
                }
            }
        });
    }

    // ================= START TEST =================
    private void startTest() {

        String url = txtUrl.getText().trim();
        String expectedText = txtExpected.getText().trim();

        // Validate dữ liệu nhập
        if (url.isEmpty() || url.equals(DEFAULT_URL_TEXT)
                || expectedText.isEmpty() || expectedText.equals(DEFAULT_EXPECTED_TEXT)) {

            JOptionPane.showMessageDialog(
                    this,
                    "❌ Vui lòng nhập URL và dữ liệu Expected",
                    "Thiếu dữ liệu",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        btnRun.setEnabled(false);

        // ===== GIAI ĐOẠN 1: LOAD DỮ LIỆU (INDETERMINATE) =====
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        progressBar.setString("Đang load dữ liệu dropdown...");

        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

            DefaultTableModel model = new DefaultTableModel();

            @Override
            protected Void doInBackground() {

                // Lấy dữ liệu expected
                List<String> expectedList = java.util.Arrays.asList(expectedText.split("\\n"));

                // Gọi service (Selenium / API / HTML parser)
                List<String> actualList = DropdownService.getDropdownData(url);

                // ===== GIAI ĐOẠN 2: SO SÁNH DỮ LIỆU =====
                SwingUtilities.invokeLater(() -> {
                    progressBar.setIndeterminate(false);
                    progressBar.setStringPainted(true);
                    progressBar.setValue(0);
                    progressBar.setString("Đang so sánh dữ liệu...");
                });

                model.addColumn("STT");
                model.addColumn("Expected");
                model.addColumn("Actual");
                model.addColumn("Result");

                int max = Math.max(expectedList.size(), actualList.size());

                for (int i = 0; i < max; i++) {

                    String expected = i < expectedList.size() ? expectedList.get(i).trim() : "";
                    String actual = i < actualList.size() ? actualList.get(i) : "";

                    model.addRow(new Object[]{
                            i + 1,
                            expected,
                            actual,
                            expected.equals(actual) ? "PASS" : "FAIL"
                    });

                    // Update progress theo số dòng đã xử lý
                    int progress = (i + 1) * 100 / max;
                    setProgress(progress);

                    try {
                        Thread.sleep(120); // giả lập thời gian xử lý
                    } catch (InterruptedException ignored) {}
                }

                return null;
            }

            @Override
            protected void done() {
                table.setModel(model);
                progressBar.setValue(100);
                progressBar.setString("Hoàn thành");
                btnRun.setEnabled(true);
            }
        };

        // Lắng nghe progress từ SwingWorker
        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((Integer) evt.getNewValue());
            }
        });

        worker.execute();
    }
}
