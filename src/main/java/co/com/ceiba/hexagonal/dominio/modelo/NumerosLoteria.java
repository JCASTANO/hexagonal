package co.com.ceiba.hexagonal.dominio.modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.Set;

public class NumerosLoteria {

	private final Set<Integer> numeros;

	public static final int MIN_NUMBER = 1;
	public static final int MAX_NUMBER = 20;
	public static final int NUM_NUMBERS = 4;

	private NumerosLoteria() {
		numeros = new HashSet<>();
		generarNumerosAleatorios();
	}

	private NumerosLoteria(Set<Integer> givenNumbers) {
		numeros = new HashSet<>();
		numeros.addAll(givenNumbers);
	}

	public static NumerosLoteria crearAleatorio() {
		return new NumerosLoteria();
	}

	public static NumerosLoteria crear(Set<Integer> givenNumbers) {
		return new NumerosLoteria(givenNumbers);
	}

	public String obtenerNumerosComoString() {
		List<Integer> list = new ArrayList<>();
		list.addAll(numeros);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < NUM_NUMBERS; i++) {
			builder.append(list.get(i));
			if (i < NUM_NUMBERS - 1) {
				builder.append(",");
			}
		}
		return builder.toString();
	}

	private void generarNumerosAleatorios() {
		numeros.clear();
		RandomNumberGenerator generator = new RandomNumberGenerator(MIN_NUMBER, MAX_NUMBER);
		while (numeros.size() < NUM_NUMBERS) {
			int num = generator.nextInt();
			if (!numeros.contains(num)) {
				numeros.add(num);
			}
		}
	}

	@Override
	public String toString() {
		return "LotteryNumbers{" + "numbers=" + numeros + '}';
	}

	private static class RandomNumberGenerator {

		private PrimitiveIterator.OfInt randomIterator;

		public RandomNumberGenerator(int min, int max) {
			randomIterator = new Random().ints(min, max + 1).iterator();
		}

		/**
		 * @return a random number in the range (min, max)
		 */
		public int nextInt() {
			return randomIterator.nextInt();
		}
	}
}
