package working_it.witcontainer;

/**
 *
 *
 * @author Bernd Ledig
 *
 */
public class ServiceNotBoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7828066474151936194L;

	public ServiceNotBoundException(Object key) {
		super("Key '"+key+"' not bound ! ");
	}


}
