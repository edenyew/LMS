/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 *
 * @author edenyew
 */
@Entity
public class LendAndReturn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lendAndReturnId;
    
    @Column(nullable = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date lendDate;
    
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;
    
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal fineAmount;
    
    @Column(nullable = false)
    @NotNull
    private boolean paidAlr;
    
    @ManyToOne(optional = false, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "BookID")
    private BookEntity book;
    
    @ManyToOne(optional = false, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "MemberID")
    private MemberEntity member;

    
    public LendAndReturn() {
        this.paidAlr = Boolean.FALSE;
    }
    
    
    public LendAndReturn(Date lendDate, Date returnDate, BigDecimal fineAmount) {
        this.lendDate = lendDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
    }
    

    public Long getLendAndReturnId() {
        return lendAndReturnId;
    }

    public void setLendAndReturnId(Long lendAndReturnId) {
        this.lendAndReturnId = lendAndReturnId;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lendAndReturnId != null ? lendAndReturnId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the lendAndReturnId fields are not set
        if (!(object instanceof LendAndReturn)) {
            return false;
        }
        LendAndReturn other = (LendAndReturn) object;
        if ((this.lendAndReturnId == null && other.lendAndReturnId != null) || (this.lendAndReturnId != null && !this.lendAndReturnId.equals(other.lendAndReturnId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.LendAndReturn[ id=" + lendAndReturnId + " ]";
    }
    
    /**
     * @return the lendDate
     */
    public Date getLendDate() {
        return lendDate;
    }

    /**
     * @param lendDate the lendDate to set
     */
    public void setLendDate(Date lendDate) {
        this.lendDate = lendDate;
    }

    /**
     * @return the returnDate
     */
    public Date getReturnDate() {
        return returnDate;
    }

    /**
     * @param returnDate the returnDate to set
     */
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * @return the fineAmount
     */
    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    /**
     * @param fineAmount the fineAmount to set
     */
    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }   

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public boolean isPaidAlr() {
        return paidAlr;
    }

    public void setPaidAlr(boolean paidAlr) {
        this.paidAlr = paidAlr;
    }
    
}
