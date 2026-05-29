package ui.forms;

import dao.CurrentEmpDAO;
import dao.CompanyDAO;
import main.RegistrationSession;
import models.CurrentEmpRecordTable;
import models.CompanyDetailsTable;
import ui.frames.SignUpFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;
import java.util.List;

public class CurrentEmpForm extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentAmber = new Color(251, 191, 36);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color textWhite   = Color.WHITE;

    // ── Fields ────────────────────────────────────────────────────────────────
    public JTextField pagIbigMidNoField;
    public JTextField dateEmployedField;
    public JTextField occupationField;

    public JComboBox<String> companyBox;           // loaded from DB
    public JComboBox<String> employmentStatusBox;
    public JComboBox<String> typeOfWorkBox;
    public JComboBox<String> countryOfAssignmentBox;

    // ── New Company fields (shown when "Other" is selected) ───────────────────
    private JPanel   newCompanyPanel;
    private JTextField newCompanyNameField;
    private JTextField newCompanyAddressField;
    private JComboBox<String> newCompanyOfficeBox;
    private JTextField newCompanyBranchField;
    private JPanel   newCompanyBranchPanel;

    // ── Company list from DB ──────────────────────────────────────────────────
    private List<CompanyDetailsTable> companyList;

    public CurrentEmpForm() {
        setLayout(new BorderLayout());

        JPanel bg = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());

        JPanel card = new JPanel() {
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
        card.setPreferredSize(new Dimension(900, 600));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(40, 45, 40, 45));

        // ── Scrollable content ────────────────────────────────────────────────
        JPanel content = buildContent();
        JScrollPane scroll = new JScrollPane(content);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // ── Title ─────────────────────────────────────────────────────────────
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JPanel titleTextPanel = new JPanel();
        titleTextPanel.setOpaque(false);
        titleTextPanel.setLayout(new BoxLayout(titleTextPanel, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Current Employment Record");
        heading.setFont(new Font("Arial Black", Font.BOLD, 24));
        heading.setForeground(textWhite);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Provide your current employment information.");
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(new Color(255, 255, 255, 160));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleTextPanel.add(heading);
        titleTextPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        titleTextPanel.add(sub);
        titlePanel.add(titleTextPanel, BorderLayout.WEST);

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setOpaque(false);

        JButton returnBtn = buildButton("Back", accentRed);
        JButton submitBtn = buildButton("Save", accentGreen);

        returnBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    CurrentEmpForm.this,
                    "Are you sure you want to go back?\nUnsaved changes will be lost.",
                    "Return to Sign Up", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                Window w = SwingUtilities.getWindowAncestor(CurrentEmpForm.this);
                if (w != null) w.dispose();
                SwingUtilities.invokeLater(() -> new SignUpFrame());
            }
        });

        submitBtn.addActionListener(e -> handleSave());

        buttonPanel.add(returnBtn);
        buttonPanel.add(submitBtn);

        // ── Assemble card ─────────────────────────────────────────────────────
        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(titlePanel, BorderLayout.CENTER);
        north.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.add(buttonPanel, BorderLayout.WEST);
        south.setBorder(new EmptyBorder(20, 0, 0, 0));

        card.add(north,  BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(south,  BorderLayout.SOUTH);
        bg.add(card);
        add(bg, BorderLayout.CENTER);
    }

    // ── Build Form Content ────────────────────────────────────────────────────
    private JPanel buildContent() {
        JPanel c = new JPanel();
        c.setOpaque(false);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(new EmptyBorder(10, 0, 10, 0));

        // ── MID (read-only, from session) ─────────────────────────────────────
        JPanel r0 = row(2);
        pagIbigMidNoField = buildTextField();
        pagIbigMidNoField.setText(RegistrationSession.getInstance().getTempMID());
        pagIbigMidNoField.setEditable(false);
        pagIbigMidNoField.setForeground(accentAmber);
        r0.add(fieldPanel("PAG-IBIG MID NO. (Temporary)", pagIbigMidNoField));

        // ── Company dropdown (loaded from DB) ─────────────────────────────────
        companyBox = new JComboBox<>();
        companyBox.setFont(new Font("Arial", Font.PLAIN, 14));
        companyBox.setForeground(Color.WHITE);
        companyBox.setBackground(new Color(25, 35, 60));
        loadCompanies();
        r0.add(fieldPanel("COMPANY *", companyBox));
        c.add(r0);
        c.add(gap(16));

        // ── New Company Panel (hidden by default) ─────────────────────────────
        newCompanyPanel = new JPanel();
        newCompanyPanel.setOpaque(false);
        newCompanyPanel.setLayout(new BoxLayout(newCompanyPanel, BoxLayout.Y_AXIS));
        newCompanyPanel.setVisible(false);

        JPanel nc1 = row(2);
        newCompanyNameField    = buildTextField();
        newCompanyAddressField = buildTextField();
        nc1.add(fieldPanel("COMPANY NAME *", newCompanyNameField));
        nc1.add(fieldPanel("COMPANY ADDRESS *", newCompanyAddressField));
        newCompanyPanel.add(nc1);
        newCompanyPanel.add(gap(12));

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
            newCompanyPanel.revalidate();
            newCompanyPanel.repaint();
        });

        nc2.add(fieldPanel("OFFICE ASSIGNMENT *", newCompanyOfficeBox));
        nc2.add(newCompanyBranchPanel);
        newCompanyPanel.add(nc2);
        newCompanyPanel.add(gap(12));

        // ── Show/hide new company panel ───────────────────────────────────────
        companyBox.addActionListener(e -> {
            boolean isOther = "Other (Add New Company)".equals(companyBox.getSelectedItem());
            newCompanyPanel.setVisible(isOther);
            newCompanyPanel.revalidate();
            newCompanyPanel.repaint();
        });

        c.add(newCompanyPanel);

        // ── Row 1 ─────────────────────────────────────────────────────────────
        JPanel r1 = row(2);
        r1.add(fieldPanel("OCCUPATION *", occupationField = buildTextField()));
        r1.add(fieldPanel("DATE EMPLOYED (YYYY-MM-DD) *", dateEmployedField = buildTextField()));
        c.add(r1);
        c.add(gap(16));

        // ── Row 2 ─────────────────────────────────────────────────────────────
        JPanel r2 = row(3);
        r2.add(fieldPanel("EMPLOYMENT STATUS *", employmentStatusBox = buildComboBox(new String[]{
                "Select",
                "PERMANENT/REGULAR",
                "CASUAL",
                "CONTRACTUAL",
                "PROJECT BASED",
                "PART-TIME/TEMPORARY"
        })));
        r2.add(fieldPanel("TYPE OF WORK", typeOfWorkBox = buildComboBox(new String[]{
                "Select", "LAND-BASED", "SEA-BASED"
        })));
        r2.add(fieldPanel("COUNTRY OF ASSIGNMENT *", countryOfAssignmentBox = buildComboBox(new String[]{
                "Select", "Philippines", "Saudi Arabia", "United Arab Emirates",
                "Qatar", "Kuwait", "Singapore", "Hong Kong",
                "United States", "Canada", "Other"
        })));
        c.add(r2);

        return c;
    }

    // ── Load Companies from DB ────────────────────────────────────────────────
    private void loadCompanies() {
        companyBox.removeAllItems();
        companyBox.addItem("Select");
        try {
            CompanyDAO dao = new CompanyDAO();
            companyList = dao.getAllCompanies();
            for (CompanyDetailsTable company : companyList) {
                companyBox.addItem(company.getCompanyName() + " (" + company.getCompanyCode() + ")");
            }
        } catch (Exception e) {
            System.err.println("[CurrentEmpForm] Failed to load companies: " + e.getMessage());
        }
        companyBox.addItem("Other (Add New Company)");
    }

    // ── Handle Save ───────────────────────────────────────────────────────────
    private void handleSave() {

        // ── Validate ──────────────────────────────────────────────────────────
        if (occupationField.getText().trim().isEmpty()) {
            showError("Please enter your occupation."); return;
        }
        if (dateEmployedField.getText().trim().isEmpty()) {
            showError("Please enter the date employed."); return;
        }
        if ("Select".equals(companyBox.getSelectedItem())) {
            showError("Please select a company."); return;
        }
        if ("Select".equals(employmentStatusBox.getSelectedItem())) {
            showError("Please select an employment status."); return;
        }
        if ("Select".equals(countryOfAssignmentBox.getSelectedItem())) {
            showError("Please select a country of assignment."); return;
        }

        // ── Validate date ─────────────────────────────────────────────────────
        Date dateEmployed;
        try {
            dateEmployed = Date.valueOf(dateEmployedField.getText().trim());
        } catch (IllegalArgumentException ex) {
            showError("Date Employed must be in YYYY-MM-DD format."); return;
        }

        // ── Resolve company code ──────────────────────────────────────────────
        String companyCode;
        boolean isNewCompany = "Other (Add New Company)".equals(companyBox.getSelectedItem());

        if (isNewCompany) {
            String newName    = newCompanyNameField.getText().trim();
            String newAddress = newCompanyAddressField.getText().trim();
            String newOffice  = (String) newCompanyOfficeBox.getSelectedItem();
            String newBranch  = newCompanyBranchField.getText().trim();

            if (newName.isEmpty() || newAddress.isEmpty()) {
                showError("Please fill in the new company name and address."); return;
            }
            if ("BRANCH".equals(newOffice) && newBranch.isEmpty()) {
                showError("Please enter the branch location."); return;
            }

            // Generate company code from name initials
            companyCode = generateCompanyCode(newName);

            // Save new company to DB
            CompanyDetailsTable newCompany = new CompanyDetailsTable(
                    companyCode, newName, newAddress, newOffice,
                    "BRANCH".equals(newOffice) ? newBranch : null
            );
            CompanyDAO companyDAO = new CompanyDAO();

            // If code already exists, append a number
            while (companyDAO.companyCodeExists(companyCode)) {
                companyCode = companyCode + "1";
                newCompany.setCompanyCode(companyCode);
            }

            boolean companySaved = companyDAO.insertCompany(newCompany);
            if (!companySaved) {
                showError("Failed to save new company. Please try again."); return;
            }

        } else {
            // Extract code from selected item e.g. "Seanna Tech (STECH)" → "STECH"
            String selected = (String) companyBox.getSelectedItem();
            companyCode = selected.substring(selected.lastIndexOf("(") + 1, selected.lastIndexOf(")"));
        }

        // ── Build model ───────────────────────────────────────────────────────
        String mid = RegistrationSession.getInstance().getTempMID();
        String typeOfWork = "Select".equals(typeOfWorkBox.getSelectedItem())
                ? null
                : (String) typeOfWorkBox.getSelectedItem();

        CurrentEmpRecordTable record = new CurrentEmpRecordTable(
                mid,
                companyCode,
                occupationField.getText().trim(),
                (String) employmentStatusBox.getSelectedItem(),
                typeOfWork,
                (String) countryOfAssignmentBox.getSelectedItem(),
                dateEmployed
        );

        // ── Save to DB ────────────────────────────────────────────────────────
        CurrentEmpDAO dao = new CurrentEmpDAO();
        boolean saved = dao.insertCurrentEmp(record);

        if (!saved) {
            showError("Failed to save employment record. Please try again."); return;
        }

        // ── Mark session done ─────────────────────────────────────────────────
        RegistrationSession.getInstance().setCurrentEmpDone(true);

        JOptionPane.showMessageDialog(CurrentEmpForm.this,
                "Current employment record saved successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        Window w = SwingUtilities.getWindowAncestor(CurrentEmpForm.this);
        if (w != null) w.dispose();
        SwingUtilities.invokeLater(() -> new SignUpFrame());
    }

    // ── Generate Company Code from Name ───────────────────────────────────────
    // "Bank of the Philippine Islands" → "BPI"
    // "Seanna Tech" → "ST"
    private String generateCompanyCode(String name) {
        StringBuilder code = new StringBuilder();
        String[] skipWords = {"of", "the", "and", "for", "a", "an", "in", "at", "to"};
        java.util.Set<String> skip = new java.util.HashSet<>(java.util.Arrays.asList(skipWords));

        for (String word : name.split("\\s+")) {
            if (!skip.contains(word.toLowerCase()) && !word.isEmpty()) {
                code.append(Character.toUpperCase(word.charAt(0)));
            }
        }
        return code.toString().toUpperCase();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
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
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setBorder(new EmptyBorder(10, 14, 10, 14));
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { field.repaint(); }
            public void focusLost(FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

    private JComboBox<String> buildComboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Arial", Font.PLAIN, 14));
        box.setForeground(Color.WHITE);
        box.setBackground(new Color(25, 35, 60));
        box.setBorder(BorderFactory.createEmptyBorder());
        return box;
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
        btn.setPreferredSize(new Dimension(160, 46));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel fieldPanel(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(new Color(168, 208, 255));
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 18, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        return p;
    }

    private Component gap(int h) { return Box.createVerticalStrut(h); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Current Employment Form");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1100, 700);
            f.setLocationRelativeTo(null);
            f.setContentPane(new CurrentEmpForm());
            f.setVisible(true);
        });
    }
}