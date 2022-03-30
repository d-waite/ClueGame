// Authors: David Waite & Dillinger Day
package clueGame;

public class Solution {
	private Card room;
	private Card weapon;
	private Card person;
	
	public Solution(Card room, Card weapon,Card person) {
		this.room = room;
		this.weapon = weapon;
		this.person = person;
	}
	
	public Card getRoom() {
		return room;
	}
	public Card getWeapon() {
		return weapon;
	}
	public Card getPerson() {
		return person;
	}
	
}
