/**
 * 
 */
package com.cmm.jft.trading.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Level;

import com.cmm.jft.core.Configuration;
import com.cmm.jft.core.enums.GeneralStatus;
import com.cmm.jft.core.enums.Objects;
import com.cmm.jft.data.connection.Connection;
import com.cmm.jft.data.connection.Event;
import com.cmm.jft.data.connection.EventFields;
import com.cmm.jft.data.connection.Events;
import com.cmm.jft.data.exceptions.ConnectionException;
import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.jft.financial.DistributionRule;
import com.cmm.jft.financial.JournalEntry;
import com.cmm.jft.financial.Rule;
import com.cmm.jft.financial.exceptions.RegistrationException;
import com.cmm.jft.financial.services.JournalService;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.OrdersPrices;
import com.cmm.jft.trading.Position;
import com.cmm.jft.trading.account.Broker;
import com.cmm.jft.trading.account.Brokerage;
import com.cmm.jft.trading.account.Commission;
import com.cmm.jft.trading.account.ExchangeTax;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;
import com.cmm.jft.trading.exceptions.OrderException;
import com.cmm.jft.trading.securities.Security;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>TradingService.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 10/03/2014 16:11:33
 * 
 */
public class TradingService {


	/**
	 * Broker used for trading account.
	 */
	private Broker brokerID;


	private static TradingService instance;

	/**
	 * Market Connection.
	 */
	private Connection connection;

	/**
	 * Map to the existing orders, orders are referenced by Position,
	 * the orderkey are the key.
	 */
	private ConcurrentHashMap<String, Orders> orders;

	/**
	 * Map to get open trades. The String key are the symbol.
	 */
	private ConcurrentHashMap<String, Position> openPositions;

	/**
	 * 
	 */
	private TradingService() {
		String strBroker = (String) Configuration.getInstance().getConfiguration("brokerID");
		brokerID = (Broker) DBFacade.getInstance().findObject(
				"Broker.findByBrokerCode", "brokerCode", strBroker);

		this.orders = new ConcurrentHashMap<String, Orders>();
		this.openPositions = new ConcurrentHashMap<String, Position>();

	}

	/**
	 * @return the instance
	 */
	public static TradingService getInstance() {
		if (instance == null) {
			instance = new TradingService();
		}
		return instance;
	}	

	/**
	 * Load trades with OPEN status from database;
	 */
	public void loadOpenTrades(){
		try {
			//searches for open trades with the brokerid
			String query = String.format("select tradeID from Position "
					+ "where tradestatus = 'OPEN' and brokerID = %d", 
					brokerID.getBrokerID());
			List rs = DBFacade.getInstance().queryNative(query);
			for(Position tr : (List<Position>)rs){
				openPositions.put(tr.getSymbol(), tr);
			}

		} catch (DataBaseException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	}


	/**
	 * Cria as Ordens de compra e venda e registra suas respectivas execucoes
	 * sem passar pelo mercado
	 * 
	 * @param symbol Simbolo do instrumento negociado.
	 * @param orderTypes Tipo de ordem.
	 * @param tradeTypes tipo de Negocio
	 * @param volume Quantidade negociada
	 * @param buyPrice preco executado na compra
	 * @param sellPrice preco executado na venda
	 * @param tradeDate Data da negociacao
	 * @param brokerID corretora
	 * @throws DataBaseException
	 */
	//	public void addCompleteTrade(String symbol, OrderTypes orderTypes,
	//			TradeTypes tradeType, int volume, OrdersPrices buyPrices,
	//			OrdersPrices sellPrices, Date tradeDate, Broker brokerID) throws DataBaseException {
	//		try {
	//
	//			DBFacade.getInstance().beginTransaction();
	//			// obtem o trade
	//			Position trade = openPositions.get(symbol);
	//
	//			// gera as ordens
	//			Orders buyOrder = newOrder(orderTypes, Side.BUY, symbol, volume, buyPrices, tradeDate, tradeType);
	//			Orders sellOrder = newOrder(orderTypes, Side.SELL, symbol, volume, sellPrices, tradeDate, tradeType);
	//
	//			addExecution(buyOrder.getOrderSerial(), tradeDate, volume, buyOrder.getPrice());
	//			addExecution(sellOrder.getOrderSerial(), tradeDate, volume, sellOrder.getPrice());
	//
	//			registerTrade(trade);
	//
	//			DBFacade.getInstance().commit();
	//
	//		} catch (Exception e) {
	//			Logging.getInstance().log(getClass(),
	//					"Erro ao Adicionar ordens: " + e.getMessage(), e,
	//					Level.ERROR, false);
	//		} finally {
	//			DBFacade.getInstance().closeSession();
	//		}
	//
	//	}

	public Orders newOrder(OrderTypes orderType, Side side, String symbol,
			int volume, double price, double stopPrice, Date duration, TradeTypes tradeType) {

		Orders ordr = null;

		try {
			Position position = null;

			if(!openPositions.containsKey(symbol)){
				openPositions.put(symbol, new Position(symbol)); 
			}

			position = openPositions.get(symbol);

			Security securityID = SecurityService.getInstance().provideSecurity(symbol);
			Orders[] ordrs = OrderService.getInstance().newOrder(orderType, securityID, side, price, stopPrice, volume);
			for(Orders o:ordrs){

				ordr.setDuration(duration);
				ordr.setTradeType(tradeType);

				position.addOrder(ordr);
				orders.put(ordr.getOrderSerial(), ordr);
			}
			//Cria o evento e adiciona no mercado
			sendNewOrderEvent(ordr);

		} catch (OrderException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		} catch (Exception e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
		return ordr;
	}	


	/**
	 * 
	 * @param trade
	 */
	public void closePosition(Position position) {

		try {

			// cancela as ordens abertas
			for (Orders order : position.getOrdersList()) {
				cancelOrder(order.getOrderSerial()); 
			}

			// cria ordem de acordo com posicao aberta
			int openPosition = position.getPosition();
			if (openPosition != 0) {
				Side side = openPosition < 0 ? Side.BUY : Side.SELL;//se ta comprado passa vendido ou vv
				int volume = openPosition>0?openPosition:openPosition*-1;//ajusta o volume para nao passar negativo

				//calcula a duracao da ordem - ate o fim do dia
				LocalDateTime ldt = LocalDate.now().atTime(23, 59, 50);
				Date duration = Date.from(ZonedDateTime.of(ldt, ZoneId.systemDefault()).toInstant());

				// lanca ordem inversa(a posicao) a mercado do mesmo tipo do Position
				newOrder(OrderTypes.Market, side, position.getSymbol(), volume, 0, 0, duration, TradeTypes.DAY_TRADE);

			}

		} catch (Exception e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}

	}

	public void cancelOrder(String orderSerial) {
		Orders order = orders.get(orderSerial);
		try {
			sendCancelOrderEvent(order);
			order.cancel();
			orders.put(order.getOrderSerial(), order);
		} catch (ConnectionException | OrderException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}

	}

	public void changePrice(String orderSerial, double price, double stopPrice) {
		// verifica se a ordem pode ser alterada
		Orders order = orders.get(orderSerial);
		try{
			if (order.changePrice(price) && order.changeStopPrice(stopPrice)) {
				sendChangeOrderEvent(order);
				orders.put(order.getOrderSerial(), order);
			}
		} catch(OrderException e){
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		} catch (ConnectionException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	}

	public void changeVolume(String orderSerial, int volume) {
		// verifica se a ordem pode ser alterada
		Orders order = orders.get(orderSerial);
		try{
			if (order.changeVolume(volume)) {
				sendChangeOrderEvent(order);
				orders.put(order.getOrderSerial(), order);
			}
		} catch(OrderException e){
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		} catch (ConnectionException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	}


	private void sendNewOrderEvent(Orders ordr) throws ConnectionException, OrderException{
		Event event = new Event();

		event.addValue(EventFields.EventType, Events.ORDER_SEND);
		event.addValue(EventFields.OrderID, ordr.getOrderID());
		event.addValue(EventFields.OrderSide, ordr.getSide());
		event.addValue(EventFields.OrderDate, ordr.getOrderDateTime());
		event.addValue(EventFields.OrderExpireDate, ordr.getDuration());
		event.addValue(EventFields.OrderType, ordr.getOrderType());	
		event.addValue(EventFields.OrderVolume, ordr.getVolume());
		event.addValue(EventFields.OrderStopPrice, ordr.getStopPrice());

		//envia o evento para a conexao
		Event retev = connection.sendEvent(event);

		//verifica o retorno
		if(retev.getValue(EventFields.EventType) == Events.ORDER_SEND){
			Logging.getInstance().log(getClass(), "Order " + ordr.getOrderSerial() + " has sent to market.", Level.INFO);
		}else{
			throw new OrderException("Error sending order " + ordr.getOrderSerial() + retev.getValue(EventFields.Message));
		}

	}

	private void sendCancelOrderEvent(Orders ordr) throws ConnectionException, OrderException{
		Event event = new Event();

		event.addValue(EventFields.EventType, Events.ORDER_CANCEL);
		event.addValue(EventFields.OrderID, ordr.getOrderSerial());

		Event ret = connection.sendEvent(event);
		if(ret.getValue(EventFields.EventType) == Events.ORDER_CANCEL){
			Logging.getInstance().log(getClass(), "Order " + ordr.getOrderSerial() + " has cancelled.", Level.INFO);
		}else{
			throw new OrderException("Error sending order " + ordr.getOrderSerial() + ret.getValue(EventFields.Message));
		}

	}

	private void sendChangeOrderEvent(Orders ordr) throws ConnectionException, OrderException{
		Event event = new Event();

		event.addValue(EventFields.EventType, Events.ORDER_UPDATE);
		event.addValue(EventFields.OrderID, ordr.getOrderSerial());
		event.addValue(EventFields.OrderVolume, ordr.getVolume());
		event.addValue(EventFields.OrderStopPrice, ordr.getStopPrice());

		Event ret = connection.sendEvent(event);
		if(ret.getValue(EventFields.EventType) == Events.ORDER_UPDATE){
			Logging.getInstance().log(getClass(), "Order " + ordr.getOrderSerial() + " has changed.", Level.INFO);
		}else{
			throw new OrderException("Error sending order " + ordr.getOrderSerial() + ret.getValue(EventFields.Message));
		}
	}

	/**
	 * Cria o evento de execucao e a execucao e os adiciona na ordem 
	 * passada por parametro.
	 * 
	 * @param order
	 * @param executionDateTime
	 * @param execVolume
	 * @param execPrice
	 * @return Ordem adicionada da Execucao e Evento de Execucao
	 */
	public boolean addExecution(String orderSerial, Date executionDateTime, int execVolume, double execPrice) {
		boolean ret = false;
		try {
			if(orders.contains(orderSerial)){
				ret = orders.get(orderSerial).addExecution(executionDateTime, execVolume, execPrice);
			}
		} catch (OrderException e) {
			Logging.getInstance().log(getClass(),
					"Erro ao criar execucao de ordem: " + e.getMessage(), e,
					Level.ERROR, false);
		}
		return ret;
	}

	/**
	 * Realiza a contabilidade de uma posicao zerada
	 * @param position
	 */
	public void registerTrade(Position position) {

		try {
			// verifica se o trade esta aberto
			JournalEntry je = JournalService.getInstance().createEntry();

			// verifica se o trade esta fechado, caso esteja, a posizao ja foi
			// zerada e deve adicionar registro de lucro/perda e custos
			if (position.getPosition() == 0) {

				Brokerage brokerage = position.getBrokerage();
				int volume = position.getTradedVolume();
				double value = position.getTradeValue().doubleValue();

				DistributionRule dRule = JournalService.getInstance().getDistributionRule(Objects.Trade);

				for(Rule rule:dRule.getRuleSet()){

					//-------------------------------------------Registra Comissoes
					if(rule.getObject() == Objects.Commission){
						for (Commission comm : brokerage.getCommissionList()) {
							double valaux = 0;
							switch (comm.getCalcType()) {
							case VALUE:
								valaux = value;
								break;
							case VOLUME:
								valaux = volume;
								break;
							default:
								valaux = value;
								break;
							}

							if (valaux > comm.getValueMin() && valaux <= comm.getValueMax()) {
								BigDecimal commValue = new BigDecimal(comm.getCommValue());
								JournalService.getInstance().registerEntry(je, 
										rule.getCreditAccountID(), rule.getDebitAccountID(),
										commValue, "Commission");
								break;
							}

						}
					}

					//-----------------------------------------------Registra Taxas
					else if(rule.getObject() == Objects.ExchangeTax){
						BigDecimal taxValue = null;
						for (ExchangeTax et : brokerage.getExchangeTaxList()) {
							switch (et.getCalcType()) {
							case TAX:
								taxValue = new BigDecimal(et.getTax() * value);
								break;
							case VALUE:
								taxValue = new BigDecimal(et.getTax());
								break;
							default:
								taxValue = new BigDecimal(et.getTax() * value);
								break;
							}

							JournalService.getInstance().registerEntry(je,
									rule.getCreditAccountID(), rule.getDebitAccountID(),
									taxValue, et.getTaxName());
						}
					}

					else {
						//-------------------------------------Registra lucro/prejuizo
						BigDecimal buyPrice = new BigDecimal(0);
						BigDecimal sellPrice = new BigDecimal(0);

						for (Orders order : position.getOrdersList()) {
							if (order.getSide() == Side.BUY) {
								volume = order.getExecutedVolume();
								buyPrice = buyPrice.add(order.getAveragePrice());
							} else if (order.getSide() == Side.SELL) {
								sellPrice = sellPrice.add(order.getAveragePrice());
							}
						}

						String descr = "Profit register";
						BigDecimal profit = buyPrice.subtract(sellPrice).multiply(new BigDecimal(volume));

						JournalService.getInstance().registerEntry(je,
								rule.getCreditAccountID(), rule.getDebitAccountID(), profit, descr);	
					}

				}

				//fecha o je
				je = JournalService.getInstance().closeEntry(je);

			}

		} catch (DataBaseException | RegistrationException e) {
			Logging.getInstance().log(TradingService.class,
					"Error in Order Register.", e, Level.ERROR, false);
		} 
	}




	//-------------------------------------------------------------------------
	//	private Orders updateOrder(Orders order) {
	//		try {
	//			order = (Orders) DBFacade.getInstance()._update(order);
	//			//order.refreshOrder();
	//			order = (Orders) DBFacade.getInstance()._update(order);
	//		} catch (OrderException e) {
	//			Logging.getInstance().log(getClass(),
	//					"Erro ao atualizar Ordem: " + order.getOrderSerial(), e,
	//					Level.ERROR, false);
	//		} catch (DataBaseException e) {
	//			Logging.getInstance().log(getClass(),
	//					"Erro ao atualizar Ordem: " + order.getOrderSerial(), e,
	//					Level.ERROR, false);
	//		}
	//		return order;
	//	}


}