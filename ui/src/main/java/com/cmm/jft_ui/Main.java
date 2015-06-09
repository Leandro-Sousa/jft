package com.cmm.jft_ui;

import com.cmm.jft.db.DBFacade;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.cmm.jft_ui.forms.FormsFactory;
import com.cmm.logging.Logging;

/**
 * <p><code>Main.java</code></p>
 * @author Cristiano M Martins
 * @version 02/08/2013 01:48:29
 *
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {

	try {
	    
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
	    //inicializa a conexao
            DBFacade.getInstance();
            
            //ajusta o log para mostrar erros tambem na saida padrao 
            Logging.getInstance().printStackTrace(true);
            
            //chama a tela inicial do programa
	    FormsFactory.openForm(ObjectForms.PROGRAM);

	} catch (ClassNotFoundException | InstantiationException
		| IllegalAccessException | UnsupportedLookAndFeelException e) {
	    e.printStackTrace();
	}

    }

}