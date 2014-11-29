package br.com.ilog.geral.presentation.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * @author Manoel Neto
 * @date   27/08/2012
 * @coment Classe responsavel para setar a codificação de ISO-8859-1 
 * para utf-8
 * 
 *
 */
public class ConverterTexto {
	public static String paraIso(String string) {
		Charset charsetUtf8 = Charset.forName("ISO-8859-1");
		CharsetEncoder encoder = charsetUtf8.newEncoder();

		Charset charsetIso88591 = Charset.forName("UTF-8");
		CharsetDecoder decoder = charsetIso88591.newDecoder();
		String s = "";
		try {
			ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(string));

			CharBuffer cbuf = decoder.decode(bbuf);
			s = cbuf.toString();
		} catch (CharacterCodingException e) {

		}
		return s;
	}

}
