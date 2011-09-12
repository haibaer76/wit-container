package working_it.witcontainer.sample;

import working_it.witcontainer.Inject;

public class D {

	private A a;

	public A getA() {
		return a;
	}

	@Inject
	public void setA(A a) {
		this.a = a;
	}
	
	
}
