/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.*;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.marketdata.HistoricalQuote;
import com.cmm.jft.marketdata.MarketOrder;
import com.cmm.jft.trading.Orders;

/**
 *
 * <p>
 * <code>Security</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
@Entity
@Table(name = "Security", schema="Security")
//, uniqueConstraints=@UniqueConstraint(columnNames= {"Symbol"}) )
@NamedQueries({
		@NamedQuery(name = "Security.findAll", query = "SELECT e FROM Security e"),
		@NamedQuery(name = "Security.findBySecurityID", query = "SELECT e FROM Security e WHERE e.securityID = :securityID"),
		@NamedQuery(name = "Security.findBySymbol", query = "SELECT e FROM Security e WHERE e.symbol = :symbol"),
// @NamedQuery(name = "Security.findByIsin", query =
// "SELECT e FROM Security e WHERE e.isin = :isin"),
// @NamedQuery(name = "Security.findBySecurityName", query =
// "SELECT e FROM Security e WHERE e.securityName = :securityName")
})
public class Security implements DBObject<Security> {

	@Id
	@SequenceGenerator(name = "SECURITY_SEQ", sequenceName = "SECURITY_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "SECURITY_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "securityID", nullable = false)
	private int securityID;
	
	@Column(name = "SecurityIDSrc", nullable = true, length = 1)
	private char securityIDSrc;

	@Basic(optional = false)
	@Column(name = "Symbol", nullable = false, length = 50)
	private String symbol;
	
	@Column(name = "Description", length = 255)
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "securityID", fetch = FetchType.LAZY)
	private Set<Orders> ordersSet;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "securityID", fetch = FetchType.LAZY)
	private Set<MarketOrder> marketOrderSet;

	@OrderBy(value = "QDateTime")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "securityID", fetch = FetchType.LAZY)
	private Set<HistoricalQuote> historicalQuoteSet;

	@OneToOne(cascade=CascadeType.ALL)
	private SecurityInfo securityInfoID;
	
	//@JoinColumn(name = "stockExchangeID", referencedColumnName = "stockExchangeID", nullable = false)
	//@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private String securityExchange;//stockExchangeID;

	public Security() {
		this.securityIDSrc = '0';
		this.ordersSet = new HashSet<Orders>();
		this.marketOrderSet = new HashSet<MarketOrder>();
		this.historicalQuoteSet = new HashSet<HistoricalQuote>();
	}

	public Security(String symbol) {
		this.symbol = symbol;
		this.securityIDSrc = '0';
		this.ordersSet = new HashSet<Orders>();
		this.marketOrderSet = new HashSet<MarketOrder>();
		this.historicalQuoteSet = new HashSet<HistoricalQuote>();
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return this.symbol;
	}

	/**
	 * @param symbol
	 *            the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the ordersSet
	 */
	public Set<Orders> getOrdersSet() {
		return this.ordersSet;
	}

	/**
	 * @return the securityID
	 */
	public int getSecurityID() {
		return this.securityID;
	}
	
	public char getSecurityIDSrc() {
		return securityIDSrc;
	}
	
	public void setSecurityID(Integer securityID) {
		this.securityID = securityID;
	}
	
	public void setSecurityIDSrc(char securityIDSrc) {
		this.securityIDSrc = securityIDSrc;
	}

	/**
	 * @return the historicalQuoteSet
	 */
	public Set<HistoricalQuote> getHistoricalQuoteSet() {
		return this.historicalQuoteSet;
	}
	
	/**
	 * @return the marketOrderSet
	 */
	public Set<MarketOrder> getMarketOrderSet() {
		return this.marketOrderSet;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the securityInfoID
	 */
	public SecurityInfo getSecurityInfoID() {
		return this.securityInfoID;
	}
	
	public void setSecurityExchange(String securityExchange) {
		this.securityExchange = securityExchange;
	}
	
	public String getSecurityExchange() {
		return securityExchange;
	}
	
	/**
	 * @param securityInfoID the securityInfoID to set
	 */
	public void setSecurityInfoID(SecurityInfo securityInfoID) {
		this.securityInfoID = securityInfoID;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    return "Security [securityID=" + securityID + ", securityIDSrc=" + securityIDSrc + ", symbol=" + symbol
		    + ", securityExchange=" + securityExchange + "]";
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

}
