/**
 * 
 */
package com.cmm.jft.engine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DoNotSend;
import quickfix.FieldConvertError;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.LogUtil;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.UnsupportedMessageType;
import quickfix.field.AvgPx;
import quickfix.field.CumQty;
import quickfix.field.ExecType;
import quickfix.field.LastPx;
import quickfix.field.LastQty;
import quickfix.field.LeavesQty;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.fix44.AllocationInstruction;
import quickfix.fix44.NewOrderCross;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.OrderCancelReplaceRequest;
import quickfix.fix44.OrderCancelRequest;
import quickfix.fix44.PositionMaintenanceRequest;
import quickfix.fix44.Quote;
import quickfix.fix44.QuoteCancel;
import quickfix.fix44.QuoteRequest;
import quickfix.fix44.QuoteRequestReject;
import quickfix.fix44.SecurityDefinitionRequest;
import quickfix.fix50sp1.ApplicationMessageRequest;

/**
 * <p><code>EntryPoint.java</code></p>
 * @author Cristiano
 * @version 17/06/2015 17:00:55
 *
 */
public class EntryPoint extends MessageCracker implements Application {

	private static final String VALID_ORDER_TYPES_KEY = "ValidOrderTypes";
	
	private HashSet<String> validOrderTypes;
	
	
	/**
	 * @param settings 
	 * @throws FieldConvertError 
	 * @throws ConfigError 
	 * 
	 */
	public EntryPoint(SessionSettings settings) throws ConfigError, FieldConvertError {
		initializeValidOrderTypes(settings);
	}
	
	
	private void initializeValidOrderTypes(SessionSettings settings) throws ConfigError, FieldConvertError {
        if (settings.isSetting(VALID_ORDER_TYPES_KEY)) {
            List<String> orderTypes = Arrays
                    .asList(settings.getString(VALID_ORDER_TYPES_KEY).trim().split("\\s*,\\s*"));
            validOrderTypes.addAll(orderTypes);
        } else {
            validOrderTypes.add(OrdType.LIMIT + "");
        }
    }
	
	
	/* (non-Javadoc)
	 * @see quickfix.Application#onCreate(quickfix.SessionID)
	 */
	public void onCreate(SessionID sessionId) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onLogon(quickfix.SessionID)
	 */
	public void onLogon(SessionID sessionId) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onLogout(quickfix.SessionID)
	 */
	public void onLogout(SessionID sessionId) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
	 */
	public void toAdmin(Message message, SessionID sessionId) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
	 */
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			RejectLogon {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
	 */
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
	 */
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			UnsupportedMessageType {
		
		crack(message, sessionId);

	}
	
	
	
	
	
	public void onMessage(NewOrderSingle message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(OrderCancelReplaceRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(OrderCancelRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(NewOrderCross message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(SecurityDefinitionRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(QuoteRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(Quote message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(QuoteCancel message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(QuoteRequestReject message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(PositionMaintenanceRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(AllocationInstruction message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(ApplicationMessageRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
/*
	public void onMessage(NewOrderSingle order, SessionID sessionID) throws FieldNotFound,
	UnsupportedMessageType, IncorrectTagValue {
		try {
			validateOrder(order);

			OrderQty orderQty = order.getOrderQty();
			Price price = getPrice(order);

			quickfix.fix44.ExecutionReport accept = new quickfix.fix44.ExecutionReport(
					genOrderID(), genExecID(), new ExecType(ExecType.FILL), new OrdStatus(
							OrdStatus.NEW), order.getSide(), new LeavesQty(order.getOrderQty()
									.getValue()), new CumQty(0), new AvgPx(0));

			accept.set(order.getClOrdID());
			accept.set(order.getSymbol());
			sendMessage(sessionID, accept);

			if (isOrderExecutable(order, price)) {
				quickfix.fix44.ExecutionReport executionReport = new quickfix.fix44.ExecutionReport(genOrderID(),
						genExecID(), new ExecType(ExecType.FILL), new OrdStatus(OrdStatus.FILLED), order.getSide(),
						new LeavesQty(0), new CumQty(orderQty.getValue()), new AvgPx(price.getValue()));

				executionReport.set(order.getClOrdID());
				executionReport.set(order.getSymbol());
				executionReport.set(orderQty);
				executionReport.set(new LastQty(orderQty.getValue()));
				executionReport.set(new LastPx(price.getValue()));

				sendMessage(sessionID, executionReport);
			}
		} catch (RuntimeException e) {
			LogUtil.logThrowable(sessionID, e.getMessage(), e);
		}
	}

	public void onMessage(quickfix.fix50.NewOrderSingle order, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		try {
			validateOrder(order);

			OrderQty orderQty = order.getOrderQty();
			Price price = getPrice(order);

			quickfix.fix50.ExecutionReport accept = new quickfix.fix50.ExecutionReport(
					genOrderID(), genExecID(), new ExecType(ExecType.FILL), new OrdStatus(
							OrdStatus.NEW), order.getSide(), new LeavesQty(order.getOrderQty()
									.getValue()), new CumQty(0));

			accept.set(order.getClOrdID());
			accept.set(order.getSymbol());
			sendMessage(sessionID, accept);

			if (isOrderExecutable(order, price)) {
				quickfix.fix50.ExecutionReport executionReport = new quickfix.fix50.ExecutionReport(
						genOrderID(), genExecID(), new ExecType(ExecType.FILL), new OrdStatus(
								OrdStatus.FILLED), order.getSide(), new LeavesQty(0), new CumQty(
										orderQty.getValue()));

				executionReport.set(order.getClOrdID());
				executionReport.set(order.getSymbol());
				executionReport.set(orderQty);
				executionReport.set(new LastQty(orderQty.getValue()));
				executionReport.set(new LastPx(price.getValue()));
				executionReport.set(new AvgPx(price.getValue()));

				sendMessage(sessionID, executionReport);
			}
		} catch (RuntimeException e) {
			LogUtil.logThrowable(sessionID, e.getMessage(), e);
		}
	}
*/


}
