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
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by admin on 28.07.2015.
 */
public class StudentFrame extends JFrame {
    StudentDAO sd;
    String path;
    public StudentFrame(String title, String path) throws Exception {
        super(title);
        this.path=path;
        sd = new StudentDAO(path);
        setLayout(new BorderLayout());
        final JTable table = sd.getTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);
        final java.util.List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(2);
        JScrollPane sp = new JScrollPane(table);
        JPanel btnPnl = new JPanel(new BorderLayout());
        JPanel topBtnPnl = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JPanel bottombtnPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topBtnPnl.add(new JLabel("Фамилия:"));
        final JTextField surname = new JTextField(10);
        final JTextField groupnumb = new JTextField(5);
        topBtnPnl.add(surname);
        topBtnPnl.add(new JLabel("Группа:"));
        topBtnPnl.add(groupnumb);
        final NumberFormat opnDisplayFormat = NumberFormat.getIntegerInstance();
        opnDisplayFormat.setGroupingUsed(false);
        final NumberFormat opnEditFormat = NumberFormat.getIntegerInstance();
        opnEditFormat.setGroupingUsed(false);
        final NumberFormatter editFormatter = new NumberFormatter(opnEditFormat);
        final DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        JButton disable = new JButton("Отмена");
        disable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sorter.setRowFilter(null);
                filters.removeAll(filters);
            }
        });
        JButton filter = new JButton("Фильтровать");
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filters.removeAll(filters);
                String text = surname.getText();
                String g = groupnumb.getText();
                if (g.length() != 0) {
                    filters.add(RowFilter.regexFilter(g, 5));
                }
                if (text.length() != 0) {
                    filters.add(RowFilter.regexFilter(text, 2));
                }
                RowFilter<Object, Object> serviceFilter = RowFilter.andFilter(filters);
                sorter.setRowFilter(serviceFilter);
            }
        });
        topBtnPnl.add(filter);
        topBtnPnl.add(disable);
        JButton add = new JButton("Добавить");
        JButton edit = new JButton("Редактировать");
        JButton delete = new JButton("Удалить");

        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                final JFormattedTextField number = new JFormattedTextField(
                        new DefaultFormatterFactory(
                                new NumberFormatter(opnDisplayFormat),
                                new NumberFormatter(opnDisplayFormat),
                                editFormatter));
                number.setText(model.getValueAt(row, 0).toString());
                JTextField name = new JTextField(model.getValueAt(row, 1).toString());
                JTextField surname = new JTextField(model.getValueAt(row, 2).toString());
                JTextField middlename = new JTextField(model.getValueAt(row, 3).toString());
                JFormattedTextField date = new JFormattedTextField(df);
                date.setColumns(10);
                date.setText(model.getValueAt(row, 4).toString());
                final JFormattedTextField group = new JFormattedTextField(
                        new DefaultFormatterFactory(
                                new NumberFormatter(opnDisplayFormat),
                                new NumberFormatter(opnDisplayFormat),
                                editFormatter));
                group.setText(model.getValueAt(row, 5).toString());
                final JComponent[] inputs = new JComponent[]{
                        new JLabel("Номер"),
                        number,
                        new JLabel("Имя"),
                        name,
                        new JLabel("Фамилия"),
                        surname,
                        new JLabel("Отчество"),
                        middlename,
                        new JLabel("Дата рождения (1970-01-01)"),
                        date,
                        new JLabel("Группа"),
                        group,
                };
                int result = JOptionPane.showConfirmDialog(null, inputs,
                        "Добавить студента", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION && isCorrect(number, name, surname, middlename, date, group)) {
                    try {
                        sd.updateStudent(Long.parseLong(model.getValueAt(row, 0).toString()), Long.parseLong(number.getText()), name.getText(), surname.getText(), middlename.getText(), Date.valueOf(date.getText()), Long.parseLong(group.getText()));
                        model.setValueAt(number.getText(), row, 0);
                        model.setValueAt(name.getText(), row, 1);
                        model.setValueAt(surname.getText(), row, 2);
                        model.setValueAt(middlename.getText(), row, 3);
                        model.setValueAt(date.getText(), row, 4);
                        model.setValueAt(group.getText(), row, 5);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

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
                JTextField name = new JTextField();
                JTextField surname = new JTextField();
                JTextField middlename = new JTextField();
                JFormattedTextField date = new JFormattedTextField(df);
                date.setColumns(10);
                final JFormattedTextField group = new JFormattedTextField(
                        new DefaultFormatterFactory(
                                new NumberFormatter(opnDisplayFormat),
                                new NumberFormatter(opnDisplayFormat),
                                editFormatter));
                final JComponent[] inputs = new JComponent[]{
                        new JLabel("Номер"),
                        number,
                        new JLabel("Имя"),
                        name,
                        new JLabel("Фамилия"),
                        surname,
                        new JLabel("Отчество"),
                        middlename,
                        new JLabel("Дата рождения (1970-01-01)"),
                        date,
                        new JLabel("Группа"),
                        group,
                };
                int result = JOptionPane.showConfirmDialog(null, inputs,
                        "Добавить студента", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION && isCorrect(number, name, surname, middlename, date, group)) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    try {
                        sd.insertStudent(Long.parseLong(number.getText()), name.getText(), surname.getText(), middlename.getText(), Date.valueOf(date.getText()), Long.parseLong(group.getText()));
                        model.addRow(new Object[]{Long.parseLong(number.getText()), name.getText(), surname.getText(), middlename.getText(), Date.valueOf(date.getText()), Long.parseLong(group.getText())});
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                }


            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                try {
                    sd.deleteStudent(Long.parseLong(model.getValueAt(row, 0).toString()));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                model.removeRow(row);

            }
        });

        bottombtnPnl.add(add);
        bottombtnPnl.add(edit);
        bottombtnPnl.add(delete);

        btnPnl.add(topBtnPnl, BorderLayout.NORTH);
        btnPnl.add(bottombtnPnl, BorderLayout.CENTER);

        add(sp, BorderLayout.CENTER);
        add(btnPnl, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public boolean isCorrect(JTextField number, JTextField name, JTextField surname, JTextField middlename, JTextField date, JTextField group) {
        return date.getText().replace(" ", "").length() != 0 && number.getText().replace(" ", "").length() != 0 && group.getText().replace(" ", "").length() != 0 && name.getText().length() != 0 && surname.getText().length() != 0 && middlename.getText().length() != 0;
    }

}