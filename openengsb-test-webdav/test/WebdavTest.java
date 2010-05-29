
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;
import com.googlecode.sardine.util.SardineException;


public class WebdavTest {
	private final String rule = 
		"when\ne : Event( name == name )\nthen\nNotification n = new Notification();\n" +
		"n.setSubject(\"My webdav test notification\");\nn.setMessage(\"Hello World!\");\n" +
		"n.setRecipient(\"email@example.com\");\nnotification.notify(n);";
	private final String folder = 
		"http://localhost:8081/drools-guvnor/org.drools.guvnor.Guvnor/webdav/packages/org.openengsb/";
	private final String file = "testrule.drl";
	private Sardine sardine = null;

	@Before
	public void setUp() throws Exception {
		sardine = SardineFactory.begin("", "");
	}

	@After
	public void tearDown() throws Exception {
		sardine.delete(folder + file);
		sardine = null;
	}
	
	@Test
	public void testUploadFile() throws SardineException {
		sardine.put(folder + file, rule.getBytes());
		assertTrue(sardine.exists(folder + file));
	}

	@Test
	public void testListFiles() throws SardineException {
		List<DavResource> resources = sardine.getResources(folder);
		for (DavResource res : resources) System.out.println(res);
		assertTrue(resources.size() > 0);
	}
	
	@Test
	public void testDeleteFile() throws SardineException {
		sardine.put(folder + file, rule.getBytes());
		assertTrue(searchFile(file));
		sardine.delete(folder + file);
		assertFalse(searchFile(file));
	}

	private boolean searchFile(String to_find) throws SardineException {
		boolean found = false;
		List<DavResource> resources = sardine.getResources(folder);
		for (DavResource res : resources) if (res.toString().indexOf(to_find) >= 0) found = true;
		return found;
	}
}
