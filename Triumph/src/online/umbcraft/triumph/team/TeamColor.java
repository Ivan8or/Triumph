package online.umbcraft.triumph.team;

public enum TeamColor {
	RED,
	BLUE,
	ORANGE,
	GREEN,
	NONE;
	
	public TeamColor next() {
		if(values().length -1  == ordinal())
			return values()[0];
		return values()[ordinal() + 1];
	}

	public TeamColor prev() {
		if(0  == ordinal())
			return values()[values().length - 1];
		return values()[ordinal() - 1];
	}
}
