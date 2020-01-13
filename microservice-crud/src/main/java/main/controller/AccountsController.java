package main.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import main.model.Accounts;
import main.model.Users;
import main.repository.AccountsRepository;
import main.repository.UserRepository;

@RestController
public class AccountsController {
	@Autowired
	private AccountsRepository accountRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/users/{userId}/accounts")
	public List<Accounts> getAccountsByUser(@PathVariable(value = "userId") Long userId) {
		return accountRepository.findByUserId(userId);
	}

	@PostMapping("/users/{userId}/accounts")
	public ResponseEntity<Object> createAccount(@PathVariable(value = "userId") Long userId,
			@Valid @RequestBody Accounts account) {
		Optional<Users> user = userRepository.findById(userId);
		account.setUser(user.get());
		Accounts account1 = accountRepository.save(account);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(account1.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/users/{userId}/accounts/{accountId}")
	public void deleteAccounts(@PathVariable(value = "userId") Long userId,
			@PathVariable(value = "accountId") Long accountId) {

		Optional<Accounts> account = accountRepository.findById(accountId);
		if (null != account) {
			accountRepository.deleteById(account);
		}
	}

	@PutMapping("/users/{userId}/accounts/{accountId}")
	public ResponseEntity<Object> deleteAccounts(@PathVariable(value = "userId") Long userId,
			@PathVariable(value = "accountId") Long accountId, @Valid @RequestBody Accounts accountRequest) {
		List<Accounts> list = accountRepository.findByUserId(userId);

		Accounts acco = list.stream().filter(acc -> acc.getId() == accountId).findFirst().orElse(null);
		if (null != acco) {
			acco.setAccountNumber(accountRequest.getAccountNumber());
			acco.setBalance(accountRequest.getBalance());
			accountRepository.save(acco);
		}

		return ResponseEntity.noContent().build();
	}
}
