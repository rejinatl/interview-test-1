package eu.cec.digit.comref.interview;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import eu.cec.digit.comref.interview.persistent.domain.Watch;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class InterviewTest1ApplicationTests {

	@Autowired
	private InterviewTest1Application interviewTest1Application;

	@AfterEach
	public void cleanup() {

		log.info("Cleaning up");
		interviewTest1Application.findAll().stream().forEach(w -> interviewTest1Application.deleteWatch(w.getName()));
		List<Watch> watches = interviewTest1Application.findAll();

		assertTrue(watches.isEmpty());
	}

	@Test
	public void testBasicCrud() {

		interviewTest1Application.addWatch("Jaeger-LeCoultre", 10000, 3, true);
		interviewTest1Application.addWatch("Lange & Söhne", 20000, 2, true);
		interviewTest1Application.addWatch("Audemars Piguet", 30000, 1, false);

		Watch watch = interviewTest1Application.getWatch("Jaeger-LeCoultre");
		assertNotNull(watch);
		assertTrue(watch.getName().equals("Jaeger-LeCoultre"));
		assertTrue(watch.getSold().equals(3));
		assertTrue(watch.getValue().equals(10000));
		assertTrue(watch.getAvailable());

		watch = interviewTest1Application.getWatch("Lange & Söhne");
		assertNotNull(watch);
		assertTrue(watch.getName().equals("Lange & Söhne"));
		assertTrue(watch.getSold().equals(2));
		assertTrue(watch.getValue().equals(20000));
		assertTrue(watch.getAvailable());

		watch = interviewTest1Application.getWatch("Audemars Piguet");
		assertNotNull(watch);
		assertTrue(watch.getName().equals("Audemars Piguet"));
		assertTrue(watch.getSold().equals(1));
		assertTrue(watch.getValue().equals(30000));
		assertFalse(watch.getAvailable());

		interviewTest1Application.updateWatch("Jaeger-LeCoultre", 10001, 3, true);

		watch = interviewTest1Application.getWatch("Jaeger-LeCoultre");
		assertNotNull(watch);
		assertTrue(watch.getName().equals("Jaeger-LeCoultre"));
		assertTrue(watch.getSold().equals(3));
		assertTrue(watch.getValue().equals(10001));
		assertTrue(watch.getAvailable());

		interviewTest1Application.deleteWatch("Jaeger-LeCoultre");
		watch = interviewTest1Application.getWatch("Jaeger-LeCoultre");
		assertNull(watch);

	}

	@Test
	public void incrementWatchSoldCount() {

		interviewTest1Application.addWatch("Jaeger-LeCoultre", 10000, 3, true);

		Watch watch = interviewTest1Application.getWatch("Jaeger-LeCoultre");
		assertNotNull(watch);
		assertTrue(watch.getName().equals("Jaeger-LeCoultre"));
		assertTrue(watch.getSold().equals(3));
		assertTrue(watch.getValue().equals(10000));
		assertTrue(watch.getAvailable());

		interviewTest1Application.incrementWatchSales("Jaeger-LeCoultre");
		watch = interviewTest1Application.getWatch("Jaeger-LeCoultre");
		assertTrue(watch.getSold().equals(4));

		interviewTest1Application.incrementWatchSales("Jaeger-LeCoultre");
		watch = interviewTest1Application.getWatch("Jaeger-LeCoultre");
		assertTrue(watch.getSold().equals(5));

	}

	@Test
	public void testWatchWithLongName() {

		interviewTest1Application.addWatch("eCRIaGRaCepYArcELpaNKLExEmeTericaMELVeRyPOnaterstr", 10000, 3, true);

		Watch watch = interviewTest1Application.getWatch("eCRIaGRaCepYArcELpaNKLExEmeTericaMELVeRyPOnaterstr");
		assertNotNull(watch);
		assertTrue(watch.getName().equals("eCRIaGRaCepYArcELpaNKLExEmeTericaMELVeRyPOnaterstr"));
		assertTrue(watch.getSold().equals(3));
		assertTrue(watch.getValue().equals(10000));
		assertTrue(watch.getAvailable());

	}

	@Test
	public void removeOutOfStockWatchesTest() {

		interviewTest1Application.addWatch("Jaeger-LeCoultre", 10000, 3, true);
		interviewTest1Application.addWatch("Lange & Söhne", 20000, 2, true);
		interviewTest1Application.addWatch("Audemars Piguet", 30000, 1, false);

		interviewTest1Application.removeOutOfStockWatches();

		List<Watch> watches = interviewTest1Application.findAll();
		assertTrue(watches.size() == 2);

		Watch watch = interviewTest1Application.getWatch("Jaeger-LeCoultre");
		assertNotNull(watch);
		assertTrue(watch.getName().equals("Jaeger-LeCoultre"));
		assertTrue(watch.getSold().equals(3));
		assertTrue(watch.getValue().equals(10000));
		assertTrue(watch.getAvailable());

		watch = interviewTest1Application.getWatch("Lange & Söhne");
		assertNotNull(watch);
		assertTrue(watch.getName().equals("Lange & Söhne"));
		assertTrue(watch.getSold().equals(2));
		assertTrue(watch.getValue().equals(20000));
		assertTrue(watch.getAvailable());

	}

	@Test
	public void addMultipleWatches() {

		List<Watch> list = new ArrayList<>();

		while (list.size() < 1000) {
			list.add(new Watch(UUID.randomUUID().toString(), 1, 1, true));

		}

		long start = System.currentTimeMillis();
		interviewTest1Application.slowAddWatches(list);
		long tookSlow = System.currentTimeMillis() - start;

		interviewTest1Application.findAll().stream().forEach(w -> interviewTest1Application.deleteWatch(w.getName()));

		start = System.currentTimeMillis();
		interviewTest1Application.fastAddWatches(list);
		long tookFast = System.currentTimeMillis() - start;

		log.info("slow: {}, fast: {}", tookSlow, tookFast);

	}
}
