package com.arch.mongo.greeting;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.arch.mongo.greeting.GreetingRepository.NORMAL_TYPE;
import static com.arch.mongo.greeting.GreetingRepository.VIP_TYPE;

@RestController
@RequestMapping("/greeting")
@AllArgsConstructor
public class GreetingController {

    private final GreetingRepository repository;

    @GetMapping
    public List<Greeting> findAll() {
        return repository.findAll();
    }

    @GetMapping("/totalPoint/vip")
    public Double getVIPTypeTotalPoint() {
        // old method aggregation
        return repository.getVIPTypeTotalPoint();
    }

    @GetMapping("/totalPoint/normal")
    public Long getNormalTypeTotalPoint() {
        // new method since 2.2, use @Aggregation annotation
        return repository.getNormalTypeTotalPoint(NORMAL_TYPE);
    }

    @PostMapping("/save")
    public Greeting save(@RequestBody Greeting greeting) {
        return repository.save(greeting);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        repository.deleteAll();
        repository.save(new Greeting(null, "Dave", VIP_TYPE, 1000D, new BigDecimal(10)));
        repository.save(new Greeting(null, "Jack", VIP_TYPE, 3000D, new BigDecimal(20)));
        repository.save(new Greeting(null, "Boris", NORMAL_TYPE, 100D, new BigDecimal(5)));
        repository.save(new Greeting(null, "Allen", NORMAL_TYPE, 500D, new BigDecimal(26)));
    }
}
