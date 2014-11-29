package br.com.ilog.geral.presentation.mbean;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ilog.geral.presentation.util.File;

@Controller("fileUploadBean")
@Scope( "session" )
public class FileUploadBean {

	private int uploadAvailableImport = 1;
	private int uploadAvailableAnexo = 1;
	
	private boolean autoUpload = false;
	private boolean useFlash = false;
	
	private List<File> filesAnexo;
	private File anexoInvoice;
	private File importacaoInvoice;
	private File exportacaoInvoice;
	
	public FileUploadBean(){
	}
	
	public synchronized void paintAnexo( OutputStream stream, Object object ) throws IOException {
		if ( getFilesAnexo() != null )
			stream.write( getFilesAnexo().get((Integer)object).getData() );
	}
	
	
	

	public synchronized void clearImportacao() {
		importacaoInvoice = null;
		setUploadAvailableImport( 1 );
		
	}
	
	
	
	/*
	 * Importar itens da invoiceExportação
	
	public synchronized void clearExportacao() {
		exportacaoInvoice= null;
		setUploadAvailableImport( 1 );
		
	}
		
	/*
	 * Getters and Setters
	 */
	
	public int getUploadAvailableImport() {
		return uploadAvailableImport;
	}

	public void setUploadAvailableImport(int uploadAvailable) {
		this.uploadAvailableImport = uploadAvailable;
	}

	public boolean isAutoUpload() {
		return autoUpload;
	}

	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}

	public boolean isUseFlash() {
		return useFlash;
	}

	public void setUseFlash(boolean useFlash) {
		this.useFlash = useFlash;
	}

	public File getAnexoInvoice() {
		return anexoInvoice;
	}

	public void setAnexoInvoice(File anexoInvoice) {
		this.anexoInvoice = anexoInvoice;
	}

	public File getImportacaoInvoice() {
		return importacaoInvoice;
	}

	public void setImportacaoInvoice(File importacaoInvoice) {
		this.importacaoInvoice = importacaoInvoice;
	}

	public List<File> getFilesAnexo() {
		return filesAnexo;
	}

	public void setFilesAnexo(List<File> filesAnexo) {
		this.filesAnexo = filesAnexo;
	}

	public int getUploadAvailableAnexo() {
		return uploadAvailableAnexo;
	}

	public void setUploadAvailableAnexo(int uploadAvailableAnexo) {
		this.uploadAvailableAnexo = uploadAvailableAnexo;
	}

	public File getExportacaoInvoice() {
		return exportacaoInvoice;
	}

	public void setExportacaoInvoice(File exportacaoInvoice) {
		this.exportacaoInvoice = exportacaoInvoice;
	}
}
