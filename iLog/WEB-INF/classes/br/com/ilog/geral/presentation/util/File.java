package br.com.ilog.geral.presentation.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.cits.commons.citsbusiness.exception.BusinessException;
import br.cits.commons.citspresentation.messages.Messages;
import br.com.ilog.geral.business.CodigoErroEspecifico;
import br.com.ilog.geral.presentation.MensagensSistema;
import br.com.ilog.geral.presentation.TemplateMessageHelper;

public class File {

	private String name;
	private byte[] data;
	private int length;
	private String mime;

	/**
	 * @brief	Realiza o upload de um arquivo
	 * @param 	UploadItem item - item do Upload
	 * @throws 	Exception - Excecao se caso ocorra algo com o upload do arquivo
	 
	public static synchronized File uploadFile(UploadItem item) throws Exception{

		File file = new File();


			//Adiciona o array de bytes ao documento
			if(item.isTempFile()){
				file.setData(File.tryFileToByteArray(item.getFile()));
			} else {
				file.setData(item.getData());
			}
			
			file.setName( file.getNomeArquivo( item.getFileName() ) );
			file.setMime(item.getContentType());


		return file;
    }
	*/
	
	/**
	 * Extrair o nome do arquivo de um PATH.
	 * @param path
	 * @return
	 */
	public String getNomeArquivo( String path ) {
		//Divisor de pasta do Windows
		int begin = path.lastIndexOf("\\");
		return path.substring( begin + 1, path.length() );
	}
	
	/**
	 * @brief	Metodo que realiza o download de um arquivo a partir de um evento do JSP
	 * @param 	ActionEvent event - Evento da actionListener da pagina JSF
	 */
	public static void fileDonwload(File file) {
		try{
			//Pega o FacesContext
			FacesContext context = FacesContext.getCurrentInstance();
			
			//Obtem o response do Contexto
			HttpServletResponse response = (HttpServletResponse) 
				context.getExternalContext().getResponse();
			//Seta o Header para attachment			
			response.setHeader("Pragma", "");
			response.setHeader("Cache-Control", "");
			response.setHeader("Content-Disposition","attachment;filename="+file.getName());
			response.setContentType("application/download");		

			//Obtem o OutputStream para inserir o conteudo do arquivo
			OutputStream sos = response.getOutputStream();
			//Escreve o conteudo no Stream
			sos.write(file.getData());
			//Envia o Stream
			sos.flush();
			//Fecha o Stream
			sos.close();
			//Seta que o response esta completo
			context.responseComplete();
			
		} catch (IOException e) {
			BusinessException be = new BusinessException( CodigoErroEspecifico.MSG_ERRO_DONWLOAD_ARQUIVO );
			
			Messages.adicionaMensagemDeErro(TemplateMessageHelper.getMessage(
					MensagensSistema.SISTEMA, be));
		}
	}
	
	/**
	* @brief Converte um File para um array de bytes, afim de persistir o tal no banco.
	* @param f O Arquivo que será convertido
	* @author Emanuel A Schimidt
	* @return Um array de bytes que represenatm o arquivo
	* @throws IOException Exceção caso algum problema ocorra
	*/

	public static byte[] tryFileToByteArray(java.io.File f) throws IOException{
		InputStream is = new FileInputStream(f);
		long tam = f.length();
		byte[] bytes = new byte[(int) tam];
		
		if (!(tam > Integer.MAX_VALUE)){
			int offset = 0;
			int numLidos = 0;
			while ( (offset < bytes.length) && ((numLidos = is.read(bytes, offset, bytes.length-offset)) >= 0) ){
				offset += numLidos;
			}
			if (offset < bytes.length) {
				throw new IOException("O arquivo " + f.getName() + "não pode ser lido completamente");
			}
			is.close();
			return bytes;

		} else {
			Logger.getLogger("Arquivo muito grande, não pode ser convertido para byte[].");
			return null;
		}
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getMime() {
		return mime;
	}
	public void setMime(String mime) {
		this.mime = mime;
	}
}
