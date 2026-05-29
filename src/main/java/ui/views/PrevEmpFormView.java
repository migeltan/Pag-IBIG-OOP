package ui.views;

import ui.frames.SignInFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PrevEmpFormView extends JPanel {

    private final Color darkBg1 = new Color(10, 22, 40);
    private final Color darkBg2 = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentRed = new Color(255, 99, 132);
    private final Color textWhite = Color.WHITE;

    private JPanel listPanel;

    public List<PrevEmpEntry> entries = new ArrayList<>();

    public PrevEmpFormView() {

        setLayout(new BorderLayout());

        // ── Gradient Background ──────────────────────────────────────────────
        JPanel bg = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                g2.setPaint(new GradientPaint(
                        0,
                        0,
                        darkBg1,
                        getWidth(),
                        getHeight(),
                        darkBg2
                ));

                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        bg.setLayout(new GridBagLayout());

        // ── Glass Card ───────────────────────────────────────────────────────
        JPanel card = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(new Color(0, 0, 0, 40));

                g2.fillRoundRect(
                        4,
                        8,
                        getWidth() - 8,
                        getHeight() - 8,
                        24,
                        24
                );

                g2.setColor(new Color(255, 255, 255, 18));

                g2.fillRoundRect(
                        0,
                        0,
                        getWidth() - 4,
                        getHeight() - 4,
                        24,
                        24
                );

                g2.setColor(new Color(255, 255, 255, 45));

                g2.drawRoundRect(
                        0,
                        0,
                        getWidth() - 5,
                        getHeight() - 5,
                        24,
                        24
                );

                g2.setColor(accentGreen);

                g2.setStroke(new BasicStroke(2.5f));

                g2.drawLine(
                        16,
                        0,
                        getWidth() - 20,
                        0
                );

                g2.dispose();

                super.paintComponent(g);
            }
        };

        card.setOpaque(false);

        card.setPreferredSize(new Dimension(980, 620));

        card.setLayout(new BorderLayout());

        card.setBorder(new EmptyBorder(40, 45, 40, 45));

        // ── Content ──────────────────────────────────────────────────────────
        JPanel content = new JPanel();

        content.setOpaque(false);

        content.setLayout(new BoxLayout(
                content,
                BoxLayout.Y_AXIS
        ));

        // ── Title ────────────────────────────────────────────────────────────
        JLabel heading = new JLabel(
                "Previous Employment Records"
        );

        heading.setFont(new Font(
                "Arial Black",
                Font.BOLD,
                24
        ));

        heading.setForeground(textWhite);

        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subHeading = new JLabel(
                "Review your previous employment history."
        );

        subHeading.setFont(new Font(
                "Arial",
                Font.PLAIN,
                13
        ));

        subHeading.setForeground(
                new Color(255, 255, 255, 160)
        );

        subHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── List Panel ───────────────────────────────────────────────────────
        listPanel = new JPanel();

        listPanel.setLayout(new BoxLayout(
                listPanel,
                BoxLayout.Y_AXIS
        ));

        listPanel.setOpaque(false);

        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Buttons ──────────────────────────────────────────────────────────
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 15, 0)
        );

        buttonPanel.setOpaque(false);

        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton returnBtn = buildButton(
                "Back",
                accentRed
        );

       
        returnBtn.addActionListener(e -> {
    	    Window window = SwingUtilities.getWindowAncestor(PrevEmpFormView.this);
    	    if (window != null) window.dispose();
    	    new SignInFrame(); // go back to the dashboard
    	});

     
        buttonPanel.add(returnBtn);
     
        // ── Assemble Content ────────────────────────────────────────────────
        content.add(heading);

        content.add(Box.createRigidArea(
                new Dimension(0, 4)
        ));

        content.add(subHeading);

        content.add(Box.createRigidArea(
                new Dimension(0, 30)
        ));

        content.add(listPanel);

        content.add(Box.createRigidArea(
                new Dimension(0, 25)
        ));

        content.add(buttonPanel);

        // ── Scroll Pane ──────────────────────────────────────────────────────
        JScrollPane scrollPane = new JScrollPane(content);

        scrollPane.setBorder(null);

        scrollPane.setOpaque(false);

        scrollPane.getViewport().setOpaque(false);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        scrollPane.getVerticalScrollBar().setPreferredSize(
                new Dimension(8, Integer.MAX_VALUE)
        );

        scrollPane.getVerticalScrollBar().setBackground(
                new Color(0, 0, 0, 0)
        );

        // ── Add Scroll to Card ───────────────────────────────────────────────
        card.add(scrollPane, BorderLayout.CENTER);

        bg.add(card);

        add(bg, BorderLayout.CENTER);

        // ── Hardcoded Dummy Data ─────────────────────────────────────────────
        addEntry(
                "1234-5678-9012",
                "COMP-001",
                "2021-01-10",
                "2023-06-30"
        );

        addEntry(
                "1234-5678-9012",
                "COMP-002",
                "2019-03-01",
                "2020-12-20"
        );

        addEntry(
                "1234-5678-9012",
                "COMP-003",
                "2017-05-15",
                "2019-02-28"
        );

        addEntry(
                "1234-5678-9012",
                "COMP-004",
                "2015-02-01",
                "2017-04-20"
        );

        addEntry(
                "1234-5678-9012",
                "COMP-005",
                "2013-08-12",
                "2015-01-10"
        );
    }

    // ── Add Entry ────────────────────────────────────────────────────────────
    public void addEntry(
            String pagibig,
            String company,
            String from,
            String to
    ) {

        PrevEmpEntry entry = new PrevEmpEntry(
                entries.size() + 1,
                pagibig,
                company,
                from,
                to
        );

        entries.add(entry);

        listPanel.add(entry);

        listPanel.add(Box.createRigidArea(
                new Dimension(0, 14)
        ));

        listPanel.revalidate();

        listPanel.repaint();
    }

    // ── Entry Card ───────────────────────────────────────────────────────────
    public class PrevEmpEntry extends JPanel {

        private JLabel numberLabel;

        public JTextField pagIbigMidNoField;
        public JTextField companyCodeField;
        public JTextField fromDateField;
        public JTextField toDateField;

        public PrevEmpEntry(
                int number,
                String pagibig,
                String company,
                String from,
                String to
        ) {

            setLayout(new BorderLayout());

            setOpaque(false);

            setMaximumSize(new Dimension(
                    Integer.MAX_VALUE,
                    200
            ));

            setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel inner = new JPanel() {

                @Override
                protected void paintComponent(Graphics g) {

                    Graphics2D g2 = (Graphics2D) g.create();

                    g2.setRenderingHint(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON
                    );

                    g2.setColor(new Color(
                            255,
                            255,
                            255,
                            12
                    ));

                    g2.fillRoundRect(
                            0,
                            0,
                            getWidth() - 1,
                            getHeight() - 1,
                            18,
                            18
                    );

                    g2.setColor(new Color(
                            255,
                            255,
                            255,
                            35
                    ));

                    g2.drawRoundRect(
                            0,
                            0,
                            getWidth() - 1,
                            getHeight() - 1,
                            18,
                            18
                    );

                    g2.dispose();

                    super.paintComponent(g);
                }
            };

            inner.setOpaque(false);

            inner.setLayout(new BorderLayout(0, 15));

            inner.setBorder(new EmptyBorder(
                    18,
                    20,
                    18,
                    20
            ));

            JPanel header = new JPanel(
                    new BorderLayout()
            );

            header.setOpaque(false);

            numberLabel = new JLabel(
                    "Previous Employer " + number
            );

            numberLabel.setFont(new Font(
                    "Arial Black",
                    Font.BOLD,
                    13
            ));

            numberLabel.setForeground(accentGreen);

            header.add(numberLabel, BorderLayout.WEST);

            JPanel fields = new JPanel();

            fields.setLayout(new BoxLayout(
                    fields,
                    BoxLayout.Y_AXIS
            ));

            fields.setOpaque(false);

            JPanel r1 = row(2);

            r1.add(fieldPanel(
                    "PAG-IBIG MID NO.",
                    pagIbigMidNoField = buildTextField(pagibig)
            ));

            r1.add(fieldPanel(
                    "COMPANY CODE",
                    companyCodeField = buildTextField(company)
            ));

            JPanel r2 = row(2);

            r2.add(fieldPanel(
                    "FROM DATE (YYYY-MM-DD)",
                    fromDateField = buildTextField(from)
            ));

            r2.add(fieldPanel(
                    "TO DATE (YYYY-MM-DD)",
                    toDateField = buildTextField(to)
            ));

            fields.add(r1);

            fields.add(Box.createRigidArea(
                    new Dimension(0, 16)
            ));

            fields.add(r2);

            inner.add(header, BorderLayout.NORTH);

            inner.add(fields, BorderLayout.CENTER);

            add(inner, BorderLayout.CENTER);
        }
    }

    // ── Styled TextField ────────────────────────────────────────────────────
    private JTextField buildTextField(String value) {

        JTextField field = new JTextField(value) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(new Color(
                        255,
                        255,
                        255,
                        15
                ));

                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        12,
                        12
                );

                g2.setColor(new Color(
                        255,
                        255,
                        255,
                        50
                ));

                g2.setStroke(new BasicStroke(1.5f));

                g2.drawRoundRect(
                        0,
                        0,
                        getWidth() - 1,
                        getHeight() - 1,
                        12,
                        12
                );

                g2.dispose();

                super.paintComponent(g);
            }
        };

        field.setOpaque(false);

        field.setForeground(Color.WHITE);

        field.setCaretColor(Color.WHITE);

        field.setFont(new Font(
                "Arial",
                Font.PLAIN,
                14
        ));

        field.setBorder(new EmptyBorder(
                10,
                14,
                10,
                14
        ));

        field.setEditable(false);

        field.setFocusable(false);

        return field;
    }

    // ── Styled Button ───────────────────────────────────────────────────────
    private JButton buildButton(
            String text,
            Color color
    ) {

        JButton btn = new JButton(text) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(
                        getModel().isRollover()
                                ? color.darker()
                                : color
                );

                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        12,
                        12
                );

                g2.dispose();

                super.paintComponent(g);
            }
        };

        btn.setPreferredSize(new Dimension(
                220,
                46
        ));

        btn.setMaximumSize(new Dimension(
                340,
                46
        ));

        btn.setContentAreaFilled(false);

        btn.setBorderPainted(false);

        btn.setFocusPainted(false);

        btn.setOpaque(false);

        btn.setForeground(new Color(
                10,
                22,
                40
        ));

        btn.setFont(new Font(
                "Arial Black",
                Font.BOLD,
                13
        ));

        btn.setCursor(new Cursor(
                Cursor.HAND_CURSOR
        ));

        return btn;
    }

    // ── Label + Field ───────────────────────────────────────────────────────
    private JPanel fieldPanel(
            String label,
            JComponent field
    ) {

        JPanel p = new JPanel(
                new BorderLayout(0, 6)
        );

        p.setOpaque(false);

        JLabel lbl = new JLabel(label);

        lbl.setFont(new Font(
                "Arial",
                Font.BOLD,
                11
        ));

        lbl.setForeground(
                new Color(168, 208, 255)
        );

        p.add(lbl, BorderLayout.NORTH);

        p.add(field, BorderLayout.CENTER);

        return p;
    }

    // ── Row ─────────────────────────────────────────────────────────────────
    private JPanel row(int cols) {

        JPanel p = new JPanel(
                new GridLayout(1, cols, 18, 0)
        );

        p.setOpaque(false);

        p.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                72
        ));

        return p;
    }

    // ── Main ────────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame f = new JFrame(
                    "Previous Employment Form View"
            );

            f.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            f.setSize(1100, 700);

            f.setLocationRelativeTo(null);

            f.setContentPane(new PrevEmpFormView());

            f.setVisible(true);
        });
    }
}

