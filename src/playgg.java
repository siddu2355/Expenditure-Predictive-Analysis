import javax.swing.JFrame;
import javax.swing.JLabel;

public class playgg {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hello, World!");
        JLabel label = new JLabel("Hello, World!");
        frame.add(label);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
