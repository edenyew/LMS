/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.MemberEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author edenyew
 */
@Local
public interface MemberEntitySessionBeanLocal {

    public MemberEntity createNewMember(MemberEntity newMemberEntity);

    public List<MemberEntity> retrieveAllMembers();

    public void deleteMemberEntity(Long memberEntityId);

    public MemberEntity retrieveMemberEntityByIdentityNumber(String identityNumber);

    public MemberEntity retrieveMemberEntityById(Long memberId);
    
}
