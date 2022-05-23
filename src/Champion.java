public class Champion {

    private int AD;
    private int AP;
    private int Armor;

    public Champion(int AD, int AP, int armor) {
        this.AD = AD;
        this.AP = AP;
        Armor = armor;
    }

    public Champion() {
        AD = 5;
        AP = 5;
    }

    public int getAD() {
        return AD;
    }

    public int getAP() {
        return AP;
    }

    public void setAP(int AP) {
        this.AP = AP;
    }

    public int getArmor() {
        return Armor;
    }

    public void setArmor(int armor) {
        Armor = armor;
    }

//    @Override
//    public String toString() {
//        return "Champion{" +
//                "AD=" + AD +
//                ", AP=" + AP +
//                ", Armor=" + Armor +
//                '}';
//    }
}
