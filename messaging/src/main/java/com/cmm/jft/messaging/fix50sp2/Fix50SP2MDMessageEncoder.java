/**
 * 
 */
package com.cmm.jft.messaging.fix50sp2;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import com.cmm.jft.core.Configuration;
import com.cmm.jft.messaging.MarketDataMessageEncoder;
import com.cmm.jft.messaging.enums.DepthTypes;
import com.cmm.jft.messaging.enums.EntryTypes;
import com.cmm.jft.trading.enums.UpdateActions;
import com.cmm.jft.security.Security;
import com.cmm.jft.trading.Orders;

import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.FieldException;
import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.field.Headline;
import quickfix.field.MDEntryBuyer;
import quickfix.field.MDEntryDate;
import quickfix.field.MDEntryPositionNo;
import quickfix.field.MDEntryPx;
import quickfix.field.MDEntrySeller;
import quickfix.field.MDEntrySize;
import quickfix.field.MDEntryTime;
import quickfix.field.MDEntryType;
import quickfix.field.MDUpdateAction;
import quickfix.field.NewSeqNo;
import quickfix.field.NumberOfOrders;
import quickfix.field.OrderID;
import quickfix.field.OrigTime;
import quickfix.field.RptSeq;
import quickfix.field.SecurityReqID;
import quickfix.field.SecurityRequestResult;
import quickfix.field.SecurityResponseID;
import quickfix.field.Text;
import quickfix.field.TradeID;
import quickfix.field.TradeVolume;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataIncrementalRefresh.NoMDEntries;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import quickfix.fix44.News;
import quickfix.fix44.SecurityList;
import quickfix.fix44.SecurityStatus;
import quickfix.fix44.SequenceReset;

/**
 * <p>
 * <code>Fix50SP2MDMessageEncoder.java</code>
 * </p>
 * 
 * @author cristiano
 * @version Feb 26, 2016 4:56:04 PM
 *
 */
public class Fix50SP2MDMessageEncoder implements MarketDataMessageEncoder {
    
    private Configuration configuration;
    private DataDictionary dictionary;
    private static Fix50SP2MDMessageEncoder instance;

    private Fix50SP2MDMessageEncoder() {
	this.configuration = Configuration.getInstance();
	try {
	    this.dictionary = new DataDictionary(this.getClass().getResourceAsStream("/dictionaries/FIX44.xml"));
	} catch (ConfigError e) {
	    e.printStackTrace();
	}
	
    }

    public static synchronized Fix50SP2MDMessageEncoder getInstance() {
	if (instance == null) {
	    instance = new Fix50SP2MDMessageEncoder();
	}

	return instance;
    }

    private void putSecurity(Security security, Group entry){
	entry.setInt(48, security.getSecurityID());
	entry.setString(22, "8");
	entry.setString(207, "BVMF");
    }
    
    private void setHeader(Message message) {
	
	String scid = (String) configuration.getConfiguration("senderCompID");
	message.setString(49, scid);
	
    }
    

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#sequenceReset()
     */
    @Override
    public SequenceReset sequenceReset() {
	return new SequenceReset(new NewSeqNo(1));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#securityList()
     */
    @Override
    public SecurityList securityList(Security[] securities) {

	SecurityList list = null;
	try {
	    list = new SecurityList();

	    // list.setString(SecurityReqID.FIELD, "0");
	    // list.setString(SecurityResponseID.FIELD, "0");
	    // list.setInt(SecurityRequestResult.FIELD,
	    // SecurityRequestResult.VALID_REQUEST);

	    for (int i = 0; (i < securities.length && securities[i] != null); i++) {
		SecurityList.NoRelatedSym g = new SecurityList.NoRelatedSym();
		g.setString(55, securities[i].getSymbol());

		putSecurity(securities[i], g);

		g.setString(107, securities[i].getDescription());

		g.setDouble(969, securities[i].getSecurityInfoID().getTickSize());
		g.setInt(9749, securities[i].getSecurityInfoID().getMinVolume());
		g.setInt(9748, securities[i].getSecurityInfoID().getMaxVolume());
		g.setInt(5151, securities[i].getSecurityInfoID().getDigits());

		list.addGroup(g);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}

	return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmm.jft.messaging.MarketDataMessageEncoder#mdIncrementalRefresh()
     */
    @Override
    public MarketDataIncrementalRefresh mdIncrementalRefresh(Queue<MarketDataIncrementalRefresh.NoMDEntries> entries) {

	MarketDataIncrementalRefresh refresh = new MarketDataIncrementalRefresh();

	for (Group g : entries) {
	    refresh.addGroup(g);
	}

	return refresh;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmm.jft.messaging.MarketDataMessageEncoder#mdSnapShotFullRefresh()
     */
    @Override
    public MarketDataSnapshotFullRefresh mdSnapShotFullRefresh(Queue<MarketDataSnapshotFullRefresh.NoMDEntries> mdEntries) {

	MarketDataSnapshotFullRefresh snapshotFullRefresh = new MarketDataSnapshotFullRefresh();
	for (Group g : mdEntries) {
	    snapshotFullRefresh.addGroup(g);
	}

	return snapshotFullRefresh;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#securityStatus()
     */
    @Override
    public SecurityStatus securityStatus() {
	// TODO Auto-generated method stub
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#nonFixData()
     */
    @Override
    public Message nonFixData() {
	// TODO Auto-generated method stub
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.messaging.MessageEncoder#heartbeat()
     */
    @Override
    public Heartbeat heartbeat() {
	Heartbeat heart = new Heartbeat();

	return heart;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmm.jft.messaging.MarketDataMessageEncoder#news(java.lang.String)
     */
    @Override
    public News news(String title, String message, String source, int newsID) {
	News news = new News(new Headline(title));
	news.set(new OrigTime(new Date()));
	news.setString(6940, "18");
	news.setString(1474, "pt");
	
	news.setString(6940, source);//NewsSource
	news.setString(1472, String.format("%1$07d", newsID));
	
	News.LinesOfText lines = new News.LinesOfText();
	lines.set(new Text(message));
	news.addGroup(lines);
	
//	try {
//	    dictionary.validate(news);
//	} catch (IncorrectTagValue | FieldNotFound | IncorrectDataFormat | FieldException e) {
//	    e.printStackTrace();
//	}

	return news;
    }




    //-----------------------------------------------------------------INCREMENTAL
    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#bidEntryIncMBO(
     * com.cmm.jft.trading.Orders, int, com.cmm.jft.messaging.enums.UpdateActions)
     */
    @Override
    public NoMDEntries bidEntryIncMBO(Orders order, int positionNo, UpdateActions updateAction, int rptSeq) {
	
	MarketDataIncrementalRefresh.NoMDEntries entry = new MarketDataIncrementalRefresh.NoMDEntries();

	putSecurity(order.getSecurityID(), entry);

	Date d = Date.from(Instant.now());
	
	entry.setInt(83, rptSeq);
	entry.set(updateAction.getValue());
	entry.set(EntryTypes.Bid.getValue());
	entry.set(new MDEntryPx(order.getPrice()));
	entry.set(new MDEntrySize(order.getVolume()));
	entry.set(new MDEntryDate(d));
	entry.set(new MDEntryTime(d));
	entry.setUtcDateOnly(37016, order.getInsertDate());
	entry.setUtcTimeOnly(37017, order.getInsertTime());

	entry.set(new MDEntryBuyer(order.getBrokerID()));

	entry.set(new MDEntryPositionNo(positionNo));
	entry.set(new OrderID(order.getOrderID()));

	return entry;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#bidEntryIncMBP(
     * double, int, int, com.cmm.jft.security.Security, int, com.cmm.jft.messaging.enums.UpdateActions)
     */
    @Override
    public NoMDEntries bidEntryIncMBP(double price, int size, int numOrders, Security security, int positionNo,
	    UpdateActions updateAction, int rptSeq) {
	MarketDataIncrementalRefresh.NoMDEntries entry = new MarketDataIncrementalRefresh.NoMDEntries();

	putSecurity(security, entry);

	Date d = Date.from(Instant.now());

	entry.setInt(83, rptSeq);
	entry.set(updateAction.getValue());
	entry.set(EntryTypes.Bid.getValue());
	if(price >0){
	    entry.set(new MDEntryPx(price));
	}

	if(size >0){
	    entry.set(new MDEntrySize(size));
	}

	entry.set(new MDEntryDate(d));
	entry.set(new MDEntryTime(d));

	if(numOrders > 0){
	    entry.set(new NumberOfOrders(numOrders));
	}

	entry.set(new MDEntryPositionNo(positionNo));

	return entry;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#offerEntryIncMBO(
     * com.cmm.jft.trading.Orders, int, com.cmm.jft.messaging.enums.UpdateActions)
     */
    @Override
    public NoMDEntries offerEntryIncMBO(Orders order, int positionNo, UpdateActions updateAction, int rptSeq) {
	
	MarketDataIncrementalRefresh.NoMDEntries entry = new MarketDataIncrementalRefresh.NoMDEntries();

	putSecurity(order.getSecurityID(), entry);

	Date d = Date.from(Instant.now());

	entry.setInt(83, rptSeq);
	entry.set(updateAction.getValue());
	entry.set(EntryTypes.Offer.getValue());
	entry.set(new MDEntryPx(order.getPrice()));
	entry.set(new MDEntrySize(order.getVolume()));
	entry.set(new MDEntryDate(d));
	entry.set(new MDEntryTime(d));
	entry.setUtcDateOnly(37016, order.getInsertDate());
	entry.setUtcTimeOnly(37017, order.getInsertTime());

	entry.set(new MDEntryBuyer(order.getBrokerID()));

	entry.set(new MDEntryPositionNo(positionNo));
	entry.set(new OrderID(order.getOrderID()));

	return entry;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#offerEntryIncMBP(
     * double, int, int, com.cmm.jft.security.Security, int, com.cmm.jft.messaging.enums.UpdateActions)
     */
    @Override
    public NoMDEntries offerEntryIncMBP(double price, int size, int numOrders, Security security, int positionNo,
	    UpdateActions updateAction, int rptSeq) {
	
	MarketDataIncrementalRefresh.NoMDEntries entry = new MarketDataIncrementalRefresh.NoMDEntries();

	putSecurity(security, entry);

	Date d = Date.from(Instant.now());
	
	entry.setInt(83, rptSeq);
	entry.set(updateAction.getValue());
	entry.set(EntryTypes.Offer.getValue());
	if(price >0){
	    entry.set(new MDEntryPx(price));
	}

	if(size >0){
	    entry.set(new MDEntrySize(size));
	}

	entry.set(new MDEntryDate(d));
	entry.set(new MDEntryTime(d));

	if(numOrders > 0){
	    entry.set(new NumberOfOrders(numOrders));
	}

	entry.set(new MDEntryPositionNo(positionNo));

	return entry;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#tradeEntryInc(
     * com.cmm.jft.messaging.enums.UpdateActions, com.cmm.jft.security.Security, 
     * java.lang.String, java.lang.String, double, int, java.util.Date, java.util.Date, java.lang.String, int)
     */
    @Override
    public NoMDEntries tradeEntryInc(UpdateActions updateAction, Security security, String buyer, String seller,
            double price, int size, Date tradeDate, Date tradeTime, String tradeID, int tradeVolume, int rptSeq) {
	
	MarketDataIncrementalRefresh.NoMDEntries entry = new MarketDataIncrementalRefresh.NoMDEntries();

	putSecurity(security, entry);

	Date d = Date.from(Instant.now());

	entry.setInt(83, rptSeq);
	entry.set(EntryTypes.Trade.getValue());
	entry.set(updateAction.getValue());
	entry.set(new MDEntryDate(d));
	entry.set(new MDEntryTime(d));
	
	entry.set(new MDEntryPx(price));
	entry.set(new MDEntrySize(size));
	
	entry.setUtcDateOnly(37016, tradeDate);
	entry.setUtcTimeOnly(37017, tradeTime);
	
	entry.set(new MDEntryBuyer(buyer));
	entry.set(new MDEntrySeller(seller));
	entry.setDouble(1020, tradeVolume);
	entry.setString(1003, tradeID);
	
	return entry;
    }
    
    

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#openPriceEntryInc(
     * com.cmm.jft.messaging.enums.UpdateActions, com.cmm.jft.security.Security, double)
     */
    @Override
    public MarketDataIncrementalRefresh.NoMDEntries openPriceEntryInc(
	    UpdateActions updateAction, Security security, double openPrice, int rptSeq) {
	
	MarketDataIncrementalRefresh.NoMDEntries entry = new MarketDataIncrementalRefresh.NoMDEntries();

	putSecurity(security, entry);

	Date d = Date.from(Instant.now());

	entry.setInt(83, rptSeq);
	entry.set(EntryTypes.OpeningPrice.getValue());
	entry.set(updateAction.getValue());
	entry.set(new MDEntryDate(d));
	entry.set(new MDEntryTime(d));
	entry.set(new MDEntryPx(openPrice));
	
	return entry;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#closePriceEntryInc(
     * com.cmm.jft.security.Security, double)
     */
    @Override
    public MarketDataIncrementalRefresh.NoMDEntries closePriceEntryInc(
	    Security security, double closePrice, int rptSeq) {
	
	MarketDataIncrementalRefresh.NoMDEntries entry = new MarketDataIncrementalRefresh.NoMDEntries();

	putSecurity(security, entry);

	Date d = Date.from(Instant.now());

	entry.setInt(83, rptSeq);
	entry.set(EntryTypes.ClosingPrice.getValue());
	entry.set(UpdateActions.New.getValue()); //No delete, aways new(replace)
	entry.set(new MDEntryDate(d));
	entry.set(new MDEntryTime(d));
	entry.set(new MDEntryPx(closePrice));
	
	return entry;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#highPriceEntryInc(
     * com.cmm.jft.messaging.enums.UpdateActions, com.cmm.jft.security.Security, double)
     */
    @Override
    public MarketDataIncrementalRefresh.NoMDEntries highPriceEntryInc(
	    UpdateActions updateAction, Security security, double highPrice, int rptSeq) {

	MarketDataIncrementalRefresh.NoMDEntries entry = new MarketDataIncrementalRefresh.NoMDEntries();

	putSecurity(security, entry);

	Date d = Date.from(Instant.now());

	entry.setInt(83, rptSeq);
	entry.set(EntryTypes.HighPrice.getValue());
	entry.set(updateAction.getValue()); 
	entry.set(new MDEntryDate(d));
	entry.set(new MDEntryTime(d));
	entry.set(new MDEntryPx(highPrice));
	
	return entry;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#lowPriceEntryInc(
     * com.cmm.jft.messaging.enums.UpdateActions, com.cmm.jft.security.Security, double)
     */
    @Override
    public MarketDataIncrementalRefresh.NoMDEntries lowPriceEntryInc(
	    UpdateActions updateAction, Security security, double lowPrice, int rptSeq) {
	
	MarketDataIncrementalRefresh.NoMDEntries entry = new MarketDataIncrementalRefresh.NoMDEntries();

	putSecurity(security, entry);

	Date d = Date.from(Instant.now());

	entry.setInt(83, rptSeq);
	entry.set(EntryTypes.LowPrice.getValue());
	entry.set(updateAction.getValue()); 
	entry.set(new MDEntryDate(d));
	entry.set(new MDEntryTime(d));
	entry.set(new MDEntryPx(lowPrice));
	
	return entry;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#vwapPriceEntryInc(
     * com.cmm.jft.messaging.enums.UpdateActions, com.cmm.jft.security.Security, double)
     */
    @Override
    public MarketDataIncrementalRefresh.NoMDEntries vwapPriceEntryInc(
	    UpdateActions updateAction, Security security, double vwapPrice, int rptSeq) {
	
	MarketDataIncrementalRefresh.NoMDEntries entry = new MarketDataIncrementalRefresh.NoMDEntries();

	putSecurity(security, entry);

	Date d = Date.from(Instant.now());

	entry.setInt(83, rptSeq);
	entry.set(EntryTypes.VWAPPrice.getValue());
	entry.set(updateAction.getValue()); 
	entry.set(new MDEntryDate(d));
	entry.set(new MDEntryTime(d));
	entry.set(new MDEntryPx(vwapPrice));
	
	return entry;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#tradeVolumeEntryInc(
     * com.cmm.jft.security.Security, double)
     */
    @Override
    public MarketDataIncrementalRefresh.NoMDEntries tradeVolumeEntryInc(
	    Security security, int numOfTrades, double financialVolume, double tradedVolume, int rptSeq) {
	
	MarketDataIncrementalRefresh.NoMDEntries entry = new MarketDataIncrementalRefresh.NoMDEntries();

	putSecurity(security, entry);

	Date d = Date.from(Instant.now());

	entry.setInt(83, rptSeq);
	entry.set(EntryTypes.TradeVolume.getValue());
	entry.set(UpdateActions.New.getValue()); 
	entry.set(new MDEntryDate(d));
	entry.set(new MDEntryTime(d));
	entry.set(new MDEntryPx(financialVolume));
	entry.set(new MDEntrySize(numOfTrades));
	entry.setDouble(1020, tradedVolume);
	
	return entry;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#emptyBookEntryInc(com.cmm.jft.security.Security)
     */
    @Override
    public MarketDataIncrementalRefresh.NoMDEntries emptyBookEntryInc(Security security, int rptSeq) {

	MarketDataIncrementalRefresh.NoMDEntries entry = new MarketDataIncrementalRefresh.NoMDEntries();

	putSecurity(security, entry);

	Date d = Date.from(Instant.now());

	entry.setInt(83, rptSeq);
	entry.set(EntryTypes.EmptyBook.getValue());
	entry.set(UpdateActions.New.getValue()); 
	entry.set(new MDEntryDate(d));
	entry.set(new MDEntryTime(d));
	
	return entry;
	
    }




    //-----------------------------------------------------------------SNAPSHOT
    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#bidEntrySnp(
     * com.cmm.jft.trading.Orders, com.cmm.jft.messaging.enums.UpdateActions, com.cmm.jft.messaging.enums.DepthTypes)
     */
    @Override
    public MarketDataSnapshotFullRefresh.NoMDEntries bidEntrySnp(
	    Orders order, UpdateActions updateAction, DepthTypes depth) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#offerEntrySnp(
     * com.cmm.jft.trading.Orders, com.cmm.jft.messaging.enums.UpdateActions, com.cmm.jft.messaging.enums.DepthTypes)
     */
    @Override
    public MarketDataSnapshotFullRefresh.NoMDEntries offerEntrySnp(
	    Orders order, UpdateActions updateAction, DepthTypes depth) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#tradeEntrySnp(
     * com.cmm.jft.messaging.enums.UpdateActions, com.cmm.jft.trading.Orders, com.cmm.jft.trading.Orders, int, double)
     */
    @Override
    public MarketDataSnapshotFullRefresh.NoMDEntries tradeEntrySnp(
	    UpdateActions updateAction, Orders buyOrder, Orders sellOrder, int volume, double price) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#openPriceEntrySnp(
     * com.cmm.jft.messaging.enums.UpdateActions, com.cmm.jft.security.Security, double)
     */
    @Override
    public MarketDataSnapshotFullRefresh.NoMDEntries openPriceEntrySnp(
	    UpdateActions updateAction, Security security, double openPrice) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#closePriceEntrySnp(
     * com.cmm.jft.messaging.enums.UpdateActions, com.cmm.jft.security.Security, double)
     */
    @Override
    public MarketDataSnapshotFullRefresh.NoMDEntries closePriceEntrySnp(
	    UpdateActions updateAction, Security security, double closePrice) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#highPriceEntrySnp(
     * com.cmm.jft.messaging.enums.UpdateActions, com.cmm.jft.security.Security, double)
     */
    @Override
    public MarketDataSnapshotFullRefresh.NoMDEntries highPriceEntrySnp(
	    UpdateActions updateAction, Security security, double highPrice) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#lowPriceEntrySnp(
     * com.cmm.jft.messaging.enums.UpdateActions, com.cmm.jft.security.Security, double)
     */
    @Override
    public MarketDataSnapshotFullRefresh.NoMDEntries lowPriceEntrySnp(
	    UpdateActions updateAction, Security security, double lowPrice) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#vwapPriceEntrySnp(
     * com.cmm.jft.messaging.enums.UpdateActions, com.cmm.jft.security.Security, double)
     */
    @Override
    public MarketDataSnapshotFullRefresh.NoMDEntries vwapPriceEntrySnp(
	    UpdateActions updateAction, Security security, double vwapPrice) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#tradeVolumeEntrySnp(
     * com.cmm.jft.security.Security, double)
     */
    @Override
    public MarketDataSnapshotFullRefresh.NoMDEntries tradeVolumeEntrySnp(
	    Security security, double vwapPrice) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.messaging.MarketDataMessageEncoder#emptyBookEntrySnp(com.cmm.jft.security.Security)
     */
    @Override
    public MarketDataSnapshotFullRefresh.NoMDEntries emptyBookEntrySnp(Security security) {
	// TODO Auto-generated method stub
	return null;
    }

}
