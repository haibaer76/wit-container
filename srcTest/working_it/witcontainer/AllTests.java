package working_it.witcontainer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	InjectOptionalTest.class,
	WitContainerTest.class,
	ParentContainerTest.class
})
public class AllTests {
}
