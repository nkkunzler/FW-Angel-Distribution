package angels;

public enum Status {

	COMPLETE("complete"),
	HOLD("hold"),
	PULL("pull"),
	AWAITING("awaiting"),
	OUT("out");

	private final String status;

	private Status(String status) {
		this.status = status;
	}

	public final String getStatus() {
		return this.status;
	}
}
