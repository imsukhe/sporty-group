package com.sporty.jackpot.controller;

import com.sporty.jackpot.db.InMemoryDb;
import com.sporty.jackpot.model.Jackpot;
import com.sporty.jackpot.pojo.request.JackpotRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/jackpot")
public class JackpotController {

    private final InMemoryDb db = InMemoryDb.getInstance();

    @PostMapping
    public String createJackpot(@RequestBody JackpotRequest request) {
        Jackpot jackpot = new Jackpot(
                request.getId(),
                request.getInitialAmount(),
                request.getInitialAmount(),
                request.getContributionType(),
                request.getRewardType()
        );
        db.addJackpot(jackpot);
        return "Jackpot created";
    }

    @GetMapping("/{id}")
    public Jackpot getJackpot(@PathVariable String id) {
        return db.getJackpotById(id);
    }

    @GetMapping
    public Collection<Jackpot> getAllJackpots() {
        return db.getAllJackpots();
    }

}
