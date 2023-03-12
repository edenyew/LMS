/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.StaffEntity;
import javax.ejb.Local;

/**
 *
 * @author edenyew
 */
@Local
public interface StaffEntitySessionBeanLocal {

    public StaffEntity createNewStaff(StaffEntity newStaffEntity);

    public void deleteStaffEntity(Long staffId);

    public void updateStaffEntity(StaffEntity staffEntity);

    public StaffEntity retrieveStaffEntityByUsername(String username);

    public StaffEntity retrieveStaffEntityByStaffId(Long staffId);
    
}
