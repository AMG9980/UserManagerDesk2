/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author HP
 */
public class Bonus {
    
    private int id ; 
    private String  bonus1 , bonus2 ,bonus3 ,bonus4 ;
    Pack p ;

    public Bonus() {
    }

    public Bonus(String bonus1, String bonus2, String bonus3, String bonus4, Pack p) {
        this.bonus1 = bonus1;
        this.bonus2 = bonus2;
        this.bonus3 = bonus3;
        this.bonus4 = bonus4;
        this.p = p;
    }

    public Bonus(int id, String bonus1, String bonus2, String bonus3, String bonus4, Pack p) {
        this.id = id;
        this.bonus1 = bonus1;
        this.bonus2 = bonus2;
        this.bonus3 = bonus3;
        this.bonus4 = bonus4;
        this.p = p;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBonus1() {
        return bonus1;
    }

    public void setBonus1(String bonus1) {
        this.bonus1 = bonus1;
    }

    public String getBonus2() {
        return bonus2;
    } 
    
    public void setBonus2(String bonus2) {
        this.bonus2 = bonus2;
    }

    public String getBonus3() {
        return bonus3;
    }

    public void setBonus3(String bonus3) {
        this.bonus3 = bonus3;
    }

    public String getBonus4() {
        return bonus4;
    }

    public void setBonus4(String bonus4) {
        this.bonus4 = bonus4;
    }

    public Pack getP() {
        return p;
    }

    public void setP(Pack p) {
        this.p = p;
    }

    @Override
    public String toString() {
        return "Bonus{" + "id=" + id + ", bonus1=" + bonus1 + ", bonus2=" + bonus2 + ", bonus3=" + bonus3 + ", bonus4=" + bonus4 + ", p=" + p + '}';
    }

    public void setP(int id_PackAjoute) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
            
    
}
