/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import session.MemberEntitySessionBeanLocal;
import entity.MemberEntity;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;

/**
 *
 * @author edenyew
 */
@Named(value = "memberManagedBean")
@ViewScoped
public class MemberManagedBean implements Serializable {

    @EJB
    private MemberEntitySessionBeanLocal memberEntitySessionBean;

    private Long mId;
    private String firstName;
    private String lastName;
    private Character gender;
    private Integer age;
    private String identityNo;
    private String phone;
    private String address;

    private List<MemberEntity> allRegisteredMembers;
    private List<MemberEntity> filteredMembers;

    private MemberEntity selectedMember;

    /**
     * Creates a new instance of MemberManagedBean
     */
    public MemberManagedBean() {

    }

    @PostConstruct
    public void init() {
        allRegisteredMembers = memberEntitySessionBean.retrieveAllMembers();
        setAllRegisteredMembers(allRegisteredMembers);
    }

    public void loadSelectedMember() {

        FacesContext context = FacesContext.getCurrentInstance();

        try {
            this.selectedMember = memberEntitySessionBean.retrieveMemberEntityById(mId);
            this.firstName = selectedMember.getFirstName();
            this.lastName = selectedMember.getLastName();
            this.gender = selectedMember.getGender();
            this.age = selectedMember.getAge();
            this.identityNo = selectedMember.getIdentityNo();
            this.phone = selectedMember.getPhone();
            this.address = selectedMember.getAddress();

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to load member"));
        }
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.equals("")) {
            return true;
        }
//        Long filterLong = Long.getLong(filterText);

        MemberEntity member = (MemberEntity) value;
        return member.getFirstName().toLowerCase().contains(filterText)
                || member.getAddress().toLowerCase().contains(filterText)
                || member.getFirstName().toLowerCase().contains(filterText)
                || member.getLastName().toLowerCase().contains(filterText)
                || member.getPhone().toLowerCase().contains(filterText)
                || member.getGender().toString().toLowerCase().contains(filterText)
                || member.getAge().toString().toLowerCase().contains(filterText)
                || member.getIdentityNo().toLowerCase().contains(filterText);
//                || book.getBookId()< filterLong;
    }

    public void registerMember(ActionEvent evt) {
        FacesContext context = FacesContext.getCurrentInstance();
        
        MemberEntity m = new MemberEntity();
        m.setFirstName(getFirstName());
        m.setLastName(getLastName());
        m.setGender(getGender());
        m.setAge(getAge());
        m.setIdentityNo(getIdentityNo());
        m.setPhone(getPhone());
        m.setAddress(getAddress());

        try {
            memberEntitySessionBean.createNewMember(m);
            context.getExternalContext().getFlash().setKeepMessages(true);
            context.addMessage(null, new FacesMessage("Success", "Successfully added member"));
            context.getExternalContext().redirect("members.xhtml?faces-redirect=true");

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to add member"));
        }

    } //end register a new member

    public void deleteMember() {
        FacesContext context = FacesContext.getCurrentInstance();

        Map<String, String> params = context.getExternalContext()
                .getRequestParameterMap();
        String mIdStr = params.get("mId");
        Long mId = Long.parseLong(mIdStr);

        try {
            memberEntitySessionBean.deleteMemberEntity(mId);
        } catch (Exception e) {
            //show with an error icon
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to delete member"));
            return;
        }

        context.addMessage(null, new FacesMessage("Success", "Successfully deleted member"));
        init();

    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the gender
     */
    public Character getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Character gender) {
        this.gender = gender;
    }

    /**
     * @return the age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return the identityNo
     */
    public String getIdentityNo() {
        return identityNo;
    }

    /**
     * @param identityNo the identityNo to set
     */
    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public List<MemberEntity> getAllRegisteredMembers() {
        return allRegisteredMembers;
    }

    public void setAllRegisteredMembers(List<MemberEntity> allRegisteredMembers) {
        this.allRegisteredMembers = allRegisteredMembers;
    }

    /**
     * @return the filteredMembers
     */
    public List<MemberEntity> getFilteredMembers() {
        return filteredMembers;
    }

    /**
     * @param filteredMembers the filteredMembers to set
     */
    public void setFilteredMembers(List<MemberEntity> filteredMembers) {
        this.filteredMembers = filteredMembers;
    }

    /**
     * @return the selectedMember
     */
    public MemberEntity getSelectedMember() {
        return selectedMember;
    }

    /**
     * @param selectedMember the selectedMember to set
     */
    public void setSelectedMember(MemberEntity selectedMember) {
        this.selectedMember = selectedMember;
    }

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

}
