/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.MemberEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author edenyew
 */
@Stateless
public class MemberEntitySessionBean implements MemberEntitySessionBeanLocal {

    @PersistenceContext(unitName = "LMS-ejbPU")
    private EntityManager em;

    public MemberEntitySessionBean() {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public MemberEntity createNewMember(MemberEntity newMemberEntity) {
        em.persist(newMemberEntity);
        em.flush();

        return newMemberEntity;
    }

    @Override
    public MemberEntity retrieveMemberEntityById(Long memberId) {
        MemberEntity memberEntity = em.find(MemberEntity.class, memberId);

        return memberEntity;
    }

    @Override
    public MemberEntity retrieveMemberEntityByIdentityNumber(String identityNumber) {
        Query query;
        query = em.createQuery("SELECT m FROM MemberEntity m where m.identityNo = :identityNum");
        query.setParameter("identityNum", identityNumber);
        
        try {
            MemberEntity memberEntity = (MemberEntity) query.getSingleResult();
            return memberEntity;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void deleteMemberEntity(Long memberEntityId) {
        MemberEntity memberEntityToDelete = retrieveMemberEntityById(memberEntityId);

        em.remove(memberEntityToDelete);
    }

    @Override
    public List<MemberEntity> retrieveAllMembers() {
        Query query;
        query = em.createQuery("SELECT m FROM MemberEntity m ORDER BY m.memberId");
        return query.getResultList();
    }
}
