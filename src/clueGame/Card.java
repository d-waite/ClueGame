// Authors: David Waite & Dillinger Day
package clueGame;

import java.awt.Color;

public class Card {
	private String cardName;
	private CardType cardType;
	private Color playerColor;
	
	public Card(String cardName, CardType cardType) {
		this.cardName = cardName;
		this.cardType = cardType;
	}

	public void setPlayerColor(Color playerColor) {
		this.playerColor = playerColor;
	}

	public CardType getCardType() {
		return cardType;
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

	public Color getPlayerColor() {
		return playerColor;
	}

}
