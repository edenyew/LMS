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
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author edenyew
 */
@Local
public interface LendAndReturnSessionBeanLocal {

    public void deleteLendAndReturn(Long lendAndReturnId);

    public LendAndReturn retrieveLendAndReturnById(Long lendAndReturnId);

    public LendAndReturn createNewLendAndReturn(LendAndReturn newLendAndReturn, Long bookId, Long memberId);

    public LendAndReturn retrieveLendAndReturnByBook(BookEntity book);

    public List<LendAndReturn> retrieveAllLendAndReturn();

//    public LendAndReturn updateLendAndReturn(LendAndReturn lendAndReturn, BookEntity bookToReturn);

    public LendAndReturn retrieveLatestLendAndReturnRecordByBook(BookEntity book);

    public LendAndReturn updateLendAndReturn(BookEntity bookToReturn);

    public LendAndReturn updatePaymentStatus(BookEntity book);

    public LendAndReturn retrieveLendAndReturnByMember(MemberEntity member);
    
}
