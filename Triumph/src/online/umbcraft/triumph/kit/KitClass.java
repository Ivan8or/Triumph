package online.umbcraft.triumph.kit;

public enum KitClass {
	
	ASSASSIN,
	MAGE,
	RANGER,
	KNIGHT,
	BRUTE;
	
	public KitClass next() {
		if(values().length -1  == ordinal())
			return values()[0];
		return values()[ordinal() + 1];
	}

	public KitClass prev() {
		if(0  == ordinal())
			return values()[values().length - 1];
		return values()[ordinal() - 1];
	}
}
