package solitare;

public enum Pile {
	PILE1(1),PILE2(2),PILE3(3),PILE4(4),PILE5(5),PILE6(6),PILE7(7),SPADES(true),CLUBS(true),DIAMONDS(true),HEARTS(true);
	
	Pile(int index) {
		this(false, index);
	}
	Pile(boolean isFinal) {
		this(isFinal, -1);
	}
	Pile(boolean isFinal, int stackSize) {
		this.isFinal = isFinal;
		this.stackSize = stackSize;
	}

	private final boolean isFinal;
	private final int stackSize;

	public boolean isFinal() {
		return isFinal;
	}
	public int getStackSize() {
		return stackSize;
	}
	
}
