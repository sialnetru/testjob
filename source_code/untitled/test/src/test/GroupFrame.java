package test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * Created by admin on 28.07.2015.
 */
public class GroupFrame {
    static private String path;
    static private JFrame frame = new JFrame();
    static private JPanel btnPnl = new JPanel(new BorderLayout());
    static private JPanel bottombtnPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
    static private JButton add = new JButton("Добавить");
    static private JButton edit = new JButton("Редактировать");
    static private JButton delete = new JButton("Удалить");
    static private JButton students = new JButton("Студенты");
    static private NumberFormat opnDisplayFormat = NumberFormat.getIntegerInstance();
    static private NumberFormat opnEditFormat = NumberFormat.getIntegerInstance();
    static private NumberFormatter editFormatter = null;
    static private JFormattedTextField number = null;
    static private JTextField faculty = new JTextField();
    static private JTable tab = null;
    static private TableRowSorter<TableModel> sorter = null;
    static private GroupDAO d = null;
    static private JScrollPane sp = null;
    static private DefaultTableModel model = null;
    static private JComponent[] inputs = null;

    public static boolean checkValid()
    {
        return number.getText().replace(" ", "").length() != 0 && faculty.getText().length() != 0;
    }

    public static void editGroup()
    {
        int row = tab.convertRowIndexToModel(tab.getSelectedRow());

        number.setText(model.getValueAt(row, 0).toString());
        faculty.setText(model.getValueAt(row, 1).toString());
        int result = JOptionPane.showConfirmDialog(null, inputs,
                "Добавить группу", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && checkValid() ) {
            try {
                if(d.updateGroup(Long.parseLong(model.getValueAt(row, 0).toString()), Long.parseLong(number.getText()), faculty.getText()))
                {
                    model.setValueAt(number.getText(), row, 0);
                    model.setValueAt(faculty.getText(), row, 1);
                }
                else
                    JOptionPane.showMessageDialog(null, "Error on update", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void deleteGroup()
    {
        int row = tab.convertRowIndexToModel(tab.getSelectedRow());
        try {
            if (d.deleteGroup(Long.parseLong(model.getValueAt(row, 0).toString())))
                model.removeRow(row);
            else
                JOptionPane.showMessageDialog(null, "Maybe group not empty", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    public static void addGroup()
    {
        number.setText(""); faculty.setText("");
        int result = JOptionPane.showConfirmDialog(null, inputs,
                "Добавить группу", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && number.getText().replace(" ", "").length() != 0 && faculty.getText().length() != 0) {
            DefaultTableModel model = (DefaultTableModel) tab.getModel();

            try {
                if(d.insertGroup(Long.parseLong(number.getText()), faculty.getText()))
                    model.addRow(new Object[]{number.getText(), faculty.getText()});
                else
                    JOptionPane.showMessageDialog(null, "Error on insert", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void openStudents()
    {
        try {
            StudentFrame s = new StudentFrame("Студенты", path);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void prepareGUI(String p)
    {
        path = p;
        d = new GroupDAO(path);

        frame.setLayout(new BorderLayout());
        tab = d.getTable();
        model = (DefaultTableModel) tab.getModel();
        tab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sp = new JScrollPane(tab);
        sorter = new TableRowSorter<TableModel>(model);
        tab.setRowSorter(sorter);
        opnDisplayFormat.setGroupingUsed(false);
        opnEditFormat.setGroupingUsed(false);
        editFormatter = new NumberFormatter(opnEditFormat);
        number = new JFormattedTextField(
                new DefaultFormatterFactory(
                new NumberFormatter(opnDisplayFormat),
                new NumberFormatter(opnDisplayFormat),
                editFormatter));
        inputs=new JComponent[]{new JLabel("Номер"), number, new JLabel("Факультет"), faculty,};
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editGroup();
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteGroup();
            }
        });
        students.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openStudents();
            }
        });
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addGroup();
            }
        });

        bottombtnPnl.add(add);
        bottombtnPnl.add(edit);
        bottombtnPnl.add(delete);
        bottombtnPnl.add(students);

        btnPnl.add(bottombtnPnl, BorderLayout.CENTER);
        frame.add(sp, BorderLayout.CENTER);
        frame.add(btnPnl, BorderLayout.SOUTH);

        frame.setTitle("Группы");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String ar[]) throws Exception {
        prepareGUI(ar[0]);
    }
}
