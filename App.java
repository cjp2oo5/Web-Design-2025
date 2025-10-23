import javax.swing.SwingUtilities;

/**
 * Main application class and entry point for the
 * Cybersecurity Complaint System.
 *
  
 */
public class App {
    
     private static ComplaintManager manager = new ComplaintManager();
    
    /**
     * Provides global access to the single ComplaintManager instance.
     * @return The shared ComplaintManager.
     */
    public static ComplaintManager getManager() { 
        return manager; 
    }

    
    public static void main(String[] args) {
        
        // 1. Load sample data into the manager
        try {
            manager.submitComplaint(new Complaint(
                IDGenerator.nextComplaintId(),
                "Phishing attempt",
                "I received a phishing email asking for credentials",
                "U2001",
                1 // High Priority
            ));
            manager.submitComplaint(new Complaint(
                IDGenerator.nextComplaintId(),
                "Fake website",
                "Fake banking site collecting user data",
                "U2003",
                2 // Medium Priority
            ));
            manager.submitComplaint(new Complaint(
                IDGenerator.nextComplaintId(),
                "Account hacked",
                "My social account was accessed without permission",
                "U2002",
                1 // High Priority
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Launch the main UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            RootFrame ui = new RootFrame();
            ui.setVisible(true);
        });
    }
}