package eu.cec.digit.comref.interview;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import eu.cec.digit.comref.interview.persistent.domain.Watch;
import eu.cec.digit.comref.interview.persistent.repository.WatchRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class InterviewTest1Application implements CommandLineRunner {

	@Autowired
	private WatchRepository watchRepository;

	public static void main(String[] args) {
		SpringApplication.run(InterviewTest1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Starting test one");

	}

	public void slowAddWatches(List<Watch> watches) {

		for(Watch watch : watches) {
			watchRepository.save(watch);
		}

	}

	public void fastAddWatches(List<Watch> watches) {

		for(Watch watch : watches) {
			watchRepository.save(watch);
		}

	}
	
	public void removeOutOfStockWatches() {

		
		List<Watch> watches = watchRepository.findAll();
		
		for(Watch watch : watches) {
			
			if(!watch.equals("available")) {
				watchRepository.deleteAll(watches);
			}
		}
		

	}

	public Watch addWatch(String name, Integer value, Integer sold, Boolean available) {

		Watch watch = new Watch(null, null, null, null);
		watch.setAvailable(available);
		watch.setName(name);
		watch.setSold(value);
		watch.setValue(sold);

		return watchRepository.save(watch);

	}

	public Watch updateWatch(String name, Integer value, Integer sold, Boolean available) {

		Watch watch = getWatch(name);
		watch.setAvailable(available);
		watch.setSold(sold);
		watch.setValue(value);

		return watchRepository.save(watch);

	}

	public Watch getWatch(String name) {

		return watchRepository.findById(name).orElse(null);

	}

	public Watch incrementWatchSales(String name) {

		Watch watch = watchRepository.findById(name).orElse(null);

		if (watch != null) {
			watch.setValue(watch.getValue());
			return watchRepository.save(watch);

		}

		return watch;
	}

	public List<Watch> findAll() {

		return watchRepository.findAll();

	}

	public void deleteWatch(String name) {

		watchRepository.deleteById(name);
	}
}
