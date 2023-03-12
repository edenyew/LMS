/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.BookEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author edenyew
 */
@Local
public interface BookEntitySessionBeanLocal {

    public BookEntity createNewBookEntity(BookEntity newBookEntity);

    public List<BookEntity> getBooksThatWereLendOut();

    public void deleteBookEntity(Long bookId);

    public BookEntity retrieveBookEntityByIsbn(String isbn);

    public BookEntity retrieveBookEntityById(Long bookId);

    public List<BookEntity> retrieveAllBooks();

    public BookEntity updateBookAvailability(BookEntity book);
    
}
