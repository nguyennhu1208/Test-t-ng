package GiaoDien;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class Menu extends JFrame {

    private static final long serialVersionUID = 1L;

    private JDesktopPane desktopPane;
    private JLabel centerLogoLabel;
    private JPanel logoPanel;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Throwable e) {}

        SwingUtilities.invokeLater(() -> {
            Menu frame = new Menu();
            frame.setVisible(true);
        });
    }

    public Menu() {
        setTitle("TEST TỰ ĐỘNG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 972, 658);
        getContentPane().setLayout(new BorderLayout());

        // ===== MENU BAR =====
        JMenuBar menuBar = new JMenuBar();
        menuBar.setFont(new Font("SansSerif", Font.BOLD, 16));
        setJMenuBar(menuBar);

        JMenu mnTest = new JMenu("TEST TỰ ĐỘNG");
        mnTest.setFont(new Font("SansSerif", Font.BOLD, 16));
        menuBar.add(mnTest);

        JMenuItem mntmDropdown = new JMenuItem("DROPDOWN LIST");
        mnTest.add(mntmDropdown);
        
        JMenuItem mntmElement = new JMenuItem("KIỂM TRA ELEMENT");
        mnTest.add(mntmElement);

        JMenu mnLopHoc = new JMenu("LỚP HỌC");
        mnLopHoc.setFont(new Font("SansSerif", Font.BOLD, 16));
        menuBar.add(mnLopHoc);

        JMenuItem mntmThemLop = new JMenuItem("THÊM");
        mnLopHoc.add(mntmThemLop);
        
        JMenuItem mntmXoaLop = new JMenuItem("XÓA");
        mnLopHoc.add(mntmXoaLop);
        
        JMenuItem mntmSuaLop = new JMenuItem("SỬA");
        mnLopHoc.add(mntmSuaLop);
        
        JMenu mnQuiz = new JMenu("QUIZ");
        mnQuiz.setFont(new Font("SansSerif", Font.BOLD, 16));
        menuBar.add(mnQuiz);
        
        JMenuItem mntmThemQuiz = new JMenuItem("THÊM");
        mnQuiz.add(mntmThemQuiz);
        
        JMenuItem mntmXoaQuiz = new JMenuItem("XÓA");
        mnQuiz.add(mntmXoaQuiz);

        JMenu mnLichThi = new JMenu("LỊCH THI");
        mnLichThi.setFont(new Font("SansSerif", Font.BOLD, 16));
        menuBar.add(mnLichThi);
        
        JMenuItem mntmThemLichThi = new JMenuItem("THÊM");
        mnLichThi.add(mntmThemLichThi);
        
        JMenuItem mntmXoaLichThi = new JMenuItem("XÓA");
        mnLichThi.add(mntmXoaLichThi);
        
        // ===== LOGO MENU BAR =====
        ImageIcon menuLogo = new ImageIcon(
                Menu.class.getResource("/GiaoDien/LoGo/CARD_MOI.png")
        );
        Image menuImg = menuLogo.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        menuBar.add(new JLabel(new ImageIcon(menuImg)), 0);

        // ===== DESKTOP PANE =====
        desktopPane = new JDesktopPane();
        getContentPane().add(desktopPane, BorderLayout.CENTER);

        // ===== LOGO TRUNG TÂM =====
        ImageIcon centerLogo = new ImageIcon(
                Menu.class.getResource("/GiaoDien/LoGo/Cardmoi_PLT_Trang.png")
        );
        Image centerImg = centerLogo.getImage().getScaledInstance(300, 180, Image.SCALE_SMOOTH);

        desktopPane.setLayout(null);
        centerLogoLabel = new JLabel(new ImageIcon(centerImg));

        logoPanel = new JPanel(new GridBagLayout());
        logoPanel.setBounds(0, 0, 0, 0);
        logoPanel.setOpaque(false);
        logoPanel.add(centerLogoLabel);

        desktopPane.add(logoPanel);

        SwingUtilities.invokeLater(this::resizeLogoPanel);

        desktopPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                resizeLogoPanel();
            }
        });

        // ===== ACTION =====
        mntmDropdown.addActionListener(e -> openTestDropDown());
        mntmElement.addActionListener(e -> openElement());
        mntmThemLop.addActionListener(e -> openThemLopHoc());
        mntmSuaLop.addActionListener(e -> openSuaLopHoc());
        mntmXoaLop.addActionListener(e -> openXoaLopHoc());
        mntmThemQuiz.addActionListener(e -> openThemQuiz());
        mntmXoaQuiz.addActionListener(e -> openXoaQuiz());
        mntmThemLichThi.addActionListener(e -> openThemLichThi());
        mntmXoaLichThi.addActionListener(e -> openXoaLichThi());

    }

    private void resizeLogoPanel() {
        logoPanel.setBounds(0, 0,
                desktopPane.getWidth(),
                desktopPane.getHeight());
        logoPanel.revalidate();
        logoPanel.repaint();
    }

    // ===== OPEN DROPDOWN =====
    private void openTestDropDown() {
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame instanceof TestDropDownList) {
                try {
                    frame.setSelected(true);
                    frame.toFront();
                } catch (Exception ignored) {}
                return;
            }
        }

        logoPanel.setVisible(false);

        TestDropDownList testDrop = new TestDropDownList();
        testDrop.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        desktopPane.add(testDrop);

        try {
            testDrop.setMaximum(true);
        } catch (Exception ignored) {}

        testDrop.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                if (desktopPane.getAllFrames().length == 0) {
                    logoPanel.setVisible(true);
                    resizeLogoPanel();
                }
            }
        });
    }
    private void openElement() {
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame instanceof KiemTraElement) {
                try {
                    frame.setSelected(true);
                    frame.toFront();
                } catch (Exception ignored) {}
                return;
            }
        }

        logoPanel.setVisible(false);

        KiemTraElement testElement = new KiemTraElement();
        testElement.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        desktopPane.add(testElement);
        testElement.setVisible(true);


        try {
        	testElement.setMaximum(true);
        } catch (Exception ignored) {}

        testElement.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                if (desktopPane.getAllFrames().length == 0) {
                    logoPanel.setVisible(true);
                    resizeLogoPanel();
                }
            }
        });
    }

    // ===== OPEN THÊM LỚP HỌC =====
    private void openThemLopHoc() {

        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame instanceof ThemLopHoc) {
                try {
                    frame.setSelected(true);
                    frame.toFront();
                } catch (Exception ignored) {}
                return;
            }
        }

        logoPanel.setVisible(false);

        ThemLopHoc themLop = new ThemLopHoc();
        themLop.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        desktopPane.add(themLop);

        themLop.setVisible(true);

        try {
            themLop.setMaximum(true);
        } catch (Exception ignored) {}

        themLop.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                if (desktopPane.getAllFrames().length == 0) {
                    logoPanel.setVisible(true);
                    resizeLogoPanel();
                }
            }
        });
    }
 // ===== OPEN XÓA LỚP HỌC =====
    private void openXoaLopHoc() {

        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame instanceof XoaLopHoc) {
                try {
                    frame.setSelected(true);
                    frame.toFront();
                } catch (Exception ignored) {}
                return;
            }
        }

        logoPanel.setVisible(false);

        XoaLopHoc xoaLop = new XoaLopHoc();
        xoaLop.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        desktopPane.add(xoaLop);

        xoaLop.setVisible(true);

        try {
        	xoaLop.setMaximum(true);
        } catch (Exception ignored) {}

        xoaLop.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                if (desktopPane.getAllFrames().length == 0) {
                    logoPanel.setVisible(true);
                    resizeLogoPanel();
                }
            }
        });
    }
 // ===== OPEN SỬA LỚP HỌC =====
    private void openSuaLopHoc() {

        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame instanceof SuaLopHoc) {
                try {
                    frame.setSelected(true);
                    frame.toFront();
                } catch (Exception ignored) {}
                return;
            }
        }

        logoPanel.setVisible(false);

        SuaLopHoc suaLop = new SuaLopHoc(); // ✅ ĐÚNG KIỂU
        suaLop.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        desktopPane.add(suaLop);
        suaLop.setVisible(true);

        try {
            suaLop.setMaximum(true);
        } catch (Exception ignored) {}

        suaLop.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                if (desktopPane.getAllFrames().length == 0) {
                    logoPanel.setVisible(true);
                    resizeLogoPanel();
                }
            }
        });
    }

    // OPEN THEM QUIZ
    private void openThemQuiz() {

        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame instanceof ThemQuiz) {
                try {
                    frame.setSelected(true);
                    frame.toFront();
                } catch (Exception ignored) {}
                return;
            }
        }

        logoPanel.setVisible(false);

        ThemQuiz themQuiz = new ThemQuiz();
        themQuiz.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        desktopPane.add(themQuiz);

        themQuiz.setVisible(true);

        try {
        	themQuiz.setMaximum(true);
        } catch (Exception ignored) {}

        themQuiz.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                if (desktopPane.getAllFrames().length == 0) {
                    logoPanel.setVisible(true);
                    resizeLogoPanel();
                }
            }
        });
    }
 // ===== OPEN XÓA QUIZ =====
    private void openXoaQuiz() {

        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame instanceof XoaQuiz) {
                try {
                    frame.setSelected(true);
                    frame.toFront();
                } catch (Exception ignored) {}
                return;
            }
        }

        logoPanel.setVisible(false);

        XoaQuiz xoaQuiz = new XoaQuiz();
        xoaQuiz.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        desktopPane.add(xoaQuiz);

        xoaQuiz.setVisible(true);

        try {
        	xoaQuiz.setMaximum(true);
        } catch (Exception ignored) {}

        xoaQuiz.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                if (desktopPane.getAllFrames().length == 0) {
                    logoPanel.setVisible(true);
                    resizeLogoPanel();
                }
            }
        });
    }
 // ===== OPEN THÊM LỊCH THI =====
    private void openThemLichThi() {

        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame instanceof ThemLichThi) {
                try {
                    frame.setSelected(true);
                    frame.toFront();
                } catch (Exception ignored) {}
                return;
            }
        }

        logoPanel.setVisible(false);

        ThemLichThi themLichThi = new ThemLichThi();
        themLichThi.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        desktopPane.add(themLichThi);
        themLichThi.setVisible(true);

        try {
            themLichThi.setMaximum(true);
        } catch (Exception ignored) {}

        themLichThi.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                if (desktopPane.getAllFrames().length == 0) {
                    logoPanel.setVisible(true);
                    resizeLogoPanel();
                }
            }
        });
    }
 // ===== OPEN XÓA LỊCH THI =====
    private void openXoaLichThi() {

        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame instanceof XoaLichThi) {
                try {
                    frame.setSelected(true);
                    frame.toFront();
                } catch (Exception ignored) {}
                return;
            }
        }

        logoPanel.setVisible(false);

        XoaLichThi xoaLichThi = new XoaLichThi();
        xoaLichThi.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        desktopPane.add(xoaLichThi);
        xoaLichThi.setVisible(true);

        try {
            xoaLichThi.setMaximum(true);
        } catch (Exception ignored) {}

        xoaLichThi.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                if (desktopPane.getAllFrames().length == 0) {
                    logoPanel.setVisible(true);
                    resizeLogoPanel();
                }
            }
        });
    }


}
