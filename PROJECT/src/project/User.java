/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author merao
 */
@Entity
@Table (name="USER")
public class User implements java.io.Serializable{
    
    @Id
    @Column (name="email")
    private String emailS;
    @Column (name="username")
    private String usernameS;
    @Column (name="password")
    private String passwordS;
      

    public User() {
    }

    public User(String emailS, String passwordS) {
        this.emailS = emailS;
        this.passwordS = passwordS;
    }

    
    public User(String usernameS,String emailS, String passwordS ) {
        this.usernameS = usernameS;
        this.emailS = emailS;
        this.passwordS = passwordS;
    }

    public String getUsernameS() {
        return usernameS;
    }

    public void setUsernameS(String usernameS) {
        this.usernameS = usernameS;
    }

    public String getEmailS() {
        return emailS;
    }

    public void setEmailS(String emailS) {
        this.emailS = emailS;
    }

    public String getPasswordS() {
        return passwordS;
    }

    public void setPasswordS(String passwordS) {
        this.passwordS = passwordS;
    }
    
    
}
