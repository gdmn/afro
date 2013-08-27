package afro.gui;

import afro.xmltree.Leaf;
import afro.xmltree.xmlparser.DefaultXmlParser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

/**
 *
 * @author dmn
 */
public class SimpleGUI
        extends JPanel
        implements ActionListener {

    private static final String APPNAME = "XML Tree";
//    private static final String FEED_URL = "http://dmn.jogger.pl/atom/content/100/";
//    private static final String FEED_URL = "file:///home/dmn/Pulpit/opml.xml";
//    private static final String FEED_URL="http://www.google.com/reader/public/atom/user%2F12890149063310591021%2Fstate%2Fcom.google%2Fstarred";
    MyXmlTree tree;
    JTextComponent text;
    JFrame frame;

    public void setURL(String url) {
        Leaf root = null;
        try {
            root = new DefaultXmlParser(url).parse();
            tree.setRootLeaf(root);
            frame.setTitle(APPNAME + " " + url);
            tree.requestFocusInWindow();
            tree.setSelectionRow(0);
        } catch (Exception ex) {
            Logger.getLogger(SimpleGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public SimpleGUI() {
        super(new BorderLayout());

        text = new JTextPane();
        JScrollPane textScrollPane = new JScrollPane(text);
        textScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        text.setEditable(false);
        textScrollPane.setPreferredSize(new Dimension(300, 600));

        //Construct the tree.
        tree = new MyXmlTree(null, text);

        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setPreferredSize(new Dimension(500, 600));
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, treeScrollPane, textScrollPane);
        splitPane.setOneTouchExpandable(true);
        add(splitPane);
    }

    /**
     * Required by the ActionListener interface.
     * Handle events on the showDescendant and
     * showAncestore buttons.
     */
    @Override
    public void actionPerformed(ActionEvent action) {
        String actionCommand = action.getActionCommand();
        if (ACTION_QUIT.equals(actionCommand)) {
            frame.setVisible(false);
            getToolkit().getSystemEventQueue().postEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        } else if (ACTION_OPEN_URL.equals(actionCommand)) {
            String s = (String) JOptionPane.showInputDialog(
                    frame,
                    "URL to open:",
                    "Open URL",
                    JOptionPane.QUESTION_MESSAGE);
            if (s != null && !s.isEmpty()) {
                setURL(s);
            }
        } else if (ACTION_OPEN_FILE.equals(actionCommand)) {
            if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(frame)) {
                setURL(fileChooser.getSelectedFile().toURI().toString());
            }
        }
    }
    final private JFileChooser fileChooser = new JFileChooser();

    private JMenuBar creteMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu fileItem = new JMenu("File");
        fileItem.add(createMenuItem(ACTION_OPEN_URL, "Open URL", KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK)));
        fileItem.add(createMenuItem(ACTION_OPEN_FILE, "Open file", KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)));
        fileItem.addSeparator();
        fileItem.add(createMenuItem(ACTION_QUIT, "Exit", KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK)));
        bar.add(fileItem);
        return bar;
    }
    private final static Random rnd = new Random();
    private final static String ACTION_OPEN_URL = Long.toString(rnd.nextLong());
    private final static String ACTION_OPEN_FILE = Long.toString(rnd.nextLong());
    private final static String ACTION_QUIT = Long.toString(rnd.nextLong());

    private JMenuItem createMenuItem(String action, String text, KeyStroke accelerator) {
        JMenuItem item = new JMenuItem();
        item.setText(text);
        item.setActionCommand(action);
        if (accelerator != null) {
            item.setAccelerator(accelerator);
        }
        item.addActionListener(this);
        return item;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        final JFrame frame = new JFrame(APPNAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        SimpleGUI newContentPane = new SimpleGUI();
        newContentPane.frame = frame;
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        frame.setJMenuBar(newContentPane.creteMenuBar());

        //Handle quit
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                System.out.println("BYE");
                System.exit(0); //calling the method is a must
            }
        });

        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    Logger.getLogger(SimpleGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                createAndShowGUI();
            }
        });
    }
}
