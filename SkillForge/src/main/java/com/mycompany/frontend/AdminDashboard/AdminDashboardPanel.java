/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.frontend.AdminDashboard;

import com.mycompany.CourseManagement.Course;
import com.mycompany.CourseManagement.CourseServices;
import com.mycompany.CourseManagement.Status;
import com.mycompany.frontend.Main.MainFrame;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Hazem
 */
public class AdminDashboardPanel extends javax.swing.JPanel {

    private final AdminDashboardFrame adminDashboardFrame;
    DefaultTableModel model;
    
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JButton button;
        private String label;
        private JTable table;

        public ButtonEditor(JTable table) {
            this.table = table;
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new java.awt.Font("Segoe UI", 1, 18));
            button.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.getEditingRow();
            fireEditingStopped();
            String courseId = table.getValueAt(row, 1).toString();
            try{
                Course course = CourseServices.findCourseById(courseId);
                CourseDetailsPanel courseDetailsPanel = new CourseDetailsPanel(coursesManagement,course);
                coursesManagement.add(courseDetailsPanel,"courseDetailsPanel");
                ((CardLayout)coursesManagement.getLayout()).show(coursesManagement,"courseDetailsPanel");
            }catch(Exception ex)
            {
                JOptionPane.showMessageDialog(getParent(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    class ComboBoxEditor extends AbstractCellEditor implements TableCellEditor {
        private JComboBox<String> comboBox;
        private JTable table;
        private int row;
        
        public ComboBoxEditor() {
            String[] options = {"PENDING", "APPROVED", "REJECTED"};
            comboBox = new JComboBox<>(options);
            comboBox.addActionListener(e -> fireEditingStopped()); 
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            comboBox.setSelectedItem(value);
            this.row = row;
            this.table = table;
            return comboBox;
        }

        @Override
        public Object getCellEditorValue() {
            String courseId = table.getValueAt(row, 1).toString();
            try {
                CourseServices.updateCourseStatus(courseId, (String)comboBox.getSelectedItem());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                comboBox.setSelectedItem("PENDING");
            }
            return comboBox.getSelectedItem();
        }
        
    }
    
    /**
     * Creates new form AdminDashboardPanel
     */
    public AdminDashboardPanel(AdminDashboardFrame adminDashboardFrame) {
        this.adminDashboardFrame = adminDashboardFrame;
        initComponents();
        this.model = (DefaultTableModel) tableCourses.getModel();
        
        TableColumn detailsColumn = tableCourses.getColumnModel().getColumn(2);
        detailsColumn.setCellRenderer(new ButtonRenderer());
        detailsColumn.setCellEditor(new ButtonEditor(tableCourses));
        
        TableColumn statusColumn = tableCourses.getColumnModel().getColumn(3);
        statusColumn.setCellEditor(new ComboBoxEditor());
        
        tableCourses.getTableHeader().setFont(tableCourses.getFont());
        
        this.loadCoursesIntoTable(model);
        
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(this.model);
        sorter.setSortable(0, true);   
        sorter.setSortable(1, true);   
        sorter.setSortable(2, false);  
        sorter.setSortable(3, false);
        tableCourses.setRowSorter(sorter);
        
        coursesManagement.setName("coursesManagement");
        jTabbedPane1.setSelectedComponent(coursesManagement);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnLogOut = new javax.swing.JButton();
        labelTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        coursesManagement = new javax.swing.JPanel();
        scrollPending = new javax.swing.JScrollPane();
        tableCourses = new javax.swing.JTable();
        usersManagement = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(1280, 720));
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setPreferredSize(new java.awt.Dimension(1280, 720));

        btnLogOut.setBackground(new java.awt.Color(255, 0, 51));
        btnLogOut.setFont(new java.awt.Font("Segoe UI Semibold", 1, 36)); // NOI18N
        btnLogOut.setForeground(new java.awt.Color(255, 255, 255));
        btnLogOut.setText("Log Out");
        btnLogOut.setBorder(null);
        btnLogOut.setMargin(new java.awt.Insets(5, 14, 3, 14));
        btnLogOut.setOpaque(true);
        btnLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogOutActionPerformed(evt);
            }
        });

        labelTitle.setBackground(new java.awt.Color(153, 153, 153));
        labelTitle.setFont(new java.awt.Font("Segoe UI Semilight", 1, 48)); // NOI18N
        labelTitle.setForeground(new java.awt.Color(255, 255, 255));
        labelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitle.setText("Admin Control Panel");
        labelTitle.setOpaque(true);

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        jTabbedPane1.setOpaque(true);
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        coursesManagement.setLayout(new java.awt.CardLayout());

        scrollPending.setBackground(new java.awt.Color(255, 255, 255));
        scrollPending.setForeground(new java.awt.Color(0, 0, 0));
        scrollPending.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        scrollPending.setMinimumSize(new java.awt.Dimension(468, 406));
        scrollPending.setOpaque(true);
        scrollPending.setRowHeaderView(null);

        tableCourses.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        tableCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Course Title", "Course ID", "View Details", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableCourses.setAutoscrolls(false);
        tableCourses.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tableCourses.setPreferredSize(new java.awt.Dimension(249, 1200));
        tableCourses.setRowHeight(40);
        tableCourses.setShowGrid(true);
        tableCourses.setShowHorizontalLines(true);
        tableCourses.getTableHeader().setReorderingAllowed(false);
        scrollPending.setViewportView(tableCourses);

        coursesManagement.add(scrollPending, "card2");

        jTabbedPane1.addTab("Courses ", coursesManagement);

        javax.swing.GroupLayout usersManagementLayout = new javax.swing.GroupLayout(usersManagement);
        usersManagement.setLayout(usersManagementLayout);
        usersManagementLayout.setHorizontalGroup(
            usersManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1137, Short.MAX_VALUE)
        );
        usersManagementLayout.setVerticalGroup(
            usersManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 626, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Users", usersManagement);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
    logOutCallback();
    }//GEN-LAST:event_btnLogOutActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        Component selectedComponent = jTabbedPane1.getSelectedComponent();
        if(selectedComponent == null||selectedComponent.getName()==null)
            return;
        if(selectedComponent.getName().equals(coursesManagement.getName()))
        {
            ((CardLayout)coursesManagement.getLayout()).show(coursesManagement, "card2");
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogOut;
    private javax.swing.JPanel coursesManagement;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JScrollPane scrollPending;
    private javax.swing.JTable tableCourses;
    private javax.swing.JPanel usersManagement;
    // End of variables declaration//GEN-END:variables

    public final void logOutCallback()
    {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                adminDashboardFrame.dispose();
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                JOptionPane.showMessageDialog(mainFrame, "Logged Out Successfully", "Successful Operation!", JOptionPane.INFORMATION_MESSAGE);
            }
    }
    
    public final void loadCoursesIntoTable(DefaultTableModel model) {
        model.setRowCount(0);
        try
        {
            for (Course course : CourseServices.getCoursesByStatus(Status.PENDING)) {
                Object[] row = {course.getTitle(),course.getCourseId(),"View Details",course.getStatus().toString()};
                model.addRow(row);
            }    
        }
        catch(Exception ex)
        {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          logOutCallback();
        }
        
    }
}
