/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cmm.searchbox;

import com.cmm.searchbox.core.SearchUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.JTextField;

/**
 *
 * @author Cristiano M Martins
 */
public class SearchBox extends javax.swing.JPanel {

    /**
     * Creates new form SearchBox
     */
    public SearchBox() {
        initComponents();
        this.sUtil = new SearchUtil(null);
        this.sUtil.setTxtField(txtResult);
        addListeners();
    }
    
    private void addListeners(){
        btnSearch.addActionListener(new GerEvents());
    }
    
    public void setResultFieldValue(String resultFieldValue) {
        this.sUtil.setResultField(resultFieldValue);
    }

    public String getResultFieldValue() {
        return sUtil.getResultField();
    }
    
    public void setSearchField(String searchField) {
        this.sUtil.setSearchField(searchField);
    }

    public String getSearchField() {
        return sUtil.getSearchField();
    }
    
    public void setQuery(String query) {
        this.sUtil.setQuery(query);
    }

    public String getQuery() {
        return sUtil.getQuery();
    }
    
    public void setConnection(Connection connection){
        this.sUtil.setConnection(connection);
    }
    
    public void setFields(String[] fields){
        for (String f : fields) {
            this.sUtil.addField(f);
        }
    }
    
    public String[] getFields(){
        return this.sUtil.getFields();
    }
    
    public JTextField getResultField(){
        return txtResult;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtResult = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/magnifier-small.png"))); // NOI18N
        btnSearch.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(txtResult, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtResult, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JTextField txtResult;
    // End of variables declaration//GEN-END:variables

    private SearchUtil sUtil;
    

    private class GerEvents implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnSearch){
                String param = txtResult.getText();
                sUtil.showGrid(param);
            }
        }
        
    }    
    
}
