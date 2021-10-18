package model;

public class CityInfo {
    private String name;
    private int nowConfirm;
    private int confirm;
    private int heal;
    private int dead;

    @Override
    public String toString() {
        return "CityInfo{" +
                "name='" + name + '\'' +
                ", nowConfirm=" + nowConfirm +
                ", confirm=" + confirm +
                ", heal=" + heal +
                ", dead=" + dead +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNowConfirm() {
        return nowConfirm;
    }

    public void setNowConfirm(int nowConfirm) {
        this.nowConfirm = nowConfirm;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public int getHeal() {
        return heal;
    }

    public void setHeal(int heal) {
        this.heal = heal;
    }

    public int getDead() {
        return dead;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

    public CityInfo() {
    }

    public CityInfo(String name, int nowConfirm, int confirm, int heal, int dead) {
        this.name = name;
        this.nowConfirm = nowConfirm;
        this.confirm = confirm;
        this.heal = heal;
        this.dead = dead;
    }
}
