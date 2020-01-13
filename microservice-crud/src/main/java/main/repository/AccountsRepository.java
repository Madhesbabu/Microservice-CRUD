package main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import main.model.Accounts;

public interface AccountsRepository extends JpaRepository<Accounts, Long> {
	List<Accounts> findByUserId(Long userId);

	void deleteById(Optional<Accounts> account);

}
