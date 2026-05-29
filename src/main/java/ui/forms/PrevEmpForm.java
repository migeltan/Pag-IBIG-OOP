package ui.forms;

import dao.CompanyDAO;
import dao.PrevEmpDAO;
import main.RegistrationSession;
import models.CompanyDetailsTable;
import models.PrevEmpTable;
import ui.frames.SignUpFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PrevEmpForm extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentAmber = new Color(251, 191, 36);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color textWhite   = Color.WHITE;

    private JPanel listPanel;
    private int empCount = 0;
    public List<PrevEmpEntry> entries = new ArrayList<>();

    // ── Company list loaded once, shared by all entries ───────────────────────
    private List<CompanyDetailsTable> companyList = new ArrayList<>();
    private String[] companyDisplayItems;

    public PrevEmpForm() {
        setLayout(new BorderLayout());

        // Load companies once upfront
        loadCompanies();

        JPanel bg = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setOpaque(true);

        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(4, 8, getWidth()-8, getHeight()-8, 24, 24);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillRoundRect(0, 0, getWidth()-4, getHeight()-4, 24, 24);
                g2.setColor(new Color(255, 255, 255, 45));
                g2.drawRoundRect(0, 0, getWidth()-5, getHeight()-5, 24, 24);
                g2.setColor(accentGreen);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(16, 0, getWidth()-20, 0);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(40, 45, 36, 45));

        // ── NORTH: Title ──────────────────────────────────────────────────────
        JLabel heading = new JLabel("Previous Employment Records");
        heading.setFont(new Font("Arial Black", Font.BOLD, 24));
        heading.setForeground(textWhite);

        JLabel subHeading = new JLabel("Manage and review your previous employment history.");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 13));
        subHeading.setForeground(new Color(255, 255, 255, 160));

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.add(heading);
        titleBlock.add(Box.createVerticalStrut(4));
        titleBlock.add(subHeading);
        titleBlock.setBorder(new EmptyBorder(0, 0, 22, 0));
        card.add(titleBlock, BorderLayout.NORTH);

        // ── CENTER: Scrollable list + Add button ──────────────────────────────
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JButton addBtn = buildButton("+ Add Another Previous Employer", accentGreen);
        addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addBtn.addActionListener(e -> addEntry());

        JPanel scrollContent = new JPanel();
        scrollContent.setOpaque(false);
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.add(listPanel);
        scrollContent.add(Box.createVerticalStrut(16));
        scrollContent.add(addBtn);
        scrollContent.add(Box.createVerticalStrut(4));

        JScrollPane scroll = new JScrollPane(scrollContent);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        card.add(scroll, BorderLayout.CENTER);

        // ── SOUTH: Back / Save buttons ────────────────────────────────────────
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton returnBtn   = buildButton("Back",  accentRed);
        JButton continueBtn = buildButton("Save",  accentGreen);

        returnBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    PrevEmpForm.this,
                    "Are you sure you want to go back?\nUnsaved changes will be lost.",
                    "Return to Sign Up",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                Window w = SwingUtilities.getWindowAncestor(PrevEmpForm.this);
                if (w != null) w.dispose();
                SwingUtilities.invokeLater(() -> new SignUpFrame());
            }
        });

        continueBtn.addActionListener(e -> handleSave());

        buttonPanel.add(returnBtn);
        buttonPanel.add(continueBtn);
        card.add(buttonPanel, BorderLayout.SOUTH);

        JPanel cardWrap = new JPanel(new BorderLayout());
        cardWrap.setOpaque(false);
        cardWrap.setBorder(new EmptyBorder(28, 28, 28, 28));
        cardWrap.add(card, BorderLayout.CENTER);
        bg.add(cardWrap, BorderLayout.CENTER);
        add(bg, BorderLayout.CENTER);

        addEntry(); // start with one blank entry
    }

    // ── Load Companies from DB ────────────────────────────────────────────────
    private void loadCompanies() {
        try {
            companyList = new CompanyDAO().getAllCompanies();
        } catch (Exception e) {
            System.err.println("[PrevEmpForm] Failed to load companies: " + e.getMessage());
        }

        List<String> items = new ArrayList<>();
        items.add("Select");
        for (CompanyDetailsTable c : companyList) {
            items.add(c.getCompanyName() + " (" + c.getCompanyCode() + ")");
        }
        items.add("Other (Add New Company)");
        companyDisplayItems = items.toArray(new String[0]);
    }

    // ── Handle Save ───────────────────────────────────────────────────────────
    private void handleSave() {
        String mid = RegistrationSession.getInstance().getTempMID();
        PrevEmpDAO dao = new PrevEmpDAO();
        CompanyDAO companyDAO = new CompanyDAO();

        for (int i = 0; i < entries.size(); i++) {
            PrevEmpEntry entry = entries.get(i);
            int entryNum = i + 1;

            // ── Validate company ──────────────────────────────────────────────
            if ("Select".equals(entry.companyBox.getSelectedItem())) {
                showError("Entry " + entryNum + ": Please select a company."); return;
            }

            // ── Validate dates ────────────────────────────────────────────────
            if (entry.fromDateField.getText().trim().isEmpty()) {
                showError("Entry " + entryNum + ": Please enter the From date."); return;
            }
            if (entry.toDateField.getText().trim().isEmpty()) {
                showError("Entry " + entryNum + ": Please enter the To date."); return;
            }

            Date fromDate, toDate;
            try {
                fromDate = Date.valueOf(entry.fromDateField.getText().trim());
            } catch (IllegalArgumentException ex) {
                showError("Entry " + entryNum + ": From date must be YYYY-MM-DD."); return;
            }
            try {
                toDate = Date.valueOf(entry.toDateField.getText().trim());
            } catch (IllegalArgumentException ex) {
                showError("Entry " + entryNum + ": To date must be YYYY-MM-DD."); return;
            }

            if (!toDate.after(fromDate)) {
                showError("Entry " + entryNum + ": To date must be after From date."); return;
            }

            // ── Resolve company code ──────────────────────────────────────────
            String companyCode;
            boolean isNew = "Other (Add New Company)".equals(entry.companyBox.getSelectedItem());

            if (isNew) {
                String newName    = entry.newCompanyNameField.getText().trim();
                String newAddress = entry.newCompanyAddressField.getText().trim();
                String newOffice  = (String) entry.newCompanyOfficeBox.getSelectedItem();
                String newBranch  = entry.newCompanyBranchField.getText().trim();

                if (newName.isEmpty() || newAddress.isEmpty()) {
                    showError("Entry " + entryNum + ": Please fill in company name and address."); return;
                }
                if ("BRANCH".equals(newOffice) && newBranch.isEmpty()) {
                    showError("Entry " + entryNum + ": Please enter the branch location."); return;
                }

                companyCode = generateCompanyCode(newName);
                CompanyDetailsTable newCompany = new CompanyDetailsTable(
                        companyCode, newName, newAddress, newOffice,
                        "BRANCH".equals(newOffice) ? newBranch : null
                );

                while (companyDAO.companyCodeExists(companyCode)) {
                    companyCode = companyCode + "1";
                    newCompany.setCompanyCode(companyCode);
                }

                if (!companyDAO.insertCompany(newCompany)) {
                    showError("Entry " + entryNum + ": Failed to save new company."); return;
                }

                // Reload company list so subsequent entries see the new company
                loadCompanies();

            } else {
                String selected = (String) entry.companyBox.getSelectedItem();
                companyCode = selected.substring(
                        selected.lastIndexOf("(") + 1, selected.lastIndexOf(")"));
            }

            // ── Insert prev emp record ────────────────────────────────────────
            PrevEmpTable record = new PrevEmpTable(mid, companyCode, fromDate, toDate);
            if (!dao.insertPrevEmp(record)) {
                showError("Entry " + entryNum + ": Failed to save. Please try again."); return;
            }
        }

        // ── All entries saved ─────────────────────────────────────────────────
        RegistrationSession.getInstance().setPrevEmpDone(true);

        JOptionPane.showMessageDialog(PrevEmpForm.this,
                "Previous employment records saved successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        Window w = SwingUtilities.getWindowAncestor(PrevEmpForm.this);
        if (w != null) w.dispose();
        SwingUtilities.invokeLater(() -> new SignUpFrame());
    }

    // ── Generate Company Code ─────────────────────────────────────────────────
    private String generateCompanyCode(String name) {
        StringBuilder code = new StringBuilder();
        java.util.Set<String> skip = new java.util.HashSet<>(
                java.util.Arrays.asList("of", "the", "and", "for", "a", "an", "in", "at", "to"));
        for (String word : name.split("\\s+")) {
            if (!skip.contains(word.toLowerCase()) && !word.isEmpty())
                code.append(Character.toUpperCase(word.charAt(0)));
        }
        return code.toString().toUpperCase();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    // ── Add Entry ─────────────────────────────────────────────────────────────
    public void addEntry() {
        empCount++;
        PrevEmpEntry entry = new PrevEmpEntry(empCount, this);
        entries.add(entry);
        listPanel.add(entry);
        listPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Remove Entry ──────────────────────────────────────────────────────────
    public void removeEntry(PrevEmpEntry entry) {
        if (entries.size() == 1) {
            JOptionPane.showMessageDialog(this,
                    "At least one entry is required.",
                    "Cannot Remove", JOptionPane.WARNING_MESSAGE);
            return;
        }
        entries.remove(entry);
        Component[] comps = listPanel.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] == entry) {
                listPanel.remove(i);
                if (i < listPanel.getComponentCount()) listPanel.remove(i);
                break;
            }
        }
        for (int i = 0; i < entries.size(); i++) entries.get(i).updateNumber(i + 1);
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Entry Card ────────────────────────────────────────────────────────────
    public class PrevEmpEntry extends JPanel {

        private JLabel numberLabel;
        public JComboBox<String> companyBox;
        public JTextField fromDateField;
        public JTextField toDateField;

        // New company fields (hidden by default)
        public JTextField        newCompanyNameField;
        public JTextField        newCompanyAddressField;
        public JComboBox<String> newCompanyOfficeBox;
        public JTextField        newCompanyBranchField;
        private JPanel           newCompanyPanel;
        private JPanel           newCompanyBranchPanel;

        public PrevEmpEntry(int number, PrevEmpForm parent) {
            setLayout(new BorderLayout());
            setOpaque(false);
            setAlignmentX(Component.LEFT_ALIGNMENT);

            // Dynamic height — taller when new company panel is visible
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
            setMinimumSize(new Dimension(0, 200));
            setPreferredSize(new Dimension(0, 200));

            JPanel inner = new JPanel(new BorderLayout(0, 0)) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 12));
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
                    g2.setColor(new Color(255, 255, 255, 35));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            inner.setOpaque(false);
            inner.setBorder(new EmptyBorder(18, 20, 18, 20));

            // ── Header ────────────────────────────────────────────────────────
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            numberLabel = new JLabel("Previous Employer " + number);
            numberLabel.setFont(new Font("Arial Black", Font.BOLD, 13));
            numberLabel.setForeground(accentGreen);

            JButton removeBtn = new JButton("✕ Remove");
            removeBtn.setForeground(new Color(255, 120, 120));
            removeBtn.setFont(new Font("Arial", Font.BOLD, 12));
            removeBtn.setBorderPainted(false);
            removeBtn.setContentAreaFilled(false);
            removeBtn.setFocusPainted(false);
            removeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            removeBtn.addActionListener(e -> parent.removeEntry(this));
            header.add(numberLabel, BorderLayout.WEST);
            header.add(removeBtn,   BorderLayout.EAST);

            JPanel divider = new JPanel();
            divider.setOpaque(true);
            divider.setBackground(new Color(255, 255, 255, 40));
            divider.setPreferredSize(new Dimension(0, 1));

            JPanel headerBlock = new JPanel(new BorderLayout(0, 8));
            headerBlock.setOpaque(false);
            headerBlock.add(header,  BorderLayout.NORTH);
            headerBlock.add(divider, BorderLayout.SOUTH);

            // ── Fields ────────────────────────────────────────────────────────
            JPanel fields = new JPanel();
            fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
            fields.setOpaque(false);

            // Row 1: MID (read-only) + Company dropdown
            JPanel r1 = row(2);
            JTextField midField = buildTextField();
            midField.setText(RegistrationSession.getInstance().getTempMID());
            midField.setEditable(false);
            midField.setForeground(accentAmber);
            r1.add(fieldPanel("PAG-IBIG MID NO. (Temporary)", midField));

            companyBox = new JComboBox<>(companyDisplayItems);
            companyBox.setFont(new Font("Arial", Font.PLAIN, 14));
            companyBox.setForeground(Color.WHITE);
            companyBox.setBackground(new Color(25, 35, 60));
            r1.add(fieldPanel("COMPANY *", companyBox));

            // ── New Company Panel ─────────────────────────────────────────────
            newCompanyPanel = new JPanel();
            newCompanyPanel.setOpaque(false);
            newCompanyPanel.setLayout(new BoxLayout(newCompanyPanel, BoxLayout.Y_AXIS));
            newCompanyPanel.setVisible(false);

            JPanel nc1 = row(2);
            newCompanyNameField    = buildTextField();
            newCompanyAddressField = buildTextField();
            nc1.add(fieldPanel("COMPANY NAME *",    newCompanyNameField));
            nc1.add(fieldPanel("COMPANY ADDRESS *", newCompanyAddressField));
            newCompanyPanel.add(nc1);
            newCompanyPanel.add(Box.createVerticalStrut(8));

            JPanel nc2 = row(2);
            newCompanyOfficeBox = new JComboBox<>(new String[]{"HEAD OFFICE", "BRANCH"});
            newCompanyOfficeBox.setFont(new Font("Arial", Font.PLAIN, 14));
            newCompanyOfficeBox.setForeground(Color.WHITE);
            newCompanyOfficeBox.setBackground(new Color(25, 35, 60));

            newCompanyBranchField = buildTextField();
            newCompanyBranchPanel = new JPanel(new BorderLayout());
            newCompanyBranchPanel.setOpaque(false);
            newCompanyBranchPanel.add(fieldPanel("BRANCH LOCATION", newCompanyBranchField));
            newCompanyBranchPanel.setVisible(false);

            newCompanyOfficeBox.addActionListener(e -> {
                boolean isBranch = "BRANCH".equals(newCompanyOfficeBox.getSelectedItem());
                newCompanyBranchPanel.setVisible(isBranch);
                fields.revalidate(); fields.repaint();
            });

            nc2.add(fieldPanel("OFFICE ASSIGNMENT *", newCompanyOfficeBox));
            nc2.add(newCompanyBranchPanel);
            newCompanyPanel.add(nc2);

            // Show/hide new company panel
            companyBox.addActionListener(e -> {
                boolean isOther = "Other (Add New Company)".equals(companyBox.getSelectedItem());
                newCompanyPanel.setVisible(isOther);
                fields.revalidate(); fields.repaint();
            });

            // Row 2: From / To dates
            JPanel r2 = row(2);
            r2.add(fieldPanel("FROM DATE (YYYY-MM-DD) *", fromDateField = buildTextField()));
            r2.add(fieldPanel("TO DATE (YYYY-MM-DD) *",   toDateField   = buildTextField()));

            fields.add(Box.createVerticalStrut(12));
            fields.add(r1);
            fields.add(Box.createVerticalStrut(8));
            fields.add(newCompanyPanel);
            fields.add(Box.createVerticalStrut(8));
            fields.add(r2);

            inner.add(headerBlock, BorderLayout.NORTH);
            inner.add(fields,      BorderLayout.CENTER);
            add(inner, BorderLayout.CENTER);
        }

        public void updateNumber(int n) {
            numberLabel.setText("Previous Employer " + n);
        }
    }

    // ── UI Helpers ────────────────────────────────────────────────────────────
    private JTextField buildTextField() {
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(isFocusOwner()
                        ? new Color(96, 216, 164, 180)
                        : new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(10, 14, 10, 14));
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

    private JButton buildButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.darker() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(220, 46));
        btn.setMaximumSize(new Dimension(340, 46));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
        btn.setFont(new Font("Arial Black", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel fieldPanel(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(new Color(168, 208, 255));
        p.add(lbl,   BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 18, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        p.setPreferredSize(new Dimension(0, 60));
        p.setMinimumSize(new Dimension(0, 60));
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Previous Employment Form");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1100, 700);
            f.setLocationRelativeTo(null);
            f.setContentPane(new PrevEmpForm());
            f.setVisible(true);
        });
    }
}