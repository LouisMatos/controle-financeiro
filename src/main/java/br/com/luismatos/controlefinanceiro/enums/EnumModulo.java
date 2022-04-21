package br.com.luismatos.controlefinanceiro.enums;

public enum EnumModulo {
	
	ADM("ROLE_ADMINISTRADOR"), USUARIO("ROLE_USUARIO");

	private String modulo;

	private EnumModulo(String modulo) {
		this.modulo = modulo;
	}

	@Override
	public String toString() {
		return this.modulo;
	}

}
