/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cmm.jft.ui.trading;

import com.cmm.jft.db.DBFacade;
import com.cmm.jft.security.Security;
import com.cmm.jft.security.SecurityInfo;
import com.cmm.jft.trading.enums.OrderValidityTypes;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;
import com.cmm.jft.trading.services.SecurityService;
import com.cmm.jft.trading.services.TradingService;
import com.cmm.jft.ui.forms.AbstractForm;
import com.cmm.jft.ui.forms.Events;
import com.cmm.jft.ui.utils.FormUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.Date;

import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;

/**
 *
 * @author Cristiano
 */
public class PlaceOrderForm extends AbstractForm {

	/**
	 * Creates new form PlaceOrderForm
	 */
	public PlaceOrderForm() {
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

        spnVolume = new javax.swing.JSpinner();
        spnPrice = new javax.swing.JSpinner();
        spnStopLoss = new javax.swing.JSpinner();
        spnStopGain = new javax.swing.JSpinner();
        spnLimitPrice = new javax.swing.JSpinner();
        cmbOrderTypes = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnSellOrder = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnCancel = new javax.swing.JButton();
        cmbExpirationType = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        dtExpiration = new com.toedter.calendar.JDateChooser();
        btnBuyOrder = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        txtSecurity = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);

        spnVolume.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        spnPrice.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        spnStopLoss.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        spnStopGain.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        spnLimitPrice.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        cmbOrderTypes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Volume");

        jLabel2.setText("Order Type");

        jLabel3.setText("Price");

        jLabel4.setText("Stop Loss");

        jLabel5.setText("Stop Gain");

        jLabel7.setText("Limit Price");

        btnSellOrder.setText("Sell");

        jLabel8.setText("Security");

        btnCancel.setText("Cancel");

        cmbExpirationType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setText("Expiration");

        jLabel10.setText("Date");

        dtExpiration.setDateFormatString("d MMM yyyy");

        btnBuyOrder.setText("Buy");

        jLabel6.setText("Comment");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel))
                    .addComponent(jSeparator2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnBuyOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSellOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(103, 103, 103))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbOrderTypes, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(27, 27, 27)
                                .addComponent(txtSecurity, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jLabel9)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbExpirationType, 0, 100, Short.MAX_VALUE)
                                    .addComponent(spnStopLoss, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(spnVolume, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(spnPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel7))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(spnStopGain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(spnLimitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(dtExpiration, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtSecurity, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbOrderTypes, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spnVolume, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spnPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(spnLimitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spnStopLoss, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(spnStopGain, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbExpirationType, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)))
                    .addComponent(dtExpiration, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(10, 10, 10)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSellOrder)
                    .addComponent(btnBuyOrder))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addContainerGap())
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
			javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(PlaceOrderForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(PlaceOrderForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(PlaceOrderForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(PlaceOrderForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new PlaceOrderForm().setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuyOrder;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSellOrder;
    private javax.swing.JComboBox cmbExpirationType;
    private javax.swing.JComboBox cmbOrderTypes;
    private com.toedter.calendar.JDateChooser dtExpiration;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JSpinner spnLimitPrice;
    private javax.swing.JSpinner spnPrice;
    private javax.swing.JSpinner spnStopGain;
    private javax.swing.JSpinner spnStopLoss;
    private javax.swing.JSpinner spnVolume;
    private javax.swing.JTextField txtSecurity;
    // End of variables declaration//GEN-END:variables

	@Override
	public void addListeners() {
		//sbSecurity.getResultField().getDocument().addDocumentListener(new GerEvents(this));
		btnCancel.addActionListener(new GerEvents(this));
		btnBuyOrder.addActionListener(new GerEvents(this));
		btnSellOrder.addActionListener(new GerEvents(this));
		cmbOrderTypes.addItemListener(new GerEvents(this));
		cmbExpirationType.addItemListener(new GerEvents(this));
	}

	@Override
	public void loadData() {
		dtExpiration.setDate(new Date());
		int p=0;
		OrderTypes[] ots = new OrderTypes[5];
		ots[p++] = OrderTypes.Limit;
		ots[p++] = OrderTypes.Market;
		ots[p++] = OrderTypes.Stop;
		ots[p++] = OrderTypes.StopLimit;

		FormUtils.getInstance().addItensToCombo(cmbOrderTypes, ots);
		FormUtils.getInstance().addItensToCombo(cmbExpirationType, OrderValidityTypes.values());
		cmbExpirationType.setSelectedIndex(0);
		cmbOrderTypes.setSelectedIndex(0);
	}

	private class GerEvents extends Events{

		public GerEvents(AbstractForm frame) {
			super(frame);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btnCancel){
				frame.dispose();
			}
			else if(e.getSource() == btnSellOrder){
				sendOrder(Side.SELL);
			}
			else if(e.getSource() == btnBuyOrder){
				sendOrder(Side.BUY);
			}

		}


		@Override
		public void itemStateChanged(ItemEvent e) {

			if(e.getSource() == cmbExpirationType){
				OrderValidityTypes et = (OrderValidityTypes) cmbExpirationType.getModel().getSelectedItem();
				if(et != null){
					switch(et){
					case DAY:
						dtExpiration.setEnabled(true);
						dtExpiration.setDate(new Date());
						break;

					case FOK:
						dtExpiration.setEnabled(false);
						dtExpiration.setDate(new Date());
						break;

					case GTC:
						break;

					case GTD:
						break;

					case IOC:
						break;

					case MOA:
						break;

					case MOC:
						break;

					}
				}
			}

			else if(e.getSource() == cmbOrderTypes) {
				OrderTypes ot = (OrderTypes) cmbOrderTypes.getSelectedItem();
				if(ot==null) {
					spnPrice.setEnabled(true);
					spnLimitPrice.setEnabled(true);
					spnStopGain.setEnabled(true);
					spnStopLoss.setEnabled(true);
				}
				else {
					switch(ot) {
					case Limit:
						spnPrice.setEnabled(true);
						spnLimitPrice.setEnabled(false);
						spnStopGain.setEnabled(false);
						spnStopLoss.setEnabled(false);
						break;
					case Market:
						spnPrice.setEnabled(false);
						spnLimitPrice.setEnabled(false);
						spnStopGain.setEnabled(false);
						spnStopLoss.setEnabled(false);
						break;
					case Stop:
						spnPrice.setEnabled(false);
						spnLimitPrice.setEnabled(false);
						spnStopGain.setEnabled(false);
						spnStopLoss.setEnabled(true);
						break;
					case StopLimit:
						spnPrice.setEnabled(true);
						spnLimitPrice.setEnabled(true);
						spnStopGain.setEnabled(true);
						spnStopLoss.setEnabled(true);
						break;
					}
				}
			}

		}


		/* (non-Javadoc)
		 * @see com.cmm.jft.ui.forms.Events#changedUpdate(javax.swing.event.DocumentEvent)
		 */
		@Override
		public void changedUpdate(DocumentEvent e) {
			String symbol = txtSecurity.getText();
			Security s = SecurityService.getInstance().provideSecurity(symbol);
			SecurityInfo si = s.getSecurityInfoID();
			si.getMinimalVolume();
			si.getDigits();
			si.getTickSize();
			si.getTickValue();

			double actualPrice = 0;

			spnVolume.setModel(new SpinnerNumberModel(Integer.valueOf(0), si.getMinimalVolume(), null, si.getMinimalVolume()));
			spnPrice.setModel(new SpinnerNumberModel(actualPrice, Double.valueOf(0.0d), null, si.getTickSize()));
			spnLimitPrice.setModel(new SpinnerNumberModel(actualPrice, Double.valueOf(0.0d), null, si.getTickSize()));

			spnStopLoss.setModel(new SpinnerNumberModel(actualPrice, Double.valueOf(0.0d), null, si.getTickSize()));
			spnStopGain.setModel(new SpinnerNumberModel(actualPrice, Double.valueOf(0.0d), null, si.getTickSize()));

		}


		private void sendOrder(Side side){

			try{
				OrderTypes orderType = (OrderTypes)cmbOrderTypes.getSelectedItem();
				String symbol = txtSecurity.getText().trim();

				double price = (double)spnPrice.getValue();
				double limitPrice = (double)spnLimitPrice.getValue();
				double stopPrice = (double)spnStopLoss.getValue();
				double stopGain = (double)spnStopGain.getValue();
				int volume = (int)spnVolume.getValue();

				Date duration = dtExpiration.getDate();
				boolean day = duration.toInstant().isAfter(new Date().toInstant());
				TradeTypes tradeType = day?TradeTypes.DAY_TRADE:TradeTypes.NORMAL;

				TradingService.getInstance().newOrder(
						orderType, side, symbol, volume, 
						price, limitPrice, stopPrice, stopGain, 
						duration, tradeType);

			}catch(Exception ex){

			}

		}


	}


}
