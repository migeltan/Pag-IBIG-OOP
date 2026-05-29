package ui.frames;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class PasswordRecoveryFrame extends JFrame {

    private static final Color BG_MAIN      = new Color(0x0A, 0x19, 0x31);
    private static final Color BG_CARD      = new Color(0x15, 0x2A, 0x4A);
    private static final Color ACCENT_GREEN = new Color(0x00, 0xC8, 0x97);
    private static final Color TEXT_PRI     = new Color(0xE2, 0xE8, 0xF0);
    private static final Color TEXT_SEC     = new Color(0x94, 0xA3, 0xB8);
    private static final Color BORDER_DIM   = new Color(0x7C, 0xC3, 0xED, 55);

    private static final Font F_TITLE = new Font("Dialog", Font.BOLD, 26);
    private static final Font F_SECT  = new Font("Dialog", Font.BOLD, 11);
    private static final Font F_INPUT = new Font("Dialog", Font.PLAIN, 13);
    private static final Font F_BTN   = new Font("Dialog", Font.BOLD, 11);

    private String selectedMethod = "";
    private JButton gmailBtn;
    private JButton questionsBtn;

    // ── Save original UIManager defaults before Nimbus overrides ─────────────
    private Map<Object, Object> originalDefaults;

    public PasswordRecoveryFrame() {
        super(" ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        applyNimbusDark();

        getContentPane().setBackground(BG_MAIN);
        setLayout(new BorderLayout());

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);
        setVisible(true);
    }

    private void applyNimbusDark() {

        // Save originals BEFORE overriding anything
        originalDefaults = new HashMap<>(UIManager.getDefaults());

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {}
        }

        UIManager.put("control", BG_CARD);
        UIManager.put("info", BG_CARD);
        UIManager.put("nimbusBase", BG_MAIN);
        UIManager.put("nimbusBlueGrey", BG_CARD);
        UIManager.put("nimbusLightBackground", new Color(0x0D, 0x1F, 0x3C));
        UIManager.put("nimbusFocus", ACCENT_GREEN);
        UIManager.put("text", TEXT_PRI);
    }

    private void restoreDefaults() {
        if (originalDefaults != null) {
            UIManager.getDefaults().clear();
            UIManager.getDefaults().putAll(originalDefaults);
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }
    }

    private JPanel buildTopBar() {

        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(0x09, 0x15, 0x28));
        bar.setBorder(new MatteBorder(0, 0, 1, 0,
                new Color(0x94, 0xA3, 0xB8, 40)));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        right.setOpaque(false);

        JButton backBtn = topBtn("← Back to Login");
        backBtn.addActionListener(e -> {
            restoreDefaults();
            new LoginFrame();
            dispose();
        });
        right.add(backBtn);

        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JButton topBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(F_BTN);
        b.setForeground(TEXT_SEC);
        b.setBackground(new Color(0x15, 0x2A, 0x4A));
        b.setBorder(new CompoundBorder(
                new LineBorder(new Color(0x94, 0xA3, 0xB8, 80), 1, true),
                new EmptyBorder(4, 12, 4, 12)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel buildBody() {

        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG_MAIN);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(BG_MAIN);
        inner.setMaximumSize(new Dimension(860, Integer.MAX_VALUE));

        JLabel lockIcon = new JLabel("🔐", SwingConstants.CENTER);
        lockIcon.setFont(new Font("Dialog", Font.PLAIN, 52));
        lockIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel heading = new JLabel("Recover Your Password", SwingConstants.CENTER);
        heading.setFont(F_TITLE);
        heading.setForeground(TEXT_PRI);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subheading = new JLabel(
                "<html><div style='text-align:center;'>" +
                "Choose how you would like to reset your " +
                "Pag-CONNECT account password.</div></html>",
                SwingConstants.CENTER);
        subheading.setFont(F_INPUT);
        subheading.setForeground(TEXT_SEC);
        subheading.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(lockIcon);
        inner.add(Box.createVerticalStrut(12));
        inner.add(heading);
        inner.add(Box.createVerticalStrut(10));
        inner.add(subheading);
        inner.add(Box.createVerticalStrut(36));

        JPanel sectRow = new JPanel(new BorderLayout());
        sectRow.setBackground(BG_MAIN);
        sectRow.setBorder(new CompoundBorder(
                new MatteBorder(0, 3, 0, 0, ACCENT_GREEN),
                new EmptyBorder(0, 10, 6, 0)));
        sectRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JLabel sectLabel = new JLabel("SELECT RECOVERY METHOD");
        sectLabel.setFont(F_SECT);
        sectLabel.setForeground(ACCENT_GREEN);
        sectRow.add(sectLabel, BorderLayout.WEST);

        inner.add(sectRow);
        inner.add(Box.createVerticalStrut(16));

        gmailBtn = buildCardButton(
                "📧", "Via Gmail",
                "A password reset link will be sent to your registered Gmail address.\n" +
                "Check your inbox and follow the link to set a new password.",
                "gmail");

        questionsBtn = buildCardButton(
                "🔒", "Via Security Questions",
                "Answer the security questions you set during registration\n" +
                "to verify your identity and proceed to reset your password.",
                "questions");

        JPanel cardsRow = new JPanel(new GridLayout(1, 2, 24, 0));
        cardsRow.setBackground(BG_MAIN);
        cardsRow.add(gmailBtn);
        cardsRow.add(questionsBtn);
        cardsRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));
        cardsRow.setPreferredSize(new Dimension(860, 340));

        inner.add(cardsRow);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets  = new Insets(40, 60, 40, 60);
        outer.add(inner, gbc);

        return outer;
    }

    private JButton buildCardButton(
            String emoji, String title,
            String description, String method) {

        String html = "<html><div style='text-align:center;'>"
                + "<div style='font-size:36px;'>" + emoji + "</div>"
                + "<br>"
                + "<div style='font-size:14px; font-weight:bold; color:#E2E8F0;'>" + title + "</div>"
                + "<br>"
                + "<div style='font-size:11px; color:#94A3B8; width:260px;'>" + description + "</div>"
                + "</div></html>";

        JButton btn = new JButton(html);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);
        btn.setBackground(BG_CARD);
        btn.setForeground(TEXT_PRI);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new CompoundBorder(
                new LineBorder(BORDER_DIM, 2, true),
                new EmptyBorder(36, 32, 36, 32)));

        btn.addActionListener(e -> {
            if (selectedMethod.equals(method)) return;
            selectedMethod = method;

            gmailBtn.setBackground(BG_CARD);
            gmailBtn.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_DIM, 2, true),
                    new EmptyBorder(36, 32, 36, 32)));
            questionsBtn.setBackground(BG_CARD);
            questionsBtn.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_DIM, 2, true),
                    new EmptyBorder(36, 32, 36, 32)));

            btn.setBackground(new Color(0x0A, 0x1F, 0x38));
            btn.setBorder(new CompoundBorder(
                    new LineBorder(ACCENT_GREEN, 2, true),
                    new EmptyBorder(36, 32, 36, 32)));

            if (method.equals("gmail")) {
                restoreDefaults();
                new PasswordSentFrame().setVisible(true);
                dispose();
            } else if (method.equals("questions")) {
                restoreDefaults();
                new SecurityQuestionResetFrame().setVisible(true);
                dispose();
            }
        });

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!selectedMethod.equals(method))
                    btn.setBackground(new Color(0x1C, 0x35, 0x5E));
            }
            @Override public void mouseExited(MouseEvent e) {
                if (!selectedMethod.equals(method))
                    btn.setBackground(BG_CARD);
            }
        });

        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new PasswordRecoveryFrame().setVisible(true));
    }
}