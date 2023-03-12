/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.StaffEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author edenyew
 */
@Stateless
public class StaffEntitySessionBean implements StaffEntitySessionBeanLocal {

    @PersistenceContext(unitName = "LMS-ejbPU")
    private EntityManager em;

    public StaffEntitySessionBean() {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public StaffEntity createNewStaff(StaffEntity newStaffEntity) {
        em.persist(newStaffEntity);
        em.flush();

        return newStaffEntity;
    }

    @Override
    public StaffEntity retrieveStaffEntityByStaffId(Long staffId) {
        StaffEntity staffEntity = em.find(StaffEntity.class, staffId);

        return staffEntity; // add exception class if entity not found here

    }

    @Override
    public StaffEntity retrieveStaffEntityByUsername(String username) {
        Query query = em.createQuery("Select s From StaffEntity s Where s.userName = :username");
        query.setParameter("username", username);
        StaffEntity staffEntity = (StaffEntity) query.getSingleResult();

        return staffEntity;
    }

    @Override
    public void updateStaffEntity(StaffEntity staffEntity) {

        StaffEntity staffEntityToUpdate = retrieveStaffEntityByStaffId(staffEntity.getStaffId());

        staffEntityToUpdate.setFirstName(staffEntity.getFirstName());
        staffEntityToUpdate.setLastName(staffEntity.getLastName());
        staffEntityToUpdate.setUserName(staffEntity.getUserName());
        staffEntityToUpdate.setPassword(staffEntity.getPassword());

        // em.flush();
    }

    @Override
    public void deleteStaffEntity(Long staffId) {

        StaffEntity staffEntityToDelete = retrieveStaffEntityByStaffId(staffId);

        em.remove(staffEntityToDelete);

    }

}
