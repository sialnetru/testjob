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
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by admin on 28.07.2015.
 */
public class StudentFrame extends JFrame {
    private StudentDAO sd;
    private String path;
    private JTable table = null;
    private JPanel btnPnl = new JPanel(new BorderLayout());
    private JPanel topBtnPnl = new JPanel(new FlowLayout(FlowLayout.TRAILING));
    private JPanel bottombtnPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private DefaultTableModel model = null;
    private TableRowSorter<TableModel> sorter = null;
    private java.util.List<RowFilter<Object, Object>> filters = null;
    private JScrollPane sp = null;
    private JTextField sort_surname = new JTextField(10);
    private JTextField sort_groupnumb = new JTextField(5);
    private NumberFormat opnDisplayFormat = NumberFormat.getIntegerInstance();
    private NumberFormat opnEditFormat = NumberFormat.getIntegerInstance();
    private NumberFormatter editFormatter = null;
    private DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
    private JButton disable = new JButton("Отмена");
    private JButton filter = new JButton("Фильтровать");
    private JButton add = new JButton("Добавить");
    private JButton edit = new JButton("Редактировать");
    private JButton delete = new JButton("Удалить");
    private JFormattedTextField number = null;
    private JFormattedTextField group = null;
    private JTextField name = new JTextField();
    private JTextField surname = new JTextField();
    private JTextField middlename = new JTextField();
    private JFormattedTextField date = new JFormattedTextField(df);
    private JComponent[] inputs = null;

    public void disableFilters() {
        sorter.setRowFilter(null);
        filters.removeAll(filters);
        sort_groupnumb.setText("");
        sort_surname.setText("");
    }

    public void enableFilters() {
        filters.removeAll(filters);
        String text = sort_surname.getText();
        String g = sort_groupnumb.getText();
        if (g.length() != 0) {
            filters.add(RowFilter.regexFilter(g, 5));
        }
        if (text.length() != 0) {
            filters.add(RowFilter.regexFilter(text, 2));
        }
        RowFilter<Object, Object> serviceFilter = RowFilter.andFilter(filters);
        sorter.setRowFilter(serviceFilter);
    }

    public void editStudent() {
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        number.setText(model.getValueAt(row, 0).toString());
        name.setText(model.getValueAt(row, 1).toString());
        surname.setText(model.getValueAt(row, 2).toString());
        middlename.setText(model.getValueAt(row, 3).toString());
        date.setText(model.getValueAt(row, 4).toString());
        group.setText(model.getValueAt(row, 5).toString());
        int result = JOptionPane.showConfirmDialog(null, inputs,
                "Изменить студента", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && isCorrect(number, name, surname, middlename, date, group)) {

            if (sd.updateStudent(Long.parseLong(model.getValueAt(row, 0).toString()), Long.parseLong(number.getText()), name.getText(), surname.getText(), middlename.getText(), Date.valueOf(date.getText()), Long.parseLong(group.getText()))) {
                model.setValueAt(number.getText(), row, 0);
                model.setValueAt(name.getText(), row, 1);
                model.setValueAt(surname.getText(), row, 2);
                model.setValueAt(middlename.getText(), row, 3);
                model.setValueAt(date.getText(), row, 4);
                model.setValueAt(group.getText(), row, 5);
            } else
                JOptionPane.showMessageDialog(null, "Error on update", "Error", JOptionPane.ERROR_MESSAGE);


        }
    }

    public void addStudent() {
        resetFields();
        int result = JOptionPane.showConfirmDialog(null, inputs,
                "Добавить студента", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && isCorrect(number, name, surname, middlename, date, group)) {

            if (sd.insertStudent(Long.parseLong(number.getText()), name.getText(), surname.getText(), middlename.getText(), Date.valueOf(date.getText()), Long.parseLong(group.getText())))
                model.addRow(new Object[]{Long.parseLong(number.getText()), name.getText(), surname.getText(), middlename.getText(), Date.valueOf(date.getText()), Long.parseLong(group.getText())});
            else JOptionPane.showMessageDialog(null, "Error on insert. Maybe group doesn't exists", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteStudent() {
        int row = table.convertRowIndexToModel(table.getSelectedRow());

        if (sd.deleteStudent(Long.parseLong(model.getValueAt(row, 0).toString())))
            model.removeRow(row);
        else
            JOptionPane.showMessageDialog(null, "Maybe group not empty", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void resetFields() {
        number.setText("");
        date.setText("");
        group.setText("");
        name.setText("");
        surname.setText("");
        middlename.setText("");
    }

    public void prepareGUI() {
        sd = new StudentDAO(path);
        setLayout(new BorderLayout());
        table = sd.getTable();
        model = (DefaultTableModel) table.getModel();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);
        filters = new ArrayList<RowFilter<Object, Object>>(2);
        sp = new JScrollPane(table);
        topBtnPnl.add(new JLabel("Фамилия:"));
        topBtnPnl.add(sort_surname);
        topBtnPnl.add(new JLabel("Группа:"));
        topBtnPnl.add(sort_groupnumb);
        opnDisplayFormat.setGroupingUsed(false);
        opnEditFormat.setGroupingUsed(false);
        editFormatter = new NumberFormatter(opnEditFormat);
        number = new JFormattedTextField(
                new DefaultFormatterFactory(
                        new NumberFormatter(opnDisplayFormat),
                        new NumberFormatter(opnDisplayFormat),
                        editFormatter));
        group = new JFormattedTextField(
                new DefaultFormatterFactory(
                        new NumberFormatter(opnDisplayFormat),
                        new NumberFormatter(opnDisplayFormat),
                        editFormatter));
        date.setColumns(10);
        inputs = new JComponent[]{
                new JLabel("Номер"),
                number,
                new JLabel("Имя"),
                name,
                new JLabel("Фамилия"),
                surname,
                new JLabel("Отчество"),
                middlename,
                new JLabel("Дата рождения (ГГГГ-ММ-ДД)"),
                date,
                new JLabel("Группа"),
                group,
        };
        disable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disableFilters();
            }
        });
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableFilters();
            }
        });
        topBtnPnl.add(filter);
        topBtnPnl.add(disable);

        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStudent();
            }
        });
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
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

    public StudentFrame(String title, String path) throws Exception {
        super(title);
        this.path = path;
        prepareGUI();
    }

    public boolean isCorrect(JTextField number, JTextField name, JTextField surname, JTextField middlename, JTextField date, JTextField group) {
        return date.getText().replace(" ", "").length() != 0
                && number.getText().replace(" ", "").length() != 0
                && group.getText().replace(" ", "").length() != 0
                && name.getText().length() != 0 && surname.getText().length() != 0
                && middlename.getText().length() != 0;
    }

}