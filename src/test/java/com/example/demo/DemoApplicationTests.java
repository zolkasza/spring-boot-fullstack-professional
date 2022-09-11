package com.example.demo;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@SpringBootTest
class DemoApplicationTests {

	Calculator underTest = new Calculator();

	@Test
	void contextLoads() {
	}

	@Test
	void itShouldAddNumbers() {
		int num1 = 20;
		int num2 = 30;
		int result = underTest.add(num1, num2);
		assertThat(result).isEqualTo(50);
	}

	class Calculator {
		int add(int a, int b) {
			return a+b;
		}
	}

}
