package br.com.transmissor.api;

public interface XMLConverter {

	public String toString(Object obj, boolean formatado) throws Exception;
	public <T> T toObj(String xml, Class<T> clazz) throws Exception;
}