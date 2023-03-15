/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.BookEntity;
import entity.LendAndReturn;
import entity.MemberEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author edenyew
 */
@Stateless
public class LendAndReturnSessionBean implements LendAndReturnSessionBeanLocal {

    @EJB
    private BookEntitySessionBeanLocal bookEntitySessionBean;

    @EJB
    private MemberEntitySessionBeanLocal memberEntitySessionBean;

    @PersistenceContext(unitName = "LMS-ejbPU")
    private EntityManager em;

    public LendAndReturnSessionBean() {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public LendAndReturn createNewLendAndReturn(LendAndReturn newLendAndReturn, Long bookId, Long memberId) {

        newLendAndReturn.setFineAmount(BigDecimal.ZERO);
        newLendAndReturn.setReturnDate(null);

        BookEntity book = bookEntitySessionBean.retrieveBookEntityById(bookId);
        book.getLending().add(newLendAndReturn);
        book.setAvailable(false);

        MemberEntity member = memberEntitySessionBean.retrieveMemberEntityById(memberId);
        member.getLending().add(newLendAndReturn);

        newLendAndReturn.setMember(member);
        newLendAndReturn.setBook(book);

        em.persist(newLendAndReturn);
        em.flush();

        return newLendAndReturn;
    }

    @Override
    public LendAndReturn updateLendAndReturn(BookEntity bookToReturn) {

        LendAndReturn lendAndReturnLatest = retrieveLatestLendAndReturnRecordByBook(bookToReturn);
        Date returnDate = new Date();

        long time_difference = returnDate.getTime() - lendAndReturnLatest.getLendDate().getTime();
        long days_difference = Math.abs(time_difference / (1000 * 60 * 60 * 24));

        double fineFee = 0.50;
        double totalFine = 0;
        BigDecimal fineAmount = BigDecimal.ZERO;

        if (days_difference > 14L) {
            totalFine = (double) (fineFee * (days_difference - 14L));
            fineAmount = BigDecimal.valueOf(totalFine);
        }

        bookToReturn.setAvailable(true);
        bookToReturn = em.merge(bookToReturn);

        lendAndReturnLatest.setReturnDate(returnDate);
        lendAndReturnLatest.setFineAmount(fineAmount);
        lendAndReturnLatest.setPaidAlr(true);
        lendAndReturnLatest.setBook(bookToReturn);

        lendAndReturnLatest = em.merge(lendAndReturnLatest);

        return lendAndReturnLatest;
    }

    @Override
    public LendAndReturn retrieveLendAndReturnById(Long lendAndReturnId) {
        LendAndReturn lendAndReturn = em.find(LendAndReturn.class, lendAndReturnId);

        return lendAndReturn;
    }

    @Override
    public LendAndReturn retrieveLatestLendAndReturnRecordByBook(BookEntity book) {
        TypedQuery<LendAndReturn> query = em.createQuery(
                "SELECT lr FROM LendAndReturn lr JOIN lr.book b WHERE b = :book ORDER BY lr.lendDate DESC",
                LendAndReturn.class);
        query.setParameter("book", book);
        query.setMaxResults(1);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public LendAndReturn updatePaymentStatus(BookEntity book) {
        LendAndReturn lendAndReturnLatest = retrieveLatestLendAndReturnRecordByBook(book);
        lendAndReturnLatest.setPaidAlr(true);
        
        em.merge(lendAndReturnLatest);
        return lendAndReturnLatest;
    }

    @Override
    public List<LendAndReturn> retrieveAllLendAndReturn() {
        Query query;
        query = em.createQuery("Select l from LendAndReturn l");
        return query.getResultList();

    }

    @Override
    public LendAndReturn retrieveLendAndReturnByBook(BookEntity book) {
        TypedQuery<LendAndReturn> query = em.createQuery(
                "SELECT lr FROM LendAndReturn lr JOIN lr.book b WHERE b = :book",
                LendAndReturn.class);
        query.setParameter("book", book);
        return query.getSingleResult();
    }
    
    @Override
    public LendAndReturn retrieveLendAndReturnByMember(MemberEntity member) {
        TypedQuery<LendAndReturn> query = em.createQuery(
                "SELECT lr FROM LendAndReturn lr JOIN lr.member m WHERE m = :member",
                LendAndReturn.class);
        query.setParameter("member", member);
        return query.getSingleResult();
    }

    @Override
    public void deleteLendAndReturn(Long lendAndReturnId) {
        LendAndReturn lendAndReturnToDelete = retrieveLendAndReturnById(lendAndReturnId);

        em.remove(lendAndReturnToDelete);
    }

}
