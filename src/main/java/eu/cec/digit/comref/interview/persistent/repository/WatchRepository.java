package eu.cec.digit.comref.interview.persistent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.cec.digit.comref.interview.persistent.domain.Watch;

public interface WatchRepository extends JpaRepository<Watch, String> {

}
