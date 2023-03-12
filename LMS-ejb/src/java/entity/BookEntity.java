/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.validation.constraints.Size;

/**
 *
 * @author edenyew
 */
@Entity
public class BookEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String title;

    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String isbn;

    @Column(nullable = false, length = 255)
    @NotNull
    @Size(min = 1, max = 255)
    private String author;

    @Column(nullable = false)
    @NotNull
    private boolean available;

    @OneToMany(mappedBy = "book", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<LendAndReturn> lending;

    public BookEntity() {
        lending = new ArrayList<>();
        available = Boolean.TRUE;
    }

    public BookEntity(String title, String isbn, String author, boolean available, List<LendAndReturn> lending) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.available = available;
        this.lending = lending;
    }

    public BookEntity(String title, String isbn, String author) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        available = Boolean.TRUE;
        lending = new ArrayList<>();
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookId != null ? bookId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the bookId fields are not set
        if (!(object instanceof BookEntity)) {
            return false;
        }
        BookEntity other = (BookEntity) object;
        if ((this.bookId == null && other.bookId != null) || (this.bookId != null && !this.bookId.equals(other.bookId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BookEntity[ id=" + bookId + " ]";
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the lending
     */
    public List<LendAndReturn> getLending() {
        return lending;
    }

    /**
     * @param lending the lending to set
     */
    public void setLending(List<LendAndReturn> lending) {
        this.lending = lending;
    }

    /**
     * @return the available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * @param available the available to set
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public Date getLendAndReturnLendDate() {
        LendAndReturn latestLendAndReturn = lending.get(lending.size() - 1);
        return latestLendAndReturn.getLendDate();
    }
    
    public String getLendAndReturnMemberIdentityNumber() {
        LendAndReturn latestLendAndReturn = lending.get(lending.size() - 1);
        return latestLendAndReturn.getMember().getIdentityNo();
    }

}
