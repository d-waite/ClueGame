// Authors: David Waite & Dillinger Day
package clueGame;

public class Card {
	private String cardName;

	public CardType getCardType() {
		return null;
	}
	
	public boolean equals(Card card) {
		if (this.cardName.equals(card.getCardName())) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getCardName() {
		return cardName;
	}

}
