package proj.view;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import proj.controller.GameController;
import proj.model.vo.Player;

public class GameMenu {
	private Scanner sc = new Scanner(System.in);
	private GameController gc = new GameController();
	private Player[] players;
	private int round = 1;
	
	public GameMenu() {
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("=== 게임 시작 : yyyy년 M월 dd일 a h시 m분 s초 ===");
		String fmt = sdf.format(today);
		System.out.println(fmt);
	}
	
	public void mainMenu() {
		System.out.println("본 게임은 사용자와 컴퓨터가 플레이어가 되어 1vs1로 싸우는 게임입니다.\n"
				+ "상대의 HP를 0 이하로 만들면 해당 플레이어의 승으로 게임은 끝납니다.\n"
				+ "플레이어는 한 라운드에 '어택(Attack)' 또는 '큐어(Cure)'를 선택할 수 있으며\n"
				+ "어택 또는 큐어의 크기는 랜덤하게 결정됩니다.");
		while(true) {
			System.out.println();
			System.out.println("▶ 게임 메뉴 ◀");
			System.out.println("1. 새 게임");
			System.out.println("2. 이어하기");
			System.out.println("3. 게임 초기화");
			System.out.println("9. 게임 종료");
			System.out.print("메뉴 번호 선택 : ");
			int menu = 0;
			try {
				menu = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				
			} finally {
				switch (menu) {
				case 1:
					newGame(null);
					break;
				case 2:
					loadGame();
					break;
				case 3:
					resetGame();
					break;
				case 9:
					System.out.println("게임을 종료합니다.");
					return;
					
				default:
					System.out.println("잘못된 메뉴입니다. 다시 선택해주세요.");
				}
			}
		}
	}
	
	
	
	public void newGame(Player player) {
		if(player == null) {
			System.out.println();
			System.out.print("사용자의 이름을 입력해주세요 : ");
			String name = sc.nextLine();
			System.out.println("반갑습니다, " + name + "님.");
			players = gc.createPlayer(name);
		}
		play();
	}
	
	public void loadGame() {
		while(true) {
			System.out.println();
			System.out.println("슬롯을 선택해주세요.");
			gc.printSlot();
			System.out.println("9. 뒤로가기");
			int index = 0;
			try {
				System.out.print("슬롯 번호 선택 : ");
				index = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				
			} finally {
				switch (index) {
				case 1: case 2: case 3: case 4: case 5:
					players = gc.setPlayer(index - 1);
					if(players != null) {
						System.out.println();
						System.out.println("반갑습니다, " + players[0]);
						play();
						return;
					} 
					System.out.println("슬롯이 비어있어 데이터를 불러올 수 없습니다.");
					System.out.println("다시 선택해주세요.");
					break;
				case 9:
					return;
					
				default:
					System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
				}
			}
		} 
	}
	
	public void continueGame(boolean isSaved) {
		while(true) {
			System.out.println();
			System.out.print("이어서 계속 진행하시겠습니까?(Y/N) : ");
			String check = sc.nextLine();
			if(check.toUpperCase().equals("Y")) {
				newGame(players[0]);
				break;
			} else if(check.toUpperCase().equals("N")) {
				if(isSaved) {
					break;
				}
				System.out.println();
				System.out.println("데이터가 저장되지 않았습니다.");
				System.out.print("정말로 종료하시겠습니까?(Y/N) : ");
				String over = sc.nextLine();
				if(over.toUpperCase().equals("Y")) {
					break;
				} else if(over.toUpperCase().equals("N")) {
					saveGame(null);
				} else {
					System.out.println("Y 또는 N만 입력해주세요.");
				}
			} else {
				System.out.println("Y 또는 N만 입력해주세요.");
			}
		}
	}
	
	public void resetGame() {
		while(true) {
			System.out.println();
			System.out.println("게임 데이터가 전부 지워지며, 복구할 수 없습니다.");
			System.out.print("정말로 초기화하시겠습니까?(Y/N) : ");
			String check = sc.nextLine();
			if(check.toUpperCase().equals("Y")) {
				gc.deleteAllSlot();
				System.out.println("게임 데이터가 모두 삭제되었습니다.");
				return;
			} else if(check.toUpperCase().equals("N")) {
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			} else {
				System.out.println("Y 또는 N만 입력해주세요.");
			}
		}
		
	}
	
	public void play() {
		System.out.println();
		System.out.println("게임을 시작합니다.");
		gc.initializeHp();
		
		boolean first = chooseFirst();
		fight(first);
				
		round--;
		System.out.printf("===--- 총 %dround에 걸친 게임 결과 발표 ---=== %n", round);
		if(players[0].getGameHp() > players[1].getGameHp()) {
			System.out.println(players[0] + "의 승리입니다. 축하합니다!");
		} else if(players[0].getGameHp() == players[1].getGameHp()) {
			System.out.println("접전 끝에 무승부로 판정되었습니다.");
		} else {
			System.out.println(players[1] + "의 승리입니다. (패배자는 아무 말도 하지 못합니다.)");
		}
		boolean isSaved = saveGame(null);
		round = 1;
		continueGame(isSaved);
	}
	
	public boolean saveGame(String yn) {
		while(true) {
			String check = yn;
			System.out.println();
			if(check == null) {
				System.out.print("저장하시겠습니까?(Y/N) : ");
				check = sc.nextLine();
			}
			if(check.toUpperCase().equals("Y")) {
				System.out.println("저장할 슬롯을 골라주세요.");
				gc.printSlot();
				int index = 0;
				try {
					System.out.print("슬롯 번호 선택 : ");
					index = Integer.parseInt(sc.nextLine());
				} catch (NumberFormatException e) {
					
				} finally {
					switch (index) {
					case 1: case 2: case 3: case 4: case 5:
						checkSaveFile(index - 1);
						return true;
						
					default:
						System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
					}
				}
			} else if(check.toUpperCase().equals("N")) {
				break;
			} else {
				System.out.println("Y 또는 N만 입력해주세요.");
			}
		}
		return false;
	}
	
	public void checkSaveFile(int index) {
		System.out.println();
		while(true) {
			ArrayList<Player> slots = gc.readSlot();
			if(slots.get(index).getName() == null) {
				gc.setSlot(index, players[0]);
				System.out.println("저장 완료 되었습니다.");
				gc.printSlot();
				break;
			}
			System.out.println("데이터가 덮어씌워집니다.");
			System.out.print("정말로 저장하시겠습니까?(Y/N) : ");
			String check = sc.nextLine();
			if(check.toUpperCase().equals("Y")) {
				gc.setSlot(index, players[0]);
				System.out.println("저장 완료 되었습니다.");
				gc.printSlot();
				break;
			} else if(check.toUpperCase().equals("N")) {
				saveGame("Y");
				break;
			} else {
				System.out.println("Y 또는 N만 입력해주세요.");
			}
		}
	}
	
	public boolean chooseFirst() { 
		String[] rockPaperScissors = {"가위", "바위", "보"};
		String[] rockPaperScissorsWin = {"바위", "보", "가위"};
		while(true) {
			System.out.println();
			System.out.println("선(先)을 정합니다. 가위/바위/보 중 아무거나 입력해주세요.");
			System.out.print("가위/바위/보 : ");
			String choose = sc.nextLine();
			
			if(choose.equals("가위") || choose.equals("바위") || choose.equals("보")) {
				int random = (int) (Math.random() * 3);
				System.out.println(players[0] + " : " + choose);
				System.out.println(players[1] + " : " + rockPaperScissors[random]);
				if(!choose.equals(rockPaperScissors[random])) {
					if(choose.equals(rockPaperScissorsWin[random])) {
						return true;
					} else {
						return false;
					}
				}
				System.out.println("비겼습니다. 가위바위보를 다시 진행하겠습니다.");
			} else {
				System.out.println("잘못 입력하셨습니다. 가위/바위/보 중 하나만 입력해주세요.");
			}
		}
	}
	
	public void fight(boolean userWin) {
		while (players[0].getGameHp() > 0 && players[1].getGameHp() > 0) {
			String[] user;
			String[] comp;
			if(userWin) {
				user = doUser();
				comp = doComp();
			} else {
				comp = doComp();
				user = doUser();
			}
			print1RoundResult(user, comp);
		}
	}
	
	public void print1RoundResult(String[] nRoundUser, String[] nRoundComp) {
		System.out.println("--- " + round + "round 결과 ---");
		System.out.println(nRoundUser[1]);
		System.out.println(nRoundComp[1]);

		int hp1 = players[0].getGameHp() % 10; 
		int hp2 = players[0].getGameHp() / 10; 
		System.out.print(players[0] + " : ");
		
		if(hp1 > 10) {
			System.out.print("●●●●●●●●●●");
		} else if (5 <= hp1 && hp1 <= 9) {
			for (int i = 0; i < hp2; i++) {
				System.out.print("●");
			}
			System.out.print("◐");
			for (int i = 0; i < 10 - hp2 - 1; i++) {
				System.out.print("○");
			}
		} else {
			for(int i = 0; i < hp2; i++) {
				System.out.print("●");
			}
			for(int i = 0; i < 10 - hp2; i++) {
				System.out.print("○");
			}
		}
		System.out.print(" (" + players[0].getGameHp() + ")");    
		System.out.println();
		if(nRoundUser[0] != null) {
			System.out.println(nRoundUser[0]);
		}
		
		int com1 = players[1].getGameHp() % 10;
		int com2 = players[1].getGameHp() / 10; 
		System.out.print(players[1].getName() + " : ");
		if(com2 > 10) {
			System.out.print("●●●●●●●●●●");
		} else if (5 <= com1 && com1 <= 9) {
			for (int i = 0; i < com2; i++) {
				System.out.print("●");
			}
			System.out.print("◐");
			for (int i = 0; i < 10 - com2 - 1; i++) {
				System.out.print("○");
			}
		} else {
			for(int i = 0; i < com2; i++) {
				System.out.print("●");
			}
			for(int i = 0; i < 10 - com2; i++) {
				System.out.print("○");
			}
		}
		System.out.print(" (" + players[1].getGameHp() + ")");    
		System.out.println();
		if(nRoundComp[0] != null) {
			System.out.println(nRoundComp[0]);
		}		
		System.out.println();
		round++;
	}
	
	public String[] doUser() {
		String[] userResult = new String[2];
		while(true) {
			System.out.print("어택 또는 큐어 중에 선택해주세요 : ");
			String action = sc.nextLine();
			
			if(!action.equals("어택") && !action.equals("큐어")) {
				System.out.println("잘못 입력하셨습니다. 어택 또는 큐어만 입력해주세요.");
			} else {
				int energy = 0;
				if(action.equals("어택")) {
					energy = (int)(Math.random() * (players[0].getPower() + 1));
					gc.doUserAttack(energy);
				} else if(action.equals("큐어")) {
					energy = (int)(Math.random() * (players[0].getIntelligence() + 1));
					userResult[0] = gc.doUserCure(energy);
				}
				System.out.println("[ " + round + " round " + players[0] + " 선택 종료 ] ");
				userResult[1] = players[0] + ") " + action + " : " + energy;
				return userResult;
			}
		}
	}
	
	public String[] doComp() {
		String[] compAction = {"어택", "큐어"};
		String[] compResult = new String[2];
		
		int random = (int)(Math.random() * 2);
		int energy = 0;
		if(compAction[random].equals("어택")) {
			energy = (int)(Math.random() * (players[1].getPower() + 1));
			gc.doCompAttack(energy);
		} else {
			energy = (int)(Math.random() * (players[1].getIntelligence() + 1));
			compResult[0] = gc.doCompCure(energy);
		}
		System.out.println("[ " + round + " round " + players[1] + " 선택 종료 ] ");
		compResult[1] = players[1] + ") " + compAction[random] + " : " + energy;
		return compResult;
	}
}
