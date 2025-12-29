package GiaoDien;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class KiemTraElement extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtUrl;
    private JTextArea txtLabels;
    private JTable tblResult;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Demo Autotest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);

            KiemTraElement internal = new KiemTraElement();
            frame.add(internal);
            internal.setVisible(true);

            frame.setVisible(true);
        });
    }

    public KiemTraElement() {
        setTitle("Kiểm Tra Element Web");
        setBounds(0, 0, 780, 450);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setLayout(new BorderLayout(10, 10));

        setMinimumSize(new Dimension(600, 400));

        initUI();
    }

    private void initUI() {

        /* ===== PANEL NHẬP LINK ===== */
        JPanel pnlTop = new JPanel(new BorderLayout(5, 5));
        pnlTop.add(new JLabel("Link website:"), BorderLayout.WEST);
        txtUrl = new JTextField();
        pnlTop.add(txtUrl, BorderLayout.CENTER);

        /* ===== PANEL NHẬP LABEL ===== */
        JPanel pnlInput = new JPanel(new BorderLayout(5, 5));
        pnlInput.setBorder(
                BorderFactory.createTitledBorder("Nhập label cần kiểm tra (mỗi dòng 1 label)")
        );

        txtLabels = new JTextArea();
        JScrollPane labelScroll = new JScrollPane(txtLabels);
        pnlInput.add(labelScroll, BorderLayout.CENTER);

        JButton btnTest = new JButton("BẮT ĐẦU KIỂM TRA");
        btnTest.addActionListener(e -> startTest());

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlButton.add(btnTest);
        pnlInput.add(pnlButton, BorderLayout.SOUTH);

        /* ===== BẢNG KẾT QUẢ ===== */
        String[] columns = {"Label", "Kết quả"};
        tableModel = new DefaultTableModel(columns, 0);
        tblResult = new JTable(tableModel);

        JScrollPane resultScroll = new JScrollPane(tblResult);
        resultScroll.setBorder(
                BorderFactory.createTitledBorder("Bảng kết quả kiểm tra")
        );

        /* ===== SPLIT PANE ===== */
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                pnlInput,
                resultScroll
        );
        splitPane.setResizeWeight(0.5);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        add(pnlTop, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    /* ==================================================
       START TEST 
       ================================================== */
    private void startTest() {

        tableModel.setRowCount(0);

        if (txtUrl.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập link website");
            return;
        }

        if (txtLabels.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ít nhất 1 label");
            return;
        }

        String[] labels = txtLabels.getText().split("\\n");

        SeleniumKiemTraElement selenium = new SeleniumKiemTraElement();

        try {
            selenium.openWebsite(txtUrl.getText().trim());

            for (String label : labels) {
                label = label.trim();
                if (label.isEmpty()) continue;

                boolean found = selenium.checkElement(label);

                tableModel.addRow(new Object[]{
                        label,
                        found ? "PASS" : "FAIL"
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi Selenium: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            selenium.close();
        }
    }
}
