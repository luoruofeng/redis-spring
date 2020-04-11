package org.lrf.redisdemo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CoffeeService {

	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	@PostConstruct
	public void handleCoffee() {
		Coffee coffee = new Coffee();
		coffee.setId(0);
		coffee.setBrand("lao");
		coffee.setColor("red");
		
		Coffee coffee2 = new Coffee();
		coffee.setId(1);
		coffee2.setBrand("jie");
		coffee2.setColor("black");
		
		List<Coffee> coffees = Arrays.asList(coffee,coffee2);
		saveCoffee(coffees);
		printCoffees();
		
	}
	
	private void saveCoffee(List<Coffee> coffees) {
		for(Coffee coffee : coffees) {
			//delete
			String id = "coffee:"+coffee.getId();
			redisTemplate.delete(id);

			//save
			Map<Object, Object> map = new HashMap<>();
			map.put("brand", coffee.getBrand());
			map.put("color", coffee.getColor());		
			redisTemplate.opsForHash().putAll(id,map);
		}
	}
	
	private void printCoffees(){
		Set<String> keys = redisTemplate.keys("user:*");
		
		keys.stream().map(key->{
			Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
			return map.get("color");
		}).forEach(color -> System.out.println(color+""));
	}
	
	
}
