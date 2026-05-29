package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MemberRecordForm extends JFrame {

    // ── Theme Colors ──────────────────────────────────────────────────────────
    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color textWhite   = Color.WHITE;

    // ── Caller reference ──────────────────────────────────────────────────────
    private final JFrame caller;

    // ── Fields ────────────────────────────────────────────────────────────────
    public JTextField pagIbigMidNoField, membershipTypeOthersField, crnField;
    public JTextField memberNameField, fatherNameField, motherNameField, spouseNameField;
    public JTextField birthdateField, birthplaceField, employeeNumField, tinField, sssField;
    public JTextField presentAddrField, permanentAddrField;
    public JTextField homeTelField, cellphoneField, emailField;
    public JTextField busDirectField, busTrunkField, localField;
    public JTextField allowBasicField, allowOtherField, totalIncomeField;
    public JTextField curPagIbigField, curCompanyCodeField, curOccupationField, curDateEmpField;

    public JComboBox<String> membershipTypeBox, membershipCategoryBox, occupationalStatusBox, frequencyBox;
    public JComboBox<String> preferredMailBox, maritalStatusBox, citizenshipBox, sexBox;
    public JComboBox<String> curEmpStatusBox, curTypeOfWorkBox, curCountryBox;

    private JPanel prevEmpListPanel;
    private JPanel heirListPanel;
    private int prevEmpCount = 0;
    private int heirCount = 0;

    // =========================================================================
    public MemberRecordForm(JFrame caller) {

        this.caller = caller;

        setTitle("Pag-CONNECT — Member Record");
        setSize(1100, 760);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(
                        0, 0, darkBg1,
                        getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(6, 8, getWidth() - 8, getHeight() - 8, 24, 24);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 24, 24);
                g2.setColor(new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 24, 24);
                g2.setColor(accentGreen);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(16, 0, getWidth() - 16, 0);
                g2.dispose();
            }
        };

        card.setOpaque(false);
        card.setPreferredSize(new Dimension(980, 680));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Member Information Record");
        title.setForeground(textWhite);
        title.setFont(new Font("Arial Black", Font.BOLD, 24));

        JLabel sub = new JLabel("Complete member profile and employment history.");
        sub.setForeground(new Color(255, 255, 255, 170));
        sub.setFont(new Font("Arial", Font.PLAIN, 13));

        header.add(title);
        header.add(Box.createRigidArea(new Dimension(0, 5)));
        header.add(sub);

        JScrollPane scroll = new JScrollPane(buildContent());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JButton backBtn   = buildButton("Back", accentRed);
        JButton submitBtn = buildButton("Save", accentGreen);

        backBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    MemberRecordForm.this,
                    "Are you sure you want to go back?\nUnsaved changes will be lost.",
                    "Return",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                if (caller != null) caller.setVisible(true);
                dispose();
            }
        });

        submitBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        MemberRecordForm.this,
                        "Edited information successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                ));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(18, 0, 0, 0));
        bottom.add(backBtn);
        bottom.add(submitBtn);

        card.add(header, BorderLayout.NORTH);
        card.add(scroll,  BorderLayout.CENTER);
        card.add(bottom,  BorderLayout.SOUTH);

        bg.add(card);

        setVisible(true);
    }

    // =========================================================================
    // FORM CONTENT
    // =========================================================================
    private JPanel buildContent() {

        JPanel c = new JPanel();
        c.setOpaque(false);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(new EmptyBorder(20, 0, 20, 0));

        // ── Membership Information ────────────────────────────────────────────
        c.add(sectionHeader("Membership Information"));
        c.add(vgap(14));

        JPanel r1 = row(3);
        r1.add(lf("Pag-IBIG MID No. *",
                pagIbigMidNoField = tf("1234-5678-9012")));
        r1.add(lf("Membership Type *",
                membershipTypeBox = cb(new String[]{
                        "Select", "Employed", "Overseas Filipino Worker",
                        "Self-Employed", "Others"
                }, "Employed")));
        c.add(r1);
        c.add(vgap(16));

        JPanel r2 = row(2);
        r2.add(lf("Membership Category *",
                membershipCategoryBox = cb(new String[]{
                        "Select", "Private", "Government", "Private Household",
                        "Overseas Filipino Worker", "Professional/Business Owner",
                        "Job Order Personnel", "Other Earning Groups", "Others"
                }, "Private")));
        r2.add(lf("Occupational Status *",
                occupationalStatusBox = cb(new String[]{
                        "Select", "Employed", "Unemployed", "First Time Jobseeker"
                }, "Employed")));
        c.add(r2);
        c.add(vgap(16));

        JPanel r3 = row(2);
        r3.add(lf("Frequency of Membership Savings *",
                frequencyBox = cb(new String[]{
                        "Select", "Monthly", "Quarterly", "Semi-Annual", "Annual"
                }, "Monthly")));
        r3.add(lf("CRN", crnField = tf("CRN-2024-00123")));
        c.add(r3);
        c.add(vgap(26));

        // ── Personal Information ──────────────────────────────────────────────
        c.add(sectionHeader("Personal Information"));
        c.add(vgap(14));

        JPanel r4 = row(1);
        r4.add(lf("Member Name *", memberNameField = tf("dela Cruz, Juan Santos")));
        c.add(r4);
        c.add(vgap(16));

        JPanel r5 = row(3);
        r5.add(lf("Father's Name",  fatherNameField = tf("dela Cruz, Roberto M.")));
        r5.add(lf("Mother's Name",  motherNameField = tf("dela Cruz, Maria Reyes")));
        r5.add(lf("Spouse's Name",  spouseNameField = tf("")));
        c.add(r5);
        c.add(vgap(16));

        JPanel r6 = row(3);
        r6.add(lf("Birthdate (YYYY-MM-DD) *", birthdateField  = tf("1990-03-15")));
        r6.add(lf("Birthplace *",             birthplaceField = tf("Manila City")));
        r6.add(lf("Marital Status *",
                maritalStatusBox = cb(new String[]{
                        "Select", "Single", "Married", "Widowed",
                        "Legally Separated", "Annulled"
                }, "Single")));
        c.add(r6);
        c.add(vgap(16));

        JPanel r7 = row(3);
        r7.add(lf("Sex *",
                sexBox = cb(new String[]{"Select", "Male", "Female"}, "Male")));
        r7.add(lf("Citizenship *",
                citizenshipBox = cb(new String[]{"Select", "Filipino", "Other"}, "Filipino")));
        r7.add(lf("Employee No.", employeeNumField = tf("EMP-20240001")));
        c.add(r7);
        c.add(vgap(26));

        // ── Government IDs ────────────────────────────────────────────────────
        c.add(sectionHeader("Government IDs"));
        c.add(vgap(14));

        JPanel r8 = row(2);
        r8.add(lf("TIN",    tinField = tf("123-456-789-000")));
        r8.add(lf("SSS No.", sssField = tf("34-5678901-2")));
        c.add(r8);
        c.add(vgap(26));

        // ── Address Information ───────────────────────────────────────────────
        c.add(sectionHeader("Address Information"));
        c.add(vgap(14));

        JPanel r9 = row(1);
        r9.add(lf("Present Home Address *",
                presentAddrField = tf("123 Rizal St., Brgy. 123, Quezon City, Metro Manila 1100")));
        c.add(r9);
        c.add(vgap(16));

        JPanel r10 = row(1);
        r10.add(lf("Permanent Home Address *",
                permanentAddrField = tf("456 Mabini Ave., Brgy. Poblacion, Manila City, Metro Manila 1000")));
        c.add(r10);
        c.add(vgap(16));

        JPanel r11 = row(2);
        r11.add(lf("Preferred Mailing Address *",
                preferredMailBox = cb(new String[]{
                        "Select", "Present Home Address",
                        "Permanent Home Address", "Employer/Business Address"
                }, "Present Home Address")));
        r11.add(lf("", new JLabel()));
        c.add(r11);
        c.add(vgap(26));

        // ── Contact Information ───────────────────────────────────────────────
        c.add(sectionHeader("Contact Information"));
        c.add(vgap(14));

        JPanel r12 = row(3);
        r12.add(lf("Cellphone No. * (+63...)", cellphoneField = tf("+63 917 123 4567")));
        r12.add(lf("Home Telephone No.",       homeTelField   = tf("(02) 8123-4567")));
        r12.add(lf("Email Address",            emailField     = tf("juan.delacruz@email.com")));
        c.add(r12);
        c.add(vgap(16));

        JPanel r13 = row(3);
        r13.add(lf("Business Direct Line", busDirectField = tf("(02) 8999-0001")));
        r13.add(lf("Business Trunk Line",  busTrunkField  = tf("(02) 8999-0000")));
        r13.add(lf("Local/Extension",      localField     = tf("204")));
        c.add(r13);
        c.add(vgap(26));

        // ── Income Information ────────────────────────────────────────────────
        c.add(sectionHeader("Income Information"));
        c.add(vgap(14));

        JPanel r14 = row(3);
        r14.add(lf("Basic Allowance *",              allowBasicField  = tf("35000.00")));
        r14.add(lf("Other Sources",                  allowOtherField  = tf("5000.00")));
        r14.add(lf("Total Monthly Income (System-Derived)", totalIncomeField = tf("40000.00")));
        c.add(r14);

        totalIncomeField.setEditable(false);
        totalIncomeField.setForeground(accentGreen);

        FocusAdapter income = new FocusAdapter() {
            public void focusLost(FocusEvent e) { calcIncome(); }
        };
        allowBasicField.addFocusListener(income);
        allowOtherField.addFocusListener(income);

        c.add(vgap(14));

        JButton confirmIncomeBtn = buildButton("Confirm Member Information", accentGreen);
        confirmIncomeBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Income Information Confirmed!", "Confirmed",
                        JOptionPane.INFORMATION_MESSAGE));
        c.add(confirmIncomeBtn);
        c.add(vgap(26));

        // ── Current Employment Record ─────────────────────────────────────────
        c.add(sectionHeader("Current Employment Record"));
        c.add(vgap(14));

        JPanel r15 = row(2);
        r15.add(lf("Pag-IBIG MID No. *", curPagIbigField     = tf("1234-5678-9012")));
        r15.add(lf("Company Code",        curCompanyCodeField = tf("PH-BGC-00421")));
        c.add(r15);
        c.add(vgap(16));

        JPanel r16 = row(2);
        r16.add(lf("Occupation",                  curOccupationField = tf("Software Engineer")));
        r16.add(lf("Date Employed (YYYY-MM-DD)",  curDateEmpField    = tf("2022-06-01")));
        c.add(r16);
        c.add(vgap(16));

        JPanel r17 = row(3);
        r17.add(lf("Employment Status",
                curEmpStatusBox = cb(new String[]{
                        "Select", "Regular", "Probationary", "Contractual",
                        "Project-based", "Casual", "Part-time"
                }, "Regular")));
        r17.add(lf("Type of Work",
                curTypeOfWorkBox = cb(new String[]{
                        "Select", "Private", "Government", "Self-Employed", "Mixed"
                }, "Private")));
        r17.add(lf("Country of Assignment",
                curCountryBox = cb(new String[]{
                        "Select", "Philippines", "Saudi Arabia", "United Arab Emirates",
                        "Qatar", "Kuwait", "Singapore", "Hong Kong",
                        "United States", "Canada", "Other"
                }, "Philippines")));
        c.add(r17);
        c.add(vgap(14));

        JButton confirmCurrentBtn = buildButton("Confirm Current Employment", accentGreen);
        confirmCurrentBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Current Employment Record Confirmed!", "Confirmed",
                        JOptionPane.INFORMATION_MESSAGE));
        c.add(confirmCurrentBtn);
        c.add(vgap(26));

        // ── Previous Employment Records ───────────────────────────────────────
        c.add(sectionHeader("Previous Employment Records"));
        c.add(vgap(14));

        prevEmpListPanel = new JPanel();
        prevEmpListPanel.setLayout(new BoxLayout(prevEmpListPanel, BoxLayout.Y_AXIS));
        prevEmpListPanel.setOpaque(false);
        prevEmpListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.add(prevEmpListPanel);
        c.add(vgap(10));

        addPrevEmp("1234-5678-9012", "PH-MNL-00185", "2019-01-15", "2022-05-31");
        addPrevEmp("1234-5678-9012", "PH-QC-00072",  "2016-07-01", "2018-12-31");

        JButton addPrevBtn = buildAddButton("+ Add Previous Employer");
        addPrevBtn.addActionListener(e -> addPrevEmp("", "", "", ""));
        c.add(addPrevBtn);
        c.add(vgap(14));

        JButton confirmPrevBtn = buildButton("Confirm Previous Employment", accentGreen);
        confirmPrevBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Previous Employment Records Confirmed!", "Confirmed",
                        JOptionPane.INFORMATION_MESSAGE));
        c.add(confirmPrevBtn);
        c.add(vgap(26));

        // ── Heirs & Dependents ────────────────────────────────────────────────
        c.add(sectionHeader("Heirs & Dependents"));
        c.add(vgap(14));

        heirListPanel = new JPanel();
        heirListPanel.setLayout(new BoxLayout(heirListPanel, BoxLayout.Y_AXIS));
        heirListPanel.setOpaque(false);
        heirListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.add(heirListPanel);
        c.add(vgap(10));

        addHeir("9876-5432-1001", "dela Cruz, Maria Reyes", "Parent", "1965-08-20");
        addHeir("9876-5432-1002", "dela Cruz, Roberto M.",  "Parent", "1962-04-11");

        JButton addHeirBtn = buildAddButton("+ Add Heir / Dependent");
        addHeirBtn.addActionListener(e -> addHeir("", "", "", ""));
        c.add(addHeirBtn);
        c.add(vgap(14));

        JButton confirmHeirBtn = buildButton("Confirm Heirs & Dependents", accentGreen);
        confirmHeirBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Heirs & Dependents Confirmed!", "Confirmed",
                        JOptionPane.INFORMATION_MESSAGE));
        c.add(confirmHeirBtn);

        return c;
    }

    // =========================================================================
    // DYNAMIC ENTRIES
    // =========================================================================
    private void addPrevEmp(String pagIbig, String code, String from, String to) {

        prevEmpCount++;

        JPanel sub = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 16, 16);
                g2.setColor(new Color(96, 216, 164, 60));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        sub.setOpaque(false);
        sub.setLayout(new BoxLayout(sub, BoxLayout.Y_AXIS));
        sub.setBorder(new EmptyBorder(14, 16, 16, 16));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setMaximumSize(new Dimension(Integer.MAX_VALUE, 210));

        JPanel subHdr = new JPanel(new BorderLayout());
        subHdr.setOpaque(false);

        JLabel subLbl = new JLabel("Previous Employer " + prevEmpCount);
        subLbl.setFont(new Font("Arial Black", Font.BOLD, 12));
        subLbl.setForeground(accentGreen);

        JButton removeBtn = buildRemoveButton();
        removeBtn.addActionListener(e -> {
            prevEmpListPanel.remove(sub);
            prevEmpListPanel.revalidate();
            prevEmpListPanel.repaint();
        });

        subHdr.add(subLbl,    BorderLayout.WEST);
        subHdr.add(removeBtn, BorderLayout.EAST);

        sub.add(subHdr);
        sub.add(vgap(12));

        JPanel r1 = row(2);
        r1.add(lf("Pag-IBIG MID No.", tf(pagIbig)));
        r1.add(lf("Company Code",     tf(code)));
        sub.add(r1);
        sub.add(vgap(10));

        JPanel r2 = row(2);
        r2.add(lf("From Date (YYYY-MM-DD)", tf(from)));
        r2.add(lf("To Date (YYYY-MM-DD)",   tf(to)));
        sub.add(r2);

        prevEmpListPanel.add(sub);
        prevEmpListPanel.add(vgap(10));
        prevEmpListPanel.revalidate();
        prevEmpListPanel.repaint();
    }

    private void addHeir(String mid, String name, String rel, String bdate) {

        heirCount++;

        JPanel sub = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 16, 16);
                g2.setColor(new Color(255, 99, 132, 60));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        sub.setOpaque(false);
        sub.setLayout(new BoxLayout(sub, BoxLayout.Y_AXIS));
        sub.setBorder(new EmptyBorder(14, 16, 16, 16));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setMaximumSize(new Dimension(Integer.MAX_VALUE, 210));

        JPanel subHdr = new JPanel(new BorderLayout());
        subHdr.setOpaque(false);

        JLabel subLbl = new JLabel("Heir / Dependent " + heirCount);
        subLbl.setFont(new Font("Arial Black", Font.BOLD, 12));
        subLbl.setForeground(accentRed);

        JButton removeBtn = buildRemoveButton();
        removeBtn.addActionListener(e -> {
            heirListPanel.remove(sub);
            heirListPanel.revalidate();
            heirListPanel.repaint();
        });

        subHdr.add(subLbl,    BorderLayout.WEST);
        subHdr.add(removeBtn, BorderLayout.EAST);

        sub.add(subHdr);
        sub.add(vgap(12));

        JPanel r1 = row(2);
        r1.add(lf("Pag-IBIG MID No.",                    tf(mid)));
        r1.add(lf("Heir's Name (Last, First, Middle)",   tf(name)));
        sub.add(r1);
        sub.add(vgap(10));

        JPanel r2 = row(2);
        r2.add(lf("Relationship",
                cb(new String[]{
                        "Select", "Spouse", "Child", "Parent",
                        "Sibling", "Legal Heir", "Other"
                }, rel)));
        r2.add(lf("Birthdate (YYYY-MM-DD)", tf(bdate)));
        sub.add(r2);

        heirListPanel.add(sub);
        heirListPanel.add(vgap(10));
        heirListPanel.revalidate();
        heirListPanel.repaint();
    }

    // =========================================================================
    // HELPERS
    // =========================================================================
    private void calcIncome() {
        try {
            double a = Double.parseDouble(allowBasicField.getText().replace(",", ""));
            double b = Double.parseDouble(allowOtherField.getText().replace(",", ""));
            totalIncomeField.setText(String.format("%.2f", a + b));
        } catch (Exception ignored) {}
    }

    private JPanel sectionHeader(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel l = new JLabel(text);
        l.setForeground(accentGreen);
        l.setFont(new Font("Arial Black", Font.BOLD, 15));
        p.add(l, BorderLayout.WEST);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 14, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private JPanel lf(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setForeground(new Color(255, 255, 255, 180));
        l.setFont(new Font("Arial", Font.BOLD, 11));
        p.add(l,     BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JTextField tf(String value) {
        JTextField field = new JTextField(value) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(isFocusOwner()
                        ? new Color(96, 216, 164, 180)
                        : new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
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

    private JComboBox<String> cb(String[] items, String selected) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Arial", Font.PLAIN, 13));
        box.setForeground(Color.WHITE);
        box.setBackground(new Color(25, 40, 65));
        box.setBorder(BorderFactory.createEmptyBorder());
        box.setSelectedItem(selected);
        return box;
    }

    private JButton buildButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.darker() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(260, 46));
        return btn;
    }

    private JButton buildAddButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, getModel().isRollover() ? 15 : 8));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(96, 216, 164, getModel().isRollover() ? 200 : 120));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(accentGreen);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setPreferredSize(new Dimension(220, 36));
        btn.setMaximumSize(new Dimension(220, 36));
        return btn;
    }

    private JButton buildRemoveButton() {
        JButton btn = new JButton("✕ Remove");
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(new Color(255, 99, 132, 200));
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private Component vgap(int h) {
        return Box.createVerticalStrut(h);
    }

    // =========================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemberRecordForm(null).setVisible(true));
    }
}