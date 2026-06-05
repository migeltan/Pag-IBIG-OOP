package ui.views;

import dao.HeirsDAO;
import models.HeirsTable;
import ui.frames.SignInFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class HeirsFormView extends JPanel {

    private final Color darkBg1     = new Color(10, 22, 40);
    private final Color darkBg2     = new Color(21, 101, 192);
    private final Color accentGreen = new Color(96, 216, 164);
    private final Color accentRed   = new Color(255, 99, 132);
    private final Color textWhite   = Color.WHITE;

    private JPanel listPanel;
    private int heirCount = 0;
    public List<HeirEntry> entries = new ArrayList<>();

    private final String loggedInMID;

    // ── Constructors ─────────────────────────────────────────────────────────
    public HeirsFormView(String mid) {
        this.loggedInMID = mid;
        initUI();
    }

    public HeirsFormView() {
        this.loggedInMID = null;
        initUI();
    }

    // ── Init ─────────────────────────────────────────────────────────────────
    private void initUI() {
        setLayout(new BorderLayout());

        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, darkBg1, getWidth(), getHeight(), darkBg2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(4, 8, getWidth() - 8, getHeight() - 8, 24, 24);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 24, 24);
                g2.setColor(new Color(255, 255, 255, 45));
                g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 24, 24);
                g2.setColor(accentGreen);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(16, 0, getWidth() - 20, 0);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(1000, 700));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(40, 45, 40, 45));

        // ── Header ───────────────────────────────────────────────────────────
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Heirs & Dependents");
        heading.setFont(new Font("Arial Black", Font.BOLD, 24));
        heading.setForeground(textWhite);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subHeading = new JLabel("View and update heirs and dependents information.");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 13));
        subHeading.setForeground(new Color(255, 255, 255, 160));
        subHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(heading);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        headerPanel.add(subHeading);

        // ── List Panel (scrollable) ───────────────────────────────────────────
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // ── Buttons ───────────────────────────────────────────────────────────
        JButton addBtn  = buildButton("+ Add Heir", accentGreen);
        JButton saveBtn = buildButton("Save",        accentGreen);
        JButton backBtn = buildButton("Back",         accentRed);

        addBtn.addActionListener(e -> addNewEntry());

        saveBtn.addActionListener(e -> saveAllHeirs());

        backBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(HeirsFormView.this);
            if (window != null) window.dispose();
            new SignInFrame(loggedInMID);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(18, 0, 0, 0));
        buttonPanel.add(addBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(backBtn);

        // ── Content wrapper ───────────────────────────────────────────────────
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(20, 0, 0, 0));
        content.add(scroll, BorderLayout.CENTER);

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(content,     BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        bg.add(card);
        add(bg, BorderLayout.CENTER);

        // ── Load data ─────────────────────────────────────────────────────────
        if (loggedInMID != null && !loggedInMID.isEmpty()) {
            loadHeirsFromDB(loggedInMID);
        } else {
            addEntry(null); // start with one blank entry for fallback/dummy
            loadDummyData();
        }
    }

    // ── Load from DB ──────────────────────────────────────────────────────────
    private void loadHeirsFromDB(String mid) {
        HeirsDAO dao = new HeirsDAO();
        List<HeirsTable> heirsList = dao.getHeirsByMID(mid);

        if (heirsList.isEmpty()) {
            // No heirs yet — give one blank entry to fill in
            addEntry(null);
        } else {
            for (HeirsTable h : heirsList) {
                addEntry(h);
            }
        }
    }

    // ── Save all heirs to DB ──────────────────────────────────────────────────
    private void saveAllHeirs() {
        if (loggedInMID == null || loggedInMID.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Cannot save: no member MID loaded.",
                "Save Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            HeirsDAO dao = new HeirsDAO();
            int successCount = 0;
            int failCount    = 0;

            for (HeirEntry entry : entries) {
                String name         = entry.heirsNameField.getText().trim();
                String relationship = (String) entry.heirsRelationshipBox.getSelectedItem();
                String bdText       = entry.heirsBirthdateField.getText().trim();

                if (name.isEmpty()) continue; // skip blank rows

                Date birthdate = null;
                if (!bdText.isEmpty()) {
                    try {
                        birthdate = Date.valueOf(bdText);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(this,
                            "Invalid date for heir \"" + name + "\". Use YYYY-MM-DD format.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                HeirsTable h = new HeirsTable(
                    loggedInMID,
                    entry.heirCode,   // 0 = new record, >0 = existing
                    name,
                    "Select".equals(relationship) ? null : relationship,
                    birthdate
                );

                boolean ok;
                if (entry.heirCode > 0) {
                    ok = dao.updateHeir(h);
                } else {
                    ok = dao.insertHeir(h);
                }

                if (ok) successCount++;
                else    failCount++;
            }

            if (failCount == 0) {
                JOptionPane.showMessageDialog(this,
                    successCount + " heir record(s) saved successfully.",
                    "Saved", JOptionPane.INFORMATION_MESSAGE);
                // Reload to get DB-assigned Heir_Codes for new rows
                reloadAfterSave();
            } else {
                JOptionPane.showMessageDialog(this,
                    successCount + " saved, " + failCount + " failed.",
                    "Partial Save", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "An error occurred while saving:\n" + ex.getMessage(),
                "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Reload entries after save (refreshes heirCodes) ──────────────────────
    private void reloadAfterSave() {
        entries.clear();
        listPanel.removeAll();
        heirCount = 0;
        if (loggedInMID != null) loadHeirsFromDB(loggedInMID);
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Add entry with optional DB data ──────────────────────────────────────
    public void addEntry(HeirsTable data) {
        heirCount++;
        HeirEntry entry = new HeirEntry(heirCount, this, data);
        entries.add(entry);
        listPanel.add(entry);
        listPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ── Add blank new entry (for Add Heir button) ─────────────────────────────
    public void addNewEntry() {
        addEntry(null);
        // Scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollPane sp = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, listPanel);
            if (sp != null) {
                JScrollBar sb = sp.getVerticalScrollBar();
                sb.setValue(sb.getMaximum());
            }
        });
    }

    // ── Dummy data fallback ───────────────────────────────────────────────────
    private void loadDummyData() {
        if (entries.isEmpty()) return;
        HeirEntry h1 = entries.get(0);
        h1.pagIbigMidNoField.setText("1234-5678-9012");
        h1.heirsNameField.setText("Dela Cruz, Maria Santos");
        h1.heirsBirthdateField.setText("1998-05-21");
        h1.heirsRelationshipBox.setSelectedItem("Spouse");
    }

    // ── Remove entry ──────────────────────────────────────────────────────────
    public void removeEntry(HeirEntry entry) {
        if (entries.size() == 1) {
            JOptionPane.showMessageDialog(this,
                "At least one heir entry is required.",
                "Cannot Remove", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // If it has a DB record, delete it
        if (entry.heirCode > 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this heir from the database?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;

            HeirsDAO dao = new HeirsDAO();
            dao.deleteHeir(entry.heirCode);
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

    // ═════════════════════════════════════════════════════════════════════════
    // ── Heir Entry Inner Class ────────────────────────────────────────────────
    // ═════════════════════════════════════════════════════════════════════════
    public class HeirEntry extends JPanel {

        private JLabel numberLabel;

        public JTextField pagIbigMidNoField;
        public JTextField heirsNameField;
        public JTextField heirsBirthdateField;
        public JComboBox<String> heirsRelationshipBox;

        // Tracks the DB primary key; 0 = unsaved new row
        public int heirCode = 0;

        public HeirEntry(int number, HeirsFormView parent, HeirsTable data) {
            setLayout(new BorderLayout());
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));

            JPanel inner = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 12));
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                    g2.setColor(new Color(255, 255, 255, 35));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            inner.setOpaque(false);
            inner.setLayout(new BorderLayout(0, 15));
            inner.setBorder(new EmptyBorder(18, 20, 18, 20));

            // ── Header ───────────────────────────────────────────────────────
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);

            numberLabel = new JLabel("Heir / Dependent " + number);
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

            // ── Fields ───────────────────────────────────────────────────────
            JPanel fields = new JPanel();
            fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
            fields.setOpaque(false);

            JPanel r1 = row(2);
            r1.add(fieldPanel("PAG-IBIG MID NO.", pagIbigMidNoField  = buildTextField()));
            r1.add(fieldPanel("HEIR'S NAME",      heirsNameField     = buildTextField()));

            JPanel r2 = row(2);
            r2.add(fieldPanel("RELATIONSHIP",     heirsRelationshipBox = buildComboBox(new String[]{
                    "Select", "Spouse", "Child", "Parent", "Sibling", "Legal Heir", "Other"
            })));
            r2.add(fieldPanel("BIRTHDATE (YYYY-MM-DD)", heirsBirthdateField = buildTextField()));

            fields.add(r1);
            fields.add(Box.createRigidArea(new Dimension(0, 16)));
            fields.add(r2);

            inner.add(header, BorderLayout.NORTH);
            inner.add(fields, BorderLayout.CENTER);
            add(inner, BorderLayout.CENTER);

            // ── Populate if data provided ─────────────────────────────────────
            if (data != null) {
                heirCode = data.getHeirCode();
                pagIbigMidNoField.setText(safe(data.getPagIbigMIDNo()));
                heirsNameField.setText(   safe(data.getHeirsName()));
                if (data.getHeirsBirthdate() != null)
                    heirsBirthdateField.setText(data.getHeirsBirthdate().toString());
                setCombo(heirsRelationshipBox, data.getHeirsRelationship());

                // Pag-IBIG MID is always locked (it's the FK, not user-editable)
                pagIbigMidNoField.setEditable(false);
                pagIbigMidNoField.setFocusable(false);
            } else {
                // New row — pre-fill MID from parent, lock it
                if (loggedInMID != null) pagIbigMidNoField.setText(loggedInMID);
                pagIbigMidNoField.setEditable(false);
                pagIbigMidNoField.setFocusable(false);
            }
        }

        public void updateNumber(int n) {
            numberLabel.setText("Heir / Dependent " + n);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private String safe(String s) { return s != null ? s : ""; }

    private void setCombo(JComboBox<String> box, String value) {
        if (value == null || value.isEmpty()) return;
        for (int i = 0; i < box.getItemCount(); i++) {
            if (box.getItemAt(i).equalsIgnoreCase(value)) {
                box.setSelectedIndex(i);
                return;
            }
        }
        box.addItem(value);
        box.setSelectedItem(value);
    }

    // ── Styled TextField ──────────────────────────────────────────────────────
    private JTextField buildTextField() {
        JTextField field = new JTextField() {
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

    // ── Styled ComboBox ───────────────────────────────────────────────────────
    private JComboBox<String> buildComboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Arial", Font.PLAIN, 14));
        box.setForeground(Color.WHITE);
        box.setBackground(new Color(25, 35, 60));
        box.setBorder(BorderFactory.createEmptyBorder());
        return box;
    }

    // ── Styled Button ─────────────────────────────────────────────────────────
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
        btn.setPreferredSize(new Dimension(180, 46));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setForeground(new Color(10, 22, 40));
        btn.setFont(new Font("Arial Black", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Field Panel ───────────────────────────────────────────────────────────
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

    // ── Row ───────────────────────────────────────────────────────────────────
    private JPanel row(int cols) {
        JPanel p = new JPanel(new GridLayout(1, cols, 18, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        return p;
    }

    // ── Main ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Heirs Form View");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1200, 850);
            f.setLocationRelativeTo(null);
            f.setContentPane(new HeirsFormView());
            f.setVisible(true);
        });
    }
}