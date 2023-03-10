package proj.model.vo;

import java.io.Serializable;

public class Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2240343429292432398L;
	private String name;
	private int hp;
	private int gameHp;
	private int gold;
	private int power;
	private int intelligence;
	
	public Player() {}
	public Player(String name) {
		this.name = name;
	}
	public Player(String name, int hp) {
		this(name);
		this.hp = hp;
	}
	
	public Player(String name, int hp, int gameHp, int gold, int power, int intelligence) {
		this.name = name;
		this.hp = hp;
		this.gameHp = gameHp;
		this.gold = gold;
		this.power = power;
		this.intelligence = intelligence;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getGameHp() {
		return gameHp;
	}
	public void setGameHp(int gameHp) {
		this.gameHp = gameHp;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public int getIntelligence() {
		return intelligence;
	}
	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}
	
	@Override
	public String toString() {
		return name + "님";
	}
}
