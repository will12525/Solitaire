# Solitaire
What it does: This program allows someone to play solitaire. The deck is randomly shuffled and a game is dealt. 
How it does it:
 There is a class that handles everything about the deck; this class can, load all the card images, generate a deck 
 of 52 cards, it can be passed a parameter for how many decks you want in the game and it will make that many decks of 
 52 cards, it can shuffle the deck, cards can be added to it and removed from it by other classes.
	Each card is its own class; it stores the x and y location on screen, whether it’s been flipped over or not, what kind 
	of card it is, and the cards image.
	Then theres the main class; it knows how the user is allowed to interact with the cards and is able to determine if the 
	player is placing cards in the right order. 
Struggle:
	Only main struggles were getting the location of the mouse click when the user was trying to pick up a card. 
	Solved that by taking the location of the mouse based on the entire screen then modifying it based on the location of the
	JFrame.
