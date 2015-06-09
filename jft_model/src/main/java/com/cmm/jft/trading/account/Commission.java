/**
 * 
 */
package com.cmm.jft.trading.account;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.financial.Account;
import com.cmm.jft.trading.enums.ValueTypes;

/**
 * <p>
 * <code>Commission.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 09/08/2014 01:10:06
 *
 */
@Entity
@Table(name = "Commission")
public class Commission implements DBObject<Commission> {

	@Id
	@SequenceGenerator(name = "COMMISSION_SEQ", sequenceName = "COMMISSION_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "COMMISSION_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "commissionID", nullable = false)
	private Long commissionID;

	@Column(name = "ValueMin")
	private double valueMin;

	@Column(name = "ValueMax")
	private double valueMax;

	@Column(name = "CommValue")
	private double commValue;

	@Enumerated(EnumType.STRING)
	@Column(name = "CalcTypes", length = 30)
	private ValueTypes calcType;

	@JoinColumn(name = "brokerageID", referencedColumnName = "brokerageID", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Brokerage brokerageID;

	/**
     * 
     */
	public Commission() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param valueMin
	 * @param valueMax
	 * @param commValue
	 * @param calcType
	 * @param brokerageID
	 * @param accountID
	 */
	public Commission(double valueMin, double valueMax, double commValue,
			ValueTypes calcType, Brokerage brokerageID) {
		super();
		this.valueMin = valueMin;
		this.valueMax = valueMax;
		this.commValue = commValue;
		this.calcType = calcType;
		this.brokerageID = brokerageID;
	}

	/**
	 * @return the valueMin
	 */
	public double getValueMin() {
		return this.valueMin;
	}

	/**
	 * @param valueMin
	 *            the valueMin to set
	 */
	public void setValueMin(double valueMin) {
		this.valueMin = valueMin;
	}

	/**
	 * @return the valueMax
	 */
	public double getValueMax() {
		return this.valueMax;
	}

	/**
	 * @param valueMax
	 *            the valueMax to set
	 */
	public void setValueMax(double valueMax) {
		this.valueMax = valueMax;
	}

	/**
	 * @return the commValue
	 */
	public double getCommValue() {
		return this.commValue;
	}

	/**
	 * @param commValue
	 *            the commValue to set
	 */
	public void setCommValue(double commValue) {
		this.commValue = commValue;
	}

	/**
	 * @return the calcType
	 */
	public ValueTypes getCalcType() {
		return this.calcType;
	}

	/**
	 * @param calcType
	 *            the calcType to set
	 */
	public void setCalcType(ValueTypes calcType) {
		this.calcType = calcType;
	}

	/**
	 * @return the commissionID
	 */
	public Long getCommissionID() {
		return this.commissionID;
	}

	/**
	 * @return the brokerageID
	 */
	public Brokerage getBrokerageID() {
		return this.brokerageID;
	}

}