package working_it.witcontainer;

public class DuplicateBindException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8896837774111388801L;

	public DuplicateBindException(Object key) {
		super("Duplicate bind key="+key);
	}

}
