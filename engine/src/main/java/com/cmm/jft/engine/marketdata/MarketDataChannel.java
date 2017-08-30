/**
 * 
 */
package com.cmm.jft.engine.marketdata;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import org.apache.log4j.Level;

import com.cmm.jft.engine.IdGenerator;
import com.cmm.jft.engine.SessionRepository;
import com.cmm.jft.engine.match.Summary;
import com.cmm.jft.marketdata.MDEntry;
import com.cmm.jft.marketdata.MDSnapshot;
import com.cmm.jft.messaging.MarketDataMessageEncoder;
import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.MessageSender;
import com.cmm.jft.messaging.fix50sp2.Fix50SP2MDMessageEncoder;
import com.cmm.jft.security.Security;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.MDEntryTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.StreamTypes;
import com.cmm.jft.trading.enums.UpdateActions;
import com.cmm.logging.Logging;

import quickfix.Group;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.field.NoMDEntries;
import quickfix.field.SecurityExchange;
import quickfix.field.SecurityID;
import quickfix.field.SecurityIDSource;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * <p>
 * <code>MarketDataChannel.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Feb 26, 2016 3:57:55 PM
 *
 */
public class MarketDataChannel implements MessageSender {


    private enum IncrementalStates{
	OPEN, CLOSED, PACKING
    }

    private int sequence;
    private IdGenerator idGen;
    private Security security;
    private SecurityID securityID;
    private SecurityIDSource securityIDSrc;
    private SecurityExchange exchangeID;

    private IncrementalStates packState;

    private static String stream = "E";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");

    private MarketDataMessageEncoder encoder;

    private LinkedBlockingQueue<MarketDataIncrementalRefresh.NoMDEntries> mbpPackets;
    private LinkedBlockingQueue<MarketDataIncrementalRefresh.NoMDEntries> mboPackets;

    public MarketDataChannel(Security security) {
	encoder = Fix50SP2MDMessageEncoder.getInstance();
	this.security = security;
	securityID = new SecurityID(security.getSecurityID()+"");
	securityIDSrc = new SecurityIDSource(security.getSecurityIDSrc()+"");
	exchangeID = new SecurityExchange(security.getSecurityExchange());

	packState = IncrementalStates.OPEN;
	mbpPackets = new LinkedBlockingQueue<>(1000);
	mboPackets = new LinkedBlockingQueue<>(1000);

	idGen = new IdGenerator(security, new Date());
    }


    public void openNewPacket(){
	if(packState == IncrementalStates.CLOSED || packState == null){
	    packState = IncrementalStates.OPEN;
	    mbpPackets.clear();
	    mboPackets.clear();
	}
    }

    public void packing(){
	if(packState ==IncrementalStates.OPEN || packState == IncrementalStates.PACKING){
	    packState = IncrementalStates.PACKING;
	}
    }

    /**
     * Close last open packet and send market data to listeners
     */
    public void closePacket(){
	if(packState == IncrementalStates.PACKING){
	    packState = IncrementalStates.CLOSED;
	    Message msgMBO = encoder.mdIncrementalRefresh(mboPackets);
	    Message msgMBP = encoder.mdIncrementalRefresh(mbpPackets);
	    sendUMDF(msgMBO);
	    sendUMDF(msgMBP);
	}
    }


    public MDEntry createMBOEntry(Orders order, UpdateActions updtAction, int position){
	MDEntry mboEntry = new MDEntry();

	if(position >0) {
	    mboEntry.setOrderID(order.getOrderID().toString());
	    if(order.getSide() == Side.BUY){
		mboEntry.setMdEntryBuyer(order.getBrokerID());
	    }else{
		mboEntry.setMdEntrySeller(order.getBrokerID());
	    }
	    mboEntry.setMdEntryDate(order.getInsertDate());
	    mboEntry.setMdEntryTime(order.getInsertTime());
	    mboEntry.setMdEntryType(order.getSide() == Side.BUY? MDEntryTypes.BID: MDEntryTypes.OFFER);

	    if(updtAction != null) {
		mboEntry.setMdUpdateAction(updtAction);
	    }

	    mboEntry.setMdEntryPx(order.getPrice());
	    mboEntry.setMdEntrySize((int) order.getVolume());
	    mboEntry.setMdEntryPosNo(position);
	}
	return mboEntry;
    }

    public MDEntry createMBPEntry(UpdateActions updtAction, Summary summary, int position) {
	MDEntry mbpEntry = new MDEntry();
	if(summary != null) {

	}

	return mbpEntry;
    }


    //----------------------------------------BID OFFER packets
    /**
     * @param order
     */
    public void informNewOrder(Orders order, int posMBO, Summary summary, int posMBP) {
	if(posMBO >0) {
	    try {
		MarketDataIncrementalRefresh.NoMDEntries entry = null;
		if(order.getSide() == Side.BUY) {
		    mboPackets.put(encoder.bidEntryIncMBO(order, posMBO, UpdateActions.New, idGen.nextInt()));
		    /*
		    mbpPackets.put(encoder.bidEntryIncMBP(
			    summary.getPrice(), summary.getOrderVolume(), summary.getOrderCount(), 
			    order.getSecurityID(), posMBO, UpdateActions.New, idGen.actualInt())
			    );*/
		}else {
		    mboPackets.put(encoder.offerEntryIncMBO(order, posMBO, UpdateActions.New, idGen.nextInt()));
		    /*
		    mbpPackets.put(encoder.bidEntryIncMBP(
			    summary.getPrice(), summary.getOrderVolume(), summary.getOrderCount(), 
			    order.getSecurityID(), posMBO, UpdateActions.New, idGen.actualInt())
			    );*/
		}

	    }catch(InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }

    public void informChangeOrder(MDEntry mboInfo, MDEntry mbpInfo) {

    }


    public void informDeleteOrder(MDEntry mboInfo, MDEntry mbpInfo){

	//delete pode conter duas entradas, o proprio delete e um new 
	//para indicar qual ordem tomou o lugar da que foi deletada 
	//somente ordens no topo sao 
	//1128=935=X34=175353552=20140610-13:26:15.95175=20140610268=2
	//279=2269=122=8207=BVMF48=383975083=2039273=61:59:51272=2014061037=90507497239289=63290=2
	//279=0269=122=8207=BVMF48=383975083=2040270=11.26273=61:59:51271=97272=2014061037016=2014061037017=61:59:5137=90507497242289=63290=2

    }

    //---------------------------------------/BID OFFER packets




    //----------------------------------------/MarketData packets
    public void informTrade(MDEntry trade) {
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = encoder.tradeEntryInc(
		    trade.getMdUpdateAction(), security, trade.getMdEntryBuyer(), trade.getMdEntrySeller(), 
		    trade.getMdEntryPx(), trade.getMdEntrySize(), 
		    trade.getMdEntryDate(), trade.getMdEntryTime(), trade.getTradeID(), 
		    trade.getTradeVolume(), idGen.nextInt()
		    );
	    mboPackets.put(entry);
	    mbpPackets.put(entry);

	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}

    }

    public void informIndexValue(){
	throw new NotImplementedException();
    }

    public void informOpenPrice(double openPrice){
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = 
		    encoder.openPriceEntryInc(UpdateActions.New, security, openPrice, idGen.nextInt());

	    mboPackets.put(entry);
	    mbpPackets.put(entry);
	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    public void informClosePrice(double closePrice){
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = 
		    encoder.closePriceEntryInc(security, closePrice, idGen.nextInt());

	    mboPackets.put(entry);
	    mbpPackets.put(entry);
	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    public void informSettlementPrice(){
	throw new NotImplementedException();
    }

    public void informHighPrice(double highPrice){
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = 
		    encoder.highPriceEntryInc(UpdateActions.New, security, highPrice, idGen.nextInt());

	    mboPackets.put(entry);
	    mbpPackets.put(entry);
	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    public void informLowPrice(double lowPrice){
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = 
		    encoder.lowPriceEntryInc(UpdateActions.New, security, lowPrice, idGen.nextInt());

	    mboPackets.put(entry);
	    mbpPackets.put(entry);
	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    public void informVWAPPrice(double vwapPrice){
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = 
		    encoder.vwapPriceEntryInc(UpdateActions.New, security, vwapPrice, idGen.nextInt());

	    mboPackets.put(entry);
	    mbpPackets.put(entry);
	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    public void informImbalance(){
	throw new NotImplementedException();
    }

    public void informTradeVolume(int numOfTrades, double financialVolume, double tradedVolume){
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = 
		    encoder.tradeVolumeEntryInc(
			    security, numOfTrades, financialVolume, tradedVolume, idGen.nextInt());

	    mboPackets.put(entry);
	    mbpPackets.put(entry);
	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    public void informOpenInterest(){
	throw new NotImplementedException();
    }

    public void informEmptyBook(){
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = 
		    encoder.emptyBookEntryInc(security, idGen.nextInt());

	    mboPackets.put(entry);
	    mbpPackets.put(entry);
	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    public void informSecurityState(){
	throw new NotImplementedException();
    }

    public void informPriceBand(){
	throw new NotImplementedException();
    }

    public void informQuantityBand(){
	throw new NotImplementedException();
    }

    public void informCompositeUnderlingPrice(){
	throw new NotImplementedException();
    }

    /**
     * Mercado por Oferta, 
     * Esse tipo contem informacoes sobre cada uma das ofertas, com cada registro representando uma unica ordem. 
     * A BM&FBOVESPA sempre enviara a profundidade total para livro agregado por oferta, i.e. o cliente
     * recebera atualizacoes para todas as ofertas contidas no livro, mesmo que seja a ultima (pior pre�o).
     * Para livros mais �rasos� isso pode nao ser um problema, mas para livros mais profundos
     * atualizacoes completas nem sempre sao relevantes, alem de consumir banda.
     */
    public void mbo() {

    }

    /**
     * Mercado por Preco, 
     * O livro agregado por pre�o contem as informacoes individuais de pre�o, em que cada registro
     * representa a agrega��o de todas as ofertas (e suas quantidades) por aquele pre�o. 
     * Alem de quantidade e pre�o, o livro agregado por pre�o informa o numero de ofertas inseridas por
     * determinado pre�o. A BM&FBOVESPA preestabelece a profundidade do livro que estara disponivel 
     * por instrumento, com atribuicao automatica de 5. Isso significa que o cliente somente recebera os 5 
     * melhores niveis de pre�o no livro.
     */
    public void mbp() {

    }

    /**
     * Topo do Book. Apresenta as melhores ofertas de compra e venda
     * agrupadas por preco em somente um nivel
     * 
     */
    public void tob() {

    }

    /**
     * Informa ao mercado os eventos ocorridos
     */
    public void informMarket() {

	mbo();
	mbp();
	tob();

    }


    private void sendUMDF(Message message) {
	for (SessionID sid : SessionRepository.getInstance().getSessions(StreamTypes.MARKET_DATA).values()) {
	    sendMessage(message, sid);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.messaging.MessageSender#sendMessage(quickfix.Message,
     * quickfix.SessionID)
     */
    @Override
    public boolean sendMessage(Message message, SessionID sessionID) {
	return MessageRepository.getInstance().addMessage(message, sessionID);
    }

}
