import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel; 

 
class RootFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public RootFrame() {
        setTitle("Cybersecurity Complaint System (DS/OOP Project)");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create the three main "views"
        MainMenuPanel menu = new MainMenuPanel(mainPanel, cardLayout);
        ClientPortalPanel clientPortal = new ClientPortalPanel(mainPanel, cardLayout);
        OfficerPortalPanel officerPortal = new OfficerPortalPanel(mainPanel, cardLayout);
        
        // Add them to the main panel
        mainPanel.add(menu, "MENU");
        mainPanel.add(clientPortal, "CLIENT_PORTAL");
        mainPanel.add(officerPortal, "OFFICER_PORTAL");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "MENU"); 
        
    }
}

/**
 * The main menu screen, showing the two portal options.
 */
class MainMenuPanel extends JPanel {
    public MainMenuPanel(JPanel mainPanel, CardLayout cardLayout) {
        setLayout(new GridBagLayout());
        setBackground(new Color(30, 40, 51)); 
        
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridy = 0;

        JLabel title = new JLabel("Cybersecurity Complaint System");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        add(title, gbc);
        
        gbc.gridy++;
        JButton clientButton = new JButton("Client Portal (Submit & Track Complaint)");
        clientButton.setFont(new Font("Arial", Font.BOLD, 18));
        clientButton.addActionListener(e -> cardLayout.show(mainPanel, "CLIENT_PORTAL"));
        add(clientButton, gbc);
        
        gbc.gridy++;
        JButton officerButton = new JButton("Officer Portal (Process Complaints)");
        officerButton.setFont(new Font("Arial", Font.BOLD, 18));
        officerButton.addActionListener(e -> cardLayout.show(mainPanel, "OFFICER_PORTAL"));
        add(officerButton, gbc);
    }
}

/**
 * An abstract base panel for the Client and Officer portals.
 */
abstract class PortalPanel extends JPanel {
     protected Color colorHeaderBlue = new Color(0, 114, 198);
    protected Color colorNavDark = new Color(51, 51, 51);
    protected Color colorNavHover = new Color(80, 80, 80);
    protected Color colorSectionBorder = new Color(220, 220, 220);
    
    protected JPanel contentPanel;  
    protected CardLayout contentCardLayout;

    public PortalPanel(JPanel rootPanel, CardLayout rootCardLayout, String title) {
        setLayout(new BorderLayout());
        
        // 1. Header Panel (NORTH) with "Back" button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(colorHeaderBlue);
        JLabel appTitle = new JLabel("  " + title); 
        appTitle.setFont(new Font("Arial", Font.BOLD, 18));
        appTitle.setForeground(Color.WHITE);
        headerPanel.add(appTitle, BorderLayout.CENTER);
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> rootCardLayout.show(rootPanel, "MENU"));
        headerPanel.add(backButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Navigation Panel (WEST)
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(colorNavDark);
        navPanel.setPreferredSize(new Dimension(200, 0));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(navPanel, BorderLayout.WEST);

        // 3. Main Content Panel (CENTER)
        contentCardLayout = new CardLayout();
        contentPanel = new JPanel(contentCardLayout);
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);

         createNavigation(navPanel);
        createContentCards(contentPanel);
    }

     protected abstract void createNavigation(JPanel navPanel);
    protected abstract void createContentCards(JPanel contentPanel);
    protected JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(colorNavDark);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { button.setBackground(colorNavHover); }
            public void mouseExited(MouseEvent evt) { button.setBackground(colorNavDark); }
        });
        return button;
    }

    /** Helper method to create a styled section heading label. */
    protected JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(colorHeaderBlue);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, colorSectionBorder),
            BorderFactory.createEmptyBorder(15, 0, 5, 0)
        ));
        return label;
    }
}

/**
 * The Client Portal, containing "Submit" and "Track" panels.
 */
class ClientPortalPanel extends PortalPanel {
    public ClientPortalPanel(JPanel rootPanel, CardLayout rootCardLayout) {
        super(rootPanel, rootCardLayout, "Client Portal");
    }

    @Override
    protected void createNavigation(JPanel navPanel) {
        JButton addBtn = createNavButton("1. Submit Complaint");   
        JButton trackSingleBtn = createNavButton("2. Track by ID");
        
        navPanel.add(addBtn);
        navPanel.add(trackSingleBtn);
        navPanel.add(Box.createVerticalGlue()); 

        addBtn.addActionListener(e -> contentCardLayout.show(contentPanel, "Submit"));
        trackSingleBtn.addActionListener(e -> contentCardLayout.show(contentPanel, "Track"));
    }

    @Override
    protected void createContentCards(JPanel contentPanel) {
        contentPanel.add(new SubmitComplaintPanel(this), "Submit");
        contentPanel.add(new TrackComplaintPanel(this), "Track");
    }
}

/**
 * The Officer Portal, containing "Process" (PQ) and "View All" (LL) panels.
 */
class OfficerPortalPanel extends PortalPanel {
    private OfficerDashboardPanel dashboardPanel; // Reference to the "View All" panel
    private ProcessComplaintPanel processPanel; // Reference to the "Process" panel

    public OfficerPortalPanel(JPanel rootPanel, CardLayout rootCardLayout) {
        super(rootPanel, rootCardLayout, "Officer Portal");
    }

    @Override
    protected void createNavigation(JPanel navPanel) {
        JButton processBtn = createNavButton("1. Process Next (PQ)");   
        JButton viewAllBtn = createNavButton("2. View Full Log (LL)");
        
        navPanel.add(processBtn);
        navPanel.add(viewAllBtn);
        navPanel.add(Box.createVerticalGlue()); 

        // Add actions to refresh panels when they are shown
        processBtn.addActionListener(e -> {
            processPanel.refreshView(); // Refresh PQ view
            contentCardLayout.show(contentPanel, "Process");
        });
        viewAllBtn.addActionListener(e -> {
            dashboardPanel.refreshDashboard(); // Refresh LL view
            contentCardLayout.show(contentPanel, "Dashboard");
        });
    }

    @Override
    protected void createContentCards(JPanel contentPanel) {
        processPanel = new ProcessComplaintPanel(this);
        dashboardPanel = new OfficerDashboardPanel(this);
        contentPanel.add(processPanel, "Process");
        contentPanel.add(dashboardPanel, "Dashboard");
    }
}

/**
 * Panel for Submitting Complaints (Client Portal).
 */
class SubmitComplaintPanel extends JPanel {
    private ComplaintManager manager;
    private JTextField titleField, priorityField, reporterField;
    private JTextArea descArea;
    private JButton submitBtn;
    private Color colorSubmitGreen = new Color(40, 167, 69);
    private PortalPanel portal; // Reference to parent portal for helpers

    public SubmitComplaintPanel(PortalPanel portal) {
        this.portal = portal;
        manager = App.getManager(); 
        setupUI();
    }
    
    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(Color.WHITE);
        JLabel title = new JLabel("1. Submit New Complaint");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        titleBar.add(title, BorderLayout.WEST);
        submitBtn = new JButton("Submit Complaint");
        submitBtn.setBackground(colorSubmitGreen);
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        submitBtn.addActionListener(e -> onSubmitComplaint());
        titleBar.add(submitBtn, BorderLayout.EAST);
        add(titleBar, BorderLayout.NORTH);

        // Form Panel
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(portal.createSectionLabel("Complaint Details"), gbc);
        gbc.gridy++; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        form.add(new JLabel("Complaint Title:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        titleField = new JTextField(20); form.add(titleField, gbc);
        gbc.gridx = 2; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        form.add(new JLabel("Priority (1=High):"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5; gbc.fill = GridBagConstraints.HORIZONTAL;
        priorityField = new JTextField("2", 5); form.add(priorityField, gbc);
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        form.add(new JLabel("Reporter ID (e.g., U2001):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        reporterField = new JTextField(); form.add(reporterField, gbc);
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(portal.createSectionLabel("Description and Details"), gbc);
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        form.add(new JLabel("Detailed Description:"), gbc);
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        descArea = new JTextArea(10, 50);
        form.add(new JScrollPane(descArea), gbc);
        add(form, BorderLayout.CENTER);
    }

    /** Logic to handle the submit button click. */
    private void onSubmitComplaint() {
        String id = IDGenerator.nextComplaintId(); //
        String title = titleField.getText().trim();
        String desc = descArea.getText().trim();
        String reporter = reporterField.getText().trim();
        int pr = 2;
        
        try {
            String prText = priorityField.getText().trim();
            if (prText.isEmpty()) throw new ValidationException("Priority cannot be empty."); //
            pr = Integer.parseInt(prText);
            if (pr < 1) pr = 1;
        } catch (NumberFormatException ex) { 
            JOptionPane.showMessageDialog(this, "Error: Priority must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; 
        } catch (ValidationException ve) { //
             JOptionPane.showMessageDialog(this, "Error: " + ve.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            return; 
        }
        
        Complaint c = new Complaint(id, title, desc, reporter, pr); //
        
        try {
            manager.submitComplaint(c); //
            JOptionPane.showMessageDialog(this, 
                String.format("Complaint Submitted!%nID: %s%nTitle: %s", id, title), 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            titleField.setText(""); descArea.setText(""); priorityField.setText("2"); 
        } catch (ValidationException ve) { //
            JOptionPane.showMessageDialog(this, "Submission Error: " + ve.getMessage(), "Submission Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}

/**
 * Panel for Officer Dashboard (Full Log view).
 * Shows all complaints from the LinkedList in a modern JTable.
 */
class OfficerDashboardPanel extends JPanel {
    private ComplaintManager manager;
    private JTable complaintTable; 
    private DefaultTableModel tableModel; 

    public OfficerDashboardPanel(PortalPanel portal) {
        manager = App.getManager(); 
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        top.setBackground(Color.WHITE);
        JLabel title = new JLabel("Full Complaint Log (Chronological)");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        top.add(title);
        top.add(Box.createHorizontalStrut(50));
        JButton refresh = new JButton("Refresh (from Linked List)");
        refresh.addActionListener(e -> refreshDashboard());
        top.add(refresh);
        add(top, BorderLayout.NORTH);
        
        String[] columnNames = {"ID", "Title", "Status", "Priority", "Reporter ID", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        complaintTable = new JTable(tableModel);
        complaintTable.setFillsViewportHeight(true);
        complaintTable.setRowHeight(25);
        complaintTable.setFont(new Font("Arial", Font.PLAIN, 14));
        complaintTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        add(new JScrollPane(complaintTable), BorderLayout.CENTER);
        
        refreshDashboard();
    }

    /** Refreshes the display with data from the LinkedList into the JTable. */
    public void refreshDashboard() {
        tableModel.setRowCount(0); 
        MyLinkedList<Complaint> list = manager.listChronological(); //
        if (list.size() > 0) { //
            for (Complaint c: list) { //
                Object[] row = {
                    c.getComplaintId(),     //
                    c.getTitle(),         //
                    c.getStatus(),        //
                    c.getPriority(),      //
                    c.getReporterId(),    //
                    c.getCreatedAt().toLocalDate().toString() //
                };
                tableModel.addRow(row);
            }
        }
    }
}


class TrackComplaintPanel extends JPanel {
    private ComplaintManager manager;
    private JTextField trackIdField;
    private JTextArea displayArea;
    private JButton trackBtn; // Declare the button at the class level

    public TrackComplaintPanel(PortalPanel portal) {
        manager = App.getManager();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Simplified Top Panel for Debugging ---
        JPanel top = new JPanel(); // Use default FlowLayout for simplicity
        top.setBackground(Color.LIGHT_GRAY); // Make it visible

        JLabel titleLabel = new JLabel("Track Complaint by ID:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        top.add(titleLabel);

        trackIdField = new JTextField(12);
        top.add(trackIdField);

        trackBtn = new JButton(">>> TRACK <<<");  
        trackBtn.setFont(new Font("Arial", Font.BOLD, 14));
        trackBtn.addActionListener(e -> onTrackComplaint());

         top.add(trackBtn);
 
        add(top, BorderLayout.NORTH);
 
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        displayArea.setText("\nEnter a Complaint ID and click 'Track Complaint'.\n\n(Uses the custom Binary Search Tree for fast lookup)");
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

      }

     private void onTrackComplaint(){
        String id = trackIdField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Complaint ID to track.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Complaint c = manager.findById(id); //

        if (c == null) {
            displayArea.setText(String.format("No complaint found with ID: %s", id));
        } else {
            displayArea.setText(String.format("--- COMPLAINT FOUND (BST LOOKUP) ---\n\n%s", c.toString())); //
        }
    }
}
 
class ProcessComplaintPanel extends JPanel {
    private ComplaintManager manager;
    private JTextArea displayArea;
    private JButton processButton;

    public ProcessComplaintPanel(PortalPanel portal) {
        manager = App.getManager(); 
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE); 
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        JLabel title = new JLabel("1. Process Next Urgent Complaint (Priority Queue)");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        top.add(title, BorderLayout.WEST);
        add(top, BorderLayout.NORTH);
        
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        
        processButton = new JButton("Enforce / Process This Complaint");
        processButton.setFont(new Font("Arial", Font.BOLD, 16));
        processButton.setBackground(new Color(220, 53, 69)); // Red color
        processButton.setForeground(Color.WHITE);
        processButton.addActionListener(e -> onProcessComplaint());
        add(processButton, BorderLayout.SOUTH);
        
        refreshView();
    }
    
    /** Updates the text area with the next complaint from the Priority Queue. */
    public void refreshView() {
        Complaint next = manager.peekNextComplaint(); //
        int count = manager.getPendingQueueSize(); //
        
        if (next == null) {
            displayArea.setText(String.format("\n--- No pending complaints in the queue. ---", count));
            processButton.setEnabled(false);
        } else {
            displayArea.setText(String.format(
                "--- PENDING COMPLAINTS: %d ---%n%n" +
                "NEXT URGENT COMPLAINT (from Priority Queue):%n%n" +
                "ID:       %s%n" +
                "PRIORITY: %d (1 is highest)%n" +
                "TITLE:    %s%n" +
                "STATUS:   %s%n" +
                "DETAILS:  %s",
                count,
                next.getComplaintId(),
                next.getPriority(),
                next.getTitle(),
                next.getStatus(),
                next.getDescription()
            ));
            processButton.setEnabled(true);
        }
    }

    /** Called when the "Process" button is clicked. */
    private void onProcessComplaint() {
        Complaint processed = manager.processNextComplaint(); //
        
        if (processed != null) {
            JOptionPane.showMessageDialog(this, 
                "Successfully processed complaint: " + processed.getComplaintId(),
                "Complaint Processed",
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        refreshView();  
    }
}