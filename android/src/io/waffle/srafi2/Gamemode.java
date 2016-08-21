package io.waffle.srafi2;

public enum Gamemode {
	CLASSIC(0),
	SIGHT(1),
	SNOWBALL(2),
	UPHILL(3),
	FEED(4),
	SCROLL(5),
	JUMP(6);
	private final int value;

	Gamemode(int i) {
		this.value = i;
	}

	public int getValue() {
		return value;
	}
}
