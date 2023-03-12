/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.BookEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author edenyew
 */
@Stateless
public class BookEntitySessionBean implements BookEntitySessionBeanLocal {

    @PersistenceContext(unitName = "LMS-ejbPU")
    private EntityManager em;

    
    public BookEntitySessionBean() {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public BookEntity createNewBookEntity(BookEntity newBookEntity) {
        em.persist(newBookEntity);
        em.flush();
        
        return newBookEntity;
    }
    
    @Override
    public BookEntity retrieveBookEntityById(Long bookId) {
        BookEntity bookEntity = em.find(BookEntity.class, bookId);
        
        return bookEntity;
    }
    
    @Override
    public BookEntity updateBookAvailability(BookEntity book) {
        book.setAvailable(Boolean.TRUE);
        
        em.merge(book);
        
        return book;
    }
    
    @Override
    public BookEntity retrieveBookEntityByIsbn(String isbn) {
        Query query;
        query = em.createQuery("Select b from BookEntity b Where b.isbn = :isbn");
        query.setParameter("isbn", isbn);
        BookEntity bookEntity = (BookEntity) query.getSingleResult();
        
        return bookEntity;
    }
    
    @Override
    public List<BookEntity> retrieveAllBooks() {
        Query query;
        query = em.createQuery("Select b from BookEntity b ORDER BY b.bookId");
        return query.getResultList();
    }
    
    @Override
    public void deleteBookEntity(Long bookId) {
        BookEntity bookEntityToDelete = retrieveBookEntityById(bookId);
        
        em.remove(bookEntityToDelete);
    }
    
    @Override
    public List<BookEntity> getBooksThatWereLendOut() {
        Query query;
        boolean availability = false;
        query = em.createQuery("Select b from BookEntity b Where b.available := availability");
        query.setParameter("availability", availability);
        
        return query.getResultList();
    }
    
}
