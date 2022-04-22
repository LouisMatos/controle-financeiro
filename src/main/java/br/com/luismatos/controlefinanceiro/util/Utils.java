package br.com.luismatos.controlefinanceiro.util;

import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Utils {

	private static BCryptPasswordEncoder bc = new BCryptPasswordEncoder();

	public static String getRandomNumberString() {
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		return String.format("%06d", number);
	}

	public static String encrypt(String encrypt) {
		return bc.encode(encrypt);
	}

	public static boolean verificaSenha(String senha, String senhaEncrypt) {
		if(bc.matches(senha, senhaEncrypt )){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(encrypt("123999"));
	}

}
