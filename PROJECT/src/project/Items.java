/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//comments by Shahad
/**
 *
 * @author shahd
 */
@Entity
@Table(name="ITEMS")
public class Items implements java.io.Serializable  {
     @Id
     @Column(name="Id")
     private int Id;
     @Column(name="Name")
     private String Name;
     @Column(name="Quantity")
     private int Quantity;
     @Column(name="bDate")
     private Date bDate;
     @Column(name="eDate")
     private Date eDate;
     @Column(name="Email")
     private String Email;

    public Items() {
    }

    public Items(int Id, String Name, int Quantity, Date bDate, Date eDate, String Email) {
        this.Id = Id;
        this.Name = Name;
        this.Quantity = Quantity;
        this.bDate = bDate;
        this.eDate = eDate;
        this.Email = Email;
    }
    
   

    public void setId(int Id) {
        this.Id = Id;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }
    
    public void  setBDate(Date bDate) {
        this.bDate = bDate;
    }

    public void setEDate(Date eDate) {
        this.eDate = eDate;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public int getQuantity() {
        return Quantity;
    }

    public Date getBDate() {
        return bDate;
    }

    public Date getEDate() {
        return eDate;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getEmail() {
        return Email;
    }
    
    
}
