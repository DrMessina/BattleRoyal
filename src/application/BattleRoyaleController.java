package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BattleRoyaleController {	
	@FXML
	private HBox HBox;
	@FXML
	private Label afficheur;
	@FXML
	private ListView<String> list = new ListView<>();
	private ListView<String> list1 = new ListView<>();
	private ListView<String> list2 = new ListView<>();

	private ObservableList<String> data = FXCollections.observableArrayList();
	private ArrayList<Champion> teamA = new ArrayList<>();
	//Hashtable<String, Champion> teamAToHeal = new Hashtable<>();
	private ArrayList<Champion> teamAToHeal = new ArrayList<>();
	private ArrayList<Champion> teamB = new ArrayList<>();
	//Hashtable<String, Champion> teamBToHeal = new Hashtable<>();
	private ArrayList<Champion> teamBToHeal = new ArrayList<>();
	private ArrayList<Boolean> percent = new ArrayList<>();
	private ArrayList<ScheduledExecutorService> teamATimers = new ArrayList<>();
	private ArrayList<ScheduledExecutorService> teamBTimers = new ArrayList<>();
	private int nbUpTeamA=10;
	private int nbUpTeamB=10;//check fonction if 0 stop all thread
	private ScheduledExecutorService timer1;
	
	public void init() {
		list.setItems(data);
		list.setPrefWidth(850);
		list.setMaxWidth(1366);
		HBox.getChildren().add(list);
		Random intgenerator = new Random();
		/**	
		 * create first team
		 */
		createTeam(teamA);
		for(int i=0; i<10; i++) {
			data.add(teamA.get(i).getNom() + " TeamA");
		}
		/**
		 * create second team
		 */
		createTeam(teamB);
		for(int i=0; i<10; i++) {
			data.add(teamB.get(i).getNom() + " TeamB");
		}
		 
		/**
		 * creation des threads
		 */
		ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
			
		Runnable champion1 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(0).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
								System.out.println("in :" + i);
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(0).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(0), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(0), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(0), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get(0).scheduleAtFixedRate(champion1, 0, 1000/teamA.get(0).getInitiative(), TimeUnit.MILLISECONDS);
		
		Runnable champion2 =new Runnable() {
			public void run() {
				try {
					if (teamB.get(0).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(0).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(0), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {
									heal(teamB.get(0), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(0), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get(0).scheduleAtFixedRate(champion2, 0, 1000/teamB.get(0).getInitiative(), TimeUnit.MILLISECONDS);
		
		Runnable champion3 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(1).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(1).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(1), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(1), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(1), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get(1).scheduleAtFixedRate(champion3, 0, 1000/teamA.get(1).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion4 =new Runnable() {
			public void run() {
				try {
					if (teamB.get(1).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(1).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(1), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {
									heal(teamB.get(1), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(1), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get(1).scheduleAtFixedRate(champion4, 0, 1000/teamB.get(1).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion5 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(2).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(2), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(2), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(2), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get(2).scheduleAtFixedRate(champion5, 0, 1000/teamA.get(2).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion6 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(2).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(2), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {
									heal(teamB.get(2), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(2), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get( 2).scheduleAtFixedRate(champion6, 0, 1000/teamB.get( 2).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion7 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(3).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(3).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(3), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(3), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(3), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get( 3).scheduleAtFixedRate(champion7, 0, 1000/teamA.get( 3).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion8 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(3).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(3), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {
									heal(teamB.get(3), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(3), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get( 3).scheduleAtFixedRate(champion8, 0, 1000/teamB.get( 3).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion9 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(4).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(4), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(4), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(4), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get( 4).scheduleAtFixedRate(champion9, 0, 1000/teamA.get( 4).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion10 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(4).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(3), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {
									heal(teamB.get(4), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(3), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get( 4).scheduleAtFixedRate(champion10, 0, 1000/teamB.get( 4).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion11 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(5).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(5), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(5), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(5), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get( 5).scheduleAtFixedRate(champion11, 0, 1000/teamA.get( 5).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion12 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(5).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(5), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {

									heal(teamB.get(5), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(5), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						}  else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get( 5).scheduleAtFixedRate(champion12, 0, 1000/teamB.get( 5).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion13 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(6).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(6), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(6), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(6), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						}  else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get( 6).scheduleAtFixedRate(champion13, 0, 1000/teamA.get( 6).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion14 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(6).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(6), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {
									heal(teamB.get(6), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(6), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get( 6).scheduleAtFixedRate(champion14, 0, 1000/teamB.get( 6).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion15 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(7).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(7), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(7), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(7), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get( 7).scheduleAtFixedRate(champion15, 0, 1000/teamA.get( 7).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion16 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(7).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(7), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {

									heal(teamB.get(7), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(7), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						}  else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get( 7).scheduleAtFixedRate(champion16, 0, 1000/teamB.get( 7).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion17 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(8).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(8), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(8), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(8), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						}  else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get( 8).scheduleAtFixedRate(champion17, 0, 1000/teamA.get( 8).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion18 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(8).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(8), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {

									heal(teamB.get(8), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(8), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get( 8).scheduleAtFixedRate(champion18, 0, 1000/teamB.get( 8).getInitiative(), TimeUnit.MILLISECONDS);
		//
		Runnable champion19 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamB.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamA.get(9).getType() == "Prêtre" && teamAToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamA.get(9), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
								} else {
									heal(teamA.get(9), teamAToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamA.get(9), teamB.get(Integer.parseInt(toAttack.get(0))), teamBToHeal);
							}
						}  else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamATimers.add(timer);
		this.teamATimers.get(9).scheduleAtFixedRate(champion19, 0, 1000/teamA.get(9).getInitiative(), TimeUnit.MILLISECONDS);
		Runnable champion20 =new Runnable() {
			public void run() {
				try {
					if (teamA.get(2).getPv() > 0) {
						ArrayList<String> toAttack = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							if (teamA.get(i).getPv() > 0) {
								toAttack.add(String.valueOf(i));
							}
						}
						if (toAttack.size() > 0) {
							if (teamB.get(9).getType() == "Prêtre" && teamBToHeal.size() > 0) {
								int chooseAttOrHeal = intgenerator.nextInt(2);
								if (chooseAttOrHeal == 1) {
									Collections.shuffle(toAttack);
									attack(teamB.get(9), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
								} else {

									heal(teamB.get(9), teamBToHeal);
								}
							} else {
								Collections.shuffle(toAttack);
								attack(teamB.get(9), teamA.get(Integer.parseInt(toAttack.get(0))), teamAToHeal);
							}
						} else {
							System.out.println("no champ to attack");
						} 
					}else {
						System.out.println("cant attack! no more pv.");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		teamBTimers.add(timer);
		this.teamBTimers.get(9).scheduleAtFixedRate(champion20, 0, 1000/teamB.get(9).getInitiative(), TimeUnit.MILLISECONDS);
/*
		Runnable check =new Runnable() {
			public void run() {
				try {
					System.out.println(java.lang.Thread.activeCount() + "threads.");
					for(int i=teamB.size()-1; i>=0; i--) {
						if(teamB.get(i).getPv()<=0) {
							teamBTimers.get(i).shutdown();
							teamBTimers.get(i).awaitTermination(33, TimeUnit.MILLISECONDS);
						}
					}
					for(int i=teamA.size()-1; i>0; i--) {
						if(teamA.get(i).getPv()<=0) {
							teamATimers.get(i).shutdown();
							teamATimers.get(i).awaitTermination(33, TimeUnit.MILLISECONDS);
						}
					}
					System.out.println(java.lang.Thread.activeCount() + "threads.");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		this.timer1 = Executors.newSingleThreadScheduledExecutor();
		this.timer1.scheduleAtFixedRate(check, 0, 1, TimeUnit.SECONDS);*/
		Runnable check =new Runnable() {
			public void run() {
				System.out.println(teamAToHeal.size() +" team a / "+ teamBToHeal.size()+" team b");
				int teamAOut=0;
				int teamBOut=0;
				for(int i=0; i<teamB.size(); i++) {
					System.out.println(teamB.get(i).getPv()+" "+teamB.get(i).getNom());
					if(teamB.get(i).getPv()<=0) {
						teamBOut++;
					}
				}
				
				for(int i=0; i<teamA.size(); i++) {
					System.out.println(teamA.get(i).getPv()+" "+teamA.get(i).getNom());
					if(teamA.get(i).getPv()<=0) {
						teamAOut++;
					}
				}
				if(teamAOut == 10 || teamBOut == 10) {
					try {
						//stop all thread teamA
						for(int i =0; i<teamATimers.size(); i++) {
							teamATimers.get(i).shutdown();
							
							teamATimers.get(i).awaitTermination(33, TimeUnit.MILLISECONDS);
						}
						//stop all thread teamB
						for(int i =0; i<teamBTimers.size(); i++) {
							teamBTimers.get(i).shutdown();
							teamBTimers.get(i).awaitTermination(33, TimeUnit.MILLISECONDS);
						}
						timer1.shutdown();
						timer1.awaitTermination(1, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				
			}
		};
		this.timer1 = Executors.newSingleThreadScheduledExecutor();
		this.timer1.scheduleAtFixedRate(check, 0, 1, TimeUnit.SECONDS);
		
	}
	public void createTeam(	ArrayList<Champion> team) {
		ArrayList<String> t= new ArrayList<>();
		t.add("Guerrier");
		t.add("Voleur");
		t.add("Mage");
		t.add("Prêtre");
		for(int i=0; i<10;i++) {
			Collections.shuffle(t);
			if(t.get(0)=="Guerrier") {
				Guerrier g = new Guerrier("Guerrier"+i);
				Champion c = new Champion(g);
				team.add(c);
				
			}else if(t.get(0)=="Voleur") {
				Voleur v = new Voleur("voleur"+i);
				Champion c = new Champion(v);
				team.add(c);
				
			}else if(t.get(0)=="Mage") {
				Mage m = new Mage("Mage"+i);
				Champion c = new Champion(m);
				team.add(c);
				
			}else if(t.get(0)=="Prêtre") {
				Prêtre p = new Prêtre("Prêtre"+i);
				Champion c = new Champion(p);
				team.add(c);
				
			}else {
				System.out.println("erreur de creation de champion!");
			}
			
		}
	}
	public void setListViewWidth(Number newSceneWidth) {
		list.setPrefWidth(newSceneWidth.intValue());
		list1.setPrefWidth(newSceneWidth.intValue()/2);
		list2.setPrefWidth(newSceneWidth.intValue()/2);


	}
	public int getRandomInt(int max) {
		Random intgenerator = new Random();
		int intgenerated = intgenerator.nextInt(max);
		return intgenerated;
	}
	public void attack(Champion attacker,Champion attacked, ArrayList<Champion> toHeal) {
		
		if(attacked.getType() == "Guerrier" || attacked.getType() == "Prêtre") {
			if(parade(attacked) == true)
			{
				data.add(attacked.getNom() + " ne reçoit pas de degats.");
			}else {
				if(criticalHit(attacker)==true) 
				{
					int pvOfAttacked = attacked.getPv();
					pvOfAttacked = pvOfAttacked - attacker.getAtt();
					attacked.setPv(pvOfAttacked);
					System.out.println("pv new value "+attacked.getPv());
					data.add("Coup critique! " + attacker.getNom() + "inflige des degats perçants à " + attacked.getNom() +" d'une valeur de " + attacker.getAtt()+"." );
					//add or remove in/from to heal  team table
					if(attacked.getPv() > 0) {
						int i;
						if(toHeal.size()==0) {
							toHeal.add(attacked);
						}else {
							for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
							if (i>=toHeal.size()) {	toHeal.add(attacked);}
						}
					}
				}else{
					int pvOfAttacked = attacked.getPv();
					if(attacked.getDef() < attacker.getAtt()) 
					{
						int damage = attacker.getAtt() - attacked.getDef();
						pvOfAttacked = pvOfAttacked-damage;
						attacked.setPv(pvOfAttacked);
						System.out.println("pv new value "+attacked.getPv());
						data.add(attacker.getNom()+" attaque avec "+attacker.getAtt()+" pt dattaque, "+attacked.getNom()+" qui a "+attacked.getDef()+" defense et "+attacked.getPv()+" pv : il reçoit donc "+damage+" degat.");
						/*try {
							if(!(toHeal.containsKey(attacked.getNom())) ) {	toHeal.put(attacked.getNom(), attacked);}
						}catch (Exception e) {toHeal.put(attacked.getNom(), attacked);}*/
						//add or remove in/from to heal  team table
						if(attacked.getPv() > 0) {
							int i;
							if(toHeal.size()==0) {
								toHeal.add(attacked);
							}else {
								for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
								if (i>=toHeal.size()) {	toHeal.add(attacked);}
							}
						}
					}else{
						data.add(attacked.getNom() + " ne reçoit pas de degats.");
					}
				}
			}
			
		}else if(attacked.getType() == "Voleur") {
			if(esquive(attacked) == true)
			{
				data.add(attacked.getNom() + " ne reçoit pas de degats.");
			}else {
				if(criticalHit(attacker)==true) 
				{
					int pvOfAttacked = attacked.getPv();
					pvOfAttacked = pvOfAttacked - attacker.getAtt();
					attacked.setPv(pvOfAttacked);
					System.out.println("pv new value "+attacked.getPv());
					data.add("Coup critique! " + attacker.getNom() + "inflige des degats perçants à " + attacked.getNom() +" d'une valeur de " + attacker.getAtt()+"." );
					//add or remove in/from to heal  team table
					if(attacked.getPv() > 0) {
						int i;
						if(toHeal.size()==0) {
							toHeal.add(attacked);
						}else {
							for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
							if (i>=toHeal.size()) {	toHeal.add(attacked);}
						}
					}
				}else{
					int pvOfAttacked = attacked.getPv();
					if(attacked.getDef() < attacker.getAtt()) 
					{
						int damage = attacker.getAtt() - attacked.getDef();
						pvOfAttacked = pvOfAttacked-damage;
						attacked.setPv(pvOfAttacked);
						// change le msg pour qu'il soit plus parlant
						data.add(attacker.getNom()+" attaque avec "+attacker.getAtt()+" pt dattaque, "+attacked.getNom()+" qui a "+attacked.getDef()+" defense et "+attacked.getPv()+" : il reçoit donc "+damage+" degat.");
						System.out.println("pv new value "+attacked.getPv());
						/*try {
							if(!(toHeal.containsKey(attacked.getNom())) ) {	toHeal.put(attacked.getNom(), attacked);}
						}catch (Exception e) {toHeal.put(attacked.getNom(), attacked);}*/
						
						//add or remove in/from to heal  team table
						if(attacked.getPv() > 0) {
							int i;
							if(toHeal.size()==0) {
								toHeal.add(attacked);
							}else {
								for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
								if (i>=toHeal.size()) {	toHeal.add(attacked);}
							}
						}
					}else{
						data.add(attacked.getNom() + " ne reçoit pas de degats.");
					}
				}
			}			
		}else if(attacked.getType() == "Mage") {
			if(criticalHit(attacker)==true) 
			{
				int pvOfAttacked = attacked.getPv();
				pvOfAttacked = pvOfAttacked - attacker.getAtt();
				attacked.setPv(pvOfAttacked);
				System.out.println("pv new value "+attacked.getPv());
				data.add("Coup critique! " + attacker.getNom() + "inflige des degats perçants à " + attacked.getNom() +" d'une valeur de " + attacker.getAtt()+"." );
				//add or remove in/from to heal  team table
				if(attacked.getPv() > 0) {
					int i;
					if(toHeal.size()==0) {
						toHeal.add(attacked);
					}else {
						for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
						if (i>=toHeal.size()) {	toHeal.add(attacked);}
					}
				}
			}else{
				int pvOfAttacked = attacked.getPv();
				if(attacked.getDef() < attacker.getAtt()) 
				{
					int damage = attacker.getAtt() - attacked.getDef();
					pvOfAttacked = pvOfAttacked-damage;
					attacked.setPv(pvOfAttacked);
					// change le msg pour qu'il soit plus parlant
					data.add(attacker.getNom()+" attaque avec "+attacker.getAtt()+" pt dattaque, "+attacked.getNom()+" qui a "+attacked.getDef()+" defense et "+attacked.getPv()+" : il reçoit donc "+damage+" degat.");
					System.out.println("pv new value "+attacked.getPv());
					/*try {
						if(!(toHeal.containsKey(attacked.getNom())) ) {	toHeal.put(attacked.getNom(), attacked);}
					}catch (Exception e) {toHeal.put(attacked.getNom(), attacked);}*/
					//add or remove in/from to heal  team table
					if(attacked.getPv() > 0) {
						int i;
						if(toHeal.size()==0) {
							toHeal.add(attacked);
						}else {
							for(i=0; i<toHeal.size() && toHeal.get(i) != attacked;i++) {}
							if (i>=toHeal.size()) {	toHeal.add(attacked);}
						}
					}
				}else{
					data.add(attacked.getNom() + " ne reçoit pas de degats.");
				}
			}
		}else{
			System.out.println("error in champion type");
		}
	}
	public void heal(Champion healer, ArrayList<Champion> ToHeal) {
		ArrayList<String> indexToHeal = new ArrayList<>();
		for (int i=0; i<ToHeal.size(); i++) {	if(ToHeal.get(i).getPv() > 0) {	indexToHeal.add(String.valueOf(i));	System.out.println("in :" +i);} }
		
		if (indexToHeal.size()>0) {
			Collections.shuffle(indexToHeal);
			ToHeal.get(Integer.parseInt(indexToHeal.get(0))).setPv(ToHeal.get(Integer.parseInt(indexToHeal.get(0))).getPv() + healer.getHeal());
			data.add(healer.getNom() + " ajoute " + healer.getHeal() + " pv à " + ToHeal.get(Integer.parseInt(indexToHeal.get(0))).getPv() + " pv de "
					+ ToHeal.get(Integer.parseInt(indexToHeal.get(0))).getNom() + " -> new pv value : " + ToHeal.get(Integer.parseInt(indexToHeal.get(0))).getPv());
		}
	}
	public boolean criticalHit(Champion tryHarder) {
		int percentToTest = tryHarder.getCrit();
		for(int i=0; i<100; i++) {
			if(i<percentToTest){
				percent.add(true);
			}else {
				percent.add(false);
			}
		}
		Collections.shuffle(percent);
		Random intgenerator = new Random();
		int toTest = intgenerator.nextInt(100);
		Boolean toReturn = percent.get(toTest);
		return toReturn;
	}
	public Boolean parade(Champion theTank) {
		int percentToTest = theTank.getParade();
		for(int i=0; i<100; i++) {
			if(i<percentToTest){
				percent.add(true);
			}else {
				percent.add(false);
			}
		}
		Collections.shuffle(percent);
		Random intgenerator = new Random();
		int toTest = intgenerator.nextInt(100);
		Boolean toReturn = percent.get(toTest);
		return toReturn;
	}
	public Boolean esquive(Champion speedy) {
		int percentToTest = speedy.getEsquive();
		for(int i=0; i<100; i++) {
			if(i<percentToTest){
				percent.add(true);
			}else {
				percent.add(false);
			}
		}
		Collections.shuffle(percent);
		Random intgenerator = new Random();
		int toTest = intgenerator.nextInt(100);
		Boolean toReturn = percent.get(toTest);
		return toReturn;
	}
	public void close() {
		try {
			System.out.println("start to stop");
			if(teamATimers != null && teamATimers.size() > 0 && teamBTimers != null && teamBTimers.size() > 0) {
				for(int i =0; i<teamATimers.size(); i++) {
					teamATimers.get(i).shutdown();
					teamATimers.get(i).awaitTermination(33, TimeUnit.MILLISECONDS);
				}
				for(int i =0; i<teamBTimers.size(); i++) {
					teamBTimers.get(i).shutdown();
					teamBTimers.get(i).awaitTermination(33, TimeUnit.MILLISECONDS);
				}
				timer1.shutdown();
				timer1.awaitTermination(1, TimeUnit.SECONDS);
				
			}
			System.out.println("stop");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void fight() {
		
	}
	@FXML
	public void quitter() {
		System.out.println("closing the app");
		close();
		System.exit(0);
	}
	@FXML
	public void equipeA() {
		HBox.getChildren().clear();
		VBox left = new VBox();
		VBox right = new VBox();
		ObservableList<String> data1 = FXCollections.observableArrayList();
		list1.setItems(data1);
		list1.setPrefWidth(425);
		list1.autosize();
		for(int i=0; i<teamA.size(); i++) {
			data1.add(teamA.get(i).getNom());
		}
		left.getChildren().add(list1);
		
		ObservableList<String> data2 = FXCollections.observableArrayList();
		list2.setItems(data2);
		list2.autosize();
		list2.setPrefWidth(425);
		data2.add("select a champion!");
		right.getChildren().add(list2);
		
		HBox.getChildren().add(left);
		HBox.getChildren().add(right);
		list1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				Integer champselect = null;
				for(int i=0; i<teamA.size() && champselect == null ;i++) {
					if(newValue == teamA.get(i).getNom()) {
						champselect = i;
					}
				}
				data2.clear();
				data2.add("name : "+ teamA.get(champselect).getNom());
				data2.add("Attaque : "+ teamA.get(champselect).getAtt());
				data2.add("defense : "+ teamA.get(champselect).getDef());
				data2.add("Points de vie : "+ teamA.get(champselect).getPv());
				data2.add("Pourcentage de coup critique : "+ teamA.get(champselect).getCrit() +"%");
				data2.add("initiative : "+ teamA.get(champselect).getInitiative());
				if(teamA.get(champselect).getType() == "Guerrier") {
					data2.add("Pourcentage de parade : "+ teamA.get(champselect).getParade()+"%");
				}else if(teamA.get(champselect).getType() == "Voleur"){
					data2.add("Pourcentage d'esquive : "+ teamA.get(champselect).getEsquive()+"%");
				}else if(teamA.get(champselect).getType() == "Prêtre") {
					data2.add("montant de soin : "+ teamA.get(champselect).getHeal());
					data2.add("Pourcentage de parade : "+ teamA.get(champselect).getParade()+"%");
				}
			}
		});
	}
	@FXML
	public void equipeB() {
		
	}
	@FXML
	public void Historique() {
		
	}
}
