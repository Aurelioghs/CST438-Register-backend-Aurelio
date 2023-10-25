package com.cst438.controller;
import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class ResultController {
	
	@PostMapping("/result")
	public MultiplyResult check(
                  Principal principal, 
                  @RequestBody MultiplyProblem mp) {
		System.out.println(mp);
		mp = new MultiplyProblem(principal.getName(), mp.factorA(), 
                                     mp.factorB(), mp.attempt());
		MultiplyResult mr = checker.check(mp);
		history.saveHistory(mp, mr);
		//level.postMessageToLevel(mr);
		System.out.println(mr);
		return mr;
	}	
	@GetMapping("/result")
	public MultiplyResult[] getLastNresults(
			Principal principal,
              @RequestParam("lastN") Optional<Integer> lastN) {
		int n = lastN.orElse(5);
		return history.getHistory(principal.getName(), n);
	}


}
