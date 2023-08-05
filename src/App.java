import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

class GUIgraph extends JPanel {

    private int[] expenses;
    private int numMonths;

    public GUIgraph(int[] expenses, int numMonths) {
        this.expenses = expenses;
        this.numMonths = numMonths;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        // Draw x-axis
        g.drawLine(50, 250, 50 + (numMonths * 100), 250);
        for (int i = 1; i <= numMonths; i++) {
            g.drawLine(50 + (i * 100), 250, 50 + (i * 100), 260);
            g.drawString("Month " + i, 50 + (i * 100) - 25, 275);
        }

        // Draw y-axis
        g.drawLine(50, 50, 50, 250);
        for (int i = 0; i <= 10; i++) {
            g.drawLine(50, 250 - (i * 20), 60, 250 - (i * 20));
            g.drawString("" + i * 1000, 15, 250 - (i * 20));
        }

        // Plot expenses
        g.setColor(Color.RED);
        for (int i = 0; i < numMonths; i++) {
            int x = 50 + (i * 100) + 50;
            int y = 250 - (expenses[i] / 100);
            g.fillOval(x - 5, y - 5, 10, 10);
            if (i > 0) {
                int prevX = 50 + ((i - 1) * 100) + 50;
                int prevY = 250 - (expenses[i - 1] / 100);
                g.drawLine(prevX, prevY, x, y);
            }
        }
    }

    void graph() {
        JFrame frame = new JFrame("Monthly Expenses Graph");
        frame.setBounds(395, 230, 200 + (numMonths * 100), 400);
        GUIgraph panel = new GUIgraph(expenses, numMonths);
        frame.add(panel);
        frame.setVisible(true);
    }
}

public class App extends JFrame {
    char[] p;
    String u, Gname;
    Connection conn;
    JButton l = new JButton("Login"), r = new JButton("Register");
    JLabel exp = new JLabel("Expenditure Predictive Analysis");
    int size;

    public App() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(350, 80, 900, 600);
        setTitle("Expenditure Predictive Analysis");
        setLayout(null);
        try {
            String url = "jdbc:mysql://localhost:3306/expend_stats";
            conn = DriverManager.getConnection(url, "root", "#$iddu2355");
            exp.setBounds(250, 0, 1000, 300);
            exp.setFont(new Font("Arial", Font.BOLD, 24));
            add(exp);
            l.setBounds(330, 220, 170, 40);
            add(l);
            r.setBounds(330, 270, 170, 40);
            add(r);

            l.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    r.setVisible(false);
                    l.setVisible(false);
                    exp.setVisible(false);
                    Linput("Login");
                }
            });

            r.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    r.setVisible(false);
                    l.setVisible(false);
                    exp.setVisible(false);
                    Rinput("Register");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        setVisible(true);
    }

    void Linput(String title) {
        setTitle(title + " Form");
        JLabel username = new JLabel("USERNAME"), password = new JLabel("PASSWORD"),
                m1 = new JLabel(),
                m2 = new JLabel();
        JButton LRButton = new JButton(title), back = new JButton("back");
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        back.setBounds(770, 30, 80, 35);
        add(back);
        username.setBounds(280, 138, 120, 40);
        add(username);
        usernameField.setBounds(395, 140, 200, 40);
        add(usernameField);
        password.setBounds(280, 218, 120, 40);
        add(password);
        passwordField.setBounds(395, 220, 200, 40);
        add(passwordField);
        m1.setBounds(340, 175, 200, 40);
        m1.setFont(new Font("Arial", Font.PLAIN, 15));
        m1.setForeground(Color.red);
        add(m1);
        m2.setBounds(340, 255, 200, 40);
        m2.setFont(new Font("Arial", Font.PLAIN, 15));
        m2.setForeground(Color.red);
        add(m2);
        LRButton.setBounds(370, 290, 120, 30);
        add(LRButton);
        m1.setVisible(false);
        m2.setVisible(false);

        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                r.setVisible(true);
                l.setVisible(true);
                exp.setVisible(true);

                username.setVisible(false);
                usernameField.setVisible(false);
                m1.setVisible(false);
                m2.setVisible(false);
                LRButton.setVisible(false);
                password.setVisible(false);
                passwordField.setVisible(false);
                back.setVisible(false);
                setTitle("Expenditure Predictive Analysis");
            }
        });
        LRButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m1.setVisible(false);
                m2.setVisible(false);
                u = usernameField.getText();
                p = passwordField.getPassword();
                try {
                    if (u.length() != 0 && new String(p).length() != 0) {
                        String query = String.format("SELECT * FROM users WHERE username = \'%s\'", u);
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        String un, pw;
                        if (rs.next() != false) {
                            un = rs.getString("username");
                            Gname = un;
                            pw = rs.getString("pass");
                            if (new String(p).equals(pw)) {
                                username.setVisible(false);
                                usernameField.setVisible(false);
                                m1.setVisible(false);
                                m2.setVisible(false);
                                LRButton.setVisible(false);
                                password.setVisible(false);
                                passwordField.setVisible(false);
                                back.setVisible(false);
                                System.out.println("Logged In..");
                                expensesInput();
                            } else {
                                m2.setText("Incorrect Password");
                                m2.setVisible(true);
                            }
                        } else {
                            m1.setText("Incorrect Username");
                            m1.setVisible(true);
                        }
                    } else {
                        if (u.length() == 0 && new String(p).length() == 0) {
                            m1.setText("Username is Required");
                            m1.setVisible(true);
                            m2.setText("Password is Required");
                            m2.setVisible(true);
                        } else if (u.length() == 0) {
                            m1.setText("Username is Required");
                            m1.setVisible(true);
                        } else if (new String(p).length() == 0) {
                            m2.setText("Password is Required");
                            m2.setVisible(true);
                        }
                    }
                } catch (SQLException ec) {
                    System.out.println(ec.getMessage());
                }
            }
        });
    }

    void Rinput(String title) {
        setTitle(title + " Form");
        JLabel username = new JLabel("USERNAME"), password = new JLabel("PASSWORD"), name = new JLabel("NAME"),
                age = new JLabel("AGE"),
                m1 = new JLabel(),
                m2 = new JLabel(),
                m3 = new JLabel(),
                m4 = new JLabel();
        JButton LRButton = new JButton(title), back = new JButton("back"), credentialsButton = new JButton("SAVE");
        JTextField usernameField = new JTextField(10), nameField = new JTextField(10),
                ageField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        back.setBounds(10, 10, 80, 35);
        add(back);
        username.setBounds(280, 168, 120, 40);
        add(username);
        usernameField.setBounds(395, 170, 200, 40);
        add(usernameField);
        password.setBounds(280, 248, 120, 40);
        add(password);
        passwordField.setBounds(395, 250, 200, 40);
        add(passwordField);
        m1.setBounds(340, 205, 200, 40);
        m1.setFont(new Font("Arial", Font.PLAIN, 15));
        m1.setForeground(Color.red);
        add(m1);
        m2.setBounds(340, 285, 200, 40);
        m2.setFont(new Font("Arial", Font.PLAIN, 15));
        m2.setForeground(Color.red);
        add(m2);
        LRButton.setBounds(370, 330, 120, 30);
        add(LRButton);
        m1.setVisible(false);
        m2.setVisible(false);

        name.setBounds(280, 168, 120, 40);
        add(name);
        nameField.setBounds(395, 170, 200, 40);
        add(nameField);
        age.setBounds(280, 248, 120, 40);
        add(age);
        ageField.setBounds(395, 250, 200, 40);
        add(ageField);
        credentialsButton.setBounds(370, 330, 120, 30);
        add(credentialsButton);
        m3.setBounds(340, 205, 200, 40);
        m3.setFont(new Font("Arial", Font.PLAIN, 15));
        m3.setForeground(Color.red);
        add(m3);
        m4.setBounds(340, 285, 200, 40);
        m4.setFont(new Font("Arial", Font.PLAIN, 15));
        m4.setForeground(Color.red);
        add(m4);
        m3.setVisible(false);
        m4.setVisible(false);
        name.setVisible(false);
        age.setVisible(false);
        nameField.setVisible(false);
        credentialsButton.setVisible(false);
        ageField.setVisible(false);

        credentialsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m3.setVisible(false);
                m4.setVisible(false);
                try {
                    String n = nameField.getText();
                    int a = Integer.parseInt(ageField.getText());
                    if (n.length() != 0 && ageField.getText().length() != 0) {
                        try {
                            PreparedStatement stmt = conn
                                    .prepareStatement("INSERT INTO customers(name, age, username) VALUES(?, ?, ?)");
                            stmt.setString(1, n);
                            stmt.setInt(2, a);
                            stmt.setString(3, u);
                            stmt.executeUpdate();
                            Gname = n;
                            name.setVisible(false);
                            age.setVisible(false);
                            nameField.setVisible(false);
                            credentialsButton.setVisible(false);
                            ageField.setVisible(false);
                            m3.setVisible(false);
                            m4.setVisible(false);
                            expensesInput();
                        } catch (SQLException g) {
                            System.out.println(g.getMessage());
                        }
                    } else {
                        if (n.length() == 0 && ageField.getText().length() == 0) {
                            m3.setText("Name is Required");
                            m3.setVisible(true);
                            m4.setText("Age is Required");
                            m4.setVisible(true);
                        } else if (n.length() == 0) {
                            m3.setText("Name is Required");
                            m3.setVisible(true);
                        } else if (new String(p).length() == 0) {
                            m4.setText("Age is Required");
                            m4.setVisible(true);
                        }
                    }
                } catch (NumberFormatException u) {
                    m4.setText("Enter a Valid Number");
                    m4.setVisible(true);
                }
            }
        });

        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                back.setVisible(false);
                username.setVisible(false);
                usernameField.setVisible(false);
                LRButton.setVisible(false);
                m1.setVisible(false);
                m2.setVisible(false);
                password.setVisible(false);
                passwordField.setVisible(false);
                setTitle("Expenditure Predictive Analysis");
                r.setVisible(true);
                l.setVisible(true);
                exp.setVisible(true);
            }
        });

        LRButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m1.setVisible(false);
                m2.setVisible(false);
                u = usernameField.getText();
                p = passwordField.getPassword();
                if (new String(p).length() != 0 && u.length() != 0) {
                    try {
                        String query = String.format("SELECT * FROM users WHERE username = \'%s\'", u);
                        Statement st = conn.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        if (!rs.next()) {
                            PreparedStatement stmt = conn
                                    .prepareStatement("INSERT INTO users (username, pass) VALUES (?, ?)");
                            stmt.setString(1, u);
                            stmt.setString(2, new String(p));
                            stmt.executeUpdate();
                            // panel-false
                            back.setVisible(false);
                            username.setVisible(false);
                            usernameField.setVisible(false);
                            LRButton.setVisible(false);
                            m1.setVisible(false);
                            m2.setVisible(false);
                            password.setVisible(false);
                            passwordField.setVisible(false);
                            // nPanel-true
                            name.setVisible(true);
                            age.setVisible(true);
                            nameField.setVisible(true);
                            credentialsButton.setVisible(true);
                            ageField.setVisible(true);
                        } else {
                            m1.setText("Username already exists..");
                            m1.setVisible(true);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    if (u.length() == 0 && new String(p).length() == 0) {
                        m1.setText("Username is Required.");
                        m1.setVisible(true);
                        m2.setText("Password is Required.");
                        m2.setVisible(true);
                    } else if (new String(p).length() == 0) {
                        m2.setText("Password is Required.");
                        m2.setVisible(true);
                    } else if (u.length() == 0) {
                        m1.setText("Username is Required.");
                        m1.setVisible(true);
                    }
                }
            }
        });
    }

    void expensesInput() {
        JLabel expen = new JLabel("Expenditure");
        JTextArea prev = new JTextArea();
        JLabel m1 = new JLabel();
        JButton save = new JButton("SAVE"), addNew = new JButton("ADD New Expenditure"),
                viewPrev = new JButton("View Previous Month's Expenses"), logout = new JButton("Log Out"),
                graphView = new JButton("View Graph"), goBack = new JButton("Back");
        JTextField t = new JTextField(10);
        setTitle(Gname);
        addNew.setBounds(340, 150, 200, 40);
        add(addNew);
        viewPrev.setBounds(340, 200, 200, 40);
        add(viewPrev);
        logout.setBounds(800, 20, 80, 40);
        add(logout);
        graphView.setBounds(340, 250, 200, 40);
        add(graphView);
        goBack.setBounds(10, 10, 80, 35);
        add(goBack);
        t.setBounds(395, 250, 200, 40);
        add(t);
        expen.setBounds(280, 248, 120, 40);
        add(expen);
        save.setBounds(370, 330, 120, 30);
        add(save);
        m1.setBounds(395, 285, 200, 40);
        m1.setFont(new Font("Arial", Font.PLAIN, 15));
        m1.setForeground(Color.red);
        add(m1);

        goBack.setVisible(false);
        expen.setVisible(false);
        save.setVisible(false);
        t.setVisible(false);
        m1.setVisible(false);
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // panel-false
                goBack.setVisible(false);
                expen.setVisible(false);
                save.setVisible(false);
                t.setVisible(false);
                addNew.setVisible(false);
                viewPrev.setVisible(false);
                logout.setVisible(false);
                graphView.setVisible(false);
                prev.setVisible(false);
                m1.setVisible(false);
                // pan-true
                exp.setVisible(true);
                r.setVisible(true);
                l.setVisible(true);
                setTitle("Expenditure Predictive Analysis");
            }
        });

        addNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save.setVisible(true);
                viewPrev.setVisible(false);
                addNew.setVisible(false);
                graphView.setVisible(false);
                goBack.setVisible(true);
                expen.setVisible(true);
                t.setVisible(true);
            }
        });

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = String.format("SELECT * FROM customers WHERE username =\'%s\'", u);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    String ex = t.getText(), res = "0";
                    try {
                        Integer.parseInt(ex);
                        if (ex.length() != 0) {
                            if (rs.next() != false) {
                                String expen = rs.getString("expenses");
                                if (expen == null) {
                                    res = ex;
                                } else {
                                    res = expen + "-" + ex;
                                }
                            } else {
                                System.out.println("none present with given username in customers table");
                            }
                            String q1 = "UPDATE customers SET expenses = \'" + res + "\' WHERE username =\'" + u + "\'";
                            PreparedStatement st = conn.prepareStatement(q1);
                            st.executeUpdate();
                            expen.setVisible(false);
                            save.setVisible(false);
                            addNew.setVisible(true);
                            viewPrev.setVisible(true);
                            graphView.setVisible(true);
                            goBack.setVisible(false);
                            prev.setVisible(false);
                            t.setVisible(false);
                            m1.setVisible(false);

                            t.setText("");

                            st.close();
                            stmt.close();
                            rs.close();
                        } else {
                            m1.setText("Expenditure is Required.");
                            m1.setVisible(true);
                        }
                    } catch (NumberFormatException k) {
                        m1.setText("Enter a Valid Number");
                        m1.setVisible(true);
                    }
                } catch (SQLException eq) {
                    System.out.println(eq.getMessage());
                }
            }
        });

        viewPrev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                prev.setBounds(250, 100, 400, 400);
                add(prev);
                prev.setVisible(false);
                goBack.setVisible(true);
                expen.setVisible(false);
                save.setVisible(false);
                addNew.setVisible(false);
                viewPrev.setVisible(false);
                graphView.setVisible(false);
                try {
                    String query = String.format("SELECT * FROM customers WHERE username =\'%s\'", u);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next() != false) {
                        String expen2 = rs.getString("expenses");
                        if (expen2 == null) {
                            prev.setText("No Expenses entered previously..");
                        } else {
                            StringTokenizer strt = new StringTokenizer(expen2, "-");
                            StringTokenizer strt2 = new StringTokenizer(expen2, "-");
                            String temp = "";
                            int i = 0;
                            while (strt.hasMoreTokens()) {
                                i += 1;
                                temp += ("                                        Month " + i + "    --    "
                                        + strt.nextToken() + "\n");
                            }
                            size = i;
                            int[] expenses_vas = new int[size];
                            int im = 0;
                            while (strt2.hasMoreTokens()) {
                                expenses_vas[im] = Integer.parseInt(strt2.nextToken());
                                im += 1;
                            }
                            try {
                                int n = i;
                                int[] months = new int[n];
                                for (int vasi = 0; vasi < n; vasi++) {
                                    months[vasi] = vasi;
                                }

                                System.out.println("Predicting...");
                                String[] command = { "python",
                                        "C:/Users/Hp/Expenditure-Predictive-Analysis/src/prediction.py",
                                        Arrays.toString(months),
                                        Arrays.toString(expenses_vas) };
                                Process process = Runtime.getRuntime().exec(command);
                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(process.getInputStream()));
                                String line;
                                ArrayList<String> output = new ArrayList<String>();
                                while ((line = reader.readLine()) != null) {
                                    output.add(line);
                                }
                                process.waitFor();
                                int predictedExpense = Integer.parseInt(output.get(0));
                                temp += ("                                        Month " + (i + 1) + "    --    "
                                        + predictedExpense + "(Predicted)\n");
                            } catch (Exception ek) {
                                System.out.println(ek.getMessage());
                            }
                            prev.setText(temp);
                            prev.setEditable(false);
                            expenses_vas = null;
                        }
                        prev.setVisible(true);
                    } else {
                        System.out.println("none present");
                    }
                } catch (SQLException eq) {
                    System.out.println(eq.getMessage());
                }
            }
        });

        graphView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = String.format("SELECT * FROM customers WHERE username =\'%s\'", u);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next() != false) {
                        String expen2 = rs.getString("expenses");
                        if (expen2 == null) {
                            System.out.println("no expenses in database.");
                        } else {
                            StringTokenizer strt = new StringTokenizer(expen2, "-");
                            int size2 = strt.countTokens();
                            int arr[] = new int[size2];
                            int i = 0;
                            int ay = arr.length;
                            int newarr[] = new int[ay + 1];
                            while (strt.hasMoreTokens()) {
                                arr[i] = Integer.parseInt(strt.nextToken());
                                i += 1;
                            }
                            try {
                                int n = i;
                                int[] months = new int[n];
                                for (int vasi = 0; vasi < n; vasi++) {
                                    months[vasi] = vasi;
                                }

                                System.out.println("Predicting...");
                                String[] command = { "python",
                                        "C:/Users/Hp/Expenditure-Predictive-Analysis/src/prediction.py",
                                        Arrays.toString(months),
                                        Arrays.toString(arr) };
                                Process process = Runtime.getRuntime().exec(command);
                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(process.getInputStream()));
                                String line;
                                ArrayList<String> output = new ArrayList<String>();
                                while ((line = reader.readLine()) != null) {
                                    output.add(line);
                                }
                                process.waitFor();
                                int predictedExpense = Integer.parseInt(output.get(0));
                                System.out.println(predictedExpense);
                                for (int lv = 0; lv < ay; lv++) {
                                    newarr[lv] = arr[lv];
                                }
                                newarr[ay] = predictedExpense;
                            } catch (Exception ek) {
                                System.out.println(ek.getMessage());
                            }
                            new GUIgraph(newarr, ay + 1).graph();
                        }
                    } else {
                        System.out.println("reult set is false.");
                    }
                } catch (SQLException eq) {
                    System.out.println(eq.getMessage());
                }
            }
        });

        goBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                expen.setVisible(false);
                save.setVisible(false);
                addNew.setVisible(true);
                viewPrev.setVisible(true);
                graphView.setVisible(true);
                goBack.setVisible(false);
                prev.setVisible(false);
                t.setVisible(false);
                m1.setVisible(false);
            }
        });
    }

    public static void main(String[] args) {
        new App();
    }
}
