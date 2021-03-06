/**
 * 
 */
package com.cmm.jft.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.cmm.jft.core.enums.GeneralStatus;
import com.cmm.jft.db.DBObject;
import com.cmm.jft.financial.Currency;
import com.cmm.jft.trading.enums.AssetTypes;

/**
 * <p>
 * <code>Isin.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 29/09/2013 03:45:42
 *
 */
@Entity
@Table(name = "Isin", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"ISIN"}) })
@NamedQueries({ @NamedQuery(name = "Isin.findAll", query = "SELECT i FROM Isin i") })
public class Isin implements DBObject<Isin> {

	@Id
	@SequenceGenerator(name = "ISIN_SEQ", sequenceName = "ISIN_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "ISIN_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "isinID", nullable = false)
	private Long isinID;

	@Column(name = "ISIN", length = 12, unique=true)
	private String isin;

	@Column(name = "Description", length = 255)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "Status", length = 50)
	private GeneralStatus status;

	public Isin() {
	}

	public Isin(String isin) {
		this.isin = isin;
	}

	/**
	 * @return the isinID
	 */
	public Long getIsinID() {
		return this.isinID;
	}

	/**
	 * @return the isin
	 */
	public String getIsin() {
		return this.isin;
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
	 * @return the status
	 */
	public GeneralStatus getStatus() {
		return this.status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(GeneralStatus status) {
		this.status = status;
	}


}
