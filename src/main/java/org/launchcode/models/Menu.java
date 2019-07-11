package org.launchcode.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Menu {

    //Properties or state.

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3,max=20)
    private String name;

    @ManyToMany //This class (Menu) is related to Cheese Class via rel.
    private List<Cheese> cheeses;

    //Constructors

    public Menu (){ }

    public Menu(String name){
        this.name = name;
    }

    //Utility method

    public void addItem(Cheese item){

        cheeses.add(item);
    }

    //Getters and setters.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public List<Cheese> getCheeses() {
        return cheeses;
    }

}
