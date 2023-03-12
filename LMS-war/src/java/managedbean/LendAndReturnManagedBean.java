package managedbean;

import com.sun.xml.wss.util.DateUtils;
import entity.BookEntity;
import entity.LendAndReturn;
import entity.MemberEntity;
import exception.BookNotFoundException;
import exception.MemberNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import session.BookEntitySessionBeanLocal;
import session.LendAndReturnSessionBeanLocal;
import session.MemberEntitySessionBeanLocal;

/**
 *
 * @author edenyew
 */
@Named(value = "lendAndReturnManagedBean")
@ViewScoped
public class LendAndReturnManagedBean implements Serializable {

    @EJB
    private LendAndReturnSessionBeanLocal lendAndReturnSessionBean;

    @EJB
    private BookEntitySessionBeanLocal bookEntitySessionBean;

    @EJB
    private MemberEntitySessionBeanLocal memberEntitySessionBean;

    /**
     * Creates a new instance of LendAndReturnManagedBean
     */
    private String memberIdentityNumber;
    private String memberFirstName;
    private String memberLastName;
    private Long memberId;
    private String bookIsbn;
    private Long bookId;

    /**
     * Creates a new instance of LendAndReturnManagedBean
     */
    public LendAndReturnManagedBean() {
    }

    @PostConstruct
    public void init() {
    }

    // add book does not exist and member does not exist exception
    public void lendABook(ActionEvent evt) throws BookNotFoundException, MemberNotFoundException, IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        BookEntity bookEntityToLend = bookEntitySessionBean.retrieveBookEntityByIsbn(bookIsbn); // retrieve book to lend
        if (bookEntityToLend == null) {
            throw new BookNotFoundException("Book not found");
        }
        setBookId(bookEntityToLend.getBookId());

        MemberEntity memberEntity = memberEntitySessionBean.retrieveMemberEntityByIdentityNumber(getMemberIdentityNumber()); // retrieve member
        if (memberEntity == null) {
            throw new MemberNotFoundException("Member not found");
        }
        setMemberId(memberEntity.getMemberId());

        LendAndReturn lendAndReturn = new LendAndReturn();

//        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -20);
        Date today = calendar.getTime();

        lendAndReturn.setLendDate(today);

        try {
            lendAndReturn = lendAndReturnSessionBean.createNewLendAndReturn(lendAndReturn, bookId, memberId);
            context.getExternalContext().getFlash().setKeepMessages(true);
            context.addMessage(null, new FacesMessage("Success", "Successfully loaned book to member"));
            context.getExternalContext().redirect("books.xhtml?faces-redirect=true");

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to loan book to member"));
        }

    }

    public void returnABook(ActionEvent evt, BookEntity book) {

        FacesContext context = FacesContext.getCurrentInstance();

        try {
//            LendAndReturn lendAndReturn = lendAndReturnSessionBean.retrieveLendAndReturnByBook(book);
            LendAndReturn lendAndReturn = lendAndReturnSessionBean.updateLendAndReturn(book);

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Book returned successfully!", null);
            FacesContext.getCurrentInstance().addMessage(null, message);

            init();

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to return book"));
        }

    }

    public void returnABook(ActionEvent evt) {
        BookEntity bookEntityToReturn = bookEntitySessionBean.retrieveBookEntityByIsbn(bookIsbn);
        LendAndReturn lendAndReturn = lendAndReturnSessionBean.retrieveLendAndReturnByBook(bookEntityToReturn);

        Date returnDate = new Date();

        lendAndReturn.setReturnDate(returnDate);

        lendAndReturn = lendAndReturnSessionBean.updateLendAndReturn(bookEntityToReturn);
    }

    public BigDecimal generateFineAmount(BookEntity book) {
        LendAndReturn lendAndReturn = lendAndReturnSessionBean.retrieveLatestLendAndReturnRecordByBook(book);
        Date returningTodayDate = new Date();

        if (lendAndReturn == null) {
            return BigDecimal.ZERO;
        }

        long time_difference = returningTodayDate.getTime() - lendAndReturn.getLendDate().getTime();
        long days_difference = Math.abs(time_difference / (1000 * 60 * 60 * 24));

        double fineFee = 0.50;
        double totalFine = 0;
        BigDecimal fineAmount = BigDecimal.ZERO;

        if (days_difference > 14L) {
            totalFine = (double) (fineFee * (days_difference - 14L));
            fineAmount = BigDecimal.valueOf(totalFine);
        }

        return fineAmount;
    }

    public LendAndReturn getLatestLendAndReturnRecordByBook(BookEntity book) {
        LendAndReturn lendAndReturn = lendAndReturnSessionBean.retrieveLatestLendAndReturnRecordByBook(book);
        return lendAndReturn;
    }
    
    public void payOutstandingFee(BookEntity book) {
        LendAndReturn lendAndReturn = lendAndReturnSessionBean.updatePaymentStatus(book);
    }
    
    public boolean checkPaymentStatus(BookEntity book) {
        LendAndReturn lendAndReturn = lendAndReturnSessionBean.retrieveLatestLendAndReturnRecordByBook(book);
        if (lendAndReturn == null) {
            return true;
        } else {
            return lendAndReturn.isPaidAlr();
        }   
    }

    /**
     * @return the memberIdentityNumber
     */
    public String getMemberIdentityNumber() {
        return memberIdentityNumber;
    }

    /**
     * @param memberIdentityNumber the memberIdentityNumber to set
     */
    public void setMemberIdentityNumber(String memberIdentityNumber) {
        this.memberIdentityNumber = memberIdentityNumber;
    }

    /**
     * @return the memberFirstName
     */
    public String getMemberFirstName() {
        return memberFirstName;
    }

    /**
     * @param memberFirstName the memberFirstName to set
     */
    public void setMemberFirstName(String memberFirstName) {
        this.memberFirstName = memberFirstName;
    }

    /**
     * @return the memberLastName
     */
    public String getMemberLastName() {
        return memberLastName;
    }

    /**
     * @param memberLastName the memberLastName to set
     */
    public void setMemberLastName(String memberLastName) {
        this.memberLastName = memberLastName;
    }

    /**
     * @return the memberId
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * @param memberId the memberId to set
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * @return the bookIsbn
     */
    public String getBookIsbn() {
        return bookIsbn;
    }

    /**
     * @param bookIsbn the bookIsbn to set
     */
    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    /**
     * @return the bookId
     */
    public Long getBookId() {
        return bookId;
    }

    /**
     * @param bookId the bookId to set
     */
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

}
