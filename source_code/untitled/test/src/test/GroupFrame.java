package test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.NumberFormat;

/**
 * Created by admin on 28.07.2015.
 */
public class GroupFrame {
    static String path;
    public static void main(String ar[]) throws Exception {
        JFrame frame = new JFrame();
        path=ar[0];
        final GroupDAO d = new GroupDAO(path);

        frame.setLayout(new BorderLayout());

        final JTable tab = d.getTable();
        tab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sp = new JScrollPane(tab);

        JPanel btnPnl = new JPanel(new BorderLayout());
        JPanel bottombtnPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton add = new JButton("Добавить");
        JButton edit = new JButton("Редактировать");
        JButton delete = new JButton("Удалить");
        JButton students = new JButton("Студенты");
        final NumberFormat opnDisplayFormat = NumberFormat.getIntegerInstance();
        opnDisplayFormat.setGroupingUsed(false);
        final NumberFormat opnEditFormat = NumberFormat.getIntegerInstance();
        opnEditFormat.setGroupingUsed(false);
        final NumberFormatter editFormatter = new NumberFormatter(opnEditFormat);
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) tab.getModel();
                int row = tab.getSelectedRow();
                final JFormattedTextField number = new JFormattedTextField(
                        new DefaultFormatterFactory(
                                new NumberFormatter(opnDisplayFormat),
                                new NumberFormatter(opnDisplayFormat),
                                editFormatter));
                number.setText(model.getValueAt(row, 0).toString());
                JTextField faculty = new JTextField(model.getValueAt(row, 1).toString());
                final JComponent[] inputs = new JComponent[]{
                        new JLabel("Номер"),
                        number,
                        new JLabel("Факультет"),
                        faculty,
                };
                int result = JOptionPane.showConfirmDialog(null, inputs,
                        "Добавить группу", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION && number.getText().replace(" ", "").length() != 0 && faculty.getText().length() != 0) {
                    try {
                        d.updateGroup(Long.parseLong(model.getValueAt(row, 0).toString()), Long.parseLong(number.getText()), faculty.getText());
                        model.setValueAt(number.getText(), row, 0);
                        model.setValueAt(faculty.getText(), row, 1);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tab.getSelectedRow();
                DefaultTableModel model = (DefaultTableModel) tab.getModel();
                try {
                    d.deleteGroup(Long.parseLong(model.getValueAt(row, 0).toString()));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                model.removeRow(row);

            }
        });
        students.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    StudentFrame s = new StudentFrame("Студенты",path);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final JFormattedTextField number = new JFormattedTextField(
                        new DefaultFormatterFactory(
                                new NumberFormatter(opnDisplayFormat),
                                new NumberFormatter(opnDisplayFormat),
                                editFormatter));
                JTextField faculty = new JTextField();
                final JComponent[] inputs = new JComponent[]{
                        new JLabel("Номер"),
                        number,
                        new JLabel("Факультет"),
                        faculty,
                };
                int result = JOptionPane.showConfirmDialog(null, inputs,
                        "Добавить группу", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION && number.getText().replace(" ", "").length() != 0 && faculty.getText().length() != 0) {
                    DefaultTableModel model = (DefaultTableModel) tab.getModel();
                    model.addRow(new Object[]{number.getText(), faculty.getText()});
                    try {
                        d.insertGroup(Long.parseLong(number.getText()), faculty.getText());
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
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
}
