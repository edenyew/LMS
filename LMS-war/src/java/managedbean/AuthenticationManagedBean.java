package managedbean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import entity.StaffEntity;
import exception.InvalidLoginException;
import exception.StaffNotFoundException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import session.StaffEntitySessionBeanLocal;

/**
 *
 * @author edenyew
 */
@Named(value = "authenticationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {

    @EJB
    private StaffEntitySessionBeanLocal staffEntitySessionBean;

    private String username = null;
    private String password = null;
    private StaffEntity staffLoggedIn;

    public AuthenticationManagedBean() {
    }

    @PostConstruct
    public void init() {
        staffLoggedIn = new StaffEntity();
    }

    public String login() throws StaffNotFoundException, InvalidLoginException {

        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent growl = UIComponent.getCurrentComponent(context).findComponent("loginForm:growl");

        try {
            staffLoggedIn = staffEntitySessionBean.retrieveStaffEntityByUsername(username);

            if (staffLoggedIn == null) {
                throw new StaffNotFoundException("Staff with username " + username + " not found!");
            }

            if (!staffLoggedIn.getPassword().equals(password)) {
                throw new InvalidLoginException("Invalid login credentials!");
            }

            // Login successful
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully logged in!"));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            return "/home.xhtml?faces-redirect=true";
            
        } catch (StaffNotFoundException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "StaffNotFoundException: ", ex.getMessage());
            context.addMessage(null, message);
            growl.setRendered(true);
            return null;
            
        } catch (InvalidLoginException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "InvalidLoginException: ", ex.getMessage());
            context.addMessage(null, message);
            growl.setRendered(true);
            return null;
        }
        
    }

    public String logout() {
        username = null;
        password = null;

        return "/index.xhtml?faces-redirect=true";
    } //end logout

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StaffEntity getStaffLoggedIn() {
        return staffLoggedIn;
    }

    public void setStaffLoggedIn(StaffEntity staffLoggedIn) {
        this.staffLoggedIn = staffLoggedIn;
    }

}
