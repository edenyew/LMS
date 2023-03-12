/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package singleton;

import entity.BookEntity;
import entity.MemberEntity;
import entity.StaffEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import session.BookEntitySessionBeanLocal;
import session.MemberEntitySessionBeanLocal;
import session.StaffEntitySessionBeanLocal;

/**
 *
 * @author edenyew
 */
@Singleton
@LocalBean
@Startup

public class DataInitializationSessionBean {

    @EJB
    private StaffEntitySessionBeanLocal staffEntitySessionBean;

    @EJB
    private BookEntitySessionBeanLocal bookEntitySessionBean;

    @EJB
    private MemberEntitySessionBeanLocal memberEntitySessionBean;
    
    @PersistenceContext(unitName = "LMS-ejbPU")
    private EntityManager em;

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public DataInitializationSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        
        if (em.find(StaffEntity.class, 1l) == null) {
            staffEntitySessionBean.createNewStaff(new StaffEntity("Eric", "Some", "eric", "password"));
            staffEntitySessionBean.createNewStaff(new StaffEntity("Sarah", "Brightman", "sarah", "password"));
        }
        
        if (em.find(MemberEntity.class, 1l) == null) {
            memberEntitySessionBean.createNewMember(new MemberEntity("Tony", "Shade", 'M', 31, "S8900678A", "83722773", "13 Jurong East, Ave 3"));
            memberEntitySessionBean.createNewMember(new MemberEntity("Dewi", "Tan", 'F', 35, "S8581028X", "94602711", "15 Computing Dr"));
        }
        
        if (em.find(BookEntity.class, 1l) == null) {
            bookEntitySessionBean.createNewBookEntity(new BookEntity("Anna Karenina", "0451528611", "Leo Tolstoy"));
            bookEntitySessionBean.createNewBookEntity(new BookEntity("Madame Bovary", "979-8649042031", "Gustave Flaubert"));
            bookEntitySessionBean.createNewBookEntity(new BookEntity("Hamlet", "1980625026", "William Shakespeare"));
            bookEntitySessionBean.createNewBookEntity(new BookEntity("The Hobbit", "9780007458424", "J R R Tolkien"));
            bookEntitySessionBean.createNewBookEntity(new BookEntity("Great Expectations", "1521853592", "Charles Dickens"));
            bookEntitySessionBean.createNewBookEntity(new BookEntity("Pride and Prejudice", "979-8653642272", "Jane Austen"));
            bookEntitySessionBean.createNewBookEntity(new BookEntity("Wuthering Heights", "3961300224", "Emily Brontë"));
        }
    }

}
