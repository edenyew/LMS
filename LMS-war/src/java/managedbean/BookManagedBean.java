/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.BookEntity;
import entity.LendAndReturn;
import entity.MemberEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import session.BookEntitySessionBeanLocal;
import session.LendAndReturnSessionBeanLocal;
import javax.faces.event.ActionEvent;

/**
 *
 * @author edenyew
 */
@Named(value = "bookManagedBean")
@ViewScoped
public class BookManagedBean implements Serializable {

    @EJB
    private LendAndReturnSessionBeanLocal lendAndReturnSessionBean;

    @EJB
    private BookEntitySessionBeanLocal bookEntitySessionBean;

    private Long bId;
    private String title;
    private String isbn;
    private String author;

    private String identityNum;
    private Date lendDate;

    private boolean available;
    private List<LendAndReturn> lending;

    private List<BookEntity> allBooks;
    private List<BookEntity> filteredBooks;

    private BookEntity selectedBook;
    private LendAndReturn lendAndReturn;

    private BigDecimal fineAmount;

    /**
     * Creates a new instance of BookManagedBean
     */
    public BookManagedBean() {
    }

    @PostConstruct
    public void init() {
        allBooks = bookEntitySessionBean.retrieveAllBooks();
        setAllBooks(allBooks);
    }

    public void loadSelectedBook() {

        FacesContext context = FacesContext.getCurrentInstance();
        try {

            this.selectedBook = bookEntitySessionBean.retrieveBookEntityById(bId);
            author = this.selectedBook.getAuthor();
            isbn = this.selectedBook.getIsbn();
            title = this.selectedBook.getTitle();

            lendAndReturn = lendAndReturnSessionBean.retrieveLendAndReturnByBook(this.selectedBook);
            fineAmount = lendAndReturn.getFineAmount();
//            identityNum = lendAndReturn.getMember().getIdentityNo();
            lendDate = lendAndReturn.getLendDate();

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to load book"));
        }
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.equals("")) {
            return true;
        }
//        Long filterLong = Long.getLong(filterText);

        BookEntity book = (BookEntity) value;
        return book.getAuthor().toLowerCase().contains(filterText)
                || book.getIsbn().toLowerCase().contains(filterText)
                || book.getTitle().toLowerCase().contains(filterText)
                || (book.isAvailable() ? "Available" : "Loaned").contains(filterText);
//                || book.getBookId() < filterLong;
    }

    public boolean globalFilterFunctionForUnavailableBooks(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        BookEntity book = (BookEntity) value;

        return book.getAuthor().toLowerCase().contains(filterText)
                || book.getIsbn().toLowerCase().contains(filterText)
                || book.getTitle().toLowerCase().contains(filterText);
    }

    public void addBook(ActionEvent evt) {
        FacesContext context = FacesContext.getCurrentInstance();

        BookEntity b = new BookEntity();
        b.setAuthor(getAuthor());
        b.setIsbn(getIsbn());
        b.setTitle(getTitle());

        try {
            bookEntitySessionBean.createNewBookEntity(b);
            context.getExternalContext().getFlash().setKeepMessages(true);
            context.addMessage(null, new FacesMessage("Success", "Successfully added new book"));
            context.getExternalContext().redirect("books.xhtml?faces-redirect=true");

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to add new book"));
        }

    }

    public List<BookEntity> getListOfUnavailableBooks() {
        List<BookEntity> unavailableBooks = bookEntitySessionBean.retrieveAllUnavailableBooks();

        return unavailableBooks;
    }

    public void deleteBook() {
        FacesContext context = FacesContext.getCurrentInstance();

        Map<String, String> params = context.getExternalContext()
                .getRequestParameterMap();
        String bIdStr = params.get("bId");
        Long bId = Long.parseLong(bIdStr);

        try {
            bookEntitySessionBean.deleteBookEntity(bId);
        } catch (Exception e) {
            //show with an error icon
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to delete book"));
            return;
        }

        context.addMessage(null, new FacesMessage("Success", "Successfully deleted book"));
        init();

    }

    public List<BookEntity> getAllBooks() {
        return allBooks;
    }

    public void setAllBooks(List<BookEntity> allBooks) {
        this.allBooks = allBooks;
    }

    public List<BookEntity> getFilteredBooks() {
        return filteredBooks;
    }

    public void setFilteredBooks(List<BookEntity> filteredBooks) {
        this.filteredBooks = filteredBooks;
    }

    public String getTitle() {
        return title;
    }

    public String getIdentityNum() {
        return identityNum;
    }

    public void setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
    }

    public Date getLendDate() {
        return lendDate;
    }

    public void setLendDate(Date lendDate) {
        this.lendDate = lendDate;
    }

    public LendAndReturn getLendAndReturn() {
        return lendAndReturn;
    }

    public void setLendAndReturn(LendAndReturn lendAndReturn) {
        this.lendAndReturn = lendAndReturn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<LendAndReturn> getLending() {
        return lending;
    }

    public void setLending(List<LendAndReturn> lending) {
        this.lending = lending;
    }

    public BookEntity getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(BookEntity selectedBook) {
        this.selectedBook = selectedBook;
    }

    public Long getbId() {
        return bId;
    }

    public void setbId(Long bId) {
        this.bId = bId;
    }

    public void onRowSelect(SelectEvent<BookEntity> event) {
        String bookId = Long.toString(event.getObject().getBookId());
        FacesMessage msg = new FacesMessage("Book Selected", bookId);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowUnselect(UnselectEvent<BookEntity> event) {
        String bookId = Long.toString(event.getObject().getBookId());
        FacesMessage msg = new FacesMessage("Book Unselected", bookId);
        FacesContext.getCurrentInstance().addMessage(null, msg);
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

}
