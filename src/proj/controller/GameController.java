package proj.controller;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import proj.model.vo.Player;

public class GameController {
	private int compHp = 1000;
	private Player[] players = new Player[2];
	private Player computer = new Player("Computer", compHp, compHp, 0, 50, 10);
	
	public Player[] createPlayer(String userName) {
		players[0] = new Player(userName, 100, 100, 0, 10, 10);
		players[1] = computer;
		return players;
	}
	
	public Player[] setPlayer(int index) {
		ArrayList<Player> slots = readSlot();
		if(slots.get(index).getName() == null) {
		 return null;	
		}
		players[0] = slots.get(index);
		players[1] = computer;
		return players;
	}
	
	public void initializeHp() {
		players[0].setGameHp(players[0].getHp());
		players[1].setGameHp(compHp);
	}
	
	public void doUserAttack(int n) {
		int hp = players[1].getGameHp() - n;
		players[1].setGameHp(hp);
	}
	
	public String doUserCure(int n) {
		if(players[0].getGameHp() == 100) {
			return null;
		}
		int hp = players[0].getGameHp() + n;
		players[0].setGameHp(hp);
		if(hp >= 100) {
			players[0].setGameHp(100);
			return "HP의 최대 값인 100을 넘었기 때문에 100으로 다시 설정합니다.";
		}
		
		return null;
	}
	
	public void doCompAttack(int n) {
		int hp = players[0].getGameHp() - n;
		players[0].setGameHp(hp);
	}
	
	public String doCompCure(int n) {
		if(players[1].getGameHp() == 1000) {
			return null;
		}
		int hp = players[1].getGameHp() + n;
		players[1].setGameHp(hp);
		if(hp >= 1000) {
			players[1].setGameHp(1000);
			return "HP의 최대 값인 1000을 넘었기 때문에 1000으로 다시 설정합니다.";
		}
		
		return null;
	}
	
	public void addJunkObject() {
		ArrayList<Player> slots = new ArrayList<Player>(5);
		slots.add(new Player());
		slots.add(new Player());
		slots.add(new Player());
		slots.add(new Player());
		slots.add(new Player());
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save_data.txt"));) {
			for(int i = 0; i < slots.size(); i++) {
				oos.writeObject(slots.get(i));
			}
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		} catch (IndexOutOfBoundsException e) {
			
		}
	}
	
	public ArrayList<Player> readSlot() {
		ArrayList<Player> slots = new ArrayList<Player>(5);
		
		if(!new File("save_data.txt").exists()) {
			addJunkObject();
		}
		
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save_data.txt"));) {
			while(true) {
				slots.add((Player)ois.readObject());
			}
		} catch(FileNotFoundException e) {
			
		} catch(EOFException e) {
			
		} catch(IOException e) {
			
		} catch(ClassNotFoundException e) {
			
		}
		return slots;
	}
	
	public void setSlot(int index, Player player) {
		ArrayList<Player> slots = readSlot();
		player.setGameHp(player.getHp());
		slots.set(index, player);
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save_data.txt"));) {
			for(int i = 0; i < slots.size(); i++) {
				oos.writeObject(slots.get(i));
			}
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		} 
	}
	
	public void printSlot() {
		ArrayList<Player> slots = readSlot();
		for(int i = 0; i < slots.size(); i++) {
			if(slots.get(i).getName() != null) {
				System.out.println((i + 1) + ". " +
						slots.get(i).getName() + " \t" + slots.get(i).getHp() + " Hp\t" + 
						slots.get(i).getGold() + " Gold\t" + slots.get(i).getPower() + " Str\t" +
						slots.get(i).getIntelligence() + " Int");
			} else {
				System.out.println((i + 1) + ". " + "(비어있음)");
			}
			
		}
	}
	
	public boolean deleteAllSlot() {
		return new File("save_data.txt").delete();
	}
}
