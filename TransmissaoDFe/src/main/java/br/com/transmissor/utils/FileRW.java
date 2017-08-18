package br.com.transmissor.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileRW {
	
	private static final Logger log = LogManager.getLogger(FileRW.class.getSimpleName());
	
	public static boolean salvaXMLStr(String strXML, String pathFile) {
		log.info("Salvando XML Str...");
		File arquivo = new File(pathFile);
		try {
			arquivo.createNewFile();
			if (arquivo.canWrite()) {
				FileWriter writer = new FileWriter(arquivo);
				writer.write(strXML);
				writer.close();
				log.info("Salvou XML Str!");
				return true;
			}
		} catch (IOException e) {
			log.error(e.toString());
			e.printStackTrace();
		}
		return false;
	}
	
	public static String lerArquivo(String pathArq) {
		String retorno = "";
		
		File arquivo = new File(pathArq);
		if (arquivo.exists()) {
			try {
				log.info("Lendo Arquivo...");
				BufferedReader reader = new BufferedReader(new FileReader(arquivo));
				String linha  = "";
				StringBuilder total = new StringBuilder();
				boolean continua = true;
				while (continua) {
					linha = reader.readLine();
					if (linha == null) {
						continua = false;
					} else {
						total.append(linha);
					}
				}
				reader.close();
				retorno = total.toString();
				log.info("Arquivo lido!");
			} catch (Exception e) {
				log.error(e.toString());
				e.printStackTrace();
			}
		}
		return retorno;
	}
	
	public static void criaDiretorio(String pathDir) {
		new File(pathDir).mkdirs();
	}
}