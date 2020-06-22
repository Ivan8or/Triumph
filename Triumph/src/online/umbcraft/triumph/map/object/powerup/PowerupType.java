package online.umbcraft.triumph.map.object.powerup;

public enum PowerupType {

	REFILL,
	POINT,
	POWER;
	
	public PowerupType next() {
		if(values().length -1  == ordinal())
			return values()[0];
		return values()[ordinal() + 1];
	}

	public PowerupType prev() {
		if(0  == ordinal())
			return values()[values().length - 1];
		return values()[ordinal() - 1];
	}
}
