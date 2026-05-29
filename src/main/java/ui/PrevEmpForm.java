package ui;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
public class PrevEmpForm extends JPanel {
   private final Color darkBg1     = new Color(10, 22, 40);
   private final Color darkBg2     = new Color(21, 101, 192);
   private final Color accentGreen = new Color(96, 216, 164);
   private final Color accentRed   = new Color(255, 99, 132);
   private final Color textWhite   = Color.WHITE;
   private JPanel listPanel;
   private int empCount = 0;
   public List<PrevEmpEntry> entries = new ArrayList<>();
   public PrevEmpForm() {
       setLayout(new BorderLayout());
       // ── Gradient Background ──────────────────────────────────────────────
       JPanel bg = new JPanel(new BorderLayout()) {
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
       bg.setOpaque(true);
       // ── Glass Card ───────────────────────────────────────────────────────
       // BorderLayout: NORTH=title, CENTER=scroll(list+addBtn), SOUTH=buttons
       JPanel card = new JPanel(new BorderLayout()) {
           @Override
           protected void paintComponent(Graphics g) {
               Graphics2D g2 = (Graphics2D) g.create();
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                       RenderingHints.VALUE_ANTIALIAS_ON);
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
       card.setBorder(new EmptyBorder(40, 45, 36, 45));
       // ── NORTH: Title (never scrolls) ─────────────────────────────────────
       JLabel heading = new JLabel("Previous Employment Records");
       heading.setFont(new Font("Arial Black", Font.BOLD, 24));
       heading.setForeground(textWhite);
       JLabel subHeading = new JLabel(
               "Manage and review your previous employment history.");
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
       // ── CENTER: Scrollable list + Add button ─────────────────────────────
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
       // ── SOUTH: Return / Continue — always visible ─────────────────────────
       JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
       buttonPanel.setOpaque(false);
       buttonPanel.setBorder(new EmptyBorder(16, 0, 0, 0));
       JButton returnBtn   = buildButton("Back",   accentRed);
       JButton continueBtn = buildButton("Save", accentGreen);
       
       
       returnBtn.addActionListener(e -> {
    	    int choice = JOptionPane.showConfirmDialog(
    	            PrevEmpForm.this,
    	            "Are you sure you want to go back?\nUnsaved changes will be lost.",
    	            "Return to Sign Up",
    	            JOptionPane.YES_NO_OPTION,
    	            JOptionPane.WARNING_MESSAGE
    	    );

    	    if (choice == JOptionPane.YES_OPTION) {
    	        Window currentWindow = SwingUtilities.getWindowAncestor(PrevEmpForm.this);
    	        if (currentWindow != null) currentWindow.dispose();
    	        SwingUtilities.invokeLater(() -> new SignUpFrame().setVisible(true));
    	    }
    	});

       continueBtn.addActionListener(e ->
               JOptionPane.showMessageDialog(this,
                       "Member information submitted successfully!",
                       "Success", JOptionPane.INFORMATION_MESSAGE));
       
       buttonPanel.add(returnBtn);
       buttonPanel.add(continueBtn);
       card.add(buttonPanel, BorderLayout.SOUTH);
       // ── Card fills bg with fixed margins ─────────────────────────────────
       JPanel cardWrap = new JPanel(new BorderLayout());
       cardWrap.setOpaque(false);
       cardWrap.setBorder(new EmptyBorder(28, 28, 28, 28));
       cardWrap.add(card, BorderLayout.CENTER);
       bg.add(cardWrap, BorderLayout.CENTER);
       add(bg, BorderLayout.CENTER);
       addEntry();
   }
   // ── Add Entry ────────────────────────────────────────────────────────────
   public void addEntry() {
       empCount++;
       PrevEmpEntry entry = new PrevEmpEntry(empCount, this);
       entries.add(entry);
       listPanel.add(entry);
       listPanel.add(Box.createRigidArea(new Dimension(0, 14)));
       listPanel.revalidate();
       listPanel.repaint();
   }
   // ── Remove Entry ─────────────────────────────────────────────────────────
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
   // ── Entry Card ───────────────────────────────────────────────────────────
   public class PrevEmpEntry extends JPanel {
       private JLabel numberLabel;
       public JTextField pagIbigMidNoField;
       public JTextField companyCodeField;
       public JTextField fromDateField;
       public JTextField toDateField;
       public PrevEmpEntry(int number, PrevEmpForm parent) {
           setLayout(new BorderLayout());
           setOpaque(false);
           // header(~30) + gap(8) + divider(1) + gap(15) + row1(72) + gap(16) + row2(72) + padding(36) = ~250
           int H = 215;
           setMaximumSize(new Dimension(Integer.MAX_VALUE, H));
           setMinimumSize(new Dimension(0, H));
           setPreferredSize(new Dimension(0, H));
           setAlignmentX(Component.LEFT_ALIGNMENT);
           // ── Glass Inner ─────────────────────────────────────────────────
           JPanel inner = new JPanel(new BorderLayout(0, 0)) {
               @Override
               protected void paintComponent(Graphics g) {
                   Graphics2D g2 = (Graphics2D) g.create();
                   g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
                   g2.setColor(new Color(255, 255, 255, 12));
                   g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                   g2.setColor(new Color(255, 255, 255, 35));
                   g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                   g2.dispose();
                   super.paintComponent(g);
               }
           };
           inner.setOpaque(false);
           inner.setBorder(new EmptyBorder(18, 20, 18, 20));
           // ── Header ──────────────────────────────────────────────────────
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
           // Divider
           JPanel divider = new JPanel();
           divider.setOpaque(true);
           divider.setBackground(new Color(255, 255, 255, 40));
           divider.setPreferredSize(new Dimension(0, 1));
           JPanel headerBlock = new JPanel(new BorderLayout(0, 8));
           headerBlock.setOpaque(false);
           headerBlock.add(header,  BorderLayout.NORTH);
           headerBlock.add(divider, BorderLayout.SOUTH);
           // ── Fields ──────────────────────────────────────────────────────
           JPanel fields = new JPanel();
           fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
           fields.setOpaque(false);
           JPanel r1 = row(2);
           r1.add(fieldPanel("PAG-IBIG MID NO.",      pagIbigMidNoField = buildTextField()));
           r1.add(fieldPanel("COMPANY CODE",           companyCodeField  = buildTextField()));
           JPanel r2 = row(2);
           r2.add(fieldPanel("FROM DATE (YYYY-MM-DD)", fromDateField     = buildTextField()));
           r2.add(fieldPanel("TO DATE (YYYY-MM-DD)",   toDateField       = buildTextField()));
           fields.add(Box.createVerticalStrut(12));
           fields.add(r1);
           fields.add(Box.createVerticalStrut(10));
           fields.add(r2);
           inner.add(headerBlock, BorderLayout.NORTH);
           inner.add(fields,      BorderLayout.CENTER);
           add(inner, BorderLayout.CENTER);
       }
       public void updateNumber(int n) {
           numberLabel.setText("Previous Employer " + n);
       }
   }
   // ── Styled TextField ────────────────────────────────────────────────────
   private JTextField buildTextField() {
       JTextField field = new JTextField() {
           @Override
           protected void paintComponent(Graphics g) {
               Graphics2D g2 = (Graphics2D) g.create();
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                       RenderingHints.VALUE_ANTIALIAS_ON);
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
   // ── Styled Button ───────────────────────────────────────────────────────
   private JButton buildButton(String text, Color color) {
       JButton btn = new JButton(text) {
           @Override
           protected void paintComponent(Graphics g) {
               Graphics2D g2 = (Graphics2D) g.create();
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                       RenderingHints.VALUE_ANTIALIAS_ON);
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
   // ── Label + Field ───────────────────────────────────────────────────────
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
   // ── Row ─────────────────────────────────────────────────────────────────
   private JPanel row(int cols) {
       JPanel p = new JPanel(new GridLayout(1, cols, 18, 0));
       p.setOpaque(false);
       p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
       p.setPreferredSize(new Dimension(0, 60));
       p.setMinimumSize(new Dimension(0, 60));
       return p;
   }
   // ── Main ────────────────────────────────────────────────────────────────
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

