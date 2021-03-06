/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cmm.jft.ui.trading;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import com.cmm.jft.marketdata.MDNews;
import com.cmm.jft.services.marketdata.MarketDataService;
import com.cmm.jft.ui.ObjectForms;
import com.cmm.jft.ui.forms.AbstractForm;
import com.cmm.jft.ui.forms.Events;
import com.cmm.jft.ui.forms.FormsFactory;
import com.cmm.jft.ui.models.NewsTableModel;

/**
 *
 * @author cristiano
 */
public class NewsForm extends AbstractForm {

    /**
     * Creates new form NewsForm
     */
    public NewsForm() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblNews = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblNews.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Origem", "Hora", "Notícia"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNews.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(tblNews);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewsForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblNews;
    // End of variables declaration//GEN-END:variables
    
    
    
    
    /* (non-Javadoc)
     * @see com.cmm.jft.ui.forms.AbstractForm#loadData()
     */
    @Override
    public void loadData() {
	List<MDNews> ns = MarketDataService.getInstance().getNewsFeed();
	for(MDNews n : ns) {
	    ((NewsTableModel)tblNews.getModel()).addNews(n);
	}
	
	
	
    }
    
    private class GerEvents extends Events{

	/**
	 * @param frame
	 */
	public GerEvents(AbstractForm frame) {
	    super(frame);
	}
	
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.ui.forms.Events#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
	    if(e.getSource() == tblNews) {
		if(e.getKeyCode() == 10) {
		    openDetails();
		}
	    }
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.ui.forms.Events#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	    if(e.getSource() == tblNews) {
		if(e.getClickCount() >= 2) {
		    openDetails();
		}
	    }
	}
	
	
	private void openDetails() {
	    int r = tblNews.getSelectedRow();
	    if(r > 0) {
		FormsFactory.openForm(ObjectForms.NEWS_DETAILS, 
			((NewsTableModel)tblNews.getModel()).getMDNews(r));
	    }
	}
	
	
    }
    
    
}
